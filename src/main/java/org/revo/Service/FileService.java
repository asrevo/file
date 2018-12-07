package org.revo.Service;

import net.lingala.zip4j.exception.ZipException;
import org.revo.Domain.File;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by ashraf on 15/04/17.
 */
public interface FileService {
    void process(File file) throws ZipException, IOException;

    Path store(String fun, File file);

}
