package com.tim7.iss.tim7iss.repositories;

import com.tim7.iss.tim7iss.models.Note;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note,Long> {
    List<Note> findAllByUserId(Long userId, Pageable pageable);
}
