package upn.proj.sowad.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upn.proj.sowad.dto.AvanceDto;
import upn.proj.sowad.entities.Avance;
import upn.proj.sowad.entities.Curso;
import upn.proj.sowad.exception.domain.CursoExistsException;
import upn.proj.sowad.exception.domain.CursoNotFoundException;
import upn.proj.sowad.services.AvanceService;
import upn.proj.sowad.services.RecursoService;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/avances")
public class AvanceController {

    private AvanceService avanceService;
    private Logger log = LoggerFactory.getLogger(AvanceController.class.getName());

    @Autowired
    public AvanceController(AvanceService avanceService) {
        this.avanceService = avanceService;
    }

    @PostMapping("/register")
    public void registerAvance(@RequestBody AvanceDto avanceDto) {
        this.avanceService.registerNewAvance(avanceDto);
    }

    @GetMapping("/list/{idUser}")
    public ResponseEntity<List<AvanceDto>> getAllAvanceByUserId(@PathVariable("idUser") String idUser) {
        if(log.isInfoEnabled())
            log.info("Entering 'getAllAvanceByUserId' method");
        List<AvanceDto> avancesDto = avanceService.getAvancesByUserId(idUser);
        return new ResponseEntity<>(avancesDto, OK);
    }

}
