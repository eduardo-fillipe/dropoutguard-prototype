package br.ufs.dcomp.dropoutguard.unit.infrastructure.event.knowledgedatabase;

import br.ufs.dcomp.dropoutguard.domain.event.EventMessage;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabase;
import br.ufs.dcomp.dropoutguard.infrastructure.event.KnowledgeDatabaseEventMessagePublisher;
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
            String routingKey = publisher.getRoutingKey(new EventMessage<>("test", knowledgeDatabase));

            Assertions.assertEquals("dropoutguard.knowledgedatabase.update.update_requested", routingKey);
        }
    }
}
