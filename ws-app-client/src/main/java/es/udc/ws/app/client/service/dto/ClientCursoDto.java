package es.udc.ws.app.client.service.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class ClientCursoDto {

    private Long cursoId;
    private String ciudad;
    private String nombreCurso;
    private LocalDateTime fechaInicio;
    private double precio;
    private int numeroPlazas;
    private int plazasDisponibles;

    public ClientCursoDto() {
    }

    public ClientCursoDto(Long cursoId, String ciudad, String nombreCurso,
                          LocalDateTime fechaInicio, double precio,
                          int numeroPlazas, int plazasDisponibles) {
        this.cursoId = cursoId;
        this.ciudad = ciudad;
        this.nombreCurso = nombreCurso;
        this.fechaInicio = fechaInicio;
        this.precio = precio;
        this.numeroPlazas = numeroPlazas;
        this.plazasDisponibles = plazasDisponibles;
    }

    public ClientCursoDto(Long cursoId, String ciudad, String nombreCurso,
                          LocalDateTime fechaInicio, double precio,
                          int numeroPlazas) {
        this.cursoId = cursoId;
        this.ciudad = ciudad;
        this.nombreCurso = nombreCurso;
        this.fechaInicio = fechaInicio;
        this.precio = precio;
        this.numeroPlazas = numeroPlazas;
    }


    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
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

    /**
     * Devuelve cuántas plazas ya están reservadas
     * (número total menos las que quedan disponibles).
     */
    public int getPlazasReservadas() {
        return this.numeroPlazas - this.plazasDisponibles;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cursoId, ciudad, nombreCurso, fechaInicio, precio, numeroPlazas, plazasDisponibles);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ClientCursoDto)) return false;
        ClientCursoDto other = (ClientCursoDto) obj;
        return Objects.equals(cursoId, other.cursoId)
                && Objects.equals(ciudad, other.ciudad)
                && Objects.equals(nombreCurso, other.nombreCurso)
                && Objects.equals(fechaInicio, other.fechaInicio)
                && Double.compare(precio, other.precio) == 0
                && numeroPlazas == other.numeroPlazas
                && plazasDisponibles == other.plazasDisponibles;
    }

    @Override
    public String toString() {
        return "ClientCursoDto{" +
                "cursoId=" + cursoId +
                ", ciudad='" + ciudad + '\'' +
                ", nombreCurso='" + nombreCurso + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", precio=" + precio +
                ", numeroPlazas=" + numeroPlazas +
                ", plazasDisponibles=" + plazasDisponibles +
                ", plazasReservadas=" + getPlazasReservadas() +
                '}';
    }
}
