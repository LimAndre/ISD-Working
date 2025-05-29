package es.udc.ws.app.model.curso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlCursoDao implements SqlCursoDao {

    protected AbstractSqlCursoDao() {
    }

    @Override
    public Curso find(Connection connection, Long cursoId) throws InstanceNotFoundException {
        return null;
    }
}
