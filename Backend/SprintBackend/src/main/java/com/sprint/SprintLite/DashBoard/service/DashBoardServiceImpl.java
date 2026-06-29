package com.sprint.SprintLite.DashBoard.service;

import com.sprint.SprintLite.DashBoard.*;
import com.sprint.SprintLite.entity.*;
import com.sprint.SprintLite.entity.enums.SprintStatus;
import com.sprint.SprintLite.entity.enums.Status;
import com.sprint.SprintLite.repository.*;
import lombok.RequiredArgsConstructor;
import com.sprint.SprintLite.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    private final FeatureRepository featureRepository;

    private final ProductRepository productRepository;

    private final
    BoardService boardService;

    private final
    DashboardMetricsService
            metricsService;

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

        Integer progress =

                metricsService
                        .calculateProgress(
                                total,
                                completed
                        );

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

        long totalFeatures =
                sprint != null
                        ?
                        featureRepository
                        .findBySprintId(
                                sprint
                        )
                        .size()
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
                        metricsService
                        .calculateRemainingHours(
                                sprint
                        )
                        :
                        0;

        Integer completionRate =

                metricsService
                        .calculateCompletionRate(
                                completedStories,
                                totalStories
                        );

        String productName =

                sprint != null
                        &&
                        sprint.getProductid()!=null

                        ?

                        sprint
                        .getProductid()
                        .getProductname()
                        :
                        "No Product";

        PMDashboardDto dto =
                new PMDashboardDto(
                        sprintName,
                        progress,
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
                        completionRate,
                        totalFeatures,
                        productName
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

        Integer completion =

                metricsService
                        .calculateProgress(
                                total,
                                done
                        );

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

        Integer velocity =

                metricsService
                        .calculateProgress(
                                total,
                                completed
                        );

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
                )
                .map(

                        task ->

                                new TaskListDto(

                                        task.getId(),

                                        task.getTitle(),

                                        task.getTaskstatus() != null
                                                ?
                                                task.getTaskstatus().name()
                                                :
                                                "UNKNOWN"

                                )

                );

    }

    @Override
    public Page<StoryListDto>
    getStories(
            Integer page,
            Integer size
    ){

        Pageable pageable =
                PageRequest.of(
                        page,
                        size
                );

        return storyRepository
                .findAll(
                        pageable
                )
                .map(
                        story ->
                                new StoryListDto(
                                        story.getId(),
                                        story.getTitle(),
                                        story.getStorystatus()!=null
                                                ?
                                                story.getStorystatus().name()
                                                :
                                                "UNKNOWN"
                                )
                );

    }

    @Override
    public FeatureProgressDto
    getFeatureProgress(
            Integer featureId
    ){

        Feature feature =

                featureRepository
                        .findById(
                                featureId
                        )
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Feature not found"
                                        )
                        );

        Long total =

                storyRepository
                        .countByFeatureid(
                                feature
                        );

        Long completed =

                storyRepository
                        .countByFeatureidAndStorystatus(
                                feature,
                                Status.DONE
                        );

        Integer completion =

                metricsService
                        .calculateCompletionRate(
                                completed,
                                total
                        );
        return new FeatureProgressDto(

                feature.getId(),

                feature.getTitle(),

                total,

                completed,

                completion

        );
    }

    @Override
    public List<StoryCardDto>
    getStoriesByFeature(
            Integer featureId
    ){

        Feature feature =

                featureRepository
                        .findById(
                                featureId
                        )
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Feature not found"
                                        )
                        );

        return storyRepository
                .findAll()
                .stream()

                .filter(
                        s ->
                                s.getFeatureid()!=null
                                        &&
                                        s.getFeatureid()
                                                .getId()
                                                .equals(
                                                        feature.getId()
                                                )
                )

                .map(
                        s ->
                                new StoryCardDto(

                                        s.getId(),

                                        s.getTitle(),

                                        s.getStorystatus()!=null
                                                ?

                                                s.getStorystatus()
                                                .name()

                                                :

                                                "UNKNOWN"

                                )
                )

                .toList();

    }

    @Override
    public StoryProgressDto
    getStoryProgress(
            Integer storyId
    ){

        Story story =

                storyRepository
                        .findById(
                                storyId
                        )
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Story not found"
                                        )
                        );

        Long total =

                taskRepository
                        .countByStoryid(
                                story
                        );

        Long completed =

                taskRepository
                        .countByStoryidAndTaskstatus(
                                story,
                                Status.DONE
                        );

        Integer completion =

                metricsService
                        .calculateCompletionRate(
                                completed,
                                total
                        );

        return new StoryProgressDto(

                story.getId(),

                story.getTitle(),

                total,

                completed,

                completion

        );

    }

    @Override
    public List<BoardColumnDto>
    getBoard(
            Integer sprintId
    ){

        return boardService
                .getBoard(
                        sprintId
                );

    }

    @Override
    public void moveTask(
            MoveTaskDto dto
    ){

        Task task =

                taskRepository
                        .findById(
                                dto.getTaskId()
                        )
                        .orElseThrow(
                                ()->
                                        new RuntimeException(
                                                "Task not found"
                                        )
                        );

        task.setTaskstatus(

                Status.valueOf(
                        dto.getStatus()
                )

        );

        taskRepository
                .save(
                        task
                );

    }

}
