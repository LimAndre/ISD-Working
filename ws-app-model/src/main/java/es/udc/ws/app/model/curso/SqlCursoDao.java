package es.udc.ws.app.model.curso;

import java.sql.Connection;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlCursoDao {

    public Curso create(Connection connection, Curso curso);

    public Curso find(Connection connection, Long cursoId)
            throws InstanceNotFoundException;
}
