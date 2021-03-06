package upn.proj.sowad.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import upn.proj.sowad.dto.UserDto;
import upn.proj.sowad.entities.Grado;
import upn.proj.sowad.entities.HttpResponse;
import upn.proj.sowad.entities.User;
import upn.proj.sowad.entities.UserPrincipal;
import upn.proj.sowad.exception.ExceptionHandling;
import upn.proj.sowad.exception.domain.*;
import upn.proj.sowad.services.UserService;
import upn.proj.sowad.services.UtilityService;
import upn.proj.sowad.utility.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.mail.MessagingException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import static upn.proj.sowad.constant.FileConstant.*;
import static upn.proj.sowad.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping(path = { "/", "/user" })
public class UserResource extends ExceptionHandling {

    private Logger log = LoggerFactory.getLogger(getClass());

    public static final String EMAIL_SENT = "An email with a new password was sent to: ";
    public static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";
    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JWTTokenProvider jwtTokenProvider;

    @Resource(name = "utilityServiceV1")
    private UtilityService utilityService;

    @Autowired
    public UserResource(AuthenticationManager authenticationManager, UserService userService, JWTTokenProvider jwtTokenProvider, UtilityService utilityService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.utilityService = utilityService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody UserDto userDto) {
        if(log.isInfoEnabled())
            log.info("Entering 'login' method");
        authenticate(userDto.getUsername(), userDto.getPassword());
        User loginUser = userService.findUserByUsername(userDto.getUsername());
        UserDto loginUserDto = userService.findUserDtoByUsername(userDto.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUserDto, jwtHeader, OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto)
            throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException {
        if(log.isInfoEnabled())
            log.info("Entering 'register' method");
        UserDto newUser = userService.register(userDto.getFirstName(), userDto.getLastName(), userDto.getUsername(),
                userDto.getEmail(),userDto.getPassword(), userDto.getIdGrado());
        return new ResponseEntity<>(newUser, OK);
    }

    @PostMapping("/add")
    public ResponseEntity<UserDto> addNewUser(@RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName, @RequestParam("username") String username,
            @RequestParam("email") String email, @RequestParam("password") String password)
            throws UserNotFoundException, UsernameExistException, EmailExistException, IOException,
            NotAnImageFileException {
        if(log.isInfoEnabled())
            log.info("Entering 'addNewUser' method");
        UserDto newUser = userService.addNewUser(firstName, lastName, username, email, password);
        return new ResponseEntity<>(newUser, OK);
    }

    @PostMapping("/update")
    public ResponseEntity<UserDto> update(@RequestParam("currentUsername") String currentUsername,
            @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
            @RequestParam("username") String username, @RequestParam("email") String email,
            @RequestParam(value = "profileImageUrl", required = false) String profileImageUrl,
            @RequestParam(value = "idGrado", required = false) Long idGrado,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "isActive", required = false) String isActive,
            @RequestParam(value = "isNonLocked", required = false) String isNonLocked)
            throws UserNotFoundException, UsernameExistException, EmailExistException, IOException,
            NotAnImageFileException {
        if(log.isInfoEnabled())
            log.info("Entering 'update' method");
        UserDto updatedUser = userService.updateUser(currentUsername, firstName, lastName, username, email, profileImageUrl, idGrado, role, isActive, isNonLocked);
        return new ResponseEntity<>(updatedUser, OK);
    }

