package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestCursoDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

public class JsonToRestCursoDtoConversor {

    public static ObjectNode toObjectNode(RestCursoDto curso) {

        ObjectNode cursoObject = JsonNodeFactory.instance.objectNode();

        cursoObject.put("idCurso", curso.getIdCurso())
                .put("ciudad", curso.getCiudad())
                .put("nombreCurso", curso.getNombreCurso())
                .put("fechaInicio", curso.getFechaInicio().toString())
                .put("precio", curso.getPrecio())
                .put("numeroPlazas", curso.getNumeroPlazas())
                .put("plazasDisponibles", curso.getPlazasDisponibles());

        return cursoObject;
    }

    public static ArrayNode toArrayNode(List<RestCursoDto> cursos) {

        ArrayNode cursosNode = JsonNodeFactory.instance.arrayNode();
        for (RestCursoDto curso : cursos) {
            ObjectNode cursoObject = toObjectNode(curso);
            cursosNode.add(cursoObject);
        }

        return cursosNode;
    }

    public static RestCursoDto toRestCursoDto(InputStream jsonCurso) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonCurso);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            }

            ObjectNode cursoObject = (ObjectNode) rootNode;

            JsonNode idCursoNode = cursoObject.get("idCurso");
            Long idCurso = (idCursoNode != null && !idCursoNode.isNull()) ? idCursoNode.longValue() : null;

            String ciudad = cursoObject.get("ciudad").textValue().trim();
            String nombreCurso = cursoObject.get("nombreCurso").textValue().trim();
            LocalDateTime fechaInicio = LocalDateTime.parse(cursoObject.get("fechaInicio").textValue().trim());
            float precio = cursoObject.get("precio").floatValue();
            int numeroPlazas = cursoObject.get("numeroPlazas").intValue();

            return new RestCursoDto(idCurso, ciudad, nombreCurso, fechaInicio, precio, numeroPlazas, 0);

        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

}
