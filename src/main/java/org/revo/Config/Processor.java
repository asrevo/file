package org.revo.Config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface Processor {
    String file_queue = "file_queue";

    @Input("file_queue")
    SubscribableChannel file_queue();

    String tube_store = "tube_store";

    @Output("tube_store")
    MessageChannel tube_store();
}
