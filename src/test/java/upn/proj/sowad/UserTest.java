package upn.proj.sowad;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import upn.proj.sowad.dto.UserDto;
import upn.proj.sowad.entities.User;
import upn.proj.sowad.exception.domain.EmailExistException;
import upn.proj.sowad.exception.domain.UserNotFoundException;
import upn.proj.sowad.exception.domain.UsernameExistException;
import upn.proj.sowad.services.UserService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static upn.proj.sowad.constant.UserImplConstant.*;

@SpringBootTest
public class UserTest {

    public Integer count = 0;
    
    @Autowired
    private UserService userService;

    @Test
    public void registerUser_whenUsernameIsAlreadyRegistered_recieveException(){
        String expectedMessage = USERNAME_ALREADY_EXISTS;
        String realMessage = "";
        try {
            if(this.userService.findUserByUsername("johndoe")!=null)
                this.userService.deleteUser("johndoe");
            this.userService.register("John", "Doe", "johndoe", "johndoe@gmail.com", "johndoe", null);
            this.userService.register("John", "Doe", "johndoe", "johndoe@gmail.com", "johndoe", null);
        } catch (UsernameExistException | UserNotFoundException | EmailExistException | MessagingException | IOException e) {
            realMessage = e.getMessage();
        }

        assertEquals(expectedMessage,realMessage);
    }

    
}
