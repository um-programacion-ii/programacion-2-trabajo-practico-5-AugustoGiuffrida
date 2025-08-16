package com.empresa.gestionempleados.exceptions;

public class EmpleadoNoEncontradoException extends RuntimeException{
    public EmpleadoNoEncontradoException(Long id){
        super("No se encontro el empleado con el id: " + id);
    }
}
