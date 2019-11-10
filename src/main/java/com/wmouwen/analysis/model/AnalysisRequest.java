package com.wmouwen.analysis.model;

import java.net.MalformedURLException;
import java.net.URL;

public class AnalysisRequest {

    public URL url;

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public void setUrl(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

}
