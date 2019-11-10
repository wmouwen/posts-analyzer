package com.wmouwen.analysis.model;

import com.wmouwen.analysis.model.Analysis;

import java.net.URL;
import java.util.Date;

public class AnalysisResponse {

    public Date analyseDate;
    public URL analyseUrl;
    public Analysis details;

    private AnalysisResponse() {
        //
    }

    public AnalysisResponse(Date analyseDate, URL analyseUrl, Analysis details) {
        this.analyseDate = analyseDate;
        this.analyseUrl = analyseUrl;
        this.details = details;
    }

}