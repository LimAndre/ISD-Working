package es.udc.ws.app.model.cursoservice.exceptions;

public class CourseFullException extends Exception {

    private final Long cursoId;

    public CourseFullException(Long cursoId) {
      super("No hay plazas disponibles para el curso con ID=" + cursoId);
      this.cursoId = cursoId;
    }

    public Long getCursoId() {
      return cursoId;
    }

}
