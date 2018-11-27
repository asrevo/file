package org.revo.Service;

import org.revo.Domain.Bucket;

public interface SignedUrlService {
    String getUrl(Bucket bucket, String key);
}
