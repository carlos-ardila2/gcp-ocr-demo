package com.epam.demo.contract.web;

import com.google.cloud.documentai.v1.*;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
public class ContractOcrDemoController {

    private static final Logger logger = LoggerFactory.getLogger(ContractOcrDemoController.class);

    @Value(value = "${google.project.id}")
    private String PROJECT_ID;

    @Value(value = "${google.ocr.processor.id}")
    private String PROCESSOR_ID;

    @PostMapping(path = "/process_contract", consumes = MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> processDocument(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] fileBytes = file.getBytes();
        try (DocumentProcessorServiceClient client = DocumentProcessorServiceClient.create()) {
            String processorName = String.format("projects/%s/locations/us/processors/%s", PROJECT_ID, PROCESSOR_ID);

            // Convert the image data to a Buffer and base64 encode it.
            ByteString content = ByteString.copyFrom(fileBytes);

            RawDocument document =
                    RawDocument.newBuilder().setContent(content).setMimeType("application/pdf").build();

            // Configure the process request.
            ProcessRequest request =
                    ProcessRequest.newBuilder().setName(processorName).setRawDocument(document).build();

            // Recognizes text entities in the PDF document
            ProcessResponse response = client.processDocument(request);
            Document documentResponse = response.getDocument();

            Map<String, String> extractedFields = new HashMap<>();

            documentResponse.getEntitiesList().forEach(entity -> extractedFields.put(entity.getType(), entity.getMentionText()));

            return extractedFields;
        } catch (IOException | RuntimeException e) {  // Include RuntimeException for Document AI exceptions
            logger.error("Error processing document", e);
            HashMap<String, String> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage());
            return errorMap;
        }
    }
}
