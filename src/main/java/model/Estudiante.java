/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author varga
 */
public class Estudiante {
    //atributos
    private String codigoMatricula;
    private String identificacion;
    private String nombreEstudiante;
    private String observaciones;
    private CursoEstudiante curso;
    private EstadoMatricula estado;
    private int creditos;
    
    //Constructor 
    public Estudiante(String codigoMatricula, String identificacion, String nombreEstudiante, String observaciones) {
        this.codigoMatricula = codigoMatricula;
        this.identificacion = identificacion;
        this.nombreEstudiante = nombreEstudiante;
        this.observaciones = observaciones;
    }

    public Estudiante(String codigoMatricula, String identificacion, String nombreEstudiante, String observaciones, CursoEstudiante curso, EstadoMatricula estado, int creditos) {
        this.codigoMatricula = codigoMatricula;
        this.identificacion = identificacion;
        this.nombreEstudiante = nombreEstudiante;
        this.observaciones = observaciones;
        this.curso = curso;
        this.estado = estado;
        this.creditos = creditos;
    }    
    
    //Getters and Setters

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public EstadoMatricula getEstado() {
        return estado;
    }

    public void setEstado(EstadoMatricula estado) {
        this.estado = estado;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getCodigoMatricula() {
        return codigoMatricula;
    }

    public void setCodigoMatricula(String codigoMatricula) {
        this.codigoMatricula = codigoMatricula;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public CursoEstudiante getCurso() {
        return curso;
    }

    public void setCurso(CursoEstudiante curso) {
        this.curso = curso;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "Estudiante{" + "estado=" + estado + 
                ", nombreEstudiante=" + nombreEstudiante + 
                ", codigoMatricula=" + codigoMatricula + 
                ", identificacion=" + identificacion + 
                ", curso=" + curso + 
                ", observaciones=" + observaciones + '}';
    }    
}
