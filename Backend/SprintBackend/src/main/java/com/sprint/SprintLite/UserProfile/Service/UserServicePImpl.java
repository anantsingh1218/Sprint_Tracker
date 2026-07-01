package com.sprint.SprintLite.UserProfile.Service;

import com.sprint.SprintLite.dto.UserProfileDto;
import com.sprint.SprintLite.entity.Users;
import com.sprint.SprintLite.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServicePImpl implements IUserProfile {

   private final UsersRepository usersRepository;
    @Override
    public UserProfileDto getUserProfile(String username) {
        Users user=usersRepository.findByUsername(username)
                .orElseThrow(()->new RuntimeException("User not found"));
        UserProfileDto dto=new UserProfileDto();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());

        return dto;

    }



}
