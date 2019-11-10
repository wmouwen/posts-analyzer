package com.wmouwen.analysis;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.concurrent.TimeUnit;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestAnalysisController {

    private static ClientAndServer mockServer;

    @Autowired
    private MockMvc mockMvc;

    @BeforeClass
    public static void setUpClass() {
        mockServer = ClientAndServer.startClientAndServer(1080);
        registerSmallTestResponse();
        registerEmptyTestResponse();
        registerNotFoundResponse();
    }

    @AfterClass
    public static void tearDownClass() {
        mockServer.stop();
    }

    private static void registerSmallTestResponse() {
        mockServer
            .when(request().withMethod("GET").withPath("/test-small.xml"))
            .respond(response()
                .withStatusCode(200)
                .withHeader(new Header("Content-Type", "text/xml"))
                .withBody("<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                    + "<posts>"
                    + "<row Id=\"1\" AcceptedAnswerId=\"3\" CreationDate=\"2015-07-14T18:39:27.757\" Score=\"4\"/>"
                    + "<row Id=\"2\" CreationDate=\"2015-07-15T18:39:27.757\" Score=\"7\"/>"
                    + "<row Id=\"3\" CreationDate=\"2015-07-16T18:39:27.757\" Score=\"3\"/>"
                    + "<row Id=\"4\" AcceptedAnswerId=\"7\" CreationDate=\"2015-07-17T18:39:27.757\" Score=\"1\"/>"
                    + "<row Id=\"5\" AcceptedAnswerId=\"1\" CreationDate=\"2015-07-18T18:39:27.757\" Score=\"9\"/>"
                    + "</posts>"
                )
                .withDelay(TimeUnit.SECONDS, 1)
            );
    }

    private static void registerEmptyTestResponse() {
        mockServer
            .when(request().withMethod("GET").withPath("/test-empty.xml"))
            .respond(response()
                .withStatusCode(200)
                .withHeader(new Header("Content-Type", "text/xml"))
                .withBody("<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                    + "<posts>"
                    + "</posts>"
                )
            );
    }

    private static void registerNotFoundResponse() {
        mockServer
            .when(request().withMethod("GET").withPath("/test-not-found.xml"))
            .respond(response().withStatusCode(404));
    }

    @Test
    public void testSmall() throws Exception {
        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders
            .post("/analyze")
            .content("{\"url\":\"http://localhost:1080/test-small.xml\"}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));

        response
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.analyseDate").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.details").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.details.totalPosts").value(5))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details.totalAcceptedPosts").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details.avgScore").value(5))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details.firstPost").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.details.lastPost").exists());
    }

    @Test
    public void testEmpty() throws Exception {
        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders
            .post("/analyze")
            .content("{\"url\":\"http://localhost:1080/test-empty.xml\"}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));

        response
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.analyseDate").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.details").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.details.totalPosts").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details.totalAcceptedPosts").value(0));
    }

    @Test
    public void testNotFound() throws Exception {
        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders
            .post("/analyze")
            .content("{\"url\":\"http://localhost:1080/test-not-found.xml\"}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));

        response
            .andExpect(status().isInternalServerError());
    }
}
