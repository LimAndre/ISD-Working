package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestInscripcionDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;

public class JsonToRestInscripcionDtoConversor {

    public static RestInscripcionDto toRestInscripcionDto(InputStream jsonInscripcion) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonInscripcion);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            }

            ObjectNode inscripcionObject = (ObjectNode) rootNode;

            Long idCurso = inscripcionObject.get("idCurso").longValue();
            String emailUsuario = inscripcionObject.get("emailUsuario").textValue().trim();
            String tarjetaCredito = inscripcionObject.get("tarjetaCredito").textValue().trim();

            return new RestInscripcionDto(idCurso, emailUsuario, tarjetaCredito);

        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}
