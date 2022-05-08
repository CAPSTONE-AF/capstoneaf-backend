package upn.proj.sowad.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upn.proj.sowad.dao.GradoRepository;
import upn.proj.sowad.entities.Grado;
import upn.proj.sowad.services.GradoService;
import upn.proj.sowad.services.UtilityService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("gradoServiceV1")
public class GradoServiceImpl implements GradoService {

    private Logger log = LoggerFactory.getLogger(getClass());
    private GradoRepository gradoRepository;

    @Autowired
    public GradoServiceImpl(GradoRepository gradoRepository) {
        this.gradoRepository = gradoRepository;
    }

    @Override
    public Grado getGradyById(Long idGrado) {
        if(log.isInfoEnabled())
            log.info("Entering 'getGradyById' method");
        return this.gradoRepository.findByIdGrado(idGrado);
    }

    @Override
    public void saveGrado(Grado grado) {
        if(log.isInfoEnabled())
            log.info("Entering 'saveGrado' method");
        grado.setFechaCreacion(new Date());
        this.gradoRepository.save(grado);
    }

    @Override
    public List<Grado> getAllGrados() {
        if(log.isInfoEnabled())
            log.info("Entering 'getAllGrados' method");
        return this.gradoRepository.findAll();
    }

}
