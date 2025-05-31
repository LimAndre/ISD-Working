package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.model.cursoservice.exceptions.CourseClosedException;
import es.udc.ws.app.model.cursoservice.exceptions.CourseFullException;

public class AppExceptionToJsonConversor {


    public static ObjectNode toCourseClosedException(CourseClosedException ex) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "CourseClosed");
        exceptionObject.put("cursoId", (ex.getCursoId() != null) ? ex.getCursoId() : null);

        return exceptionObject;
    }

    public static ObjectNode toCourseFullException(CourseFullException ex) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "CourseFull");
        exceptionObject.put("cursoId", ex.getCursoId());

        return exceptionObject;
    }

}
