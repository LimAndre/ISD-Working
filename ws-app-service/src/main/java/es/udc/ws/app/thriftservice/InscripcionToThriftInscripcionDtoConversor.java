package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.inscripcion.Inscripcion;
import es.udc.ws.app.cursos.thrift.ThriftInscripcionDto;

public class InscripcionToThriftInscripcionDtoConversor {

    public static ThriftInscripcionDto toThriftInscripcionDto(Inscripcion inscripcion) {
        return new ThriftInscripcionDto(inscripcion.getCursoId(), inscripcion.getEmailUsuario(), inscripcion.getTarjetaPago());
    }

}
