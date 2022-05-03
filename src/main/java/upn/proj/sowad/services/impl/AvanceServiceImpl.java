package upn.proj.sowad.services.impl;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upn.proj.sowad.dao.AvanceRepository;
import upn.proj.sowad.dao.TemaRepository;
import upn.proj.sowad.dao.UserRepository;
import upn.proj.sowad.dto.AvanceDto;
import upn.proj.sowad.entities.Avance;
import upn.proj.sowad.entities.Tema;
import upn.proj.sowad.entities.User;
import upn.proj.sowad.services.AvanceService;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AvanceServiceImpl implements AvanceService {

    private AvanceRepository avanceRepository;
    private UserRepository userRepository;
    private TemaRepository temaRepository;

    @Autowired
    public AvanceServiceImpl(AvanceRepository avanceRepository, UserRepository userRepository, TemaRepository temaRepository) {
        this.avanceRepository = avanceRepository;
        this.userRepository = userRepository;
        this.temaRepository = temaRepository;
    }

    @Override
    public void registerNewAvance(AvanceDto avanceDto) {
        Avance avance = new Avance();
        avance.setFechaCreacion(new Date());


        Optional<User> foundUser = this.userRepository.findById(Long.parseLong(avanceDto.getIdUser()));

        if(foundUser.isPresent()) {
            Tema foundtema = this.temaRepository.findByIdTema(Long.parseLong(avanceDto.getIdTema()));
            User user = foundUser.get();
            if(this.avanceRepository.findByUserAndTema(user,foundtema)==null){
                avance.setUser(user);
                avance.setTema(foundtema);
                this.avanceRepository.save(avance);
            }
        }
    }

    @Override
    public List<Avance> getAvancesByUserId(String idUser) {
        return this.avanceRepository.findAllByUserId(Long.parseLong(idUser));
    }
}
