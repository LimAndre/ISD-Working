package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientCursoDto;
import es.udc.ws.cursos.thrift.ThriftCursoDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientCursoDtoToThriftCursoDtoConversor {

    public static ThriftCursoDto toThriftCursoDto(ClientCursoDto curso) {
        return new ThriftCursoDto(
                curso.getCursoId(),
                curso.getCiudad(),
                curso.getNombreCurso(),
                curso.getFechaInicio().toString(),
                curso.getPrecio(),
                curso.getNumeroPlazas(),
                curso.getPlazasDisponibles());
    }

    public static ClientCursoDto toClientCursoDto(ThriftCursoDto curso) {
        LocalDateTime fechaInicio = LocalDateTime.parse(curso.getFechaInicio());
        return new ClientCursoDto(
                curso.getIdCurso(),
                curso.getCiudad(),
                curso.getNombreCurso(),
                fechaInicio,
                curso.getPrecio(),
                curso.getNumeroPlazas(),
                curso.getPlazasDisponibles());
    }

    public static List<ClientCursoDto> toClientCursoDtos(List<ThriftCursoDto> cursos) {
        List<ClientCursoDto> results = new ArrayList<>(cursos.size());
        for (ThriftCursoDto curso : cursos) {
            results.add(toClientCursoDto(curso));
        }
        return results;
    }

}
