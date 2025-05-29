package es.udc.ws.app.model.cursoservice;

import java.util.List;

import es.udc.ws.app.model.curso.Curso;
import es.udc.ws.app.model.inscripcion.Inscripcion;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface CursoService {

    public Curso addCurso(Curso curso) throws InputValidationException;

    public Curso findCurso(Long cursoId) throws InstanceNotFoundException;

    public List<Curso> findCursos(String keywords);

    public Inscripcion inscribirseCurso(Long cursoId, String userId, String creditCardNumber)
            throws InstanceNotFoundException, InputValidationException;

}
