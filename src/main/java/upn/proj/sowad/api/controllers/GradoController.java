package upn.proj.sowad.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upn.proj.sowad.dto.AvanceDto;
import upn.proj.sowad.entities.Curso;
import upn.proj.sowad.entities.Grado;
import upn.proj.sowad.services.GradoService;

import javax.annotation.Resource;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/grados")
public class GradoController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Resource(name = "gradoServiceV1")
    private GradoService gradoService;

    @PostMapping("/register")
    public void registerGrado(@RequestBody Grado grado) {
        if(log.isInfoEnabled())
            log.info("Entering 'registerGrado' method");
        this.gradoService.saveGrado(grado);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Grado>> getAllGrados() {
        if(log.isInfoEnabled())
            log.info("Entering 'getAllGrados' method");
        List<Grado> grados = this.gradoService.getAllGrados();
        return new ResponseEntity<>(grados, OK);
    }

    @GetMapping("/find/{idGrado}")
    public ResponseEntity<Grado> getGradoById(@PathVariable("idGrado") Long idGrado) {
        if(log.isInfoEnabled())
            log.info("Entering 'getGradoById' method");
        return new ResponseEntity<>(this.gradoService.getGradyById(idGrado), OK);
    }

}
