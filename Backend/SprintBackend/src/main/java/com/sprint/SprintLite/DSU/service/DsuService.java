package com.sprint.SprintLite.DSU.service;

import com.sprint.SprintLite.dto.AutoDtoResponse;
import com.sprint.SprintLite.dto.DsuDto;
import com.sprint.SprintLite.entity.*;
import com.sprint.SprintLite.entity.enums.EntityType;
import com.sprint.SprintLite.entity.enums.Status;
import com.sprint.SprintLite.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DsuService {
    private final DSUNoteRepository dsuNoteRepository;
    private final TaskRepository taskRepository;
    private final BugRepository bugRepository;
    private final StoryRepository storyRepository;
    private final FeatureRepository featureRepository;

    public DSUNote createDSUNote(
            EntityType st1,
            Integer st2,
            DsuDto request
    ) {
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        DSUNote dsuNote1 = new DSUNote();

        dsuNote1.setNotesdate(request.getNotesdate());
        dsuNote1.setEntityid(st2);
        dsuNote1.setEntitytype(st1);
        dsuNote1.setStatus(request.getStatus());   // missing line fixed
        dsuNote1.setCompletedwork(request.getCompletedwork());
        dsuNote1.setBlockers(request.getBlockers());
        dsuNote1.setNextplan(request.getNextplan());
        dsuNote1.setCreatedBy(username);

        return dsuNoteRepository.save(dsuNote1);
    }

    public List<DSUNote> getDSUNotes(EntityType entityType, Integer entityId) {
        return dsuNoteRepository.findByEntitytypeAndEntityid(entityType, entityId);
    }




    public void DeleteDsu(Integer id) {
        dsuNoteRepository.deleteById(id);
    }

    public Map<String,Object> getDsuBuDate(LocalDate date){

        List<DSUNote> notes=dsuNoteRepository.findByNotesdate(date);

        Map<String,Object>result=new LinkedHashMap<>();
        result.put("date",date);
        for(EntityType entityType:EntityType.values()){
            Map<Status,List<DSUNote>> groupedByStatus=notes.stream().filter(note->entityType.equals(note.getEntitytype()))
                    .collect(Collectors.groupingBy(DSUNote::getStatus));

            result.put(entityType.name(),groupedByStatus);
        }
        return result;
    }

    public AutoDtoResponse generateAutoDSU(LocalDate startDate, LocalDate endDate) {
        Instant start = startDate.atStartOfDay(
                ZoneId.systemDefault()
        ).toInstant();

        Instant end = endDate.plusDays(1)
                .atStartOfDay(
                        ZoneId.systemDefault()
                ).toInstant();
        List<Task> tasks=taskRepository.findByUpdatedAtBetween(start, end);
        List<Bug> bugs=bugRepository.findByUpdatedAtBetween(start, end);
        List<Story> storys=storyRepository.findByUpdatedAtBetween(start, end);
        List<Feature> features=featureRepository.findByUpdatedAtBetween(start, end);

        StringBuilder completed=new  StringBuilder();

        for(Task task:tasks){
            if(task.getTaskstatus() == Status.DONE){
                completed.append("Completed Task: ")
                        .append(task.getTitle())
                        .append(",");
            }
            else{
                completed.append("Worked on Task: ")
                        .append(task.getTitle())
                        .append(",");
            }
        }

        for(Bug bug:bugs){
            if(bug.getBugstatus() == Status.DONE){
                completed.append("Completed Bug: ")
                        .append(bug.getDescription())
                        .append(",");
            }
            else{
                completed.append("Worked on Bug:")
                        .append(bug.getDescription())
                        .append(",");

            }
        }

        for (Story story : storys) {
            if (story.getStorystatus() == Status.DONE) {
                completed.append("Completed Story: ")
                        .append(story.getTitle())
                        .append(", ");
            } else {
                completed.append("Worked on Story: ")
                        .append(story.getTitle())
                        .append(", ");
            }
        }

        for (Feature feature : features) {
            if (feature.getFeatureStatus() == Status.DONE) {
                completed.append("Completed Feature: ")
                        .append(feature.getTitle())
                        .append(", ");
            } else {
                completed.append("Worked on Feature: ")
                        .append(feature.getTitle())
                        .append(", ");
            }
        }

        AutoDtoResponse response = new AutoDtoResponse();
        response.setCompletedWork(completed.toString());

         return response;

    }

}
