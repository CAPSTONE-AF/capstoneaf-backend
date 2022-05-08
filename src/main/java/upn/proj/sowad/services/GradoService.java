package upn.proj.sowad.services;

import upn.proj.sowad.entities.Grado;

import java.util.List;

public interface GradoService {

    Grado getGradyById(Long idGrado);

    void saveGrado(Grado grado);

    List<Grado> getAllGrados();

}
