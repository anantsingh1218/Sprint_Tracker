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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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

    private final UserProductMappingRepository userProductMappingRepository;

    private final FeatureRepository featureRepository;

    private final ProductRepository productRepository;

    private final BoardService boardService;

    private final DashboardMetricsService metricsService;

    private final WorklogRepository worklogRepository;


    public Users getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String username = authentication.getName();

        return usersRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Authenticated user not found"));

    }


    private Sprint getActiveSprint(Users user, Integer productId) {

        if (productId != null) {

            Product product = productRepository
                    .findById(productId)
                    .orElseThrow(() ->
                            new RuntimeException("Product not found"));

            return sprintRepository
                    .findByProductidAndStatus(
                            product,
                            SprintStatus.ACTIVE
                    )
                    .orElse(null);
        }

        List<UserProductMapping> mappings =
                userProductMappingRepository.findByUserid(user);

        if (mappings.isEmpty()) {
            return null;
        }

        Product product = mappings.get(0).getProductid();

        return sprintRepository
                .findByProductidAndStatus(
                        product,
                        SprintStatus.ACTIVE
                )
                .orElse(null);
    }

    @Override
    public DashboardResponseDto getDashboard() {

        Users user = getLoggedInUser();

        Role role = user.getRole();

        if (role == Role.ROLE_PM ||
                role == Role.ROLE_Scrum_Master) {

            return getPMDashboard(user);

        }

        return getTeamDashboard(user);

    }

    private DashboardResponseDto getPMDashboard(Users user) {

        List<UserProductMapping> mappings =
                userProductMappingRepository.findByUserid(user);

        List<ProductSummaryDto> products = new ArrayList<>();

        long totalFeatures = 0;
        long totalStories = 0;
        long totalTasks = 0;
        long completedTasks = 0;
        long blockedTasks = 0;

        int activeSprints = 0;
        long totalCompletedStories = 0;

        for (UserProductMapping mapping : mappings) {

            Product product = mapping.getProductid();

            Sprint sprint =
                    sprintRepository
                            .findByProductidAndStatus(
                                    product,
                                    SprintStatus.ACTIVE
                            )
                            .orElse(null);

            if (sprint == null)
                continue;

            activeSprints++;

            Long stories =
                    storyRepository.countBySprintid(sprint);

            Long completedStories =
                    storyRepository.countBySprintidAndStorystatus(
                            sprint,
                            Status.DONE
                    );

            Long features =
                    (long) featureRepository
                            .findFeaturesBySprintId(sprint)
                            .size();

            Long tasks =
                    taskRepository.countBySprintid(sprint);

            Long completed =
                    taskRepository.countBySprintidAndTaskstatus(
                            sprint,
                            Status.DONE
                    );

            Long blocked =
                    taskRepository.countBySprintidAndTaskstatus(
                            sprint,
                            Status.BLOCKED
                    );

            Integer progress =
                    metricsService.calculateProgress(
                            tasks,
                            completed
                    );

            Long pendingTasks =
                    tasks - completed;


            products.add(

                    new ProductSummaryDto(

                            product.getProductCode(),

                            product.getProductname(),

                            sprint.getSprintName(),

                            progress,

                            features,

                            stories,

                            completedStories,

                            tasks,

                            completed,

                            pendingTasks,

                            blocked

                    )

            );

            totalFeatures += features;
            totalStories += stories;
            totalCompletedStories += completedStories;
            totalTasks += tasks;
            completedTasks += completed;
            blockedTasks += blocked;
        }

        PMDashboardDto dto =
                new PMDashboardDto(

                        mappings.size(),

                        activeSprints,

                        totalFeatures,

                        totalStories,

                        totalCompletedStories,

                        totalTasks,

                        completedTasks,

                        blockedTasks,

                        products

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
            Integer productId
    ){

        Users user = getLoggedInUser();

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
                getActiveSprint(user, productId);

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
            Integer productId
    ){
        Users user = getLoggedInUser();

        Sprint sprint =
                getActiveSprint(user, productId);

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
            Integer productId
    ){

        Users user = getLoggedInUser();

        Sprint sprint = getActiveSprint(user, productId);

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
            Integer productId
    ){

        Users user = getLoggedInUser();

        Sprint sprint =
                getActiveSprint(user, productId);

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
    getSummary(Integer productId){

        return new DashboardSummaryDto(

                getReleaseReadiness(productId),

                getTeamCapacity(productId),

                getVelocity(productId),

                getBurndown(productId)

        );
    }

    @Override
    public ExportDashboardDto
    exportDashboard(Integer productId){

        DashboardSummaryDto summary =
                getSummary(
                        productId
                );

        return new ExportDashboardDto(

                "Dashboard Summary",

                summary

        );

    }

    @Override
    public Page<TaskListDto> getTasks(
            Integer page,
            Integer size
    ) {

        Users user = getLoggedInUser();

        Pageable pageable =
                PageRequest.of(
                        page,
                        size
                );

        return taskRepository
                .findByUserid(
                        user,
                        pageable
                )
                .map(task ->

                        TaskListDto.builder()

                                .taskCode(task.getTaskCode())

                                .title(task.getTitle())

                                .storyName(
                                        task.getStoryid().getTitle()
                                )

                                .sprintName(
                                        task.getSprintid().getSprintName()
                                )

                                .status(
                                        task.getTaskstatus().name()
                                )

                                .priority(
                                        task.getPriority().name()
                                )

                                .originalEstimateHours(
                                        task.getOriginalestimatehours()
                                )

                                .remainingEstimateHours(
                                        task.getRemainingestimatehours()
                                )

                                .build()

                );

    }

    @Override
    public Page<StoryListDto> getStories(
            Integer page,
            Integer size
    ){

        Users user = getLoggedInUser();

        Pageable pageable =
                PageRequest.of(
                        page,
                        size
                );

        return storyRepository
                .findByUserid(
                        user,
                        pageable
                )
                .map(story -> {

                    Long totalTasks =
                            taskRepository.countByStoryid(
                                    story
                            );

                    Long completedTasks =
                            taskRepository.countByStoryidAndTaskstatus(
                                    story,
                                    Status.DONE
                            );

                    Integer progress =
                            totalTasks == 0
                                    ? 0
                                    : (int)((completedTasks * 100) / totalTasks);

                    return StoryListDto.builder()

                            .storyCode(
                                    story.getStoryCode()
                            )

                            .title(
                                    story.getTitle()
                            )

                            .featureName(

                                    story.getFeatureid() != null

                                            ?

                                            story.getFeatureid().getTitle()

                                            :

                                            "No Feature"

                            )

                            .sprintName(

                                    story.getSprintid() != null

                                            ?

                                            story.getSprintid().getSprintName()

                                            :

                                            "No Sprint"

                            )

                            .status(

                                    story.getStorystatus() != null

                                            ?

                                            story.getStorystatus().name()

                                            :

                                            "UNKNOWN"

                            )

                            .totalTasks(
                                    totalTasks.intValue()
                            )

                            .completedTasks(
                                    completedTasks.intValue()
                            )

                            .progress(
                                    progress
                            )

                            .build();

                });

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

                feature.getFeatureCode(),

                feature.getTitle(),

                total,

                completed,

                completion

        );
    }

//    @Override
//    public List<StoryCardDto>
//    getStoriesByFeature(
//            Integer featureId
//    ){
//
//        Feature feature =
//
//                featureRepository
//                        .findById(
//                                featureId
//                        )
//                        .orElseThrow(
//                                () ->
//                                        new RuntimeException(
//                                                "Feature not found"
//                                        )
//                        );
//
//        return storyRepository
//                .findAll()
//                .stream()
//
//                .filter(
//                        s ->
//                                s.getFeatureid()!=null
//                                        &&
//                                        s.getFeatureid()
//                                                .getId()
//                                                .equals(
//                                                        feature.getId()
//                                                )
//                )
//
//                .map(
//                        s ->
//                                new StoryCardDto(
//
//                                        s.getId(),
//
//                                        s.getTitle(),
//
//                                        s.getStorystatus()!=null
//                                                ?
//
//                                                s.getStorystatus()
//                                                .name()
//
//                                                :
//
//                                                "UNKNOWN"
//
//                                )
//                )
//
//                .toList();
//
//    }

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
    public SprintProgressDto getSprintProgress(
            Integer productId
    ) {

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        Long totalFeatures =
                featureRepository.countByProductId(product);

        List<Feature> features =
                featureRepository.findByProductId(product);

        Long totalStories = 0L;
        Long totalTasks = 0L;
        Long completedTasks = 0L;
        Long blockedTasks = 0L;

        for (Feature feature : features) {

            totalStories +=
                    storyRepository.countByFeatureid(feature);

            List<Story> stories =
                    storyRepository.findByFeatureid(feature);

            for (Story story : stories) {

                totalTasks +=
                        taskRepository.countByStoryid(story);

                completedTasks +=
                        taskRepository.countByStoryidAndTaskstatus(
                                story,
                                Status.DONE
                        );

                blockedTasks +=
                        taskRepository.countByStoryidAndTaskstatus(
                                story,
                                Status.BLOCKED
                        );

            }

        }

        Long pendingTasks =
                totalTasks - completedTasks - blockedTasks;

        Integer completion =
                metricsService.calculateCompletionRate(
                        completedTasks,
                        totalTasks
                );

        return new SprintProgressDto(

                totalFeatures.intValue(),

                totalStories.intValue(),

                totalTasks.intValue(),

                completedTasks.intValue(),

                pendingTasks.intValue(),

                blockedTasks.intValue(),

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
                        .findByTaskCode(
                                dto.getTaskCode()
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

    @Override
    public List<ProductDropdownDto> getProducts() {

        Users user = getLoggedInUser();

        List<UserProductMapping> mappings =
                userProductMappingRepository.findByUserid(user);

        return mappings.stream()
                .map(mapping -> new ProductDropdownDto(
                        mapping.getProductid().getId(),
                        mapping.getProductid().getProductname()
                ))
                .sorted(Comparator.comparing(ProductDropdownDto::getProductName))
                .toList();
    }

    @Override
    public TaskListDto getFocusTask() {

        Users user = getLoggedInUser();

        List<Task> tasks =
                taskRepository.findByUserid(user);

        Task focusTask =
                tasks.stream()

                        .filter(task ->
                                task.getTaskstatus() != Status.DONE
                        )

                        .sorted((t1, t2) -> {

                            Integer p1 = switch (t1.getPriority()) {
                                case CRITICAL -> 4;
                                case HIGH -> 3;
                                case MEDIUM -> 2;
                                case LOW -> 1;
                            };

                            Integer p2 = switch (t2.getPriority()) {
                                case CRITICAL -> 4;
                                case HIGH -> 3;
                                case MEDIUM -> 2;
                                case LOW -> 1;
                            };

                            return p2.compareTo(p1);

                        })

                        .findFirst()

                        .orElse(null);

        if (focusTask == null) {

            return null;

        }

        return TaskListDto.builder()

                .taskCode(
                        focusTask.getTaskCode()
                )

                .title(
                        focusTask.getTitle()
                )

                .storyName(
                        focusTask.getStoryid().getTitle()
                )

                .sprintName(
                        focusTask.getSprintid().getSprintName()
                )

                .status(
                        focusTask.getTaskstatus().name()
                )

                .priority(
                        focusTask.getPriority().name()
                )

                .originalEstimateHours(
                        focusTask.getOriginalestimatehours()
                )

                .remainingEstimateHours(
                        focusTask.getRemainingestimatehours()
                )

                .build();

    }

    @Override
    public List<WorklogDto> getRecentWorklogs() {

        Users user = getLoggedInUser();

        List<Worklog> logs =
                worklogRepository
                        .findTop5ByUseridOrderByWorkdateDesc(user);

        System.out.println("Recent Logs = " + logs.size());

        return logs.stream()

                .map(worklog ->

                        WorklogDto.builder()

                                .taskId(
                                        worklog.getTaskid() != null
                                                ? worklog.getTaskid().getId()
                                                : null
                                )

                                .taskTitle(
                                        worklog.getTaskid() != null
                                                ? worklog.getTaskid().getTitle()
                                                : "General Work"
                                )

                                .storyTitle(
                                        worklog.getTaskid() != null &&
                                                worklog.getTaskid().getStoryid() != null
                                                ? worklog.getTaskid().getStoryid().getTitle()
                                                : "-"
                                )

                                .hoursSpent(
                                        worklog.getHoursspent()
                                )

                                .workDate(
                                        worklog.getWorkdate()
                                )

                                .remarks(
                                        worklog.getRemarks()
                                )

                                .build()

                )

                .toList();

    }

    @Override
    public List<SprintDropdownDto> getSprintDropdown() {

        return sprintRepository.findAll()

                .stream()

                .map(sprint ->

                        new SprintDropdownDto(

                                sprint.getId(),

                                sprint.getSprintCode(),

                                sprint.getSprintName()

                        )

                )

                .toList();

    }

}
