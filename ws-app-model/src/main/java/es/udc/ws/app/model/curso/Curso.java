package es.udc.ws.app.model.curso;

import java.time.LocalDateTime;
import java.util.Objects;

public class Curso {

    private Long cursoId;
    private String ciudad;
    private String nombre;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaAlta;
    private float precio;
    private int plazasMaximas;

    /** Constructor completo (incluye ID). */
    public Curso(Long cursoId, String ciudad, String nombre,
                 LocalDateTime fechaInicio, LocalDateTime fechaAlta,
                 float precio, int plazasMaximas) {
        this.cursoId = cursoId;
        this.ciudad = ciudad;
        this.nombre = nombre;
        // Truncamos la parte nanosegundos para evitar discrepancias con DATETIME
        this.fechaInicio = (fechaInicio != null) ? fechaInicio.withNano(0) : null;
        this.fechaAlta  = (fechaAlta  != null) ? fechaAlta.withNano(0)  : null;
        this.precio = precio;
        this.plazasMaximas = plazasMaximas;
    }

    /** Constructor para creación (sin ID). */
    public Curso(String ciudad, String nombre,
                 LocalDateTime fechaInicio, LocalDateTime fechaAlta,
                 float precio, int plazasMaximas) {
        this(null, ciudad, nombre, fechaInicio, fechaAlta, precio, plazasMaximas);
    }

    /* Getters y setters */

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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = (fechaInicio != null)
                ? fechaInicio.withNano(0) : null;
    }

    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDateTime fechaAlta) {
        this.fechaAlta = (fechaAlta != null)
                ? fechaAlta.withNano(0) : null;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getPlazasMaximas() {
        return plazasMaximas;
    }

    public void setPlazasMaximas(int plazasMaximas) {
        this.plazasMaximas = plazasMaximas;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cursoId, ciudad, nombre, fechaInicio,
                fechaAlta, precio, plazasMaximas);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Curso))
            return false;
        Curso other = (Curso) obj;
        return Objects.equals(cursoId, other.cursoId)
                && Objects.equals(ciudad, other.ciudad)
                && Objects.equals(nombre, other.nombre)
                && Objects.equals(fechaInicio, other.fechaInicio)
                && Objects.equals(fechaAlta, other.fechaAlta)
                && precio == other.precio
                && plazasMaximas == other.plazasMaximas;
    }

    @Override
    public String toString() {
        return "Curso [cursoId=" + cursoId
                + ", ciudad=" + ciudad
                + ", nombre=" + nombre
                + ", fechaInicio=" + fechaInicio
                + ", fechaAlta=" + fechaAlta
                + ", precio=" + precio
                + ", plazasMaximas=" + plazasMaximas
                + "]";
    }
}
