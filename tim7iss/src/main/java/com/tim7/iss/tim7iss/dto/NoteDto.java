package com.tim7.iss.tim7iss.dto;

import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.Note;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteDto {

    @Null(message = "Id should not be provided")
    private Long id;

    @Pattern(regexp = "^([1-9]|([012][0-9])|(3[01]))\\.([0]{0,1}[1-9]|1[012])\\.\\d\\d\\d\\d\\s([0-1]?[0-9]|2?[0-3]):([0-5]\\d):([0-5]\\d)$",
            message = "Invalid date format")
    private String date;
    @NotBlank
    @NotNull
    @NotBlank(message = "No content for note provided")
    @Size(min = 2, message = "{validation.name.size.too_short}")
    @Size(max = 250, message = "{validation.name.size.too_long}")
    private String message;

    public NoteDto(Note note) {
        this.id = note.getId();
        this.date = note.getDate().format(Constants.customDateTimeFormat);
        this.message = note.getMessage();
    }

}
