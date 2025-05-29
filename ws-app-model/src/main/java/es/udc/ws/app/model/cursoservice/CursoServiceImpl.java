package es.udc.ws.app.model.cursoservice;

import static es.udc.ws.app.model.util.ModelConstants.BASE_URL;
import static es.udc.ws.app.model.util.ModelConstants.APP_DATA_SOURCE;

import java.util.List;


import javax.sql.DataSource;

import es.udc.ws.app.model.curso.SqlCursoDao;
import es.udc.ws.app.model.curso.SqlCursoDaoFactory;
import es.udc.ws.app.model.inscripcion.SqlInscripcionDao;
import es.udc.ws.app.model.inscripcion.SqlInscripcionDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import es.udc.ws.app.model.curso.Curso;
import es.udc.ws.app.model.inscripcion.Inscripcion;
import es.udc.ws.util.sql.DataSourceLocator;


public class CursoServiceImpl implements CursoService{

    private final DataSource dataSource;
    private SqlCursoDao cursoDao = null;
    private SqlInscripcionDao inscripcionDao = null;

    public CursoServiceImpl() {
        dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        cursoDao = SqlCursoDaoFactory.getDao();
        inscripcionDao = SqlInscripcionDaoFactory.getDao();
    }

    @Override
    public Curso addCurso(Curso curso) throws InputValidationException {
        return null;
    }

    @Override
    public Curso findCurso(Long cursoId) throws InstanceNotFoundException {
        return null;
    }

    @Override
    public List<Curso> findCursos(String keywords) {
        return List.of();
    }

    @Override
    public Inscripcion inscribirseCurso(Long cursoId, String userId, String creditCardNumber) throws InstanceNotFoundException, InputValidationException {
        return null;
    }
}
