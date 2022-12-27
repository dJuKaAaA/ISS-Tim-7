package com.tim7.iss.tim7iss.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tim7.iss.tim7iss.models.Note;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteDto {

    @Null(message = "Id should not be provided")
    private Long id;
    private String date;
    @NotBlank(message = "No content for note provided")
    private String message;

    public NoteDto(Note note) {
        this.id = note.getId();
        this.date = note.getDate().toString();  // TODO: Change to better date format
        this.message = note.getMessage();
    }

}
