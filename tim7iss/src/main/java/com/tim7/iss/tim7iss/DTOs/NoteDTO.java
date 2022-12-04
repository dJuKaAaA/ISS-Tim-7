package com.tim7.iss.tim7iss.DTOs;

import com.tim7.iss.tim7iss.models.Note;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteDTO {

    private Long id;
    private String date;
    private String message;

    public NoteDTO(Note note){
        this.id = note.getId();
        this.date = note.getDate().toString();
        this.message = note.getMessage();
    }
}
