package es.udc.ws.app.model.inscripcion;

import java.time.LocalDateTime;
import java.util.Objects;

public class Inscripcion {

    private Long inscripcionId;
    private Long cursoId;
    private String emailUsuario;
    private String tarjetaPago;
    private LocalDateTime fechaInscripcion;

    /** Constructor completo (incluye ID). */
    public Inscripcion(Long inscripcionId, Long cursoId,
                       String emailUsuario, String tarjetaPago,
                       LocalDateTime fechaInscripcion) {
        this.inscripcionId = inscripcionId;
        this.cursoId = cursoId;
        this.emailUsuario = emailUsuario;
        this.tarjetaPago = tarjetaPago;
        this.fechaInscripcion = (fechaInscripcion != null)
                ? fechaInscripcion.withNano(0) : null;
    }

    /** Constructor para creación (sin ID). */
    public Inscripcion(Long cursoId, String emailUsuario,
                       String tarjetaPago, LocalDateTime fechaInscripcion) {
        this(null, cursoId, emailUsuario, tarjetaPago, fechaInscripcion);
    }

    /* Getters y setters */

    public Long getInscripcionId() {
        return inscripcionId;
    }

    public void setInscripcionId(Long inscripcionId) {
        this.inscripcionId = inscripcionId;
    }

    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getTarjetaPago() {
        return tarjetaPago;
    }

    public void setTarjetaPago(String tarjetaPago) {
        this.tarjetaPago = tarjetaPago;
    }

    public LocalDateTime getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDateTime fechaInscripcion) {
        this.fechaInscripcion = (fechaInscripcion != null)
                ? fechaInscripcion.withNano(0) : null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(inscripcionId, cursoId, emailUsuario,
                tarjetaPago, fechaInscripcion);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Inscripcion))
            return false;
        Inscripcion other = (Inscripcion) obj;
        return Objects.equals(inscripcionId, other.inscripcionId)
                && Objects.equals(cursoId, other.cursoId)
                && Objects.equals(emailUsuario, other.emailUsuario)
                && Objects.equals(tarjetaPago, other.tarjetaPago)
                && Objects.equals(fechaInscripcion, other.fechaInscripcion);
    }

    @Override
    public String toString() {
        return "Inscripcion [inscripcionId=" + inscripcionId
                + ", cursoId=" + cursoId
                + ", emailUsuario=" + emailUsuario
                + ", tarjetaPago=" + tarjetaPago
                + ", fechaInscripcion=" + fechaInscripcion
                + "]";
    }
}

