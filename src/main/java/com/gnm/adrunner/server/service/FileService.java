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
    

    // 광고 소재 폴더 삭제
    public void deleteFile(String fileName) throws IOException {
        try{
            storageObject.s3.deleteObject(storageObject.bucketName, fileName);
        }catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }
    }


    // 파일 업로드
    public String uploadFile(String fileName, MultipartFile file) throws IOException {

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        
        File convFile = new File( file.getOriginalFilename() );
        FileOutputStream fos = new FileOutputStream( convFile );
        fos.write( file.getBytes() );
        fos.close();

        PutObjectRequest putObjectRequest = 
                new PutObjectRequest(storageObject
                        .bucketName, fileName+"."+extension, convFile)
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
