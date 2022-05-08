package upn.proj.sowad.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upn.proj.sowad.dao.*;
import upn.proj.sowad.entities.Curso;
import upn.proj.sowad.entities.Grado;
import upn.proj.sowad.entities.Tema;
import upn.proj.sowad.exception.domain.CursoException;
import upn.proj.sowad.services.UtilityService;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service("utilityServiceV1")
public class UtilityServiceImpl implements UtilityService {

    private Logger log = LoggerFactory.getLogger(getClass());

    private GradoRepository gradoRepository;
    private AvanceRepository avanceRepository;
    private CursoRepository cursoRepository;
    private RecursoRepository recursoRepository;
    private UserRepository userRepository;
    private TemaRepository temaRepository;

    @Autowired
    public UtilityServiceImpl(GradoRepository gradoRepository, AvanceRepository avanceRepository, CursoRepository cursoRepository, RecursoRepository recursoRepository, UserRepository userRepository, TemaRepository temaRepository) {
        this.gradoRepository = gradoRepository;
        this.avanceRepository = avanceRepository;
        this.cursoRepository = cursoRepository;
        this.recursoRepository = recursoRepository;
        this.userRepository = userRepository;
        this.temaRepository = temaRepository;
    }

    @Override
    public void calcularPopularidadCursos() throws CursoException {
        if(log.isInfoEnabled())
            log.info("Entering 'calcularPopularidadCursos' method");
        List<Curso> cursoList = this.cursoRepository.findAll();
        Iterator<Curso> iter = cursoList.iterator();
        Double visitasTotales = 0.0;
        while(iter.hasNext()){
            Curso cursoActual = iter.next();
            Integer numeroVisitas = getNumeroVisitasCurso(cursoActual);
            visitasTotales += numeroVisitas;
        }

        if(visitasTotales==0) throw new CursoException("Aun no existen visitas registradas.");

        iter = cursoList.iterator();
        while(iter.hasNext()){
            Curso cursoActual = iter.next();
            Integer numeroVisitas = cursoActual.getNumeroVisitas() != null ? cursoActual.getNumeroVisitas() : 0;
            cursoActual.setPorcentajePopularidad(numeroVisitas / visitasTotales);
            cursoActual.setFec_curso_modi(new Date());
            cursoActual.setUsu_curso_modi("system");
            this.cursoRepository.save(cursoActual);
        }


    }


    private Integer getNumeroVisitasCurso(Curso cursoActual) {
        if(log.isInfoEnabled())
            log.info("Entering 'getNumeroVisitasCurso' method");
       List<Tema> temaList = this.temaRepository.findAllByCurso(cursoActual);
       Iterator<Tema> iter = temaList.iterator();
       Integer numeroVisitasDeCurso = 0;
       while(iter.hasNext()){
           numeroVisitasDeCurso += this.avanceRepository.countAllByTema(iter.next());
       }
       cursoActual.setNumeroVisitas(numeroVisitasDeCurso);
       cursoActual.setUsu_curso_modi("system");
       cursoActual.setFec_curso_modi(new Date());
       this.cursoRepository.save(cursoActual);
       return numeroVisitasDeCurso;
    }

    @Override
    public String obtenerFechaActualConFormatoParaArchivos() {
        Format formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
        return formatter.format(new Date());
    }

}
