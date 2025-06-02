package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientInscripcionDto;
import es.udc.ws.app.client.service.exceptions.ClientCourseClosedException;
import es.udc.ws.app.client.service.exceptions.ClientCourseFullException;
import es.udc.ws.util.exceptions.InputValidationException;

import es.udc.ws.app.client.service.dto.ClientCursoDto;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.List;

public interface ClientCursoService {

    Long addCurso(ClientCursoDto cursoDto) throws InputValidationException;

    List<ClientCursoDto> findCursos(String ciudad) throws InputValidationException;

    Long inscribirCurso(ClientInscripcionDto inscripcionDto)
            throws InputValidationException,
            InstanceNotFoundException,
            ClientCourseClosedException,
            ClientCourseFullException;

}
