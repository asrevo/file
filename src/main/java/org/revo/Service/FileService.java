package org.revo.Service;

import org.revo.Domain.File;

import java.nio.file.Path;

/**
 * Created by ashraf on 15/04/17.
 */
public interface FileService {
    void process(File file);

    Path store(String fun, File file);

}
