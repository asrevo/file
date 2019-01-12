package org.revo.Service;

import reactor.core.publisher.Mono;

/**
 * Created by ashraf on 22/04/17.
 */
public interface UserService {
    Mono<String> current();
}
