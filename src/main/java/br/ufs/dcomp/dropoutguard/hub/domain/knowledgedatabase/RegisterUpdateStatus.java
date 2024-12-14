package br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase;

/**
 * Enum representing the status of a register update in the knowledge database.
 */
public enum RegisterUpdateStatus {
    /**
     * Indicates that the register update has been created but not yet processed.
     */
    CREATED, 

    /**
     * Indicates that the register update has been processed successfully.
     */
    PROCESSED, 

    /**
     * Indicates that an error occurred during the processing of the register update.
     */
    ERROR
}
