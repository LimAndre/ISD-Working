package es.udc.ws.app.model.cursoservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import es.udc.ws.app.model.curso.SqlCursoDao;
import es.udc.ws.app.model.curso.SqlCursoDaoFactory;
import es.udc.ws.app.model.cursoservice.exceptions.CourseClosedException;
import es.udc.ws.app.model.cursoservice.exceptions.CourseNotRemovableException;
import es.udc.ws.app.model.inscripcion.SqlInscripcionDao;
import es.udc.ws.app.model.inscripcion.SqlInscripcionDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import es.udc.ws.util.validation.PropertyValidator;
import es.udc.ws.app.model.curso.Curso;
import es.udc.ws.app.model.inscripcion.Inscripcion;
import es.udc.ws.util.sql.DataSourceLocator;

import javax.sql.DataSource;

import static es.udc.ws.app.model.util.ModelConstants.APP_DATA_SOURCE;

public class CursoServiceImpl implements CursoService{

    private final DataSource dataSource;
    private final SqlCursoDao cursoDao;
    private final SqlInscripcionDao inscDao;

    public CursoServiceImpl() {
        dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        cursoDao = SqlCursoDaoFactory.getDao();
        inscDao = SqlInscripcionDaoFactory.getDao();
    }

    private void validateCurso(Curso curso) throws InputValidationException {
        PropertyValidator.validateMandatoryString("ciudad", curso.getCiudad());
        PropertyValidator.validateMandatoryString("nombre", curso.getNombre());
        // Ahora usas los nuevos métodos:
        PropertyValidator.validateFloat("precio",
                curso.getPrecio(), 0f, Float.MAX_VALUE);
        PropertyValidator.validateInteger("plazasMaximas",
                curso.getPlazasMaximas(), 0, Integer.MAX_VALUE);

        LocalDateTime fechaInicio = curso.getFechaInicio();
        LocalDateTime now = LocalDateTime.now().withNano(0);
        if (fechaInicio == null || fechaInicio.isBefore(now.plusDays(15))) {
            throw new InputValidationException(
                    "La fecha de inicio debe ser al menos 15 días después del alta");
        }
    }

    @Override
    public Curso altaCurso(Curso curso) throws InputValidationException {
        validateCurso(curso);
        curso.setFechaAlta(LocalDateTime.now().withNano(0));

        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Curso creado = cursoDao.create(connection, curso);

                connection.commit();
                return creado;

            } catch (RuntimeException | Error | SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error al dar de alta curso", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo conectar a la base de datos", e);
        }
    }

    @Override
    public void updateCurso(Curso curso)
            throws InputValidationException, InstanceNotFoundException {
        validateCurso(curso);

        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                cursoDao.update(connection, curso);

                connection.commit();
            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (RuntimeException | Error | SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error al actualizar curso", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo conectar a la base de datos", e);
        }
    }

    @Override
    public void removeCurso(Long cursoId)
            throws InstanceNotFoundException, CourseNotRemovableException {
        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                if (inscDao.existsByCurso(connection, cursoId)) {
                    throw new CourseNotRemovableException();
                }

                cursoDao.remove(connection, cursoId);

                connection.commit();
            } catch (InstanceNotFoundException | CourseNotRemovableException e) {
                connection.commit();
                throw e;
            } catch (RuntimeException | Error | SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error al eliminar curso", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo conectar a la base de datos", e);
        }
    }

    @Override
    public Curso findCurso(Long cursoId) throws InstanceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            return cursoDao.find(connection, cursoId);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar curso en la base de datos", e);
        }
    }

    @Override
    public List<Curso> buscarCursos(String ciudad, LocalDateTime desde) {
        try (Connection connection = dataSource.getConnection()) {

            List<Curso> cursos =
                    cursoDao.findByCiudadYFecha(connection, ciudad, desde);

            for (Curso curso : cursos) {
                int ocupadas = inscDao.countByCurso(connection, curso.getCursoId());
                curso.setPlazasDisponibles(curso.getPlazasMaximas() - ocupadas);
            }

            return cursos;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error al buscar cursos en la base de datos", e);
        }
    }

    @Override
    public Long inscribirUsuario(Long cursoId,
                                 String emailUsuario,
                                 String tarjetaPago)
                                 throws InstanceNotFoundException, InputValidationException, CourseClosedException
    {

        // Validación de datos de inscripción
        PropertyValidator.validateMandatoryString("emailUsuario", emailUsuario);
        PropertyValidator.validateCreditCard(tarjetaPago);

        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Curso curso = cursoDao.find(connection, cursoId);
                LocalDateTime now = LocalDateTime.now().withNano(0);
                if (now.isAfter(curso.getFechaInicio())) {
                    throw new CourseClosedException();
                }
                int ocupadas = inscDao.findByCurso(connection, cursoId).size();
                if (ocupadas >= curso.getPlazasMaximas()) {
                    throw new InputValidationException("No quedan plazas disponibles");
                }

                Inscripcion ins = new Inscripcion(cursoId, emailUsuario, tarjetaPago, now);
                Inscripcion creada = inscDao.create(connection, ins);

                connection.commit();
                return creada.getInscripcionId();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (RuntimeException | Error | SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error al inscribir usuario", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo conectar a la base de datos", e);
        }
    }

    @Override
    public Inscripcion findInscripcion(Long inscripcionId)
            throws InstanceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            return inscDao.find(connection, inscripcionId);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar inscripción en la base de datos", e);
        }
    }
}
