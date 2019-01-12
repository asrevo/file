package org.revo.Service.Impl;

import org.revo.Service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by ashraf on 22/04/17.
 */
@Service
public class UserServiceImpl implements UserService {
//    @Autowired
//    private TokenStore tokenStore;

    @Override
    public Optional<String> current() {
/*
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication instanceof OAuth2Authentication) {
                final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
                Object principal = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user");
                return Optional.ofNullable(((Map<String, Object>) principal).get("id").toString());
            } else if (authentication instanceof AnonymousAuthenticationToken) {
                return Optional.of("-1");
            } else return Optional.of("-1");
        }
*/
        return Optional.of("-1");
    }
}
