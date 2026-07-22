package com.sprint.SprintLite.UserProfile.Controller;

import com.sprint.SprintLite.UserProfile.Service.IUserProfile;
import com.sprint.SprintLite.dto.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/UserProfile")
@RequiredArgsConstructor
public class UserPController {
    private final IUserProfile profile;
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getUserProfile() {
        String username= SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(profile.getUserProfile(username));
    }

}
