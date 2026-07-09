/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

//import de utilities
import exceptions.RegistroDuplicadoException;
import exceptions.RegistroNoEncontradoException;
import exceptions.ValidacionException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import de models
import model.Estudiante;
import model.EstadoMatricula;
import model.CursoEstudiante;

/**
 *
 * @author varga
 */
public class GestorMatriculas {

    //Atributo final con hashmap
    private final Map<String, Estudiante> listaMatriculas = new HashMap<>();

    //Constructor de gestor de matricula
    public GestorMatriculas() {
        listaMatriculas.put("E-001", new Estudiante("E-001", "232323", "David Vargas", "Ninguna", CursoEstudiante.DISENO_SISTEMAS, EstadoMatricula.ACTIVA, 3));
        listaMatriculas.put("E-002", new Estudiante("E-002", "242424", "Paulo Barrantes", "Ninguna", CursoEstudiante.DISENO_SISTEMAS, EstadoMatricula.ACTIVA, 4));
    }

    //Funcion crear matricula
    public void crear(Estudiante e) throws ValidacionException, RegistroDuplicadoException {
        validar(e);
        
        String key = e.getCodigoMatricula().trim();
        
        if (listaMatriculas.containsKey(key)) {

            throw new RegistroDuplicadoException("Ya existe una matricula con ese codigo" + key); 
        }
        
        listaMatriculas.put(key, e);
    }
    
    //Funcion editar matricula
    public void editar(Estudiante e) throws ValidacionException, RegistroNoEncontradoException {
        validar(e);
        
        String key = e.getCodigoMatricula().trim();
        
        if(!listaMatriculas.containsKey(key)) {
            throw new RegistroNoEncontradoException("No existe la matricula con el codigo: " + key);
        }
        
        listaMatriculas.put(key, e);
    }
    
    //Funcion eliminar matricula
    public void eliminar(String codigo) throws ValidacionException, RegistroNoEncontradoException {
        if(codigo == null || codigo.trim().isEmpty()){
            throw new ValidacionException("Debe indicar el codigo de la matricula");
        }
        
        String key = codigo.trim();
        if(!listaMatriculas.containsKey(key)){
            throw new RegistroNoEncontradoException("No existe matricula con el codigo: " + key);
        }
        
        listaMatriculas.remove(key);
    }
    
    //Funcion buscar matricula 
    public Estudiante buscar(String codigo) throws ValidacionException, RegistroNoEncontradoException{
        if(codigo == null || codigo.trim().isEmpty()){
            throw new ValidacionException("Debe indicar el codigo de la matriucula");
        }
        
        String key = codigo.trim();
        Estudiante e =  listaMatriculas.get(key);
        
        if (e == null) throw new RegistroNoEncontradoException("No se encontro el codigo de la matricula: " + key);
        
        return e;
    }
    
    //Funcion Listar
    public List<Estudiante> listar() {
        List<Estudiante> lista = new ArrayList<>(listaMatriculas.values());
        lista.sort(Comparator.comparing(Estudiante::getCodigoMatricula));
        return lista;
    }
    
    //Funcion reutilizable para validar
    private void validar(Estudiante e) throws ValidacionException {

        if (e == null) {
            throw new ValidacionException("La matricula no puede ser nula");
        }

        if (esVacio(e.getCodigoMatricula())) {
            throw new ValidacionException("El codigo de matricula es obligatorio");
        }

        if (esVacio(e.getNombreEstudiante())) {
            throw new ValidacionException("El nombre del estudiante es obligatorio");
        }

        if (esVacio(e.getIdentificacion())) {
            throw new ValidacionException("La identificacion es obligatoria");
        }
        
        if (e.getEstado()== null) {
            throw new ValidacionException("Debe seleccionar el estado del estudiante");
        }

        if (e.getCurso()== null) {
            throw new ValidacionException("Debe seleccionar el curso del estudiante");
        }

        if (e.getCreditos() < 1 || e.getCreditos() > 6) {
            throw new ValidacionException("Los créditos deben estar entre 1 y 6");
        }

    }

    private boolean esVacio(String s) {
        return s == null || s.trim().isEmpty();
    }

}
