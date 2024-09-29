package com.homework.homeworkProject.HomeWorkApplicationTests;

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

public class JockeyEndPointTests {
    CloseableHttpClient client;
    long id;
    @DataProvider(name = "endpointsPOST")
    public Object[][] endpointsPost() {
        return new Object[][] {
                { "Johny","Joestar","Slow Dancer","USA", 201 },
                { "Diego","Brando","Silver Bullet",null, 400 },
                { "Gyro","Zeppeli",null,"Italy", 400 },
                { "Mountain",null,"Horse","USA", 400 },
                { null,"Noriske","Flame","Japan",400},
                { null,null,null,null,400}
        };
    }
    @DataProvider(name = "endpointsGET")
    public Object[][] endpointsGet() {
        return new Object[][] {
                { "Johny","Joestar",  200 },
                { "Dio","Brando", 404 },
                { "Diego","Mondata", 404 },
        };
    }
    @DataProvider(name = "endpointsPUT")
    public Object[][] endpointsPut() {
        return new Object[][] {
                { 1, "Johny","Joestar","Slow Dancer","USA", 400 },
                { id, "Jotaro","Joestar","Slow Dancer","USA", 200 },
                { id, null,null,null,null, 400 }
        };
    }
    @DataProvider(name = "endpointsDelete")
    public Object[][] endpointsDelete() {
        return new Object[][] {
                { id, 200 },
                { 1, 400 },
        };
    }
    @BeforeTest
    public void openClient() throws Exception{
        client = new DefaultHttpClient();
    }

    @Test(dataProvider = "endpointsPOST", priority = 0)
    public void PostDifferentJockeys(String first_name, String last_name, String horse_name, String country, int expectedResult) throws IOException {
        String baseUrl= PropertyReader.getProperty("basicJockeysURL");
        String postEndPoint = PropertyReader.getProperty("postJockeysENDPOINT");
        HttpPost postRequest = new HttpPost(baseUrl+postEndPoint);
        String jsonInputString = "{\"firstName\": " + (first_name != null ? "\"" + first_name + "\"" : "null") +
                ", \"lastName\": " + (last_name != null ? "\"" + last_name + "\"" : "null") +
                ", \"horseName\": " + (horse_name != null ? "\"" + horse_name + "\"" : "null") +
                ", \"country\": " + (country != null ? "\"" + country + "\"" : "null") + "}";
        postRequest.setHeader("Content-Type", "application/json");
        postRequest.setHeader("Accept", "application/json");
        postRequest.setEntity(new StringEntity(jsonInputString, ContentType.APPLICATION_JSON));
        try (CloseableHttpResponse response = client.execute(postRequest)) {
            int statusCode = response.getStatusLine().getStatusCode();
            Assert.assertEquals(statusCode, expectedResult, "Expected status code is different");
            if(statusCode == 201) {
                String Response = EntityUtils.toString(response.getEntity());
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode JSON = objectMapper.readTree(Response);
                id = JSON.get("id").asLong();
            }
        }
    }
    @Test(dataProvider = "endpointsGET", priority = 1)
    public void GetDifferentJockeys(String first_name, String last_name, int expectedResult) throws IOException{
        String baseUrl= PropertyReader.getProperty("basicJockeysURL");
        String getEndPoint = PropertyReader.getProperty("getJockeysENDPOINT");
        HttpGet getRequest = new HttpGet(baseUrl+getEndPoint+"/"+first_name+"/"+last_name);
        try (CloseableHttpResponse response = client.execute(getRequest)) {
            int statusCode = response.getStatusLine().getStatusCode();
            Assert.assertEquals(statusCode, expectedResult, "Expected status code is different");
        }
    }
    @Test(dataProvider = "endpointsPUT", priority = 2)
    public void PutDifferentJockeys(long id, String first_name, String last_name, String horse_name, String country, int expectedResult) throws IOException{
        String baseUrl= PropertyReader.getProperty("basicJockeysURL");
        String putJockeysENDPOINT = PropertyReader.getProperty("putJockeysENDPOINT");
        HttpPut putRequest = new HttpPut(baseUrl+putJockeysENDPOINT+"/"+id);
        String jsonInputString = "{\"firstName\": " + (first_name != null ? "\"" + first_name + "\"" : "null") +
                ", \"lastName\": " + (last_name != null ? "\"" + last_name + "\"" : "null") +
                ", \"horseName\": " + (horse_name != null ? "\"" + horse_name + "\"" : "null") +
                ", \"country\": " + (country != null ? "\"" + country + "\"" : "null") + "}";
        putRequest.setHeader("Content-Type", "application/json");
        putRequest.setHeader("Accept", "application/json");
        putRequest.setEntity(new StringEntity(jsonInputString, ContentType.APPLICATION_JSON));
        try (CloseableHttpResponse response = client.execute(putRequest)) {
            int statusCode = response.getStatusLine().getStatusCode();
            Assert.assertEquals(statusCode, expectedResult, "Expected status code is different");
        }
    }
    @Test(dataProvider = "endpointsDelete", priority = 4)
    public void DeleteDifferentJockeys(long id, int expectedResult) throws IOException{
        String baseUrl= PropertyReader.getProperty("basicJockeysURL");
        String deleteJockeysENDPOINT = PropertyReader.getProperty("deleteJockeysENDPOINT");
        HttpDelete DelRequest = new HttpDelete(baseUrl+deleteJockeysENDPOINT+"/"+id);
        try (CloseableHttpResponse response = client.execute(DelRequest)) {
            int statusCode = response.getStatusLine().getStatusCode();
            Assert.assertEquals(statusCode, expectedResult, "Expected status code is different");
        }
    }
    @AfterTest
    public void closeClient() throws IOException {
        client.close();
    }

}
