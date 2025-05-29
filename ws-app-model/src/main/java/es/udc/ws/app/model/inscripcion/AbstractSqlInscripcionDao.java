package es.udc.ws.app.model.inscripcion;

import es.udc.ws.app.model.curso.SqlCursoDao;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;

public abstract class AbstractSqlInscripcionDao implements SqlInscripcionDao {

    @Override
    public Inscripcion find(Connection connection, Long inscripcionId)
            throws InstanceNotFoundException {
        return null;
    }

    public boolean existsByMovieId(Connection connection, Long movieId) {
        return false;
    }

}
