package upn.proj.sowad.services;

import upn.proj.sowad.dto.AvanceDto;
import upn.proj.sowad.entities.Avance;

import java.util.List;

public interface AvanceService {

    void registerNewAvance(AvanceDto avanceDto);

    List<AvanceDto> getAvancesByUserId(String idUser);
}
