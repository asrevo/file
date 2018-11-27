package org.revo.Config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface Processor {
    String ToFile = "ToFile";

    @Input("ToFile")
    MessageChannel ToFile();

    String ToTube_store = "ToTube_store";

    @Output("ToTube_store")
    MessageChannel ToTube_store();

    String ToFeedBack_push = "ToFeedBack_push";

    @Output("ToFeedBack_push")
    MessageChannel ToFeedBack_push();
}
