package org.revo.Service.Impl;

import org.revo.Domain.Bucket;
import org.revo.Service.SignedUrlService;
import org.springframework.stereotype.Service;

import static com.amazonaws.services.cloudfront.util.SignerUtils.Protocol.https;
import static com.amazonaws.services.cloudfront.util.SignerUtils.generateResourcePath;

@Service
public class SignedUrlServiceImpl implements SignedUrlService {
    @Override
    public String getUrl(Bucket bucket, String key) {
        return generateResourcePath(https, bucket.getDomainName(), key);
    }
}
