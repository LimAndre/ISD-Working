package es.udc.ws.app.model.cursoservice;

import java.time.LocalDateTime;
import java.util.List;

import es.udc.ws.app.model.curso.Curso;
import es.udc.ws.app.model.cursoservice.exceptions.CourseClosedException;
import es.udc.ws.app.model.cursoservice.exceptions.CourseFullException;
import es.udc.ws.app.model.inscripcion.Inscripcion;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface CursoService {


        Curso altaCurso(Curso curso)
                throws InputValidationException;

        void updateCurso(Curso curso)
                throws InputValidationException,
                InstanceNotFoundException;

        void removeCurso(Long cursoId)
                throws InstanceNotFoundException;

        Curso findCurso(Long cursoId)
                throws InstanceNotFoundException;

        List<Curso> buscarCursosByFechaYCiudad(String ciudad, LocalDateTime desde);


        Long inscribirUsuario(Long cursoId,
                              String emailUsuario,
                              String tarjetaPago)
                throws InstanceNotFoundException,
                InputValidationException,
                CourseClosedException,
                CourseFullException;

        Inscripcion findInscripcion(Long inscripcionId)
                throws InstanceNotFoundException;

}

