package br.ufs.dcomp.dropoutguard.unit.shared.infrastructure.event.knowledgedatabase;

import br.ufs.dcomp.dropoutguard.shared.domain.event.EventMessage;
import br.ufs.dcomp.dropoutguard.api.domain.knowledgedatabase.KnowledgeDatabase;
import br.ufs.dcomp.dropoutguard.shared.domain.knowledgedatabase.KnowledgeDatabaseEventDTO;
import br.ufs.dcomp.dropoutguard.shared.infrastructure.event.KnowledgeDatabaseEventMessagePublisher;
import br.ufs.dcomp.dropoutguard.unit.UnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
public class KnowledgeDatabaseEventPublisherTest {

    @Nested
    class GetRoutingKey{
        @Test
        void testShouldReturnRoutingKey() {
            KnowledgeDatabaseEventMessagePublisher publisher = new KnowledgeDatabaseEventMessagePublisher(null, null, null);
            KnowledgeDatabase knowledgeDatabase = KnowledgeDatabase.builder()
                    .id("1")
                    .name("test")
                    .reason("test")
                    .registerFileLocation("test")
                    .description("test")
                    .build();


            String routingKey = publisher.getRoutingKey(new EventMessage<>("test", KnowledgeDatabaseEventDTO.of(knowledgeDatabase)));

            Assertions.assertEquals("dropoutguard.knowledgedatabase.update.update_requested", routingKey);
        }
    }
}
