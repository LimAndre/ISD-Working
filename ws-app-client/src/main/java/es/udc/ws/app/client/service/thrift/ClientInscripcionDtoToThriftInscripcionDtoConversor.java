package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientInscripcionDto;
import es.udc.ws.cursos.thrift.ThriftInscripcionDto;

public class ClientInscripcionDtoToThriftInscripcionDtoConversor {

    public static ThriftInscripcionDto toThriftInscripcionDto(ClientInscripcionDto inscripcion) {
        return new ThriftInscripcionDto(
                inscripcion.getCursoId(),
                inscripcion.getEmailUsuario(),
                inscripcion.getTarjetaCredito());
    }
}

