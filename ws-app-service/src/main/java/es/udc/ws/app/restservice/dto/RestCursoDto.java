package es.udc.ws.app.restservice.dto;

import java.time.LocalDateTime;

public class RestCursoDto {

    private Long idCurso;
    private String ciudad;
    private String nombreCurso;
    private LocalDateTime fechaInicio;
    private float precio;
    private int numeroPlazas;
    private int plazasDisponibles;

    public RestCursoDto() {}

    public RestCursoDto(Long idCurso, String ciudad, String nombreCurso,
                        LocalDateTime fechaInicio, float precio,
                        int numeroPlazas, int plazasDisponibles) {
        this.idCurso = idCurso;
        this.ciudad = ciudad;
        this.nombreCurso = nombreCurso;
        this.fechaInicio = fechaInicio;
        this.precio = precio;
        this.numeroPlazas = numeroPlazas;
        this.plazasDisponibles = plazasDisponibles;
    }

    public Long getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Long idCurso) {
        this.idCurso = idCurso;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getNumeroPlazas() {
        return numeroPlazas;
    }

    public void setNumeroPlazas(int numeroPlazas) {
        this.numeroPlazas = numeroPlazas;
    }

    public int getPlazasDisponibles() {
        return plazasDisponibles;
    }

    public void setPlazasDisponibles(int plazasDisponibles) {
        this.plazasDisponibles = plazasDisponibles;
    }
}
