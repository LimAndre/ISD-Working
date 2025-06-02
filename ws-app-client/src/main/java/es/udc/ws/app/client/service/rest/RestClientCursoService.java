package es.udc.ws.app.client.service.rest;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.ws.app.client.service.ClientCursoService;
import es.udc.ws.app.client.service.dto.ClientCursoDto;
import es.udc.ws.app.client.service.exceptions.ClientCourseClosedException;
import es.udc.ws.app.client.service.exceptions.ClientCourseFullException;
import es.udc.ws.app.client.service.rest.json.JsonToClientCursoDtoConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientExceptionConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientInscripcionDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import org.apache.hc.client5.http.fluent.Form;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpStatus;
import es.udc.ws.app.client.service.dto.ClientInscripcionDto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

public class RestClientCursoService implements ClientCursoService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientCursoService.endpointAddress";
    private String endpointAddress;

    @Override
    public Long addCurso(ClientCursoDto curso) throws InputValidationException {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "cursos")
                    .bodyStream(toInputStream(curso), ContentType.create("application/json"))
                    .execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);
            return JsonToClientCursoDtoConversor.toClientCursoDto(response.getEntity().getContent()).getCursoId();
        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientCursoDto> findCursos(String ciudad) throws InputValidationException {
        try {
            String url = getEndpointAddress() + "cursos?ciudad=" + URLEncoder.encode(ciudad, "UTF-8");
            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(url).execute().returnResponse();
            validateStatusCode(HttpStatus.SC_OK, response);
            return JsonToClientCursoDtoConversor.toClientCursoDtos(response.getEntity().getContent());
        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long inscribirCurso(ClientInscripcionDto inscripcionDto)
            throws InputValidationException, InstanceNotFoundException,
            ClientCourseClosedException, ClientCourseFullException {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "inscripciones")
                    .bodyForm(Form.form()
                            .add("cursoId", inscripcionDto.getCursoId().toString())
                            .add("email", inscripcionDto.getEmailUsuario())
                            .add("tarjeta", inscripcionDto.getTarjetaCredito())
                            .build())
                    .execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);
            return JsonToClientInscripcionDtoConversor.toClientInscripcionDto(
                    response.getEntity().getContent()).getInscripcionId();
        } catch (InputValidationException | InstanceNotFoundException |
                 ClientCourseClosedException | ClientCourseFullException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

    private InputStream toInputStream(ClientCursoDto curso) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientCursoDtoConversor.toObjectNode(curso));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void validateStatusCode(int successCode, ClassicHttpResponse response) throws Exception {

        try {

            int statusCode = response.getCode();

            /* Success? */
            if (statusCode == successCode) {
                return;
            }

            /* Handler error. */
            switch (statusCode) {
                case HttpStatus.SC_NOT_FOUND -> throw JsonToClientExceptionConversor.fromNotFoundErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_BAD_REQUEST -> throw JsonToClientExceptionConversor.fromBadRequestErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_FORBIDDEN -> throw JsonToClientExceptionConversor.fromForbiddenErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_GONE -> throw JsonToClientExceptionConversor.fromGoneErrorCode(
                        response.getEntity().getContent());
                default -> throw new RuntimeException("HTTP error; status code = "
                        + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
