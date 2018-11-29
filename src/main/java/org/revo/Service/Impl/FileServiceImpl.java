package org.revo.Service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.revo.Config.Processor;
import org.revo.Domain.File;
import org.revo.Domain.Master;
import org.revo.Service.FileService;
import org.revo.Service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.Files.createTempDirectory;
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

    @Override
    public void process(File file) {
        if (file.getUrl() != null && !file.getUrl().isEmpty()) {
            Path stored = store(file);
            if (stored != null && exists(stored)) {
                List<Path> walk = walk(stored);
                for (Path path : walk) {
                    Master master = new Master();
                    master.setId(new ObjectId().toString());
                    master.setUserId(file.getUserId());
                    master.setTitle(file.getTitle());
                    if (walk.size() > 1)
                        master.setTitle(getBaseName(path.toString()));
                    master.setMeta(file.getMeta());
                    master.setFile(file.getId());
                    s3Service.push(master.getId(), path.toFile());
                    processor.tube_store().send(MessageBuilder.withPayload(master).build());

//                    processor.ToFeedBack_push().send(MessageBuilder.withPayload(new Stater(master, Queue.TUBE_STORE, State.QUEUED)).build());
                }
                stored.toFile().delete();
            } else {
            }
        }
    }

    @Override
    public Path store(File file) {
        try {
            Path tempFile = createTempDirectory("video").resolve(getName(file.getUrl()));
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
