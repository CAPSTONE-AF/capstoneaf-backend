package upn.proj.sowad.api.controllers;

import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import upn.proj.sowad.entities.Curso;
import upn.proj.sowad.entities.HttpResponse;
import upn.proj.sowad.entities.User;
import upn.proj.sowad.exception.domain.CursoExistsException;
import upn.proj.sowad.exception.domain.CursoNotFoundException;
import upn.proj.sowad.exception.domain.EmailExistException;
import upn.proj.sowad.exception.domain.UserNotFoundException;
import upn.proj.sowad.exception.domain.UsernameExistException;
import upn.proj.sowad.services.CursoService;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/curso")
public class CursoResource {
	
	public static final String CURSO_DELETED_SUCCESSFULLY = "Curso eliminado exitosamente";
	private CursoService cursoService;
	
	@Autowired
	public CursoResource(CursoService cursoService) {
		this.cursoService = cursoService;
	}

	@GetMapping("/list")
    public ResponseEntity<List<Curso>> getAllCursos() {
        List<Curso> cursos = cursoService.getCursos();
        return new ResponseEntity<>(cursos, OK);
    }
	
	@GetMapping("/find/{nombre}")
    public ResponseEntity<Curso> getUser(@PathVariable("nombre") String nombre) {
        Curso curso = cursoService.findCursoByNombre(nombre);
        return new ResponseEntity<>(curso, OK);
    }
	
	@PostMapping("/addCurso")
	    public ResponseEntity<Curso> register(@RequestBody Curso curso) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException, CursoNotFoundException, CursoExistsException {
	        Curso newCurso = cursoService.register(curso.getNombre());
	        return new ResponseEntity<>(newCurso, OK);
	    }
	
	@PostMapping("/add")
    public ResponseEntity<Curso> addNewCurso(@RequestParam("nombre") String nombre) throws CursoNotFoundException, CursoExistsException {
        Curso newCurso = cursoService.addNewCurso(nombre);
        return new ResponseEntity<>(newCurso, OK);
    }
	
	@PostMapping("/update")
    public ResponseEntity<Curso> update(@RequestParam("currentNombre") String currentNombre,
                                       @RequestParam("nombre") String nombre) throws CursoNotFoundException, CursoExistsException {
        Curso updatedCurso = cursoService.updateCurso(currentNombre, nombre);
        return new ResponseEntity<>(updatedCurso, OK);
    }
	
	
	@DeleteMapping("/delete/{nombre}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteCurso(@PathVariable("nombre") String nombre) {
        cursoService.deleteCurso(nombre);
        return response(OK, CURSO_DELETED_SUCCESSFULLY);
    }
	
	private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }
	
}
