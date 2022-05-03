package upn.proj.sowad.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import upn.proj.sowad.entities.Tema;
import upn.proj.sowad.entities.User;
import upn.proj.sowad.exception.domain.CursoExistsException;
import upn.proj.sowad.exception.domain.CursoNotFoundException;
import upn.proj.sowad.exception.domain.EmailExistException;
import upn.proj.sowad.exception.domain.NotAnImageFileException;
import upn.proj.sowad.exception.domain.TemaExistsException;
import upn.proj.sowad.exception.domain.TemaNotFoundException;
import upn.proj.sowad.exception.domain.UserNotFoundException;
import upn.proj.sowad.exception.domain.UsernameExistException;

public interface TemaService {

    List<Tema> getTemas(String nombreCurso) throws CursoNotFoundException, CursoExistsException;

    Tema findTemaByTitulo(String nombreCurso, String titulo) throws CursoNotFoundException, CursoExistsException, TemaNotFoundException;
    
    Tema addNewTema(String nombreCurso, String titulo, MultipartFile portadaUrl) throws IOException, NotAnImageFileException, CursoNotFoundException, CursoExistsException, TemaNotFoundException, TemaExistsException;

    Tema updateTema(String nombreCurso, String currentTitulo, String newTitulo, MultipartFile portadaImage) throws CursoNotFoundException, CursoExistsException, TemaNotFoundException, TemaExistsException, IOException, NotAnImageFileException;

    void deleteTema(String nombreCurso, String titulo) throws IOException, CursoNotFoundException, CursoExistsException, TemaNotFoundException;

    Tema updatePortadaImage(String nombreCurso, String titulo, MultipartFile portadaImage) throws CursoNotFoundException, CursoExistsException, TemaNotFoundException, TemaExistsException, IOException, NotAnImageFileException;

    Tema getTemaById(String idTema);
}
