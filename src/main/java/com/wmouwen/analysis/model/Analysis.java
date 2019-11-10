package com.wmouwen.analysis.model;

import java.util.Date;

public class Analysis {

    public Integer totalPosts = 0;
    public Integer totalAcceptedPosts = 0;
    public Long sumScore = 0L;
    public Long avgScore;

    public Date firstPost;
    public Date lastPost;

    public Analysis() {
        //
    }

}