package org.example.session08.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class DocumentIngestionServiceTest {

    @Mock
    private VectorStore vectorStore;

    private DocumentIngestionService documentIngestionService;

    @BeforeEach
    void setUp() {
        documentIngestionService = new DocumentIngestionService(vectorStore);
    }

    @Test
    void testIngestDocument() {
        // Prepare a dummy resource with some text
        String dummyContent = "This is a test document. ".repeat(100);
        Resource dummyResource = new ByteArrayResource(dummyContent.getBytes()) {
            @Override
            public String getFilename() {
                return "test.txt";
            }
        };

        // Call the service
        documentIngestionService.ingestDocument(dummyResource);

        // Capture arguments to vector store
        ArgumentCaptor<List<Document>> captor = ArgumentCaptor.forClass(List.class);
        verify(vectorStore, times(1)).accept(captor.capture());

        List<Document> chunkedDocuments = captor.getValue();
        
        assertNotNull(chunkedDocuments);
        assertFalse(chunkedDocuments.isEmpty());
    }
}
