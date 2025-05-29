package es.udc.ws.app.model.inscripcion;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlInscripcionDao implements SqlInscripcionDao {

    protected AbstractSqlInscripcionDao() {
    }

    @Override
    public Inscripcion find(Connection connection, Long inscripcionId)
            throws InstanceNotFoundException {

        String queryString =
                "SELECT cursoId, emailUsuario, tarjetaPago, fechaInscripcion " +
                        "FROM Inscripcion WHERE inscripcionId = ?";

        try (PreparedStatement pst = connection.prepareStatement(queryString)) {

            int i = 1;
            pst.setLong(i++, inscripcionId);

            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.next()) {
                    throw new InstanceNotFoundException(inscripcionId,
                            Inscripcion.class.getName());
                }

                i = 1;
                Long cursoId = rs.getLong(i++);
                String email = rs.getString(i++);
                String tarjeta = rs.getString(i++);
                Timestamp ts = rs.getTimestamp(i++);
                LocalDateTime fechaInscripcion = ts.toLocalDateTime();

                return new Inscripcion(inscripcionId, cursoId,
                        email, tarjeta, fechaInscripcion);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsByCurso(Connection connection, Long cursoId) {

        String queryString =
                "SELECT COUNT(*) FROM Inscripcion WHERE cursoId = ?";

        try (PreparedStatement pst = connection.prepareStatement(queryString)) {

            int i = 1;
            pst.setLong(i++, cursoId);

            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Error retrieving number of inscripciones for curso " + cursoId);
                }

                Long count = rs.getLong(1);
                return count > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Inscripcion> findByCurso(Connection connection, Long cursoId) {

        String queryString =
                "SELECT inscripcionId, cursoId, emailUsuario, tarjetaPago, fechaInscripcion " +
                        "FROM Inscripcion WHERE cursoId = ? ORDER BY fechaInscripcion";

        try (PreparedStatement pst = connection.prepareStatement(queryString)) {

            pst.setLong(1, cursoId);

            try (ResultSet rs = pst.executeQuery()) {
                List<Inscripcion> lista = new ArrayList<>();
                while (rs.next()) {
                    int i = 1;
                    Long id = rs.getLong(i++);
                    Long cId = rs.getLong(i++);
                    String email = rs.getString(i++);
                    String tarjeta = rs.getString(i++);
                    Timestamp ts = rs.getTimestamp(i++);
                    LocalDateTime fechaInscripcion = ts.toLocalDateTime();

                    lista.add(new Inscripcion(id, cId, email, tarjeta, fechaInscripcion));
                }
                return lista;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Connection connection, Inscripcion inscripcion)
            throws InstanceNotFoundException {

        String queryString =
                "UPDATE Inscripcion SET cursoId = ?, emailUsuario = ?, tarjetaPago = ?, fechaInscripcion = ? " +
                        "WHERE inscripcionId = ?";

        try (PreparedStatement pst = connection.prepareStatement(queryString)) {

            int i = 1;
            pst.setLong(i++, inscripcion.getCursoId());
            pst.setString(i++, inscripcion.getEmailUsuario());
            pst.setString(i++, inscripcion.getTarjetaPago());
            pst.setTimestamp(i++, Timestamp.valueOf(inscripcion.getFechaInscripcion()));
            pst.setLong(i++, inscripcion.getInscripcionId());

            int updated = pst.executeUpdate();
            if (updated == 0) {
                throw new InstanceNotFoundException(
                        inscripcion.getInscripcionId(), Inscripcion.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long inscripcionId)
            throws InstanceNotFoundException {

        String queryString = "DELETE FROM Inscripcion WHERE inscripcionId = ?";

        try (PreparedStatement pst = connection.prepareStatement(queryString)) {

            int i = 1;
            pst.setLong(i++, inscripcionId);

            int removed = pst.executeUpdate();
            if (removed == 0) {
                throw new InstanceNotFoundException(
                        inscripcionId, Inscripcion.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
