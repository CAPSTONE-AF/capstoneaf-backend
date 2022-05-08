package upn.proj.sowad.services;

import upn.proj.sowad.entities.Grado;
import upn.proj.sowad.exception.domain.CursoException;

public interface UtilityService {

    void calcularPopularidadCursos() throws CursoException;

    String obtenerFechaActualConFormatoParaArchivos();

}
