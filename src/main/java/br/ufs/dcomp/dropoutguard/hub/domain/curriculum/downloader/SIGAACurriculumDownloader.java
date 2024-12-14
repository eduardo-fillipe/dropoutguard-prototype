package br.ufs.dcomp.dropoutguard.hub.domain.curriculum.downloader;

import br.ufs.dcomp.dropoutguard.shared.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.exception.CurriculumNotFoundException;
import br.ufs.dcomp.dropoutguard.shared.domain.storage.FileObject;

/**
 * Interface for downloading curriculum data for a specific register from the SIGAA system.
 * Implementations are responsible for retrieving the curriculum data associated with the provided register.
 * The curriculum is returned as a {@code FileObject}.
 */
public interface SIGAACurriculumDownloader {

    /**
     * Downloads the curriculum data associated with the provided register.
     *
     * @param register the register for which the curriculum data is to be downloaded
     * @return a FileObject containing the curriculum data for the specified register
     * @throws CurriculumNotFoundException if no curriculum data is found for the given register
     */
    FileObject download(Register register) throws CurriculumNotFoundException;
}
