package es.udc.ws.app.model.curso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Jdbc3CcSqlCursoDao extends AbstractSqlCursoDao{

    @Override
    public Curso create(Connection connection, Curso curso) {
        String sql =
                "INSERT INTO Curso (ciudad, nombre, fechaInicio, fechaAlta, precio, plazasMaximas) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, curso.getCiudad());
            pst.setString(2, curso.getNombre());
            pst.setTimestamp(3, Timestamp.valueOf(curso.getFechaInicio()));
            pst.setTimestamp(4, Timestamp.valueOf(curso.getFechaAlta()));
            pst.setFloat(5, curso.getPrecio());
            pst.setInt(6, curso.getPlazasMaximas());

            int affectedRows = pst.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating curso failed, no rows affected.");
            }

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    curso.setCursoId(id);
                } else {
                    throw new SQLException("Creating curso failed, no ID obtained.");
                }
            }

            return curso;

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear curso", e);
        }
    }

}
