package com.tim7.iss.tim7iss.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tim7.iss.tim7iss.models.Note;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteDto {

    private Long id;
    private String date;
    private String message;

    public NoteDto(Note note) {
        this.id = note.getId();
        this.date = note.getDate().toString();  // TODO: Change to better date format
        this.message = note.getMessage();
    }

}
