package upn.proj.sowad;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import upn.proj.sowad.dto.UserDto;
import upn.proj.sowad.services.UserService;
import static upn.proj.sowad.constant.UserImplConstant.*;

@SpringBootTest
public class UserTest {
    
    @Autowired
    private UserService userService;

    @Test
    public void registerUserTest1(){
        UserDto userTest = new UserDto();
        userTest.setFirstName("admin");
        userTest.setLastName("admin");
        userTest.setUsername("admin");
        userTest.setEmail("admin@admin.com");
        userTest.setPassword("admin");
        
        try {
            this.userService.register("Chad", "Mamani", "chadma", "chadma@gmail.com", "admin", null);
        } catch (Exception e) {
            String expectedMessage = USERNAME_ALREADY_EXISTS;
            assertEquals(expectedMessage, e.getMessage());
        }

    }

    
}
