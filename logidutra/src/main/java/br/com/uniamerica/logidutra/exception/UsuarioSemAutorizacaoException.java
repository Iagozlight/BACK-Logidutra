package br.com.uniamerica.logidutra.exception;

public class UsuarioSemAutorizacaoException extends RuntimeException{
    public UsuarioSemAutorizacaoException(String message){
        super(message);
    }
}
