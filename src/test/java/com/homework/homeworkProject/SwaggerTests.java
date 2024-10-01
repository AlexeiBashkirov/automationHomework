package com.homework.homeworkProject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.homeworkProject.dto.User;
import com.homework.homeworkProject.dto.UserSwagger;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
import java.util.Base64;

public class SwaggerTests {
    CloseableHttpClient client;
    CloseableHttpResponse response;
    UserSwagger testUser;
    String token;
    @BeforeTest
    public void openClient() throws Exception {
        client = new DefaultHttpClient();
    }

    @DataProvider(name = "negativeLoginData")
    public Object[][] negativeUserCreation() {
        return new Object[][] {
                { "","" },
                { ".","GOodPASS123!"  },
                { "GoodName","badpass" },
                { "GoodName",null },
                { null,"GOodPASS123!"},
                { null,null}
        };
    }
    @DataProvider(name = "negativeTokenData")
    public Object[][] negativeTokenData() {
        return new Object[][] {
                { "","", 400},
                { ".","GOodPASS123!" , 400},
                { "GoodName","badpass", 400},
                { "GoodName",null, 400 },
                { null,"GOodPASS123!", 400},
                { null,null, 400},
                { "ExistingName","m", 400}
        };
    }
    @DataProvider(name = "negativeDeleteData")
    public Object[][] negativeDeleteData() {
        return new Object[][] {
                { "","", "ea1945f9-dcf6-4bc3-bbd7-52056b6b8321", 401},
                { ".","GOodPASS123!", "ea1945f9-dcf6-4bc3-bbd7-52056b6b8321", 401},
                { "GoodName","badpass", "ea1945f9-dcf6-4bc3-bbd7-52056b6b8321", 401},
                { "GoodName",null, "ea1945f9-dcf6-4bc3-bbd7-52056b6b8321", 401 },
                { null,"GOodPASS123!", "ea1945f9-dcf6-4bc3-bbd7-52056b6b8321", 401},
                { null,null, "ea1945f9-dcf6-4bc3-bbd7-52056b6b8321", 401},
                { "ExistingName","m",  "ea1945f9-dcf6-4bc3-bbd7-52056b6b8321", 401},
                { "ExistingName","PAss123!","fakeID",  200}
        };
    }

