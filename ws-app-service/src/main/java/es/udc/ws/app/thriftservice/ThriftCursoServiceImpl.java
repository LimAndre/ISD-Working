package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.curso.Curso;
import es.udc.ws.app.model.cursoservice.CursoService;
import es.udc.ws.app.model.cursoservice.CursoServiceFactory;
import es.udc.ws.app.model.cursoservice.exceptions.CourseClosedException;
import es.udc.ws.app.model.cursoservice.exceptions.CourseFullException;
import es.udc.ws.app.model.inscripcion.Inscripcion;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import es.udc.ws.cursos.thrift.ThriftCursoService;
import es.udc.ws.cursos.thrift.ThriftInputValidationException;
import es.udc.ws.cursos.thrift.ThriftInstanceNotFoundException;
import es.udc.ws.cursos.thrift.ThriftCourseClosedException;
import es.udc.ws.cursos.thrift.ThriftCourseFullException;
import es.udc.ws.cursos.thrift.ThriftCursoDto;
import es.udc.ws.cursos.thrift.ThriftInscripcionDto;

import java.time.LocalDateTime;
import java.util.List;

public class ThriftCursoServiceImpl implements ThriftCursoService.Iface{


    @Override
    public ThriftCursoDto altaCurso(ThriftCursoDto cursoDto) throws ThriftInputValidationException {

        Curso curso = CursoToThriftCursoDtoConversor.toCurso(cursoDto);

        try {
            Curso addedCurso = CursoServiceFactory.getService().altaCurso(curso);
            return CursoToThriftCursoDtoConversor.toThriftCursoDto(addedCurso);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

    @Override
    public void updateCurso(ThriftCursoDto cursoDto) throws ThriftInputValidationException,
            ThriftInstanceNotFoundException {

        Curso curso = CursoToThriftCursoDtoConversor.toCurso(cursoDto);

        try {
            CursoServiceFactory.getService().updateCurso(curso);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(
                    e.getInstanceId().toString(), e.getInstanceType());
        }
    }

    @Override
    public void removeCurso(long cursoId) throws ThriftInstanceNotFoundException {

        try {
            CursoServiceFactory.getService().removeCurso(cursoId);
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(
                    e.getInstanceId().toString(), e.getInstanceType());
        }
    }

    @Override
    public ThriftCursoDto findCurso(long cursoId)
            throws ThriftInstanceNotFoundException {

        try {
            Curso curso = CursoServiceFactory.getService().findCurso(cursoId);
            return CursoToThriftCursoDtoConversor.toThriftCursoDto(curso);

        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(
                    e.getInstanceId().toString(), e.getInstanceType());
        }
    }

    @Override
    public List<ThriftCursoDto> buscarCursosByFechaYCiudad(String ciudad) {

        LocalDateTime desdeDt = LocalDateTime.now();

        List<Curso> cursos = CursoServiceFactory.getService().buscarCursosByFechaYCiudad(ciudad, desdeDt);

        return CursoToThriftCursoDtoConversor.toThriftCursoDtos(cursos);
    }

    @Override
    public long inscribirUsuario(long cursoId, String emailUsuario, String tarjetaCredito)
            throws ThriftInputValidationException,
            ThriftCourseClosedException,
            ThriftCourseFullException,
            ThriftInstanceNotFoundException {

        try {
            return CursoServiceFactory.getService().inscribirUsuario(cursoId, emailUsuario, tarjetaCredito);

        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());

        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(
                    e.getInstanceId().toString(), e.getInstanceType()
            );

        } catch (CourseClosedException e) {
            throw new ThriftCourseClosedException(e.getCursoId());

        } catch (CourseFullException e) {
            throw new ThriftCourseFullException(
                    e.getCursoId());
        }
    }

    @Override
    public ThriftInscripcionDto findInscripcion(long inscripcionId)
            throws ThriftInstanceNotFoundException {

        try {
            Inscripcion ins = CursoServiceFactory.getService().findInscripcion(inscripcionId);
            return InscripcionToThriftInscripcionDtoConversor.toThriftInscripcionDto(ins);

        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(
                    e.getInstanceId().toString(), e.getInstanceType());
        }
    }

}
