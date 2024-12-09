package br.ufs.dcomp.dropoutguard.domain.storage.exception;

public class ObjectIsNotAFileException extends RuntimeException {
    public ObjectIsNotAFileException(String message) {
        super(message);
    }
}
