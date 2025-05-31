package es.udc.ws.app.restservice.dto;

public class RestInscripcionDto {

    private Long idCurso;
    private String emailUsuario;
    private String tarjetaCredito;

    public RestInscripcionDto() {}

    public RestInscripcionDto(Long idCurso, String emailUsuario, String tarjetaCredito) {
        this.idCurso = idCurso;
        this.emailUsuario = emailUsuario;
        this.tarjetaCredito = tarjetaCredito;
    }

    public Long getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Long idCurso) {
        this.idCurso = idCurso;
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
