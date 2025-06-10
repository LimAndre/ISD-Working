package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.curso.Curso;
import es.udc.ws.cursos.thrift.ThriftCursoDto;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CursoToThriftCursoDtoConversor {

    public static Curso toCurso(ThriftCursoDto curso){
        LocalDateTime fechaInicio = LocalDateTime.parse(curso.getFechaInicio());

        Curso resultado = new Curso(curso.getCiudad(), curso.getNombreCurso(),
                fechaInicio, (float) curso.getPrecio(), curso.getNumeroPlazas());
        resultado.setCursoId(curso.getIdCurso());
        resultado.setPlazasDisponibles(curso.getPlazasDisponibles());
        return resultado;
    }

    public static List<ThriftCursoDto> toThriftCursoDtos(List<Curso> cursos) {

        List<ThriftCursoDto> dtos = new ArrayList<>(cursos.size());

        for (Curso curso : cursos) {
            dtos.add(toThriftCursoDto(curso));
        }
        return dtos;

    }

    public static ThriftCursoDto toThriftCursoDto(Curso curso) {

        return new ThriftCursoDto(curso.getCursoId(), curso.getCiudad(), curso.getNombre(),
                curso.getFechaInicio().toString(), curso.getPrecio(),
                curso.getPlazasMaximas(), curso.getPlazasDisponibles());

    }

}
