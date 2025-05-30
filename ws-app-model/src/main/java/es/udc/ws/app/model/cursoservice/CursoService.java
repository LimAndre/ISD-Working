package es.udc.ws.app.model.cursoservice;

import java.time.LocalDateTime;
import java.util.List;

import es.udc.ws.app.model.curso.Curso;
import es.udc.ws.app.model.cursoservice.exceptions.CourseClosedException;
import es.udc.ws.app.model.cursoservice.exceptions.CourseNotRemovableException;
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
                throws InstanceNotFoundException,
                CourseNotRemovableException;

        Curso findCurso(Long cursoId)
                throws InstanceNotFoundException;

        List<Curso> buscarCursos(String ciudad, LocalDateTime desde);


        Long inscribirUsuario(Long cursoId,
                              String emailUsuario,
                              String tarjetaPago)
                throws InstanceNotFoundException,
                InputValidationException,
                CourseClosedException;

        Inscripcion findInscripcion(Long inscripcionId)
                throws InstanceNotFoundException;

}

