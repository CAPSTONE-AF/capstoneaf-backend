package upn.proj.sowad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvanceDto {
    Long idAvance;
    Date fechaCreacion;
    Long idUser;
    Long idTema;
}
