package com.homework.homeworkProject.gitTests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.homeworkProject.PropertyReader;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

public class basicGitTests {
    CloseableHttpClient client;

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
    @AfterTest
    public void closeClient() throws IOException {
        client.close();
    }
}
