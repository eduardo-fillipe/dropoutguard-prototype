package br.ufs.dcomp.dropoutguard.shared.domain.storage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileObject {

    @Builder(toBuilder = true)
    public FileObject(String objectName, byte[] data) {
        this.objectName = objectName;
        this.data = data;
    }

    private String objectName;
    private byte[] data;
}
