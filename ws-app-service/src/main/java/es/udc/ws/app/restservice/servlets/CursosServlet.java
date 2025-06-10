package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.curso.Curso;
import es.udc.ws.app.model.cursoservice.CursoService;
import es.udc.ws.app.model.cursoservice.CursoServiceFactory;
import es.udc.ws.app.restservice.dto.CursoToRestCursoDtoConversor;
import es.udc.ws.app.restservice.dto.RestCursoDto;
import es.udc.ws.app.restservice.json.JsonToRestCursoDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.json.exceptions.ParsingException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CursosServlet extends RestHttpServletTemplate {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, InputValidationException, ParsingException {

        // Asegurarse de que no hay nada en la URL
        ServletUtils.checkEmptyPath(req);

        // Leer JSON del cliente y convertir a DTO
        RestCursoDto cursoDto = JsonToRestCursoDtoConversor.toRestCursoDto(req.getInputStream());

        // Convertir DTO a entidad del modelo
        Curso curso = CursoToRestCursoDtoConversor.toCurso(cursoDto);

        // Lógica de negocio: alta curso
        CursoService cursoService = CursoServiceFactory.getService();
        curso = cursoService.altaCurso(curso);

        // Volver a convertir a DTO para la respuesta (con ID y plazas disponibles)
        cursoDto = CursoToRestCursoDtoConversor.toRestCursoDto(curso);

        // Construir la URL del nuevo recurso
        String cursoURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + curso.getCursoId();

        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", cursoURL);

        // Responder con 201 Created y el curso como JSON
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                JsonToRestCursoDtoConversor.toObjectNode(cursoDto), headers);
    }


    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, InputValidationException {

        ServletUtils.checkEmptyPath(req);

        String ciudad = req.getParameter("ciudad");
        if (ciudad == null || ciudad.trim().isEmpty()) {
            throw new InputValidationException("Debe proporcionar una ciudad para buscar cursos.");
        }

        // Obtener fecha actual sin tiempo
        LocalDateTime fechaActual = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);

        List<Curso> cursos = CursoServiceFactory.getService()
                .buscarCursosByFechaYCiudad(ciudad.trim(), fechaActual);

        List<RestCursoDto> cursoDtos = CursoToRestCursoDtoConversor.toRestCursoDtos(cursos);

        ServletUtils.writeServiceResponse(
                resp,
                HttpServletResponse.SC_OK,
                JsonToRestCursoDtoConversor.toArrayNode(cursoDtos),
                null
        );
    }

}
