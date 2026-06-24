package com.sprint.SprintLite.DashBoard.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.sprint.SprintLite.dto.*;
import com.sprint.SprintLite.repository.UsersRepository;

@Service
@RequiredArgsConstructor
public class DashBoardServiceImpl
        implements DashBoardService {

    private final UsersRepository usersRepository;

    @Override
    public DashboardResponseDto getDashboard(
            Integer userId
    ){

        var user =
                usersRepository
                        .findById(userId)
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "User not found"
                                        )
                        );

        String role =
                user
                        .getRole()
                        .toString();

        if(
                role.equalsIgnoreCase("PM")
                        ||
                        role.equalsIgnoreCase("SCRUM_MASTER")
        ){
            return getPMDashboard();
        }

        return getTeamDashboard();

    }

    private DashboardResponseDto getPMDashboard(){

        PMDashboardDto dto =
                new PMDashboardDto(
                        "Sprint 1",
                        70,
                        120L,
                        85L,
                        20L,
                        15L,
                        160
                );

        return new DashboardResponseDto(
                "MANAGEMENT",
                dto
        );

    }

    private DashboardResponseDto getTeamDashboard(){

        TeamMemberDto dto =
                new TeamMemberDto(
                        12L,
                        7L,
                        5L
                );

        return new DashboardResponseDto(
                "TEAM",
                dto
        );

    }

}