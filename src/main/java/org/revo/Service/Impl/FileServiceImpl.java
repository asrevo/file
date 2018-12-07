package org.revo.Service.Impl;

import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.exception.ZipException;
import org.bson.types.ObjectId;
import org.revo.Config.Processor;
import org.revo.Domain.File;
import org.revo.Domain.Master;
import org.revo.Service.FileService;
import org.revo.Service.S3Service;
import org.revo.Service.TempFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.Files.exists;
import static org.apache.commons.io.FileUtils.copyURLToFile;
import static org.apache.commons.io.FilenameUtils.getBaseName;
import static org.apache.commons.io.FilenameUtils.getName;
import static org.revo.Util.FileUtil.haveSpaceFor;
import static org.revo.Util.FileUtil.walk;

/**
 * Created by ashraf on 15/04/17.
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Autowired
    private S3Service s3Service;
    @Autowired
    private Processor processor;
    @Autowired
    private TempFileService tempFileService;

    @Override
    public void process(File file) throws ZipException, IOException {
        if (file.getUrl() != null && !file.getUrl().isEmpty()) {
            Path stored = store("queue", file);
            if (stored != null && exists(stored)) {
                List<Path> walk = walk(stored).collect(Collectors.toList());
                walk.forEach(w -> {
                    Master master = new Master();
                    master.setId(new ObjectId().toString());
                    master.setUserId(file.getUserId());
                    master.setTitle(file.getTitle());
                    if (walk.size() > 1)
                        master.setTitle(getBaseName(w.toString()));
                    master.setMeta(file.getMeta());
                    master.setFile(file.getId());
                    s3Service.push(master.getId(), w.toFile());
                    log.info("send tube_store " + master.getId());
                    processor.tube_store().send(MessageBuilder.withPayload(master).build());
                });
            } else {
            }
        }
    }

    @Override
    public Path store(String fun, File file) {
        try {
            Path tempFile = tempFileService.tempFile("queue", getName(file.getUrl()));
            if (haveSpaceFor(file.getUrl(), tempFile.toFile()))
                copyURLToFile(new URL(file.getUrl()), tempFile.toFile());
            else {
                return null;
            }
            return tempFile;
        } catch (IOException e) {
            return null;
        }
    }

}
