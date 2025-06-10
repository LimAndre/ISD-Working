package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.curso.Curso;
import es.udc.ws.app.cursos.thrift.ThriftCursoDto;


import java.util.ArrayList;
import java.util.List;

public class CursoToThriftCursoDtoConversor {

    public static Curso toCurso(ThriftCursoDto curso){
        return new Curso(curso.getIdCurso(), curso.getCiudad(), curso.getNombreCurso(), curso.getFechaInicio(), curso.getPrecio(),
                curso.getNumeroPlazas(), curso.getPlazasDisponibles());
    }

    public static List<ThriftCursoDto> toThriftCursoDtos(List<Curso> cursos) {

        List<ThriftCursoDto> dtos = new ArrayList<>(cursos.size());

        for (Curso curso : cursos) {
            dtos.add(toThriftCursoDto(curso));
        }
        return dtos;

    }

    public static ThriftCursoDto toThriftCursoDto(Curso curso) {

        return new ThriftCursoDto(curso.getCursoId(), curso.getCiudad(), curso.getNombre(), curso.getFechaInicio(), curso.getPrecio(),
                curso.getPlazasMaximas(), curso.getPlazasDisponibles());

    }

}
