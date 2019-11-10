package com.wmouwen.analysis;

import com.wmouwen.analysis.model.Analysis;
import com.wmouwen.analysis.model.Post;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.Date;
import java.util.Optional;

@Service
public class AnalysisService {

    private Unmarshaller postUnmarshaller;

    private XMLInputFactory xmlInputFactory;

    public AnalysisService() throws JAXBException {
        this.postUnmarshaller = JAXBContext
            .newInstance(Post.class)
            .createUnmarshaller();

        this.xmlInputFactory = XMLInputFactory.newInstance();
    }

    /**
     * Analyze the contents of an XML file containing a list of StackOverflow posts. Uses a loop over an InputStream
     * to avoid storing data to memory.
     *
     * @param inputStream Stream of an XML file
     * @return Analysis Results of analysing XML file, or null if the analysis cannot be finished.
     */
    public Analysis analyze(InputStream inputStream) throws XMLStreamException {

        Analysis analysis = new Analysis();
        XMLStreamReader xmlStreamReader = this.xmlInputFactory.createXMLStreamReader(inputStream);

        try {
            this.goToRootElement(xmlStreamReader, "posts");

            while (XMLStreamConstants.START_ELEMENT == xmlStreamReader.getEventType()) {
                Post post = (Post) postUnmarshaller.unmarshal(xmlStreamReader);

                this.addPostToAnalysis(analysis, post);

                if (XMLStreamConstants.CHARACTERS == xmlStreamReader.getEventType()) {
                    xmlStreamReader.next();
                }
            }

            if (analysis.totalPosts > 0) {
                analysis.avgScore = this.calculateAvgScore(analysis);
            }

        } catch (JAXBException exception) {
            xmlStreamReader.close();
            return null;
        }

        xmlStreamReader.close();
        return analysis;
    }

    /**
     * @param analysis Current Analysis
     * @param post Post to append to analysis
     */
    public void addPostToAnalysis(Analysis analysis, Post post) {
        analysis.totalPosts++;

        if (null != post.acceptedAnswerId) {
            analysis.totalAcceptedPosts++;
        }

        analysis.sumScore += post.score;

        Optional<Date> currentFirstPost = Optional.ofNullable(analysis.firstPost);
        if (!currentFirstPost.isPresent() || post.creationDate.before(currentFirstPost.get())) {
            analysis.firstPost = post.creationDate;
        }

        Optional<Date> currentLastPost = Optional.ofNullable(analysis.lastPost);
        if (!currentLastPost.isPresent() || post.creationDate.after(currentLastPost.get())) {
            analysis.lastPost = post.creationDate;
        }
    }

    public long calculateAvgScore(Analysis analysis) {
        if (analysis.totalPosts == 0) {
            throw new RuntimeException("Cannot calculate average score of analyses without posts");
        }

        // Calculate average score.
        double averageScore = (double) analysis.sumScore / analysis.totalPosts;

        // Round to nearest integer.
        return Math.round(averageScore);
    }

    public void goToRootElement(XMLStreamReader xmlStreamReader, String rootElement) throws XMLStreamException {
        xmlStreamReader.nextTag();
        xmlStreamReader.require(XMLStreamConstants.START_ELEMENT, null, rootElement);
        xmlStreamReader.nextTag();
    }
}
