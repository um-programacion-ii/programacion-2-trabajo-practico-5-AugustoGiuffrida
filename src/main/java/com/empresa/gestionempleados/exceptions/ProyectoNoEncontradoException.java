package com.empresa.gestionempleados.exceptions;

public class ProyectoNoEncontradoException extends RuntimeException {
    public ProyectoNoEncontradoException(Long id) {
        super("No se encontro proyecto con el id"+id);
    }
}
