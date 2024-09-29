package com.homework.homeworkProject;


import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertyReader {

    private static Properties properties = loadProperties();

    private static Properties loadProperties(){

        Properties properties = new Properties();

        try{
            properties.load(new FileInputStream(new File("config.properties")));
        }catch (Exception e){
            e.printStackTrace();
        }
        return properties;
    }

    public static String getProperty(String propertyName){
        return properties.getProperty(propertyName);
    }
}