package com.wmouwen.analysis;

import com.wmouwen.analysis.model.Analysis;
import com.wmouwen.analysis.model.Post;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.anything;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TestAnalysisService {

    @InjectMocks
    private AnalysisService analysisService;

    @Test
    public void testAnalyze() throws XMLStreamException {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
            + "<posts>"
            + "<row Id=\"1\" AcceptedAnswerId=\"3\" CreationDate=\"2015-07-14T18:39:27.757\" Score=\"4\"/>"
            + "<row Id=\"2\" CreationDate=\"2015-07-15T18:39:27.757\" Score=\"7\"/>"
            + "<row Id=\"5\" AcceptedAnswerId=\"1\" CreationDate=\"2015-07-18T18:39:27.757\" Score=\"9\"/>"
            + "<row Id=\"3\" CreationDate=\"2015-07-16T18:39:27.757\" Score=\"3\"/>"
            + "<row Id=\"4\" AcceptedAnswerId=\"7\" CreationDate=\"2015-07-17T18:39:27.757\" Score=\"1\"/>"
            + "</posts>";

        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());

        Analysis analysis = this.analysisService.analyze(inputStream);

        assertThat(analysis.totalPosts, is(5));
        assertThat(analysis.totalAcceptedPosts, is(3));
        assertThat(analysis.avgScore, is(5L));
        assertEquals(14, analysis.firstPost.getDate());
        assertEquals(18, analysis.lastPost.getDate());
    }

    @Test
    public void testAddPost() {
        Analysis analysis = new Analysis();

        Post firstPost = new Post();
        firstPost.creationDate = new Date();
        firstPost.score = 3;

        analysisService.addPostToAnalysis(analysis, firstPost);

        assertThat(analysis.totalPosts, is(1));
        assertThat(analysis.totalAcceptedPosts, is(0));
        assertThat(analysis.sumScore, is(3L));
        assertThat(analysis.firstPost, is(firstPost.creationDate));
        assertThat(analysis.lastPost, is(firstPost.creationDate));

        Post secondPost = new Post();
        secondPost.creationDate = new Date();
        secondPost.acceptedAnswerId = 312;
        secondPost.score = 7;

        analysisService.addPostToAnalysis(analysis, secondPost);

        assertThat(analysis.totalPosts, is(2));
        assertThat(analysis.totalAcceptedPosts, is(1));
        assertThat(analysis.sumScore, is(10L));
        assertThat(analysis.firstPost, is(firstPost.creationDate));
        assertThat(analysis.lastPost, is(secondPost.creationDate));
    }

    @Test
    public void testAverageScore() {
        Analysis analysis = new Analysis();
        analysis.totalPosts = 3;
        analysis.sumScore = 100L;
        assertThat(analysisService.calculateAvgScore(analysis), is(33L));

        analysis.totalPosts = 9;
        analysis.sumScore = 75L;
        assertThat(analysisService.calculateAvgScore(analysis), is(8L));
    }

    @Test(expected = RuntimeException.class)
    public void testAverageScoreException() {
        analysisService.calculateAvgScore(new Analysis());
    }
}
