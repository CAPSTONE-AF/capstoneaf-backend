package upn.proj.sowad.services.impl;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.MediaType.IMAGE_GIF_VALUE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static upn.proj.sowad.constant.CursoImplConstant.CURSO_HAS_NO_TITLE;
import static upn.proj.sowad.constant.CursoImplConstant.NO_CURSO_FOUND_BY_NOMBRE;
import static upn.proj.sowad.constant.FileConstant.DEFAULT_TEMA_IMAGE_PATH;
import static upn.proj.sowad.constant.FileConstant.DIRECTORY_CREATED;
import static upn.proj.sowad.constant.FileConstant.DOT;
import static upn.proj.sowad.constant.FileConstant.FILE_SAVED_IN_FILE_SYSTEM;
import static upn.proj.sowad.constant.FileConstant.FORWARD_SLASH;
import static upn.proj.sowad.constant.FileConstant.JPG_EXTENSION;
import static upn.proj.sowad.constant.FileConstant.NOT_AN_IMAGE_FILE;
import static upn.proj.sowad.constant.FileConstant.TEMA_FOLDER;
import static upn.proj.sowad.constant.FileConstant.TEMA_IMAGE_PATH;
import static upn.proj.sowad.constant.TemaImplConstant.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import upn.proj.sowad.constant.QuizConstant;
import upn.proj.sowad.dao.CursoRepository;
import upn.proj.sowad.dao.TemaRepository;
import upn.proj.sowad.dao.UserRepository;
import upn.proj.sowad.dto.QuizDto;
import upn.proj.sowad.entities.Curso;
import upn.proj.sowad.entities.Tema;
import upn.proj.sowad.entities.User;
import upn.proj.sowad.exception.domain.*;
import upn.proj.sowad.services.TemaService;

@Service
@Transactional
public class TemaServiceImpl implements TemaService {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private TemaRepository temaRepository;
    private CursoRepository cursoRepository;
    private UserRepository userRepository;


