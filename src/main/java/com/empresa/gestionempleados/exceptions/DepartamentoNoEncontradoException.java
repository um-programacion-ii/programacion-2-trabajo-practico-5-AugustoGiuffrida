package com.empresa.gestionempleados.exceptions;

public class DepartamentoNoEncontradoException extends RuntimeException{
    public DepartamentoNoEncontradoException(Long id){
        super("No se encontro departamento con el id: "+id);
    }
}
