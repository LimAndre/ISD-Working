package es.udc.ws.app.client.service.exceptions;

public class ClientCourseClosedException extends Exception {

  private Long cursoId;

  public ClientCourseClosedException(Long cursoId) {
    super("El curso con id=\"" + cursoId + "\" no puede inscribirse porque ya ha comenzado.");
    this.cursoId = cursoId;
  }

  public Long getCursoId() {
    return cursoId;
  }

}
