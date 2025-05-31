package es.udc.ws.app.model.cursoservice.exceptions;

public class CourseClosedException extends Exception{

    private Long cursoId;

    public CourseClosedException(Long cursoId) {
        super("Curso with id=\"" + cursoId + "\n cannot be subscribed because it is started");
        this.cursoId = cursoId;
    }

}
