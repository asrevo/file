package org.revo.Service;

import java.io.File;

/**
 * Created by ashraf on 15/04/17.
 */
public interface S3Service {

    void push(String key, File file);

    String getUrl(String key,String bucket);

}
