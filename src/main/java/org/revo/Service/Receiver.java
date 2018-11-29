package org.revo.Service;

import lombok.extern.slf4j.Slf4j;
import org.revo.Config.Processor;
import org.revo.Domain.File;
import org.revo.Domain.Queue;
import org.revo.Domain.State;
import org.revo.Domain.Stater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

/**
 * Created by ashraf on 23/04/17.
 */
@MessageEndpoint
@Slf4j
public class Receiver {
    @Autowired
    private FileService fileService;
    @Autowired
    private Processor processor;

    @StreamListener(value = Processor.file_queue)
    public void receive(Message<File> file) {
        log.info("recive "+file.getPayload().getId());
//        processor.ToFeedBack_push().send(MessageBuilder.withPayload(new Stater(file.getPayload(), Queue.FILE, State.ON_GOING)).build());
        fileService.process(file.getPayload());
//        processor.ToFeedBack_push().send(MessageBuilder.withPayload(new Stater(file.getPayload(), Queue.FILE, State.UNDER_GOING)).build());
    }
}