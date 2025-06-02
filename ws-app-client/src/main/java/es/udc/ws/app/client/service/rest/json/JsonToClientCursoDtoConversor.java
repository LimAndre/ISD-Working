package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientCursoDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientCursoDtoConversor {

    public static ObjectNode toObjectNode(ClientCursoDto curso) throws IOException {

        ObjectNode cursoObject = JsonNodeFactory.instance.objectNode();

        // Si el ID no es nulo, lo añadimos (por ejemplo, para PUT)
        if (curso.getCursoId() != null) {
            cursoObject.put("cursoId", curso.getCursoId());
        }
        // Campo obligatorio: nombre del curso
        cursoObject.put("nombreCurso", curso.getNombreCurso())
                .put("ciudad", curso.getCiudad())
                // Fecha de inicio en formato ISO-8601 (e.g. "2025-05-15T10:00")
                .put("fechaInicio", curso.getFechaInicio().toString())
                // Número máximo de plazas
                .put("numeroPlazas", curso.getNumeroPlazas())
                // Precio del curso
                .put("precio", curso.getPrecio());

        return cursoObject;
    }


    public static ClientCursoDto toClientCursoDto(InputStream jsonCurso) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonCurso);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientCursoDto(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientCursoDto> toClientCursoDtos(InputStream jsonCursos) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonCursos);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode cursosArray = (ArrayNode) rootNode;
                List<ClientCursoDto> cursoDtos = new ArrayList<>(cursosArray.size());
                for (JsonNode cursoNode : cursosArray) {
                    cursoDtos.add(toClientCursoDto(cursoNode));
                }
                return cursoDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientCursoDto toClientCursoDto(JsonNode cursoNode) throws ParsingException {
        if (cursoNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode cursoObject = (ObjectNode) cursoNode;

            JsonNode cursoIdNode = cursoObject.get("idCurso");
            Long cursoId = (cursoIdNode != null && !cursoIdNode.isNull())
                    ? cursoIdNode.longValue()
                    : null;

            String nombre = cursoObject.get("nombreCurso").textValue().trim();
            String ciudad = cursoObject.get("ciudad").textValue().trim();
            String fechaInicioStr = cursoObject.get("fechaInicio").textValue().trim();
            java.time.LocalDateTime fechaInicio = java.time.LocalDateTime.parse(fechaInicioStr);

            int plazasMaximas = cursoObject.get("numeroPlazas").intValue();
            float precio = cursoObject.get("precio").floatValue();
            int plazasDisponibles = cursoObject.get("plazasDisponibles").intValue();

            return new ClientCursoDto(
                    cursoId,
                    ciudad,
                    nombre,
                    fechaInicio,
                    precio,
                    plazasMaximas,
                    plazasDisponibles
            );
        }
    }

}
