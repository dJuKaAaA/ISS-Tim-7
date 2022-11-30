package RequestDTOs;

import jakarta.persistence.Entity;
import jdk.jfr.Enabled;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class PassengerRequestDTO {
    public String name,surname,profilePicture,telephoneNumber,email,address, password;
}
