package org.example.session08.config;

import org.example.session08.service.DocumentIngestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RagDataLoaderTest {

    @Mock
    private DocumentIngestionService documentIngestionService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    private RagDataLoader ragDataLoader;
    private Resource dummyResource;

    @BeforeEach
    void setUp() {
        ragDataLoader = new RagDataLoader();
        dummyResource = new ByteArrayResource("test data".getBytes());
        ReflectionTestUtils.setField(ragDataLoader, "documentResource", dummyResource);
    }

    @Test
    void testInitDatabase_whenEmpty_shouldIngest() throws Exception {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(0);

        CommandLineRunner runner = ragDataLoader.initDatabase(documentIngestionService, jdbcTemplate);
        runner.run();

        verify(documentIngestionService, times(1)).ingestDocument(dummyResource);
    }

    @Test
    void testInitDatabase_whenNotEmpty_shouldNotIngest() throws Exception {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(5);

        CommandLineRunner runner = ragDataLoader.initDatabase(documentIngestionService, jdbcTemplate);
        runner.run();

        verify(documentIngestionService, never()).ingestDocument(any());
    }

    @Test
    void testInitDatabase_whenException_shouldHandleGracefully() throws Exception {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenThrow(new RuntimeException("Table not found"));

        CommandLineRunner runner = ragDataLoader.initDatabase(documentIngestionService, jdbcTemplate);
        runner.run(); // Should not throw exception

        verify(documentIngestionService, never()).ingestDocument(any());
    }
}
