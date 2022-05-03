package upn.proj.sowad.api.controllers;

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

    @Autowired
    public AvanceController(AvanceService avanceService) {
        this.avanceService = avanceService;
    }

    @PostMapping("/register")
    public void registerAvance(@RequestBody AvanceDto avanceDto) {
        this.avanceService.registerNewAvance(avanceDto);
    }

    @GetMapping("/list/{idUser}")
    public ResponseEntity<List<Avance>> getAllAvanceByUserId(@PathVariable("idUser") String idUser) {
        List<Avance> avances = avanceService.getAvancesByUserId(idUser);
        return new ResponseEntity<>(avances, OK);
    }

}
