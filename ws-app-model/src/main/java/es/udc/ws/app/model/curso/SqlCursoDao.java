package es.udc.ws.app.model.curso;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

/**
 * Interfaz DAO para la persistencia de la entidad Curso.
 * Define las operaciones CRUD y consultas específicas.
 */
public interface SqlCursoDao {

    Curso create(Connection connection, Curso curso);

    Curso find(Connection connection, Long cursoId)
            throws InstanceNotFoundException;

    List<Curso> findByCiudadYFecha(Connection connection,
                                   String ciudad,
                                   LocalDateTime desde);

    void update(Connection connection, Curso curso)
            throws InstanceNotFoundException;

    void remove(Connection connection, Long cursoId)
            throws InstanceNotFoundException;
}

