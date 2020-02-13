package com.michmzr.gimmeback.security;

import com.michmzr.gimmeback.user.User;
import com.michmzr.gimmeback.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SpringSecurityService {
    @Autowired
    UserRepository userRepository;

    /**
     * Returns current logged user
     *
     * @return
     */
    public User getCurrentUser() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal != null) {
            return userRepository.findById(principal.getId()).get();
        } else {
            return null;
        }
    }
}
