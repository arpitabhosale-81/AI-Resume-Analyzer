package com.resumeai.backend.controller;

import com.resumeai.backend.service.PdfTextExtractor;
import com.resumeai.backend.service.ResumeAnalyzerService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin(origins = "http://localhost:5173")
public class ResumeUploadController {

    private final PdfTextExtractor pdfTextExtractor;
    private final ResumeAnalyzerService resumeAnalyzerService;

    public ResumeUploadController(
            PdfTextExtractor pdfTextExtractor,
            ResumeAnalyzerService resumeAnalyzerService) {

        this.pdfTextExtractor = pdfTextExtractor;
        this.resumeAnalyzerService = resumeAnalyzerService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("jobRole") String jobRole,
            @RequestParam(value = "jobDescription", required = false)
            String jobDescription) {

        try {

            System.out.println(
                    "========== RESUME REQUEST STARTED =========="
            );

            System.out.println(
                    "File received: "
                            + file.getOriginalFilename()
            );

            System.out.println(
                    "Job role: " + jobRole
            );

            System.out.println(
                    "Job description received: "
                            + (jobDescription != null
                            && !jobDescription.trim().isEmpty())
            );

            if (file.isEmpty()) {

                return ResponseEntity.badRequest()
                        .body("Please upload a resume.");
            }

            System.out.println(
                    "STEP 1: Starting PDF text extraction..."
            );

            String extractedText =
                    pdfTextExtractor.extractText(file);

            System.out.println(
                    "STEP 2: PDF text extraction completed."
            );

            System.out.println(
                    "Extracted text length: "
                            + extractedText.length()
            );

            System.out.println(
                    "STEP 3: Starting resume analysis..."
            );

            Map<String, Object> analysis =
                    resumeAnalyzerService.analyzeResume(
                            extractedText,
                            jobRole,
                            jobDescription
                    );

            System.out.println(
                    "STEP 4: Resume analysis completed."
            );

            System.out.println(
                    "========== RESUME REQUEST FINISHED =========="
            );

            return ResponseEntity.ok(analysis);

        } catch (Exception e) {

            System.out.println(
                    "========== ERROR =========="
            );

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .body(
                            "Error analyzing resume: "
                                    + e.getMessage()
                    );
        }
    }
}