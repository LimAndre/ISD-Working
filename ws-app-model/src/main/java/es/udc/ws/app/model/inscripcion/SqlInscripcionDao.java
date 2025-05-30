package es.udc.ws.app.model.inscripcion;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.util.List;

public interface SqlInscripcionDao {

    Inscripcion create(Connection connection,
                       Inscripcion inscripcion);

    Inscripcion find(Connection connection,
                     Long inscripcionId)
            throws InstanceNotFoundException;

    boolean existsByCurso(Connection connection,
                          Long cursoId);

    List<Inscripcion> findByCurso(Connection connection,
                                  Long cursoId);

    int countByCurso(Connection connection, Long cursoId);

    void update(Connection connection,
                Inscripcion inscripcion)
            throws InstanceNotFoundException;


    void remove(Connection connection,
                Long inscripcionId)
            throws InstanceNotFoundException;

}
