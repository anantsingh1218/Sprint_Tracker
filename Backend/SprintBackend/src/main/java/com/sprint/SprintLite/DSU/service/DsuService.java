package com.sprint.SprintLite.DSU.service;

import com.sprint.SprintLite.dto.DsuDto;
import com.sprint.SprintLite.entity.DSUNote;
import com.sprint.SprintLite.entity.enums.EntityType;
import com.sprint.SprintLite.entity.enums.Status;
import com.sprint.SprintLite.repository.DSUNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DsuService {
    private final DSUNoteRepository dsuNoteRepository;

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

}
