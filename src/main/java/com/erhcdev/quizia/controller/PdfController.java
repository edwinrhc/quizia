package com.erhcdev.quizia.controller;

import com.erhcdev.quizia.model.QuizResult;
import com.erhcdev.quizia.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quiz/pdf")
@RequiredArgsConstructor
public class PdfController {

    private final PdfService pdfService;

    @PostMapping("/pdf")
    public ResponseEntity<byte[]> exportToPdf(@RequestBody QuizResult result) {
        byte[] pdf = pdfService.generatePdf(result);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=quiz_result.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}


