package upn.proj.sowad.services.impl;

import ch.qos.logback.core.net.SyslogOutputStream;
import net.minidev.json.writer.BeansMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import upn.proj.sowad.dao.AvanceRepository;
import upn.proj.sowad.dao.TemaRepository;
import upn.proj.sowad.dao.UserRepository;
import upn.proj.sowad.dto.AvanceDto;
import upn.proj.sowad.entities.Avance;
import upn.proj.sowad.entities.Tema;
import upn.proj.sowad.entities.User;
import upn.proj.sowad.services.AvanceService;
import upn.proj.sowad.services.UtilityService;


import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class AvanceServiceImpl implements AvanceService {

    private Logger log = LoggerFactory.getLogger(getClass());
    private AvanceRepository avanceRepository;
    private UserRepository userRepository;
    private TemaRepository temaRepository;

    @Resource(name = "utilityServiceV1")
    private UtilityService utilityService;

    @Autowired
    public AvanceServiceImpl(AvanceRepository avanceRepository, UserRepository userRepository, TemaRepository temaRepository, UtilityService utilityService) {
        this.avanceRepository = avanceRepository;
        this.userRepository = userRepository;
        this.temaRepository = temaRepository;
        this.utilityService = utilityService;
    }

    @Override
    public void registerNewAvance(AvanceDto avanceDto) {
        if(log.isInfoEnabled())
            log.info("Entering 'registerNewAvance' method");
        Avance avance = new Avance();
        avance.setFechaCreacion(new Date());


        Optional<User> foundUser = this.userRepository.findById(avanceDto.getIdUser());

        if(foundUser.isPresent()) {
            Tema foundtema = this.temaRepository.findByIdTema(avanceDto.getIdTema());
            User user = foundUser.get();
            foundtema.getCurso().addVisita();
            avance.setUser(user);
            avance.setTema(foundtema);
            this.avanceRepository.save(avance);
        }

    }

    @Override
    public List<AvanceDto> getAvancesByUserId(String idUser) {
        if(log.isInfoEnabled())
            log.info("Entering 'getAvancesByUserId' method");
        List<AvanceDto> respuesta = new ArrayList<>();
        List<Avance> avanceListByUser = this.avanceRepository.findAllByUserIdOrderByFechaCreacionDesc(Long.parseLong(idUser));
        Iterator<Avance> iterator = avanceListByUser.iterator();
        while(iterator.hasNext()){
            AvanceDto currentAvanceDto = new AvanceDto();
            Avance currentAvance = (Avance)iterator.next();
            currentAvanceDto.setIdAvance(currentAvance.getIdAvance());
            currentAvanceDto.setIdUser(currentAvance.getUser().getId());
            currentAvanceDto.setIdTema(currentAvance.getTema().getIdTema());
            currentAvanceDto.setFechaCreacion(currentAvance.getFechaCreacion());
            respuesta.add(currentAvanceDto);
        }
        return respuesta;
    }
}
