package com.gnm.adrunner.util;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class storageObject {

    public  static AmazonS3 s3;

    public  static String  endPoint     =   "";

    public  static String  regionName   =   "";

    public  static String  bucketName   = "";


    private static String  accessKey    =   "";

    private static String  secretKey    =   "";



    public static String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public static void setAccessKey(String s1) {
        accessKey = s1;
    }

    public static void setSecretKey(String s2) {
        secretKey = s2;
    }

    public static void setEndPoint(String s3) {
        System.out.println("BUCKET ENDPOINT : "+s3);
        endPoint = s3;
    }
  
    public static void setRegionName(String s4) {
        System.out.println("BUCKET REGION : "+s4);
        regionName = s4;
    }

    public static void setBucketName(String s5) {
        System.out.println("BUCKET NAME : "+s5);
        bucketName = s5;
    }


    public static void bucketInit(){
        // S3 client
        s3 = AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
            .build();
    }

   
}
