package br.ufs.dcomp.dropoutguard.shared.domain.storage.exception;

public class ObjectIsNotAFileException extends RuntimeException {
    public ObjectIsNotAFileException(String message) {
        super(message);
    }
}
