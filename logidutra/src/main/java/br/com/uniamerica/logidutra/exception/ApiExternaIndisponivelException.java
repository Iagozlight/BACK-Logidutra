package br.com.uniamerica.logidutra.exception;

public class ApiExternaIndisponivelException extends RuntimeException {
    public ApiExternaIndisponivelException(String message){
        super(message);
    }
}
