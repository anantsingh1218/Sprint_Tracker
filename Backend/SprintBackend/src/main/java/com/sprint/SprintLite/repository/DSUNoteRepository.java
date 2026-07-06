package com.sprint.SprintLite.repository;

import com.sprint.SprintLite.entity.DSUNote;
import com.sprint.SprintLite.entity.enums.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DSUNoteRepository extends JpaRepository<DSUNote, Integer> {

 List<DSUNote> findByNotesdate(LocalDate notesdate);

 List<DSUNote> findByEntitytypeAndEntityid(EntityType entityType, Integer entityId);

 List<DSUNote> findByBlockersContainingIgnoreCaseOrCompletedworkContainingIgnoreCaseOrNextplanContainingIgnoreCase(
         String blockers,
         String completedwork,
         String nextplan
 );

}