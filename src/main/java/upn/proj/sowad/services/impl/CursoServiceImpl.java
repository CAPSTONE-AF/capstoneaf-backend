package upn.proj.sowad.services.impl;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static upn.proj.sowad.constant.CursoImplConstant.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upn.proj.sowad.constant.QuizConstant;
import upn.proj.sowad.dao.CursoRepository;
import upn.proj.sowad.dao.UserRepository;
import upn.proj.sowad.entities.Curso;
import upn.proj.sowad.entities.User;
import upn.proj.sowad.exception.domain.CursoExistsException;
import upn.proj.sowad.exception.domain.CursoNotFoundException;
import upn.proj.sowad.exception.domain.UtilityException;
import upn.proj.sowad.services.CursoService;

import java.util.stream.Stream;

import javax.imageio.ImageIO;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
@Transactional
public class CursoServiceImpl implements CursoService {

    private CursoRepository cursoRepository;
    private UserRepository userRepository;


    @Autowired
    public CursoServiceImpl(CursoRepository cursoRepository, UserRepository userRepository) {
        this.cursoRepository = cursoRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Curso> getCursos() {
        return cursoRepository.findAll();
    }

    @Override
    public Curso findCursoByNombre(String nombre) throws UtilityException {
        return cursoRepository.findCursoByNombre(nombre);
    }

    @Override
    public Curso addNewCurso(String nombre, String idUser) throws CursoNotFoundException, CursoExistsException, UtilityException {
        if (nombre != null && !nombre.isEmpty()) {
            validateNewNombre(EMPTY, nombre);
            Curso curso = new Curso();
            curso.setNombre(nombre);
            curso.setNumeroVisitas(0);
            Optional<User> user = this.userRepository.findById(Long.parseLong(idUser));
            if (user.isPresent()) {
                curso.setUsu_crear_curso(user.get().getUsername());
                curso.setFec_curso_crear(new Date());
            }
            cursoRepository.save(curso);
            return curso;
        } else
            throw new UtilityException(CURSO_HAS_NO_TITLE);
    }

    @Override
    public void deleteCurso(String nombre) throws UtilityException {
        if (nombre != null && !nombre.isEmpty()) {
            Curso curso = this.findCursoByNombre(nombre);
            cursoRepository.deleteById(curso.getIdCurso());
        } else
            throw new UtilityException(CURSO_HAS_NO_TITLE);
    }

    @Override
    public Curso updateCurso(String currentNombre, String newNombre, String idUser) throws CursoNotFoundException, CursoExistsException, UtilityException {
        if (currentNombre != null && !currentNombre.isEmpty()) {
            Curso currentCurso = validateNewNombre(currentNombre, newNombre);
            currentCurso.setNombre(newNombre);
            Optional<User> user = this.userRepository.findById(Long.parseLong(idUser));
            if (user.isPresent()) {
                currentCurso.setUsu_curso_modi(user.get().getUsername());
                currentCurso.setFec_curso_modi(new Date());
            }
            cursoRepository.save(currentCurso);
            return currentCurso;
        } else
            throw new UtilityException(CURSO_HAS_NO_TITLE);

    }

    public Curso validateNewNombre(String currentNombre, String newNombre) throws CursoNotFoundException, CursoExistsException, UtilityException {
        Curso cursoByNewNombre = findCursoByNombre(newNombre);
        if (StringUtils.isNotBlank(currentNombre)) {
            Curso currentCurso = findCursoByNombre(currentNombre);
            if (currentCurso == null) {
                throw new CursoNotFoundException(NO_CURSO_FOUND_BY_NOMBRE + currentNombre);
            }
            if (cursoByNewNombre != null && !currentCurso.getIdCurso().equals(cursoByNewNombre.getIdCurso())) {
                throw new CursoExistsException(NOMBRE_ALREADY_EXISTS);
            }
            return currentCurso;
        } else {
            if (cursoByNewNombre != null) {
                throw new CursoExistsException(NOMBRE_ALREADY_EXISTS);
            }
            return null;
        }
    }

    @Override
    public Curso register(String nombre) throws CursoNotFoundException, CursoExistsException, UtilityException {
        validateNewNombre(EMPTY, nombre);
        Curso curso = new Curso();
        curso.setNombre(nombre);
        cursoRepository.save(curso);
        return curso;

    }

    @Override
    public ByteArrayInputStream exportarPiechartPopCursos(BufferedImage bufferedImage) {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // add text to pdf file
            com.itextpdf.text.Font font = com.itextpdf.text.FontFactory.getFont(FontFactory.COURIER, 14,
                    BaseColor.BLACK);
            Paragraph paragraph = new Paragraph("Pie Chart", font);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            document.add(Chunk.NEWLINE);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            Image img = Image.getInstance(baos.toByteArray());
            img.setAlignment(Element.ALIGN_CENTER);
            document.add(img);
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }


}
