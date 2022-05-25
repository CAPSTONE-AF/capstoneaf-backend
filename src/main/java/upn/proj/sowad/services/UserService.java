package upn.proj.sowad.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.web.multipart.MultipartFile;

import upn.proj.sowad.dto.UserDto;
import upn.proj.sowad.entities.Grado;
import upn.proj.sowad.entities.GradoPopulation;
import upn.proj.sowad.entities.User;
import upn.proj.sowad.exception.domain.*;

public interface UserService {

    UserDto register(String firstName, String lastName, String username, String email, String password, Long idGrado) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException;

    List<User> getUsers();

    User findUserByUsername(String username);

    User findByID(String id);

    UserDto findUserDtoByUsername(String username);

    User findUserByEmail(String email);

    UserDto addNewUser(String firstName, String lastName, String username, String email, String password) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;

    UserDto updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String profileImageUrl, Long idGrado, String role, String isActive, String isNonLocked) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;

    void deleteUser(String username) throws IOException;

    void resetPassword(String email) throws MessagingException, EmailNotFoundException;

    User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;

    List<GradoPopulation> buscarUsuariosInscritosPorGrado();

    ByteArrayInputStream exportarBarchartDeNumUsuByGrado(BufferedImage bufferedImage);

    Grado getGradoByUser(Long idUser);

    boolean validateIdUser(String idUser) throws UtilityException;
}
