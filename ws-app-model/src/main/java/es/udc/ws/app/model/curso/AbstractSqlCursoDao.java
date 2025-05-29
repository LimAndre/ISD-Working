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

/**
 * Implementación base de SqlCursoDao con métodos comunes (find, update, remove, findBy...).
 * El método create() queda sin implementar para que lo defina la subclase concreta.
 */
public abstract class AbstractSqlCursoDao implements SqlCursoDao {

    public AbstractSqlCursoDao() {
    }

    @Override
    public Curso find(Connection connection, Long cursoId)
            throws InstanceNotFoundException {
        String sql =
                "SELECT cursoId, ciudad, nombre, fechaInicio, fechaAlta, precio, plazasMaximas " +
                        "FROM Curso WHERE cursoId = ?";

        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setLong(1, cursoId);

            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.next()) {
                    throw new InstanceNotFoundException(
                            cursoId, Curso.class.getName());
                }
                return extractCurso(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Curso> findByCiudadYFecha(Connection connection,
                                          String ciudad,
                                          LocalDateTime desde) {
        String sql =
                "SELECT cursoId, ciudad, nombre, fechaInicio, fechaAlta, precio, plazasMaximas " +
                        "FROM Curso WHERE ciudad = ? AND fechaInicio >= ? " +
                        "ORDER BY fechaInicio";

        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, ciudad);
            pst.setTimestamp(2, Timestamp.valueOf(desde));

            try (ResultSet rs = pst.executeQuery()) {
                List<Curso> cursos = new ArrayList<>();
                while (rs.next()) {
                    cursos.add(extractCurso(rs));
                }
                return cursos;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Connection connection, Curso curso)
            throws InstanceNotFoundException {
        String sql =
                "UPDATE Curso SET ciudad = ?, nombre = ?, fechaInicio = ?, " +
                        "fechaAlta = ?, precio = ?, plazasMaximas = ? " +
                        "WHERE cursoId = ?";

        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, curso.getCiudad());
            pst.setString(2, curso.getNombre());
            pst.setTimestamp(3, Timestamp.valueOf(curso.getFechaInicio()));
            pst.setTimestamp(4, Timestamp.valueOf(curso.getFechaAlta()));
            pst.setFloat(5, curso.getPrecio());
            pst.setInt(6, curso.getPlazasMaximas());
            pst.setLong(7, curso.getCursoId());

            int updated = pst.executeUpdate();
            if (updated == 0) {
                throw new InstanceNotFoundException(
                        curso.getCursoId(), Curso.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long cursoId)
            throws InstanceNotFoundException {
        String sql = "DELETE FROM Curso WHERE cursoId = ?";

        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setLong(1, cursoId);
            int removed = pst.executeUpdate();
            if (removed == 0) {
                throw new InstanceNotFoundException(
                        cursoId, Curso.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Extrae un objeto Curso del ResultSet en la fila actual.
     */
    private static Curso extractCurso(ResultSet rs) throws SQLException {
        Long id = rs.getLong("cursoId");
        String ciudad = rs.getString("ciudad");
        String nombre = rs.getString("nombre");
        LocalDateTime fechaInicio =
                rs.getTimestamp("fechaInicio").toLocalDateTime();
        LocalDateTime fechaAlta =
                rs.getTimestamp("fechaAlta").toLocalDateTime();
        float precio = rs.getFloat("precio");
        int plazasMax = rs.getInt("plazasMaximas");

        return new Curso(id, ciudad, nombre,
                fechaInicio, fechaAlta,
                precio, plazasMax);
    }

    // El método create() se deja abstracto para que la subclase lo implemente.
}
