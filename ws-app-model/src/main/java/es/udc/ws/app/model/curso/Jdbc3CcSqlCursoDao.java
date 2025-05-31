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

            int i = 1;
            pst.setString(i++, curso.getCiudad());
            pst.setString(i++, curso.getNombre());
            pst.setTimestamp(i++, Timestamp.valueOf(curso.getFechaInicio()));
            pst.setTimestamp(i++, Timestamp.valueOf(curso.getFechaAlta()));
            pst.setFloat(i++, curso.getPrecio());
            pst.setInt(i++, curso.getPlazasMaximas());

            int affectedRows = pst.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating curso failed, no rows affected.");
            }

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    // Devolvemos un nuevo objeto Curso con el ID generado
                    return new Curso(id,
                            curso.getCiudad(),
                            curso.getNombre(),
                            curso.getFechaInicio(),
                            curso.getFechaAlta(),
                            curso.getPrecio(),
                            curso.getPlazasMaximas());
                } else {
                    throw new SQLException("Creating curso failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear curso", e);
        }
    }
}
