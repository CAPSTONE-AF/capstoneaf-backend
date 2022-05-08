package upn.proj.sowad.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long idUser;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private Long idGrado;
}
