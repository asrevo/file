package org.revo.Service.Impl;

import com.amazonaws.services.s3.AmazonS3Client;
import org.revo.Config.Env;
import org.revo.Service.S3Service;
import org.revo.Service.SignedUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by ashraf on 15/04/17.
 */
@Service
public class S3ServiceImpl implements S3Service {
    @Autowired
    private AmazonS3Client amazonS3Client;
    @Autowired
    private SignedUrlService signedUrlService;
    @Autowired
    private Env env;

    @Override
    public void push(String key, File file) {
        this.amazonS3Client.putObject(env.getBuckets().get("video").toString(), key, file);
    }

    @Override
    public String getUrl(String key, String bucket) {
        return signedUrlService.getUrl(env.getBuckets().get(bucket), key);
    }

}
