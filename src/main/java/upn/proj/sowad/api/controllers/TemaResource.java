package upn.proj.sowad.api.controllers;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static upn.proj.sowad.constant.FileConstant.FORWARD_SLASH;
import static upn.proj.sowad.constant.FileConstant.TEMA_FOLDER;
import static upn.proj.sowad.constant.FileConstant.TEMP_PORTADA_IMAGE_BASE_URL;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import upn.proj.sowad.entities.HttpResponse;
import upn.proj.sowad.entities.Tema;
import upn.proj.sowad.exception.domain.CursoExistsException;
import upn.proj.sowad.exception.domain.CursoNotFoundException;
import upn.proj.sowad.exception.domain.EmailExistException;
import upn.proj.sowad.exception.domain.NotAnImageFileException;
import upn.proj.sowad.exception.domain.TemaExistsException;
import upn.proj.sowad.exception.domain.TemaNotFoundException;
import upn.proj.sowad.exception.domain.UserNotFoundException;
import upn.proj.sowad.exception.domain.UsernameExistException;
import upn.proj.sowad.services.TemaService;

@RestController
@RequestMapping("/tema")

public class TemaResource {

    public static final String TEMA_DELETED_SUCCESSFULLY = "Tema eliminado exitosamente";
    private TemaService temaService;

    @Autowired
    public TemaResource(TemaService temaService) {
        this.temaService = temaService;
    }

    @GetMapping("/list/{nombreCurso}")
    public ResponseEntity<List<Tema>> getAllTemas(@PathVariable("nombreCurso") String nombreCurso)
            throws CursoNotFoundException, CursoExistsException {
        List<Tema> temas = temaService.getTemas(nombreCurso);
        return new ResponseEntity<>(temas, OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Tema> addNewTema(@RequestParam("nombreCurso") String nombreCurso,
            @RequestParam("titulo") String titulo,
            @RequestParam(value = "portadaUrl", required = false)MultipartFile portadaUrl,
            @RequestParam("idUser")String idUser)
            throws IOException, NotAnImageFileException, CursoNotFoundException, CursoExistsException,
            TemaNotFoundException, TemaExistsException {
        Tema newTema = temaService.addNewTema(nombreCurso, titulo, portadaUrl,idUser);
        return new ResponseEntity<>(newTema, OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Tema> update(@RequestParam("nombreCurso") String nombreCurso,
            @RequestParam("currentTitulo") String currentTitulo, @RequestParam("titulo") String titulo,
            @RequestParam(value = "portadaUrl", required = false) MultipartFile portadaUrl,
            @RequestParam("idUser")String idUser)
            throws CursoNotFoundException, CursoExistsException, TemaNotFoundException, TemaExistsException,
            IOException, NotAnImageFileException {
        Tema updatedTema = temaService.updateTema(nombreCurso, currentTitulo, titulo, portadaUrl,idUser);
        return new ResponseEntity<>(updatedTema, OK);
    }

    @DeleteMapping("/delete/{nombreCurso}/{titulo}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteCurso(@PathVariable("nombreCurso") String nombreCurso,
            @PathVariable("titulo") String titulo)
            throws IOException, CursoNotFoundException, CursoExistsException, TemaNotFoundException {
        temaService.deleteTema(nombreCurso, titulo);
        return response(OK, TEMA_DELETED_SUCCESSFULLY);
    }

    @PostMapping("/updatePortadaImage")
    public ResponseEntity<Tema> updatePortadaImage(@RequestParam("nombreCurso") String nombreCurso,
            @RequestParam("titulo") String titulo, @RequestParam(value = "portadaImage") MultipartFile portadaImage)
            throws UserNotFoundException, UsernameExistException, EmailExistException, IOException,
            NotAnImageFileException, CursoNotFoundException, CursoExistsException, TemaNotFoundException,
            TemaExistsException {
        Tema tema = temaService.updatePortadaImage(nombreCurso, titulo, portadaImage);
        return new ResponseEntity<>(tema, OK);
    }

    @GetMapping(path = "/image/{nombreCurso}/{titulo}/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] getPortadaImage(@PathVariable("nombreCurso") String nombreCurso,
            @PathVariable("titulo") String titulo, @PathVariable("fileName") String fileName) throws IOException {
        return Files
                .readAllBytes(Paths.get(TEMA_FOLDER + nombreCurso + FORWARD_SLASH + titulo + FORWARD_SLASH + fileName));
    }

    @GetMapping(path = "/image/portada/{nombreCurso}/{titulo}", produces = IMAGE_JPEG_VALUE)
    public byte[] getTempPortadaImage(@PathVariable("nombreCurso") String nombreCurso,
            @PathVariable("titulo") String titulo) throws IOException {
        URL url = new URL(TEMP_PORTADA_IMAGE_BASE_URL + titulo);
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
        return new ResponseEntity<>(
                new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message),
                httpStatus);
    }

}
