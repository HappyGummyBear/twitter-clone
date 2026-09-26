package com.business.twitter.service;

import io.awspring.cloud.s3.S3Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URL;
import java.time.Duration;

@Service
public class S3StorageService {

    private final S3Template s3Template;
    private final String bucketName;

    public S3StorageService(S3Template s3Template, @Value("${aws.s3.bucket-name}") String bucketName) {
        this.s3Template = s3Template;
        this.bucketName = bucketName;
    }

    public URL generatePresignedGetUrl(String key) {
         return s3Template.createSignedGetURL(bucketName, key, Duration.ofHours(1));
    }

    public URL generatePresignedUploadUrl(
            String key) {
        return s3Template.createSignedPutURL(
                bucketName,
                key,
                Duration.ofMinutes(15)
        );
    }

    public boolean objectExistsInS3(String key) {
        try {
            return s3Template.objectExists(bucketName, key);
        } catch (Exception e) {
            return false;
        }
    }

    public void deleteObjectFromS3(String key) {
        try {
            s3Template.deleteObject(bucketName, key);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete object from s3", e);
        }
    }
}
