package br.com.avaliacao_globo.exception;

public class MessagePublishException extends RuntimeException {

    public MessagePublishException(String message, Throwable cause) {
        super(message, cause);
    }
}