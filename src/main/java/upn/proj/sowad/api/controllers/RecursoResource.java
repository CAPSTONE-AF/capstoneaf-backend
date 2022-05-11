package upn.proj.sowad.api.controllers;

import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
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
import upn.proj.sowad.entities.Recurso;
import upn.proj.sowad.entities.Recurso;
import upn.proj.sowad.exception.domain.CursoExistsException;
import upn.proj.sowad.exception.domain.CursoNotFoundException;
import upn.proj.sowad.exception.domain.NotAnImageFileException;
import upn.proj.sowad.exception.domain.RecursoExistsException;
import upn.proj.sowad.exception.domain.RecursoNotFoundException;
import upn.proj.sowad.exception.domain.TemaExistsException;
import upn.proj.sowad.exception.domain.TemaNotFoundException;
import upn.proj.sowad.services.RecursoService;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/recurso")


public class RecursoResource {

	public static final String RECURSO_DELETED_SUCCESSFULLY = "Recurso eliminado exitosamente";
	private RecursoService recursoService;
	
	@Autowired
	public RecursoResource(RecursoService recursoService) {
		this.recursoService = recursoService;
	}

	@GetMapping("/list/{nombreCurso}/{tituloTema}")
    public ResponseEntity<List<Recurso>> getAllRecursos(@PathVariable("nombreCurso") String nombreCurso,
    		@PathVariable("tituloTema") String tituloTema) throws CursoNotFoundException, CursoExistsException, TemaNotFoundException {
        List<Recurso> Recursos = recursoService.getRecursos(nombreCurso,tituloTema);
        return new ResponseEntity<>(Recursos, OK);
    }
	
	
	@PostMapping("/add")
    public ResponseEntity<Recurso> addNewRecurso(@RequestParam("nombreCurso") String nombreCurso,
    		@RequestParam("tituloTema") String tituloTema,
    		@RequestParam("nombre") String nombre,
    		@RequestParam("tipo") String tipo,
    		@RequestParam("contenido") String contenido) throws IOException, NotAnImageFileException, CursoNotFoundException, CursoExistsException, RecursoNotFoundException, RecursoExistsException, TemaNotFoundException, TemaExistsException {
        Recurso newRecurso = recursoService.addNewRecurso(nombreCurso,tituloTema,nombre,tipo,contenido);
        return new ResponseEntity<>(newRecurso, OK);
    }
	
	@PostMapping("/update")
    public ResponseEntity<Recurso> update(@RequestParam("nombreCurso") String nombreCurso,
    		@RequestParam("tituloTema") String tituloTema,
    		@RequestParam("currentNombre") String currentNombre,
    		@RequestParam("nombre") String nombre,
    		@RequestParam("tipo") String tipo,
    		@RequestParam("contenido") String contenido) throws CursoNotFoundException, CursoExistsException, RecursoNotFoundException, RecursoExistsException, IOException, NotAnImageFileException, TemaNotFoundException, TemaExistsException {
        Recurso updatedRecurso = recursoService.updateRecurso(nombreCurso,tituloTema,currentNombre,nombre,tipo,contenido);
        return new ResponseEntity<>(updatedRecurso, OK);
    }
	
	
	@DeleteMapping("/delete/{nombreCurso}/{tituloTema}/{nombre}")
    public ResponseEntity<HttpResponse> deleteCurso(@PathVariable("nombreCurso") String nombreCurso,
    									@PathVariable("tituloTema") String tituloTema,
    									@PathVariable("nombre") String nombre) throws IOException, CursoNotFoundException, CursoExistsException, RecursoNotFoundException, TemaNotFoundException {
        recursoService.deleteRecurso(nombreCurso,tituloTema,nombre);
        return response(OK, RECURSO_DELETED_SUCCESSFULLY);
    }
	
	private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }
	
}
