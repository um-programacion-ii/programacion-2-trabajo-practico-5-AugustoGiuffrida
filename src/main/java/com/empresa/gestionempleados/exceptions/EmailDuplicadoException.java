package com.empresa.gestionempleados.exceptions;

public class EmailDuplicadoException extends RuntimeException{
    public  EmailDuplicadoException(String email){
        super("El email: "+email+" ya se encuentra registrado");
    }
}
