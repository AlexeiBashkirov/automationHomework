package com.homework.homeworkProject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.homeworkProject.dto.User;
import com.homework.homeworkProject.dto.UserSwagger;
import com.thoughtworks.xstream.mapper.Mapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class SwaggerUtilityMethods {


    public static UserSwagger jsonDeserealisation(CloseableHttpResponse response, Class <UserSwagger> ourClass) throws IOException {
        String jsonBody = EntityUtils.toString(response.getEntity());

        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(jsonBody, ourClass);
    }

    public static String getValueOfField(String response, String requestedField) throws JsonProcessingException {
        String attribute="";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode JSON = objectMapper.readTree(response);
        JsonNode field = JSON.get(requestedField);
        try{attribute = field.asText();}catch (NullPointerException e){
            return null;
        }
        return attribute;
    }
}
