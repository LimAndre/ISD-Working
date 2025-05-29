package es.udc.ws.app.model.inscripcion;

import java.sql.*;

public class Jdbc3CcSqlIncripcionDao extends AbstractSqlInscripcionDao{

    @Override
    public Inscripcion create(Connection connection, Inscripcion inscripcion) {
        String queryString =
                "INSERT INTO Inscripcion (cursoId, emailUsuario, tarjetaPago, fechaInscripcion)"
                        + " VALUES (?, ?, ?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            pst.setLong(i++, inscripcion.getCursoId());
            pst.setString(i++, inscripcion.getEmailUsuario());
            pst.setString(i++, inscripcion.getTarjetaPago());
            pst.setTimestamp(i++, Timestamp.valueOf(inscripcion.getFechaInscripcion()));

            pst.executeUpdate();

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (!rs.next()) {
                    throw new SQLException(
                            "JDBC driver did not return generated key.");
                }
                Long id = rs.getLong(1);
                return new Inscripcion(id,
                        inscripcion.getCursoId(),
                        inscripcion.getEmailUsuario(),
                        inscripcion.getTarjetaPago(),
                        inscripcion.getFechaInscripcion());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
