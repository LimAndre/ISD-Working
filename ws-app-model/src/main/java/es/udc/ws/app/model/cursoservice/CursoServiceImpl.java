package es.udc.ws.app.model.cursoservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import es.udc.ws.app.model.curso.SqlCursoDao;
import es.udc.ws.app.model.curso.SqlCursoDaoFactory;
import es.udc.ws.app.model.cursoservice.exceptions.CourseClosedException;
import es.udc.ws.app.model.cursoservice.exceptions.CourseFullException;
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
        PropertyValidator.validateDouble("precio",
                curso.getPrecio(), 0f, Float.MAX_VALUE);
        PropertyValidator.validateLong("plazasMaximas",
                curso.getPlazasMaximas(), 0, Integer.MAX_VALUE);
        // ← AQUÍ ya no validamos fechaInicio
    }

    @Override
    public Curso altaCurso(Curso curso) throws InputValidationException {
        validateCurso(curso);
        LocalDateTime fechaAlta = LocalDateTime.now().withNano(0);
        curso.setFechaAlta(fechaAlta);

        // Validar ahora fechaInicio vs. fechaAlta
        LocalDateTime fechaInicio = curso.getFechaInicio();
        if (fechaInicio == null || fechaInicio.isBefore(fechaAlta.plusDays(15))) {
            throw new InputValidationException(
                    "La fecha de inicio debe ser al menos 15 días después del alta");
        }

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

        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                // 1) Comprobar existencia y obtener fechaAlta real
                Curso existente = cursoDao.find(connection, curso.getCursoId());

                // 2) Asignar fechaAlta real antes de validar
                curso.setFechaAlta(existente.getFechaAlta());

                // 3) Validar datos completos (ahora que fechaAlta no es null)
                validateCurso(curso);

                // 4) Ejecutar UPDATE
                cursoDao.update(connection, curso);

                connection.commit();
            }
            catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            }
            catch (InputValidationException e) {
                connection.commit();
                throw e;
            }
            catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error al actualizar curso", e);
            }
            catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("No se pudo conectar a la BD", e);
        }
    }


    @Override
    public void removeCurso(Long cursoId)
            throws InstanceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                cursoDao.remove(connection, cursoId);

                connection.commit();
            } catch (InstanceNotFoundException e) {
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
    public List<Curso> buscarCursosByFechaYCiuddad(String ciudad, LocalDateTime desde) {
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
            throws InstanceNotFoundException, InputValidationException,
            CourseClosedException, CourseFullException {

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
                    throw new CourseClosedException(cursoId);
                }

                int ocupadas = inscDao.findByCurso(connection, cursoId).size();
                if (ocupadas >= curso.getPlazasMaximas()) {
                    throw new CourseFullException(cursoId);
                }

                Inscripcion ins = new Inscripcion(cursoId, emailUsuario, tarjetaPago, now);
                Inscripcion creada = inscDao.create(connection, ins);

                connection.commit();
                return creada.getInscripcionId();

            } catch (InstanceNotFoundException e) {
                connection.commit();  // ya que el find lanzó la excepción y no hay cambios
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error al inscribir usuario", e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
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
