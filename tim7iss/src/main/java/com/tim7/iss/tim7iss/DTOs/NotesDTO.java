package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.Note;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotesDTO {
    private int totalCount;
    private Set<NoteDTO> results = new HashSet<>();

    public NotesDTO(Set<Note> notes){
        for(Note note : notes){
            NoteDTO noteDTO = new NoteDTO(note);
            this.results.add(noteDTO);
        }
        this.totalCount = results.size();
    }
}
