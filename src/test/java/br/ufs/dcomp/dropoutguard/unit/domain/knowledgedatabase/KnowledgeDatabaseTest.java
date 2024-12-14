package br.ufs.dcomp.dropoutguard.unit.domain.knowledgedatabase;

import br.ufs.dcomp.dropoutguard.api.domain.knowledgedatabase.KnowledgeDatabase;
import br.ufs.dcomp.dropoutguard.shared.domain.knowledgedatabase.KnowledgeDatabaseStatus;
import br.ufs.dcomp.dropoutguard.unit.UnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@UnitTest
public class KnowledgeDatabaseTest {

    private final KnowledgeDatabase.KnowledgeDatabaseBuilder defaultBuilder = KnowledgeDatabase.builder()
                    .name("Teste")
                    .description("Teste Description")
                    .registerFileLocation("teste localtion");

    @Nested
    class CancelUpdateRequestTest {
        @Test
        void shouldChangeTheStatusToUpdateCancelled() {
            KnowledgeDatabase knowledgeDatabase = defaultBuilder.build();

            knowledgeDatabase.cancel("Test Reason");

            assertEquals(KnowledgeDatabaseStatus.UPDATE_CANCELLED, knowledgeDatabase.getStatus());
            assertEquals("Test Reason", knowledgeDatabase.getCancellationReason());
        }

        @Test
        void shouldThrowIfTheTransitionIsNotAllowed() {
            KnowledgeDatabase knowledgeDatabase = defaultBuilder.build();
            knowledgeDatabase.finishUpdate(KnowledgeDatabaseStatus.UPDATED);

            Assertions.assertThrows(IllegalArgumentException.class, () -> knowledgeDatabase.cancel("Test Reason"));
        }
    }

    @Nested
    class FinishUpdateTest {

        static Stream<Arguments> shouldChangeTheStatusToTheFinalStatusArgProvider () {
            return Stream.of(
                    Arguments.of(KnowledgeDatabaseStatus.UPDATED),
                    Arguments.of(KnowledgeDatabaseStatus.UPDATE_ERROR),
                    Arguments.of(KnowledgeDatabaseStatus.UPDATE_CANCELLED)
            );
        }

        @ParameterizedTest
        @MethodSource("shouldChangeTheStatusToTheFinalStatusArgProvider")
        void shouldChangeTheStatusToTheFinalStatus(KnowledgeDatabaseStatus status) {
            KnowledgeDatabase knowledgeDatabase = defaultBuilder.build();

            knowledgeDatabase.finishUpdate(status);

            assertEquals(status, knowledgeDatabase.getStatus());
        }

        @Test
        void shouldThrowIfTheStatusIsNotFinal() {
            KnowledgeDatabase knowledgeDatabase = defaultBuilder.build();

            assertThrows(IllegalArgumentException.class, () -> knowledgeDatabase.finishUpdate(KnowledgeDatabaseStatus.UPDATING));
        }

        @Test
        void shouldThrowIfTheTransitionIsNotAllowed() {
            KnowledgeDatabase knowledgeDatabase = defaultBuilder.build();

            knowledgeDatabase.cancel("Test Reason");

            assertThrows(IllegalArgumentException.class, () -> knowledgeDatabase.finishUpdate(KnowledgeDatabaseStatus.UPDATE_CANCELLED));
        }
    }

    @Nested
    class StartUpdateTest {

        @Test
        void shouldChangeTheStatusToUpdate() {
            KnowledgeDatabase knowledgeDatabase = defaultBuilder.build();

            knowledgeDatabase.startUpdate();

            assertEquals(KnowledgeDatabaseStatus.UPDATING, knowledgeDatabase.getStatus());
        }

        @Test
        void shouldThrowIfTheTransitionIsNotAllowed() {
            KnowledgeDatabase knowledgeDatabase = defaultBuilder.build();

            knowledgeDatabase.finishUpdate(KnowledgeDatabaseStatus.UPDATED);

            assertThrows(IllegalArgumentException.class, knowledgeDatabase::startUpdate);
        }
    }
}
