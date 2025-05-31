package es.udc.ws.app.restservice.servlets;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import es.udc.ws.app.model.cursoservice.CursoServiceFactory;
import es.udc.ws.app.model.cursoservice.exceptions.CourseClosedException;
import es.udc.ws.app.model.cursoservice.exceptions.CourseFullException;
import es.udc.ws.app.restservice.dto.RestInscripcionDto;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestInscripcionDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class InscripcionesServlet extends RestHttpServletTemplate {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, InputValidationException, InstanceNotFoundException {

        ServletUtils.checkEmptyPath(req);

        try {
            RestInscripcionDto dto = JsonToRestInscripcionDtoConversor.toRestInscripcionDto(req.getInputStream());

            Long inscripcionId = CursoServiceFactory.getService().inscribirUsuario(
                    dto.getIdCurso(),
                    dto.getEmailUsuario(),
                    dto.getTarjetaCredito()
            );

            // Respuesta con el ID como texto plano JSON
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                    JsonNodeFactory.instance.textNode(inscripcionId.toString()), null);

        } catch (CourseClosedException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_GONE,
                    AppExceptionToJsonConversor.toCourseClosedException(e), null);
        } catch (CourseFullException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                    AppExceptionToJsonConversor.toCourseFullException(e), null);
        }
    }

}
