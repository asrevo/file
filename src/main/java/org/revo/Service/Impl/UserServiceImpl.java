package org.revo.Service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.revo.Domain.User;
import org.revo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Created by ashraf on 22/04/17.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private ObjectMapper mapper;

    @Override
    public Mono<String> current() {
        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication).map(Authentication::getPrincipal)
                .cast(Jwt.class).map(Jwt::getClaims)
                .map(it -> it.get("user"))
                .map(it -> mapper.convertValue(it, User.class)).map(User::getId);
    }
}
