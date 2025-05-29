package es.udc.ws.app.model.inscripcion;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;

public interface SqlInscripcionDao {

    public Inscripcion create(Connection connection, Inscripcion inscripcion);

    public Inscripcion find(Connection connection, Long inscripcionId)
            throws InstanceNotFoundException;

    public boolean existsByMovieId(Connection connection, Long movieId);

}
