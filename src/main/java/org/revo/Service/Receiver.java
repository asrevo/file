package org.revo.Service;

import lombok.extern.slf4j.Slf4j;
import org.revo.Config.Processor;
import org.revo.Domain.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;

import java.io.IOException;

/**
 * Created by ashraf on 23/04/17.
 */
@MessageEndpoint
@Slf4j
public class Receiver {
    @Autowired
    private FileService fileService;
    @Autowired
    private TempFileService tempFileService;

    @StreamListener(value = Processor.file_queue)
    public void queue(Message<File> file) throws IOException {
        tempFileService.clear("queue");
        log.info("receive file_queue " + file.getPayload().getId());
        fileService.process(file.getPayload());
    }
}