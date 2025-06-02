package es.udc.ws.app.client.service.dto;

import java.util.Objects;

public class ClientInscripcionDto {

    private Long inscripcionId;    // ahora lo guardamos aquí
    private Long cursoId;
    private String emailUsuario;
    private String tarjetaCredito;

    // Constructor para crear una inscripción antes de enviarla al servicio:
    public ClientInscripcionDto(Long cursoId, String emailUsuario, String tarjetaCredito) {
        this.inscripcionId = null;
        this.cursoId = cursoId;
        this.emailUsuario = emailUsuario;
        this.tarjetaCredito = tarjetaCredito;
    }

    // Constructor “completo” para cuando recibamos la respuesta del servidor:
    public ClientInscripcionDto(Long inscripcionId,
                                Long cursoId,
                                String emailUsuario,
                                String tarjetaCredito) {
        this.inscripcionId = inscripcionId;
        this.cursoId = cursoId;
        this.emailUsuario = emailUsuario;
        this.tarjetaCredito = tarjetaCredito;
    }

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

    public String getTarjetaCredito() {
        return tarjetaCredito;
    }

    public void setTarjetaCredito(String tarjetaCredito) {
        this.tarjetaCredito = tarjetaCredito;
    }

}
