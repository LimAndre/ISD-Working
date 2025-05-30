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

        String queryString =
                "SELECT ciudad, nombre, fechaInicio, fechaAlta, precio, plazasMaximas " +
                        "FROM Curso WHERE cursoId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setLong(i++, cursoId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (!resultSet.next()) {
                    throw new InstanceNotFoundException(cursoId,
                            Curso.class.getName());
                }

                i = 1;
                String ciudad = resultSet.getString(i++);
                String nombre = resultSet.getString(i++);
                Timestamp fechaInicioTs = resultSet.getTimestamp(i++);
                LocalDateTime fechaInicio =
                        fechaInicioTs.toLocalDateTime();
                Timestamp fechaAltaTs = resultSet.getTimestamp(i++);
                LocalDateTime fechaAlta =
                        fechaAltaTs.toLocalDateTime();
                float precio = resultSet.getFloat(i++);
                int plazasMaximas = resultSet.getInt(i++);

                return new Curso(cursoId, ciudad, nombre,
                        fechaInicio, fechaAlta,
                        precio, plazasMaximas);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Curso> findByCiudadYFecha(Connection connection,
                                          String ciudad,
                                          LocalDateTime desde) {

        String queryString =
                "SELECT cursoId, ciudad, nombre, fechaInicio, fechaAlta, precio, plazasMaximas " +
                        "FROM Curso WHERE ciudad = ? AND fechaInicio >= ? ORDER BY fechaInicio";

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setString(i++, ciudad);
            preparedStatement.setTimestamp(i++,
                    Timestamp.valueOf(desde));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Curso> cursos = new ArrayList<>();
                while (resultSet.next()) {
                    i = 1;
                    Long id = resultSet.getLong(i++);
                    String ciu = resultSet.getString(i++);
                    String nom = resultSet.getString(i++);
                    Timestamp fechaIniTs = resultSet.getTimestamp(i++);
                    LocalDateTime fechaIni = fechaIniTs.toLocalDateTime();
                    Timestamp fechaAltTs = resultSet.getTimestamp(i++);
                    LocalDateTime fechaAlt = fechaAltTs.toLocalDateTime();
                    float precio = resultSet.getFloat(i++);
                    int plazasMax = resultSet.getInt(i++);

                    cursos.add(new Curso(id, ciu, nom,
                            fechaIni, fechaAlt,
                            precio, plazasMax));
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

        String queryString =
                "UPDATE Curso SET ciudad = ?, nombre = ?, fechaInicio = ?, " +
                        "fechaAlta = ?, precio = ?, plazasMaximas = ? WHERE cursoId = ?";

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setString(i++, curso.getCiudad());
            preparedStatement.setString(i++, curso.getNombre());
            preparedStatement.setTimestamp(i++,
                    Timestamp.valueOf(curso.getFechaInicio()));
            preparedStatement.setTimestamp(i++,
                    Timestamp.valueOf(curso.getFechaAlta()));
            preparedStatement.setFloat(i++, curso.getPrecio());
            preparedStatement.setInt(i++, curso.getPlazasMaximas());
            preparedStatement.setLong(i++, curso.getCursoId());

            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows == 0) {
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

        String queryString =
                "DELETE FROM Curso WHERE cursoId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setLong(i++, cursoId);

            int removedRows = preparedStatement.executeUpdate();
            if (removedRows == 0) {
                throw new InstanceNotFoundException(
                        cursoId, Curso.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
