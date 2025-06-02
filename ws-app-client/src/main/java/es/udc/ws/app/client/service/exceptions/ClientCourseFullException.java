package es.udc.ws.app.client.service.exceptions;

public class ClientCourseFullException extends Exception {

  private Long cursoId;

  public ClientCourseFullException(Long cursoId) {
    super("El curso con id=\"" + cursoId + "\" no puede inscribirse porque no quedan plazas disponibles.");
    this.cursoId = cursoId;
  }

  public Long getCursoId() {
    return cursoId;
  }

}
