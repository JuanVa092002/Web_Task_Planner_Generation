package com.generation.taskplanner.service;

import com.generation.taskplanner.model.AppUser;
import com.generation.taskplanner.repository.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final AppUserRepository appUserRepository;

    public UserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public AppUser ensureUser(String clerkUserId, String email, String displayName) {
        return appUserRepository.findByClerkUserId(clerkUserId).map(existing -> {
            if (email != null && !email.isBlank()) {
                existing.setEmail(email);
            }
            if (displayName != null && !displayName.isBlank()) {
                existing.setDisplayName(displayName);
            }
            return appUserRepository.save(existing);
        }).orElseGet(() -> {
            AppUser user = new AppUser();
            user.setClerkUserId(clerkUserId);
            user.setEmail(email);
            user.setDisplayName(displayName);
            return appUserRepository.save(user);
        });
    }
}
