package upn.proj.sowad.services.impl;

import upn.proj.sowad.dto.UserDto;
import upn.proj.sowad.entities.Grado;
import upn.proj.sowad.entities.GradoPopulation;
import upn.proj.sowad.entities.User;
import upn.proj.sowad.entities.UserPrincipal;
import upn.proj.sowad.enumeration.Role;
import upn.proj.sowad.exception.domain.*;
import upn.proj.sowad.dao.UserRepository;
import upn.proj.sowad.services.EmailService;
import upn.proj.sowad.services.GradoService;
import upn.proj.sowad.services.LoginAttemptService;
import upn.proj.sowad.services.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static upn.proj.sowad.constant.FileConstant.*;
import static upn.proj.sowad.constant.FileConstant.NOT_AN_IMAGE_FILE;
import static upn.proj.sowad.constant.UserImplConstant.*;
import static upn.proj.sowad.enumeration.Role.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.MediaType.*;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private LoginAttemptService loginAttemptService;
    private EmailService emailService;

    @Resource(name = "gradoServiceV1")
    private GradoService gradoService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, LoginAttemptService loginAttemptService, EmailService emailService, GradoService gradoService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
        this.emailService = emailService;
        this.gradoService = gradoService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            LOGGER.error(NO_USER_FOUND_BY_USERNAME + username);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        } else {
            validateLoginAttempt(user);
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.info(FOUND_USER_BY_USERNAME + username);
            return userPrincipal;
        }
    }

    private UserDto usertToUserDto(User user){
        UserDto resp = new UserDto();
        resp.setId(user.getId());
        resp.setUsername(user.getUsername());
        resp.setActive(user.isActive());
        resp.setAuthorities(user.getAuthorities());
        resp.setEmail(user.getEmail());
        resp.setFirstName(user.getFirstName());
        resp.setLastName(user.getLastName());
        resp.setUserId(user.getUserId());
        resp.setJoinDate(user.getJoinDate());
        resp.setRole(user.getRole());
        resp.setPassword(user.getPassword());
        resp.setNotLocked(user.isNotLocked());
        resp.setLastLoginDate(user.getLastLoginDate());
        resp.setProfileImageUrl(user.getProfileImageUrl());
        resp.setLastLoginDateDisplay(user.getLastLoginDateDisplay());
        if (user.getGrado()!=null)
            resp.setIdGrado(user.getGrado().getIdGrado());
        return resp;
    }

    @Override
    public UserDto register(String firstName, String lastName, String username, String email, String password, Long idGrado) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException {
        if(LOGGER.isInfoEnabled())
            LOGGER.info("Entering 'register' method");
        validateNewUsernameAndEmail(EMPTY, username, email);
        User user = new User();
        user.setUserId(generateUserId());
       // String password = generatePassword();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setJoinDate(new Date());
        user.setPassword(encodePassword(password));
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImageUrl(username));
        if(idGrado!=null) user.setGrado(this.gradoService.getGradyById(idGrado));
        userRepository.save(user);

       //emailSerice.sendNewPasswordEmail(firstName, password, email);v
        return this.usertToUserDto(user);
    }

    @Override
    public UserDto addNewUser(String firstName, String lastName, String username, String email, String password) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        if(LOGGER.isInfoEnabled())
            LOGGER.info("Entering 'addNewUser' method");
        validateNewUsernameAndEmail(EMPTY, username, email);
        User user = new User();
        user.setUserId(generateUserId());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setJoinDate(new Date());
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodePassword(password));
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImageUrl(username));
        userRepository.save(user);
        return this.usertToUserDto(user);
    }

    @Override
    public UserDto updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String profileImageUrl, Long idGrado, String role, String isActive, String isNonLocked) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        User currentUser = validateNewUsernameAndEmail(currentUsername, newUsername, newEmail);
        currentUser.setFirstName(newFirstName);
        currentUser.setLastName(newLastName);
        currentUser.setUsername(newUsername);
        currentUser.setEmail(newEmail);
        if(profileImageUrl!=null && !profileImageUrl.isEmpty())
            currentUser.setProfileImageUrl(profileImageUrl);
        if(idGrado!=null)
            currentUser.setGrado(this.gradoService.getGradyById(idGrado));
        if(role!=null){
            currentUser.setRole(getRoleEnumName(role).name());
            currentUser.setAuthorities(getRoleEnumName(role).getAuthorities());
        }
        if(isActive!=null)
            currentUser.setActive(Boolean.parseBoolean(isActive));
        if(isNonLocked!=null)
            currentUser.setNotLocked(Boolean.parseBoolean(isNonLocked));

        userRepository.save(currentUser);
        return this.usertToUserDto(currentUser);
    }

    @Override
    public void resetPassword(String email) throws MessagingException, EmailNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
        }
        String password = generatePassword();
        user.setPassword(encodePassword(password));
        userRepository.save(user);
        LOGGER.info("New user password: " + password);
        emailService.sendNewPasswordEmail(user.getFirstName(), password, user.getEmail());
    }

    @Override
    public User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        User user = validateNewUsernameAndEmail(username, null, null);
        saveProfileImage(user, profileImage);
        return user;
    }

    @Override
    public List<GradoPopulation> buscarUsuariosInscritosPorGrado() {
        List<Grado> gradoList = this.gradoService.getAllGrados();
        Iterator<Grado> iter = gradoList.iterator();
        List<GradoPopulation> respuesta = new ArrayList<>();
        while(iter.hasNext()){
            Grado gradoActual = iter.next();
            Integer totalAlumnos = this.userRepository.countAllByGrado(gradoActual);
            respuesta.add(new GradoPopulation(gradoActual.getValorNombreGrado(), totalAlumnos));
        }
        return respuesta;
    }

    @Override
    public ByteArrayInputStream exportarBarchartDeNumUsuByGrado(BufferedImage bufferedImage) {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // add text to pdf file
            com.itextpdf.text.Font font = com.itextpdf.text.FontFactory.getFont(FontFactory.COURIER, 14,
                    BaseColor.BLACK);
            Paragraph paragraph = new Paragraph("Bar Chart", font);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            document.add(Chunk.NEWLINE);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            Image img = Image.getInstance(baos.toByteArray());
            img.setAlignment(Element.ALIGN_CENTER);
            document.add(img);
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public Grado getGradoByUser(Long idUser) {
        Optional<User> user = this.userRepository.findById(idUser);
        if(user.isPresent()){
            Grado grado = user.get().getGrado();
            return grado;
        }
        return null;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public UserDto findUserDtoByUsername(String username) {
        User userFound = userRepository.findUserByUsername(username);
        return this.usertToUserDto(userFound);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public void deleteUser(String username) throws IOException {
        User user = userRepository.findUserByUsername(username);
        Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
        FileUtils.deleteDirectory(new File(userFolder.toString()));
        userRepository.deleteById(user.getId());
    }

    private void saveProfileImage(User user, MultipartFile profileImage) throws IOException, NotAnImageFileException {
        if (profileImage != null) {
            if(!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(profileImage.getContentType())) {
                throw new NotAnImageFileException(profileImage.getOriginalFilename() + NOT_AN_IMAGE_FILE);
            }
            Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
            if(!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                LOGGER.info(DIRECTORY_CREATED + userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + user.getUsername() + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getUsername() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            user.setProfileImageUrl(setProfileImageUrl(user.getUsername()));
            userRepository.save(user);
            LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        }
    }

    private String setProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(USER_IMAGE_PATH + username + FORWARD_SLASH
        + username + DOT + JPG_EXTENSION).toUriString();
    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }

    private String getTemporaryProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH + username).toUriString();
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private void validateLoginAttempt(User user) {
        if(user.isNotLocked()) {
            if(loginAttemptService.hasExceededMaxAttempts(user.getUsername())) {
                user.setNotLocked(false);
            } else {
                user.setNotLocked(true);
            }
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }

    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User userByNewUsername = findUserByUsername(newUsername);
        User userByNewEmail = findUserByEmail(newEmail);
        if(StringUtils.isNotBlank(currentUsername)) {
            User currentUser = findUserByUsername(currentUsername);
            if(currentUser == null) {
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
            }
            if(userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if(userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        } else {
            if(userByNewUsername != null) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if(userByNewEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return null;
        }
    }

}
