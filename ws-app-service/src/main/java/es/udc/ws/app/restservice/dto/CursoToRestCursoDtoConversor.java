package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.curso.Curso;

import java.util.ArrayList;
import java.util.List;

public class CursoToRestCursoDtoConversor {

    public static RestCursoDto toRestCursoDto(Curso curso) {
        return new RestCursoDto(
                curso.getCursoId(),
                curso.getCiudad(),
                curso.getNombre(),
                curso.getFechaInicio(),
                curso.getPrecio(),
                curso.getPlazasMaximas(),
                curso.getPlazasDisponibles()
        );
    }

    public static List<RestCursoDto> toRestCursoDtos(List<Curso> cursos) {
        List<RestCursoDto> cursoDtos = new ArrayList<>(cursos.size());

        for (Curso curso : cursos) {
            cursoDtos.add(toRestCursoDto(curso));
        }

        return cursoDtos;
    }

    public static Curso toCurso(RestCursoDto cursoDto) {
        return new Curso(
                cursoDto.getCiudad(),
                cursoDto.getNombreCurso(),
                cursoDto.getFechaInicio(),
                cursoDto.getPrecio(),
                cursoDto.getNumeroPlazas()

        );
    }

}
