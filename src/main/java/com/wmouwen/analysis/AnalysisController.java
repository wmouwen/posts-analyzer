package com.wmouwen.analysis;

import com.wmouwen.analysis.model.Analysis;
import com.wmouwen.analysis.model.AnalysisRequest;
import com.wmouwen.analysis.model.AnalysisResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

@Controller
public class AnalysisController {

    private AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<AnalysisResponse> analyze(
        @RequestBody AnalysisRequest analysisRequest
    ) throws Exception {

        InputStream inputStream;
        URL url = analysisRequest.getUrl();

        try {
            inputStream = url.openStream();

        } catch (FileNotFoundException e) {
            // Thrown when the given URL is unavailable. This may or may not be a fault
            // of the caller, so blame goes to the server.
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        }

        Analysis analysis = this.analysisService.analyze(inputStream);

        if (null == analysis) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        }

        return ResponseEntity.ok(new AnalysisResponse(new Date(), url, analysis));
    }

}
