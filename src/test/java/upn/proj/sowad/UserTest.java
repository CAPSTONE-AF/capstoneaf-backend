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

import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
            this.userService.register("Ben", "Walmart", "johndoe", "benwalmart@gmail.com", "benwalmart", null);
        } catch (UsernameExistException | UserNotFoundException | EmailExistException | MessagingException | IOException e) {
            realMessage = e.getMessage();
        }

        assertEquals(expectedMessage,realMessage);
    }

    @Test
    public void registerUser_whenEmailIsAlreadyRegistered_recieveException(){
        String expectedMessage = EMAIL_ALREADY_EXISTS;
        String realMessage = "";
        try {
            if(this.userService.findUserByUsername("Garcisama")!=null)
                this.userService.deleteUser("Garcisama");
            this.userService.register("Maria", "Garcia", "Garcisama", "arcisama@gmail.com", "12345ma", null);
            this.userService.register("Luis", "Vega", "jvegais", "arcisama@gmail.com", "vegas", null);
        } catch (UsernameExistException | UserNotFoundException | EmailExistException | MessagingException | IOException e) {
            realMessage = e.getMessage();
        }

        assertEquals(expectedMessage,realMessage);
    }

    @Test
    public void registerUser_whenRegistered_passwordIsEncrypted() throws UserNotFoundException, EmailExistException, MessagingException, UsernameExistException, IOException {
        String realMessage = "";
        User userNotSaved = new User();
        userNotSaved.setFirstName("John");
        userNotSaved.setLastName("Doe");
        userNotSaved.setUsername("johndoe");
        userNotSaved.setEmail("johndoe@gmail.com");
        userNotSaved.setPassword("johndoe");
        UserDto savedUser;
        if(this.userService.findUserByUsername("johndoe")!=null)
            this.userService.deleteUser("johndoe");
        savedUser = this.userService.register(userNotSaved.getFirstName(),
                userNotSaved.getLastName(), userNotSaved.getUsername(), userNotSaved.getEmail(), userNotSaved.getPassword(), null);
        assertNotEquals(userNotSaved.getPassword(),savedUser.getPassword());
    }



    
}
