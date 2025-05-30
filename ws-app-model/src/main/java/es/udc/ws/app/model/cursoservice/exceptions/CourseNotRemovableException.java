package es.udc.ws.app.model.cursoservice.exceptions;

public class CourseNotRemovableException extends Exception{

    private Long cursoId;

    public CourseNotRemovableException(Long cursoId) {
        super("Curso with id=\"" + cursoId + "\n cannot be deleted because it is closed");
        this.cursoId = cursoId;
    }


    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
    }

}
