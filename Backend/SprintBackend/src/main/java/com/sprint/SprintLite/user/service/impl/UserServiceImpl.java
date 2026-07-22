package com.sprint.SprintLite.user.service.impl;

import com.sprint.SprintLite.repository.UsersRepository;
import com.sprint.SprintLite.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements IUserService {

    private final UsersRepository userRepository;

}