    @GetMapping("/userHasGrado/{idUser}")
    public ResponseEntity<Grado> userHasGrado(@PathVariable("idUser") Long idUser) {
        if(log.isInfoEnabled())
            log.info("Entering 'userHasGrado' method");
        return new ResponseEntity<>(this.userService.getGradoByUser(idUser), OK);
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable("username") String username) {
        if(log.isInfoEnabled())
            log.info("Entering 'getUser' method");
        UserDto user = userService.findUserDtoByUsername(username);
        return new ResponseEntity<>(user, OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUsers() {
        if(log.isInfoEnabled())
            log.info("Entering 'getAllUsers' method");
        List<User> users = userService.getUsers();
        return new ResponseEntity<>(users, OK);
    }

    @GetMapping("/resetpassword/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email)
            throws MessagingException, EmailNotFoundException {
        if(log.isInfoEnabled())
            log.info("Entering 'resetPassword' method");
        userService.resetPassword(email);
        return response(OK, EMAIL_SENT + email);
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("username") String username) throws IOException {
        if(log.isInfoEnabled())
            log.info("Entering 'deleteUser' method");
        userService.deleteUser(username);
        return response(OK, USER_DELETED_SUCCESSFULLY);
    }

    @PostMapping("/updateProfileImage")
    public ResponseEntity<User> updateProfileImage(@RequestParam("username") String username,
            @RequestParam(value = "profileImage") MultipartFile profileImage) throws UserNotFoundException,
            UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        if(log.isInfoEnabled())
            log.info("Entering 'updateProfileImage' method");
        User user = userService.updateProfileImage(username, profileImage);
        return new ResponseEntity<>(user, OK);
    }

    @GetMapping(path = "/image/{username}/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable("username") String username, @PathVariable("fileName") String fileName)
            throws IOException {
        if(log.isInfoEnabled())
            log.info("Entering 'getProfileImage' method");
        return Files.readAllBytes(Paths.get(USER_FOLDER + username + FORWARD_SLASH + fileName));
    }

    @GetMapping(path = "/image/profile/{username}", produces = IMAGE_JPEG_VALUE)
    public byte[] getTempProfileImage(@PathVariable("username") String username) throws IOException {
        if(log.isInfoEnabled())
            log.info("Entering 'getTempProfileImage' method");
        URL url = new URL(TEMP_PROFILE_IMAGE_BASE_URL + username);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = url.openStream()) {
            int bytesRead;
            byte[] chunk = new byte[1024];
            while ((bytesRead = inputStream.read(chunk)) > 0) {
                byteArrayOutputStream.write(chunk, 0, bytesRead);
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        if(log.isInfoEnabled())
            log.info("Entering 'response' method");
        return new ResponseEntity<>(
                new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message),
                httpStatus);
    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        if(log.isInfoEnabled())
            log.info("Entering 'getJwtHeader' method");
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    private void authenticate(String username, String password) {
        if(log.isInfoEnabled())
            log.info("Entering 'authenticate' method");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @GetMapping("/exportar/barchart/num_usu_grado/image")
    public void buildBarChartImage(HttpServletResponse response)
            throws IOException {
        if(log.isInfoEnabled())
            log.info("Entering 'buildBarChartImage' method");
        final String title = "Total de usuarios inscritos seg??n grado de escuela registrado.";
        final DefaultCategoryDataset categoryDataset = buildDatasetUsuariosInscritosPorGrado();
        final String categoryAxisLabel = "Grados";
        final String valueAxisLabel = "Cantidad de usuarios";
        final boolean legend = true;
        final boolean tooltips = true;
        final boolean urls = true;

        final JFreeChart barChart = ChartFactory.createBarChart(title, categoryAxisLabel, valueAxisLabel,
                categoryDataset, PlotOrientation.VERTICAL, legend, tooltips, urls);
        final CategoryPlot categoryPlot = (CategoryPlot) barChart.getPlot();
        final CategoryItemRenderer categoryItemRenderer = categoryPlot.getRenderer();
        categoryItemRenderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        categoryItemRenderer.setDefaultItemLabelsVisible(true);

        final ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.TOP_CENTER);
        categoryItemRenderer.setDefaultPositiveItemLabelPosition(position);

        writeChartAsPNGImage(barChart, 700, 450, response);
    }

    @GetMapping("/exportar/barchart/num_usu_grado/pdf")
    public ResponseEntity<InputStreamResource> buildBarChart()
            throws IOException {
        if(log.isInfoEnabled())
            log.info("Entering 'buildBarChart' method");
        final DefaultCategoryDataset categoryDataset = buildDatasetUsuariosInscritosPorGrado();
        String prefijo = this.utilityService.obtenerFechaActualConFormatoParaArchivos();
        final String title = "Total de usuarios inscritos seg??n grado de escuela registrado.";
        final String categoryAxisLabel = "Grados";
        final String valueAxisLabel = "Cantidad de usuarios";
        final boolean legend = true;
        final boolean tooltips = true;
        final boolean urls = true;

        final JFreeChart barChart = ChartFactory.createBarChart(title, categoryAxisLabel, valueAxisLabel,
                categoryDataset, PlotOrientation.VERTICAL, legend, tooltips, urls);
        final CategoryPlot categoryPlot = (CategoryPlot) barChart.getPlot();
        final CategoryItemRenderer categoryItemRenderer = categoryPlot.getRenderer();
        categoryItemRenderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        categoryItemRenderer.setDefaultItemLabelsVisible(true);

        final ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.TOP_CENTER);
        categoryItemRenderer.setDefaultPositiveItemLabelPosition(position);

        final BufferedImage bufferedImage = barChart.createBufferedImage(700, 450);
        ByteArrayInputStream bais = this.userService.exportarBarchartDeNumUsuByGrado(bufferedImage);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename = " + prefijo + "_barchart.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bais));
    }

    private DefaultCategoryDataset buildDatasetUsuariosInscritosPorGrado() {
        if(log.isInfoEnabled())
            log.info("Entering 'buildDatasetUsuariosInscritosPorGrado' method");
        final Comparable<String> rowKey = "Total de usuarios";
        final DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
        this.userService.buscarUsuariosInscritosPorGrado().forEach((grado) -> categoryDataset
                .setValue(grado.getCantidadUsuarios(), rowKey, grado.getValorNombreGrado()));

        return categoryDataset;
    }

    private void writeChartAsPNGImage(final JFreeChart chart, final int width, final int height,
                                      HttpServletResponse response) throws IOException {
        if(log.isInfoEnabled())
            log.info("Entering 'writeChartAsPNGImage' method");
        final BufferedImage bufferedImage = chart.createBufferedImage(width, height);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        ChartUtils.writeBufferedImageAsPNG(response.getOutputStream(), bufferedImage);
    }



}
