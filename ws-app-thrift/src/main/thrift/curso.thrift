namespace java es.udc.ws.cursos.thrift

struct ThriftCursoDto {
    1: i64 idCurso
    2: string ciudad
    3: string nombreCurso
    4: string fechaInicio
    5: double precio
    6: i32 numeroPlazas
    7: i32 plazasDisponibles
}

struct ThriftInscripcionDto {
    1: i64 cursoId
    2: string emailUsuario
    3: string tarjetaCredito

}

exception ThriftInputValidationException {
    1: string message
}

exception ThriftInstanceNotFoundException {
    1: string instanceId
    2: string instanceType
}

exception ThriftCourseFullException {
    1: i64 cursoId
}

exception ThriftCourseClosedException {
    1: i64 cursoId
}

service ThriftCursoService {

   ThriftCursoDto altaCurso(1: ThriftCursoDto cursoDto) throws (1: ThriftInputValidationException e)

   void updateCurso(1: ThriftCursoDto cursoDto) throws (1: ThriftInputValidationException e, 2: ThriftInstanceNotFoundException ee)

   void removeCurso(1: i64 cursoId) throws (1: ThriftInstanceNotFoundException e)

   ThriftCursoDto findCurso(1: i64 cursoId) throws (1: ThriftInstanceNotFoundException e)

   list<ThriftCursoDto> buscarCursosByFechaYCiudad (1: string ciudad)

   i64 inscribirUsuario(1: i64 cursoId, 2: string emailUsuario, 3: string tarjetaCredito) throws (1: ThriftInputValidationException e, 2: ThriftCourseClosedException ee, 3:ThriftCourseFullException eee)

   ThriftInscripcionDto findInscripcion(1: i64 inscripcionId) throws (1: ThriftInstanceNotFoundException e)
}