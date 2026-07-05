package org.example.session08.service;

import io.micrometer.tracing.annotation.NewSpan;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentIngestionService {

    private final VectorStore vectorStore;

    public DocumentIngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    // Spring Boot + Micrometer sẽ tự động tạo một Span đo lường thời gian chạy của hàm này
    @NewSpan("Chunking-And-Vectorizing-Document")
    public void ingestDocument(Resource documentResource) {
        System.out.println("=> Bắt đầu quá trình nạp tài liệu...");

        TikaDocumentReader documentReader = new TikaDocumentReader(documentResource);
        List<Document> originalDocuments = documentReader.get();

        TokenTextSplitter splitter = new TokenTextSplitter(500, 100, 5, 10000, true);
        List<Document> splitChunks = splitter.apply(originalDocuments);
        System.out.println("=> Đã cắt tài liệu thành: " + splitChunks.size() + " đoạn (chunks).");

        // Khi gọi lệnh này, Spring AI ngầm gửi token usage và latency lên Langfuse
        vectorStore.accept(splitChunks);
        System.out.println("=> Nạp tài liệu thành công vào PgVector!");
    }
}