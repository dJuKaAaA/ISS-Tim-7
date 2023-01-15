package com.tim7.iss.tim7iss.dto;

import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.Ride;
import com.tim7.iss.tim7iss.models.Role;
import com.tim7.iss.tim7iss.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithMessageReceiveDto {

    private Long id;
    private String dtype;
    private String first_name;
    private String last_name;
    private String profile_picture;
    private String phone_number;
    private String email_address;
    private String address;
    private String password;
    private boolean is_blocked;
    private boolean is_active;
    private boolean enabled = true;
    private Timestamp last_password_reset_date;
    private LocalDateTime sent_date;
    private String type;
    private String content;
    private Long sender_id;
    private Long receiver_id;
    private Long ride_id;
    private Long rn;

}
