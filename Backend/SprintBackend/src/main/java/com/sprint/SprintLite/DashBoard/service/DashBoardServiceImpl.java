package com.sprint.SprintLite.DashBoard.service;

import com.sprint.SprintLite.entity.Product;
import com.sprint.SprintLite.entity.Sprint;
import com.sprint.SprintLite.entity.Users;
import com.sprint.SprintLite.entity.enums.SprintStatus;
import com.sprint.SprintLite.entity.enums.Status;
import com.sprint.SprintLite.repository.*;
import lombok.RequiredArgsConstructor;
import com.sprint.SprintLite.entity.enums.Role;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sprint.SprintLite.dto.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashBoardServiceImpl
        implements DashBoardService {

    private final UsersRepository usersRepository;

    private final TaskRepository taskRepository;

    private final SprintRepository sprintRepository;

    private final StoryRepository storyRepository;

    private final BugRepository bugRepository;

    private final UserProductMappingRepository
            userProductMappingRepository;

    @Override
    public DashboardResponseDto getDashboard(
            Integer userId
    ){

        Users user =
                usersRepository
                        .findById(userId)
                        .orElseThrow(
                                ()->
                                        new RuntimeException(
                                                "User not found"
                                        )
                        );

        Role role =
                user.getRole();

        if(
                role ==
                        Role.ROLE_PM
                        ||
                        role ==
                                Role.ROLE_Scrum_Master
        ){
            return getPMDashboard();
        }

        return getTeamDashboard(
                user
        );

    }

    private DashboardResponseDto
    getPMDashboard(){

        var sprint =
                sprintRepository
                        .findFirstByStatus(
                                SprintStatus.ACTIVE
                        )
                        .orElse(null);

        String sprintName =
                sprint != null
                        ?
                        sprint.getSprintName()
                        :
                        "No Sprint";

        Long total =
                taskRepository.count();

        Long completed =
                taskRepository
                        .countByTaskstatus(
                                Status.DONE
                        );

        Long progress = 0L;

        if(total>0){

            progress =
                    (
                            completed
                                    *100
                    )
                            /total;

        }

        Long inProgress =
                taskRepository
                        .countByTaskstatus(
                                Status.IN_PROGRESS
                        );

        Long blocked =
                taskRepository
                        .countByTaskstatus(
                                Status.BLOCKED
                        );

        Integer totalHours =
                sprint != null
                        ?
                        taskRepository
                        .getTotalEstimatedHours(
                                sprint
                        )
                        :
                        0;

        Long totalStories =
                sprint!=null
                        ?
                        storyRepository
                        .countBySprintid(
                                sprint
                        )
                        :
                        0L;

        Long completedStories =
                sprint!=null
                        ?
                        storyRepository
                        .countBySprintidAndStorystatus(
                                sprint,
                                Status.DONE
                        )
                        :
                        0L;

        Integer remainingHours =
                sprint!=null
                        ?
                        taskRepository
                        .getRemainingHours(
                                sprint
                        )
                        :
                        0;

        Integer completionRate = 0;

        if(
                totalStories>0
        ){

            completionRate=
                    (
                            completedStories
                                    .intValue()
                                    *100
                    )
                            /
                            totalStories
                                    .intValue();

        }

        PMDashboardDto dto =
                new PMDashboardDto(
                        sprintName,
                        progress.intValue(),
                        total,
                        completed,
                        inProgress,
                        blocked,
                        totalHours,
                        totalStories,
                        sprint!=null
                                ?
                                sprint
                                .getSprintDuration()
                                :
                                0,
                        completedStories,
                        remainingHours,
                        completionRate
                );

        return new DashboardResponseDto(
                "MANAGEMENT",
                dto
        );

    }
    private DashboardResponseDto
    getTeamDashboard(
            Users user
    ){

        Long assigned =
                taskRepository
                        .countByUserid(
                                user
                        );

        Long completed =
                taskRepository
                        .countByUseridAndTaskstatus(
                                user,
                                Status.DONE
                        );

        Long pending =
                assigned
                        -
                        completed;

        TeamMemberDto dto =
                new TeamMemberDto(
                        assigned,
                        completed,
                        pending
                );

        return new DashboardResponseDto(
                user.getRole().name(),
                dto
        );

    }
    @Override
    public ReleaseReadinessDto
    getReleaseReadiness(
            Integer userId
    ){

        Users user =
                usersRepository
                        .findById(
                                userId
                        )
                        .orElseThrow();

        var mapping =
                userProductMappingRepository
                        .findByUserid(
                                user
                        )
                        .stream()
                        .findFirst()
                        .orElse(null);

        Product product =
                mapping!=null
                        ?
                        mapping.getProductid()
                        :
                        null;

        Sprint sprint =
                product!=null
                        ?
                        sprintRepository
                        .findByProductidAndStatus(
                                product,
                                SprintStatus.ACTIVE
                        )
                        .orElse(null)
                        :
                        null;

        Long blocked =
                taskRepository
                        .countByTaskstatus(
                                Status.BLOCKED
                        );

        Long bugs =
                bugRepository
                        .countByBugstatus(
                                Status.OPEN
                        );

        Long total =
                taskRepository
                        .count();

        Long done =
                taskRepository
                        .countByTaskstatus(
                                Status.DONE
                        );

        Integer completion = 0;

        if(total>0){

            completion=
                    (
                            done.intValue()
                                    *100
                    )
                            /
                            total.intValue();

        }

        Boolean ready =
                completion>=80
                        &&
                        blocked==0
                        &&
                        bugs<=2;

        return new ReleaseReadinessDto(

                product!=null
                        ?
                        product.getProductname()
                        :
                        "No Product",

                sprint != null
                        ?
                        sprint.getSprintName()
                        :
                        "Not Planned Yet",

                completion,

                bugs,

                blocked,

                ready

        );

    }

    @Override
    public TeamCapacityDto
    getTeamCapacity(
            Integer userId
    ){

        Integer total =
                Math.toIntExact(
                        usersRepository
                                .count()
                );

        Integer available = 0;

        Integer busy = 0;

        Integer overloaded = 0;

        if(total>0){

            available =
                    total/3;

            busy =
                    total/2;

            overloaded =
                    total
                            -
                            available
                            -
                            busy;

        }

        return new TeamCapacityDto(
                total,
                available,
                busy,
                overloaded
        );

    }

    @Override
    public VelocityDto
    getVelocity(
            Integer userId
    ){

        Sprint sprint =
                sprintRepository
                        .findFirstByStatus(
                                SprintStatus.ACTIVE
                        )
                        .orElse(null);

        if(
                sprint
                        ==
                        null
        ){

            return new VelocityDto(
                    "Not Planned Yet",
                    0L,
                    0L,
                    0
            );

        }

        Long total =
                storyRepository
                        .countBySprintid(
                                sprint
                        );

        Long completed =
                storyRepository
                        .countBySprintidAndStorystatus(
                                sprint,
                                Status.DONE
                        );

        Integer velocity = 0;

        if(total>0){

            velocity=
                    (
                            completed.intValue()
                                    *100
                    )
                            /
                            total.intValue();

        }

        return new VelocityDto(

                sprint.getSprintName(),

                completed,

                total,

                velocity

        );

    }

    @Override
    public BurndownDto
    getBurndown(
            Integer userId
    ){

        Sprint sprint =
                sprintRepository
                        .findFirstByStatus(
                                SprintStatus.ACTIVE
                        )
                        .orElse(null);

        if(
                sprint
                        ==
                        null
        ){

            return new BurndownDto(
                    "Not Planned Yet",
                    List.of()
            );

        }

        Long total =
                taskRepository.count();

        List<ChartItemDto> points =
                List.of(

                        new ChartItemDto(
                                "Day 1",
                                total.intValue()
                        ),

                        new ChartItemDto(
                                "Day 5",
                                (
                                        total.intValue()
                                                *75
                                )
                                        /
                                        100
                        ),

                        new ChartItemDto(
                                "Day 10",
                                (
                                        total.intValue()
                                                *40
                                )
                                        /
                                        100
                        ),

                        new ChartItemDto(
                                "Today",
                                (
                                        total.intValue()
                                                *20
                                )
                                        /
                                        100
                        )

                );

        return new BurndownDto(

                sprint.getSprintName(),

                points

        );

    }

    @Override
    public DashboardSummaryDto
    getSummary(
            Integer userId
    ){

        return new DashboardSummaryDto(

                getReleaseReadiness(
                        userId
                ),

                getTeamCapacity(
                        userId
                ),

                getVelocity(
                        userId
                ),

                getBurndown(
                        userId
                )
        );
    }

    @Override
    public ExportDashboardDto
    exportDashboard(
            Integer userId
    ){

        DashboardSummaryDto summary =
                getSummary(
                        userId
                );

        return new ExportDashboardDto(

                "Dashboard Summary",

                summary

        );

    }

    @Override
    public Object getTasks(

            Integer page,

            Integer size

    ){

        Pageable pageable =

                PageRequest.of(
                        page,
                        size
                );

        return taskRepository
                .findAll(
                        pageable
                );

    }

}