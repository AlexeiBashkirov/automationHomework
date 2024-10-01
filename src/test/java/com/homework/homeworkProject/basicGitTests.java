package com.homework.homeworkProject;

import com.homework.homeworkProject.dto.User;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

public class basicGitTests {
    CloseableHttpClient client;
    CloseableHttpResponse response;

    @DataProvider(name = "endpointsGetTopics")
    public Object[][] endpointsGetTopics() {
        return new Object[][] {
                {"test",1,5, 200 },
                { "",1,5,422 },
                { "java",1,999,200},
                { "c",-1,-3,200},
        };
    }
    @BeforeTest
    public void openClient() throws Exception{
        client = new DefaultHttpClient();
    }

    @Test(dataProvider = "endpointsGetTopics", priority = 1)
    public void GetDifferentJockeys(String topic, int page, int results_per_page, int expectedResult) throws IOException{
        String baseUrl= PropertyReader.getProperty("basicGitURL");
        String getEndPoint = PropertyReader.getProperty("topicsSearchENDPOINT");
        HttpGet getRequest = new HttpGet(baseUrl+getEndPoint+"?q="+topic + "&page=" + page + "&per_page=" + results_per_page);
        try (CloseableHttpResponse response = client.execute(getRequest)) {
            int statusCode = response.getStatusLine().getStatusCode();
            Assert.assertEquals(statusCode, expectedResult, "Expected status code is different");
        }
    }

    @Test
    public void createRepo() throws IOException {
        HttpPost request = new HttpPost("https://api.github.com/user/repos");
        request.setHeader(HttpHeaders.AUTHORIZATION, "token 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");

        String json = "{\"name\": \"hello-world123\"}";
        request.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        System.out.println(json);

        response= client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        Assert.assertEquals(statusCode, 201);
    }
    @Test
    public void DeleteRepo() throws IOException {
        HttpDelete request = new HttpDelete("https://api.github.com/repos/AlexeiBashkirov/hello-world123");
        request.setHeader(HttpHeaders.AUTHORIZATION, "token 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");
        response= client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        Assert.assertEquals(statusCode, 204);
    }

    @Test
    public void loginDataCheck() throws IOException {
        HttpGet request = new HttpGet(PropertyReader.getProperty("basicGitURL")+ "/users/" + User.LOGIN);
        response = client.execute(request);

        User testUser = GitUtilityClass.jsonDeserealisation(response, User.class);

        Assert.assertEquals(testUser.getLogin(), User.LOGIN);
    }

    @Test
    public void messageCheck() throws IOException {
        HttpGet request = new HttpGet(PropertyReader.getProperty("basicGitURL")+ "/users/" + User.LOGIN);
        response = client.execute(request);

        User testUser = GitUtilityClass.jsonDeserealisation(response, User.class);

        Assert.assertEquals(testUser.getId(), (User.ID).toString());
    }
    @Test
    public void test() throws IOException {
        String header="Access-Control-Allow-Methods";
        String expectedReply="GET, POST, PATCH, PUT, DELETE";
        String baseUrl= PropertyReader.getProperty("basicGitURL");
        HttpOptions optionRequest = new HttpOptions(baseUrl);
        try (CloseableHttpResponse response = client.execute(optionRequest)) {
            Assert.assertEquals(expectedReply, GitUtilityClass.getHeader(response, header), "all headers are here");
        }
    }
    @Test
    public void idDataCheck() throws IOException {
        HttpGet request = new HttpGet(PropertyReader.getProperty("basicGitURL")+ "/users/" + User.LOGIN);
        response = client.execute(request);

        User testUser = GitUtilityClass.jsonDeserealisation(response, User.class);

        Assert.assertEquals(testUser.getId(), (User.ID).toString());
    }

    @AfterTest
    public void closeClient() throws IOException {
        client.close();
    }
}
