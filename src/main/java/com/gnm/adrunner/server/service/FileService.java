package com.gnm.adrunner.server.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.gnm.adrunner.util.storageObject;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

 
@Service("FileService")
public class FileService {
   
    
    @PersistenceContext
	private EntityManager entityManager;

    // 폴더 생성 
    public void makeFolder(String adsKey){
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(0L);
        objectMetadata.setContentType("application/x-directory");
        PutObjectRequest putObjectRequest = new PutObjectRequest(storageObject.bucketName, adsKey+"/", new ByteArrayInputStream(new byte[0]), objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead);

        try {
            storageObject.s3.putObject(putObjectRequest);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }

        putObjectRequest = null;
        objectMetadata = null;
    }

    // 파일 업로드
    public String uploadFile(String adsKey, MultipartFile file , String fileName) throws IOException {

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        
        File convFile = new File( file.getOriginalFilename() );
        FileOutputStream fos = new FileOutputStream( convFile );
        fos.write( file.getBytes() );
        fos.close();

        PutObjectRequest putObjectRequest = 
                new PutObjectRequest(storageObject
                    .bucketName, adsKey+"/"+fileName+"."+extension, convFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead);

        try {
            storageObject.s3.putObject(putObjectRequest);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }

        convFile.delete();
        
        putObjectRequest = null;
        convFile = null;

        return extension;
    }
  
}