    @Test(priority = 1, groups = {"smoke", "positive"})
    public void UserCreation() throws IOException {
        String baseUrl= PropertyReader.getProperty("swaggerURL");
        String UserPostEndPoint = PropertyReader.getProperty("UserPostUrl");

        String Username = "AlexeiB1";
        String Password = "PAss123!";
        HttpPost Postrequest = new HttpPost(baseUrl+UserPostEndPoint);
        String jsonInputString = "{\"userName\": " + "\"" + Username + "\"" +
                ", \"password\": " +  "\"" + Password + "\"" + "}";
        Postrequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        Postrequest.setHeader(HttpHeaders.ACCEPT, "application/json");
        Postrequest.setEntity(new StringEntity(jsonInputString, ContentType.APPLICATION_JSON));

        response = client.execute(Postrequest);
        int statusCode = response.getStatusLine().getStatusCode();
        testUser = SwaggerUtilityMethods.jsonDeserealisation(response, UserSwagger.class);

        Assert.assertEquals(statusCode, 201);
        Assert.assertNotNull(testUser.getUsername());
        Assert.assertNotNull(testUser.getId());

        testUser.setPassword(Password);

        EntityUtils.consume(response.getEntity());
        response = client.execute(Postrequest);
        statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode, 406);
        EntityUtils.consume(response.getEntity());
       }

    @Test (priority = 2, groups = {"smoke", "positive"})
    public void AuthorisationValidation() throws IOException {
        String baseUrl= PropertyReader.getProperty("swaggerURL");
        String UserAuthEndPoint = PropertyReader.getProperty("UserAuthorisationEP");

        HttpPost Postrequest = new HttpPost(baseUrl+UserAuthEndPoint);
        String jsonInputString = "{\"userName\": " + "\"" + testUser.getUsername() + "\"" +
                ", \"password\": " +  "\"" + testUser.getPassword() + "\"" + "}";
        Postrequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        Postrequest.setHeader(HttpHeaders.ACCEPT, "application/json");
        Postrequest.setEntity(new StringEntity(jsonInputString, ContentType.APPLICATION_JSON));

        response = client.execute(Postrequest);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode, 200);
        EntityUtils.consume(response.getEntity());

    }

    @Test (priority = 3, groups = {"smoke", "positive"})
    public void GenerateToken() throws IOException {
        String baseUrl= PropertyReader.getProperty("swaggerURL");
        String UserGenTokenEndPoint = PropertyReader.getProperty("UserGenerateTokenEP");

        HttpPost Postrequest = new HttpPost(baseUrl+UserGenTokenEndPoint);
        String jsonInputString = "{\"userName\": " + "\"" + testUser.getUsername() + "\"" +
                ", \"password\": " +  "\"" + testUser.getPassword() + "\"" + "}";
        Postrequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        Postrequest.setHeader(HttpHeaders.ACCEPT, "application/json");
        Postrequest.setEntity(new StringEntity(jsonInputString, ContentType.APPLICATION_JSON));

        response = client.execute(Postrequest);
        EntityUtils.consume(response.getEntity());
        response = client.execute(Postrequest);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode, 200);
        String entityString=EntityUtils.toString(response.getEntity());
        String status = SwaggerUtilityMethods.getValueOfField(entityString, "status");
        String result = SwaggerUtilityMethods.getValueOfField(entityString, "result");

        Assert.assertEquals(status,"Success");
        Assert.assertEquals(result,"User authorized successfully.");

        token = SwaggerUtilityMethods.getValueOfField(entityString, "token");
        testUser.setToken(token);
    }

    @Test (priority = 4, groups = {"smoke", "positive"})
    public void getUser() throws IOException {
        String baseUrl= PropertyReader.getProperty("swaggerURL");
        String UserGetEndpoint = PropertyReader.getProperty("UserGetEP");
        String authentification = testUser.getUsername() + ":" + testUser.getPassword();
        String encode = Base64.getEncoder().encodeToString(authentification.getBytes());

        HttpGet GetRequest = new HttpGet(baseUrl+UserGetEndpoint+testUser.getId());

        GetRequest.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encode);

        response = client.execute(GetRequest);
        String entityString=EntityUtils.toString(response.getEntity());
        int statusCode = response.getStatusLine().getStatusCode();
        String userId = SwaggerUtilityMethods.getValueOfField(entityString, "userId");
        String username = SwaggerUtilityMethods.getValueOfField(entityString, "username");

        Assert.assertEquals(statusCode, 200);
        Assert.assertEquals(userId, testUser.getId());
        Assert.assertEquals(username, testUser.getUsername());
    }

    @Test (priority = 5, groups = {"smoke", "positive"})
    public void deleteUser() throws IOException {
        String baseUrl= PropertyReader.getProperty("swaggerURL");
        String UserDeleteEndpoint = PropertyReader.getProperty("UserDeleteEP");
        String authentification = testUser.getUsername() + ":" + testUser.getPassword();
        String encode = Base64.getEncoder().encodeToString(authentification.getBytes());

        HttpDelete DeleteRequest = new HttpDelete(baseUrl+UserDeleteEndpoint+testUser.getId());

        DeleteRequest.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encode);

        response = client.execute(DeleteRequest);
        int statusCode = response.getStatusLine().getStatusCode();

        Assert.assertEquals(statusCode, 204);
    }
    @Test (priority = 10, groups = {"negative"})
    public void getForDeletedUser() throws IOException {
        String baseUrl= PropertyReader.getProperty("swaggerURL");
        String UserGetEndpoint = PropertyReader.getProperty("UserGetEP");
        String authentification = testUser.getUsername() + ":" + testUser.getPassword();
        String encode = Base64.getEncoder().encodeToString(authentification.getBytes());

        HttpGet GetRequest = new HttpGet(baseUrl+UserGetEndpoint+testUser.getId());

        GetRequest.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encode);

        response = client.execute(GetRequest);
        int statusCode = response.getStatusLine().getStatusCode();

        Assert.assertEquals(statusCode, 401);
        EntityUtils.consume(response.getEntity());
    }
    @Test(priority = 10, groups = {"negative"}, dataProvider = "negativeLoginData")
    public void negativeCreation(String username, String password) throws IOException {
        String baseUrl= PropertyReader.getProperty("swaggerURL");
        String UserPostEndPoint = PropertyReader.getProperty("UserPostUrl");

        HttpPost Postrequest = new HttpPost(baseUrl+UserPostEndPoint);
        String jsonInputString = "{\"firstName\": " + (username != null ? "\"" + username + "\"" : "null") +
                ", \"lastName\": " + (password != null ? "\"" + password + "\"" : "null") + "}";
        Postrequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        Postrequest.setHeader(HttpHeaders.ACCEPT, "application/json");
        Postrequest.setEntity(new StringEntity(jsonInputString, ContentType.APPLICATION_JSON));

        response = client.execute(Postrequest);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode, 400);
        EntityUtils.consume(response.getEntity());
    }
    @Test(priority = 10, groups = {"negative"}, dataProvider = "negativeLoginData")
    public void negativeAuth(String username, String password) throws IOException {
        String baseUrl= PropertyReader.getProperty("swaggerURL");
        String UserAuthEndPoint = PropertyReader.getProperty("UserAuthorisationEP");

        HttpPost Postrequest = new HttpPost(baseUrl+UserAuthEndPoint);
        String jsonInputString = "{\"firstName\": " + (username != null ? "\"" + username + "\"" : "null") +
                ", \"lastName\": " + (password != null ? "\"" + password + "\"" : "null") + "}";
        Postrequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        Postrequest.setHeader(HttpHeaders.ACCEPT, "application/json");
        Postrequest.setEntity(new StringEntity(jsonInputString, ContentType.APPLICATION_JSON));

        response = client.execute(Postrequest);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode, 400);
        EntityUtils.consume(response.getEntity());
    }
    @Test(priority = 10, groups = {"negative"}, dataProvider = "negativeTokenData")
    public void negativeToken(String username, String password, int code) throws IOException {
        String baseUrl= PropertyReader.getProperty("swaggerURL");
        String UserGenTokenEndPoint = PropertyReader.getProperty("UserGenerateTokenEP");

        HttpPost Postrequest = new HttpPost(baseUrl+UserGenTokenEndPoint);
        String jsonInputString = "{\"firstName\": " + (username != null ? "\"" + username + "\"" : "null") +
                ", \"lastName\": " + (password != null ? "\"" + password + "\"" : "null") + "}";
        Postrequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        Postrequest.setHeader(HttpHeaders.ACCEPT, "application/json");
        Postrequest.setEntity(new StringEntity(jsonInputString, ContentType.APPLICATION_JSON));

        response = client.execute(Postrequest);
        EntityUtils.consume(response.getEntity());
        response = client.execute(Postrequest);
        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode, code);
        String entityString=EntityUtils.toString(response.getEntity());
        token = SwaggerUtilityMethods.getValueOfField(entityString, "token");
        Assert.assertNull(token);
        if(statusCode==200) {
            String status = SwaggerUtilityMethods.getValueOfField(entityString, "status");
            String result = SwaggerUtilityMethods.getValueOfField(entityString, "result");
            Assert.assertEquals(status, "Failed");
            Assert.assertEquals(result, "User authorization failed.");
        }
        EntityUtils.consume(response.getEntity());
    }

    @Test (priority = 10, groups = {"negative"}, dataProvider = "negativeDeleteData")
    public void deleteUserNegative(String username, String password, String ID, int code) throws IOException {
        String baseUrl= PropertyReader.getProperty("swaggerURL");
        String UserDeleteEndpoint = PropertyReader.getProperty("UserDeleteEP");
        String authentification = username + ":" + password;
        String encode = Base64.getEncoder().encodeToString(authentification.getBytes());

        HttpDelete DeleteRequest = new HttpDelete(baseUrl+UserDeleteEndpoint+ID);

        DeleteRequest.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encode);

        response = client.execute(DeleteRequest);
        int statusCode = response.getStatusLine().getStatusCode();

        Assert.assertEquals(statusCode, code);
        EntityUtils.consume(response.getEntity());
    }
    @AfterTest
    public void closeClient() throws IOException {
        client.close();
        response.close();
    }
}