    @Autowired
    public TemaServiceImpl(TemaRepository temaRepository, CursoRepository cursoRepository, UserRepository userRepository) {
        super();
        this.temaRepository = temaRepository;
        this.cursoRepository = cursoRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Tema> getTemas(String nombreCurso) throws CursoNotFoundException, CursoExistsException {
        List<Tema> temasRequeridos = null;
        Curso curso = cursoRepository.findCursoByNombre(nombreCurso);
        if (curso != null) {
            temasRequeridos = temaRepository.findAllByCurso(curso);
        } else {
            throw new CursoNotFoundException(NO_CURSO_FOUND_BY_NOMBRE + nombreCurso);
        }
        return temasRequeridos;
    }

    @Override
    public Tema findTemaByTitulo(String nombreCurso, String titulo) throws CursoNotFoundException, CursoExistsException, TemaNotFoundException, UtilityException {

        Curso curso = cursoRepository.findCursoByNombre(nombreCurso);
        Tema tema = temaRepository.findTemaByTitulo(titulo);
        if (curso != null) {
            if (tema != null) {
                if (tema.getCurso().equals(curso)) return tema;
            }
        } else {
            throw new CursoNotFoundException(NO_CURSO_FOUND_BY_NOMBRE + nombreCurso);
        }
        return null;

    }

    private String getTemporaryPortadaImageUrl(String nombreCurso, String titulo) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_TEMA_IMAGE_PATH + nombreCurso + FORWARD_SLASH + titulo).toUriString();
    }


    private void savePortadaImage(Tema tema, MultipartFile portadaImage) throws IOException, NotAnImageFileException {
        if (portadaImage != null) {
            if (!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(portadaImage.getContentType())) {
                throw new NotAnImageFileException(portadaImage.getOriginalFilename() + NOT_AN_IMAGE_FILE);
            }
            Path temaFolder = Paths.get(TEMA_FOLDER + FORWARD_SLASH + tema.getCurso().getNombre() + FORWARD_SLASH + tema.getTitulo()).toAbsolutePath().normalize();
            if (!Files.exists(temaFolder)) {
                Files.createDirectories(temaFolder);
                LOGGER.info(DIRECTORY_CREATED + temaFolder);
            }
            Files.deleteIfExists(Paths.get(temaFolder + tema.getTitulo() + DOT + JPG_EXTENSION));
            Files.copy(portadaImage.getInputStream(), temaFolder.resolve(tema.getTitulo() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            tema.setPortadaUrl(setPortadaImageUrl(tema.getCurso().getNombre(), tema.getTitulo()));
            temaRepository.save(tema);
            LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM + portadaImage.getOriginalFilename());
        }
    }

    private String setPortadaImageUrl(String nombreCurso, String titulo) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(TEMA_IMAGE_PATH + nombreCurso + FORWARD_SLASH + titulo + FORWARD_SLASH
                + titulo + DOT + JPG_EXTENSION).toUriString();
    }

    @Override
    public Tema addNewTema(String nombreCurso, String titulo, String portadaImage, String idUser) throws IOException, NotAnImageFileException, CursoNotFoundException, CursoExistsException, TemaNotFoundException, TemaExistsException, UtilityException {
        if (titulo != null && !titulo.isEmpty()) {
            validateNewTitulo(nombreCurso, EMPTY, titulo);
            Curso curso = cursoRepository.findCursoByNombre(nombreCurso);
            Tema tema = null;
            if (curso != null) {
                tema = new Tema();
                tema.setTitulo(titulo);
                tema.setCurso(curso);
                tema.setPortadaUrl("https://i.vimeocdn.com/portrait/1274237_300x300.jpg");
                tema.setQuizzes(new ArrayList<>());
                Optional<User> user = this.userRepository.findById(Long.parseLong(idUser));
                if (user.isPresent()) {
                    tema.setUsu_crear_tema(user.get().getUsername());
                    tema.setFec_tema_crear(new Date());
                }

                if (portadaImage != null && !portadaImage.isEmpty())
                    tema.setPortadaUrl(portadaImage);

                temaRepository.save(tema);
//	        savePortadaImage(tema, portadaImage);
                curso.setCantTemas(true);

            } else {
                throw new CursoNotFoundException(NO_CURSO_FOUND_BY_NOMBRE + nombreCurso);
            }
            return tema;
        } else
            throw new UtilityException(TEMA_HAS_NO_TITLE);
    }

    @Override
    public Tema updateTema(String nombreCurso, String currentTitulo, String newTitulo, String portadaImage, String idUser) throws CursoNotFoundException, CursoExistsException, TemaNotFoundException, TemaExistsException, IOException, NotAnImageFileException, UtilityException {
        if (currentTitulo != null && !currentTitulo.isEmpty()) {
            Tema currentTema = validateNewTitulo(nombreCurso, currentTitulo, newTitulo);
            Curso curso = cursoRepository.findCursoByNombre(nombreCurso);
            if (curso != null) {
                currentTema.setTitulo(newTitulo);
                currentTema.setCurso(curso);
                Optional<User> user = this.userRepository.findById(Long.parseLong(idUser));
                if (user.isPresent()) {
                    currentTema.setUsu_tema_modi(user.get().getUsername());
                    currentTema.setFec_tema_modi(new Date());
                }
                if (portadaImage != null && !portadaImage.isEmpty())
                    currentTema.setPortadaUrl(portadaImage);
                temaRepository.save(currentTema);
//	        savePortadaImage(currentTema, portadaImage);
            } else {
                throw new CursoNotFoundException(NO_CURSO_FOUND_BY_NOMBRE + nombreCurso);
            }
            return currentTema;
        } else
            throw new UtilityException(TEMA_HAS_NO_TITLE);
    }

    @Override
    public void deleteTema(String nombreCurso, String titulo) throws IOException, CursoNotFoundException, CursoExistsException, TemaNotFoundException, UtilityException {
        if (titulo != null && !titulo.isEmpty()) {
            Curso curso = cursoRepository.findCursoByNombre(nombreCurso);
            Tema tema = findTemaByTitulo(nombreCurso, titulo);
            Path temaFolder = Paths.get(TEMA_FOLDER + FORWARD_SLASH + tema.getCurso().getNombre() + FORWARD_SLASH + tema.getTitulo()).toAbsolutePath().normalize();
            FileUtils.deleteDirectory(new File(temaFolder.toString()));
            temaRepository.deleteById(tema.getIdTema());
            curso.setCantTemas(false);
        } else
            throw new UtilityException(TEMA_HAS_NO_TITLE);
    }

    public Tema validateNewTitulo(String nombreCurso, String currentTitulo, String newTitulo) throws CursoNotFoundException, CursoExistsException, TemaNotFoundException, TemaExistsException, UtilityException {
        Tema temaByNewTitulo = findTemaByTitulo(nombreCurso, newTitulo);
        if (StringUtils.isNotBlank(currentTitulo)) {
            Tema currentTema = findTemaByTitulo(nombreCurso, currentTitulo);
            if (currentTema == null) {
                throw new TemaNotFoundException(NO_TEMA_FOUND_BY_TITULO + currentTitulo);
            }
            if (temaByNewTitulo != null && !currentTema.getIdTema().equals(temaByNewTitulo.getIdTema())) {
                throw new TemaExistsException(TITULO_ALREADY_EXISTS);
            }
            return currentTema;
        } else {
            if (temaByNewTitulo != null) {
                throw new TemaExistsException(TITULO_ALREADY_EXISTS);
            }
            return null;
        }
    }

    @Override
    public Tema updatePortadaImage(String nombreCurso, String titulo, MultipartFile portadaImage) throws CursoNotFoundException, CursoExistsException, TemaNotFoundException, TemaExistsException, IOException, NotAnImageFileException, UtilityException {
        Tema tema = validateNewTitulo(nombreCurso, titulo, null);
        savePortadaImage(tema, portadaImage);
        return tema;
    }

    @Override
    public Tema getTemaById(String idTema) {
        return this.temaRepository.findByIdTema(Long.parseLong(idTema));
    }

    @Override
    public boolean validateIDTema(String idTema) throws UtilityException {
        Long idTemaTmp = null;
        if (idTema == null)
            throw new UtilityException(TEMA_NOT_ENTERED);
        if (idTema != null && idTema.isEmpty())
            throw new UtilityException(TEMA_NOT_ENTERED);
        if (idTema != null && !idTema.isEmpty()) {
            try {
                idTemaTmp = Long.parseLong(idTema);
            } catch (NumberFormatException exception) {
                throw new UtilityException(ID_TEMA_IS_NOT_A_NUMBER);
            }
        }
        if (idTemaTmp != null && this.temaRepository.findByIdTema(idTemaTmp) == null)
            throw new UtilityException(TEMA_NOT_FOUND);
        return true;
    }


}
