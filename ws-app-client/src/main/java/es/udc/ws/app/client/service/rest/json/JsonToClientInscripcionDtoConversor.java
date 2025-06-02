package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientInscripcionDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;

public class JsonToClientInscripcionDtoConversor {

    public static ClientInscripcionDto toClientInscripcionDto(InputStream jsonInscripcion)
            throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonInscripcion);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode inscripcionObject = (ObjectNode) rootNode;

                Long cursoId = inscripcionObject.get("cursoId").longValue();
                String email = inscripcionObject.get("email").textValue().trim();
                String tarjeta = inscripcionObject.get("tarjeta").textValue().trim();

                return new ClientInscripcionDto(cursoId, email, tarjeta);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

}
