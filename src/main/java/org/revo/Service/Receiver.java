package org.revo.Service;

import lombok.extern.slf4j.Slf4j;
import org.revo.Config.Processor;
import org.revo.Domain.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;

/**
 * Created by ashraf on 23/04/17.
 */
@MessageEndpoint
@Slf4j
public class Receiver {
    @Autowired
    private FileService fileService;

    @StreamListener(value = Processor.file_queue)
    public void queue(Message<File> file) {
        log.info("receive file_queue " + file.getPayload().getId());
        fileService.process(file.getPayload());
    }
}