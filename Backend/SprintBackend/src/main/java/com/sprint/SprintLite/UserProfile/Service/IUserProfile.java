package com.sprint.SprintLite.UserProfile.Service;

import com.sprint.SprintLite.dto.UserProfileDto;
import org.springframework.stereotype.Service;

@Service
public interface IUserProfile {
    UserProfileDto getUserProfile(String username);
}
