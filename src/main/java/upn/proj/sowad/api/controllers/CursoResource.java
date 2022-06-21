package upn.proj.sowad.api.controllers;

import static org.springframework.http.HttpStatus.OK;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import upn.proj.sowad.entities.Curso;
import upn.proj.sowad.entities.HttpResponse;
import upn.proj.sowad.exception.domain.*;
import upn.proj.sowad.services.CursoService;
import upn.proj.sowad.services.UtilityService;

@RestController
@RequestMapping("/curso")
public class CursoResource {
	
	public static final String CURSO_DELETED_SUCCESSFULLY = "Curso eliminado exitosamente";
	private CursoService cursoService;

    @Resource(name = "utilityServiceV1")
    private UtilityService utilityService;

    @Autowired
    public CursoResource(CursoService cursoService, UtilityService utilityService) {
        this.cursoService = cursoService;
        this.utilityService = utilityService;
    }

	@GetMapping("/list")
    public ResponseEntity<List<Curso>> getAllCursos() {
        List<Curso> cursos = cursoService.getCursos();
        return new ResponseEntity<>(cursos, OK);
    }
	
	@GetMapping("/find/{nombre}")
    public ResponseEntity<Curso> getUser(@PathVariable("nombre") String nombre) throws UtilityException {
        Curso curso = cursoService.findCursoByNombre(nombre);
        return new ResponseEntity<>(curso, OK);
    }
	
	@PostMapping("/addCurso")
	    public ResponseEntity<Curso> register(@RequestBody Curso curso) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException, CursoNotFoundException, CursoExistsException, UtilityException {
	        Curso newCurso = cursoService.register(curso.getNombre());
	        return new ResponseEntity<>(newCurso, OK);
	    }
	
	@PostMapping("/add")
    public ResponseEntity<Curso> addNewCurso(@RequestParam("nombre") String nombre,
                                            @RequestParam("idUser")String idUser)
            throws CursoNotFoundException, CursoExistsException, UtilityException {
        Curso newCurso = cursoService.addNewCurso(nombre,idUser);
        return new ResponseEntity<>(newCurso, OK);
    }
	
	@PostMapping("/update")
    public ResponseEntity<Curso> update(@RequestParam("currentNombre") String currentNombre,
                                       @RequestParam("nombre") String nombre,
                                       @RequestParam("idUser")String idUser)
            throws CursoNotFoundException, CursoExistsException, UtilityException {
        Curso updatedCurso = cursoService.updateCurso(currentNombre, nombre,idUser);
        return new ResponseEntity<>(updatedCurso, OK);
    }
	
	
	@DeleteMapping("/delete/{nombre}")
    public ResponseEntity<HttpResponse> deleteCurso(@PathVariable("nombre") String nombre) throws UtilityException {
        cursoService.deleteCurso(nombre);
        return response(OK, CURSO_DELETED_SUCCESSFULLY);
    }
	
	private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }

    @GetMapping("/exportar/piechart/pop_cursos/image")
    public void buildPieChartImage(HttpServletResponse response)
            throws IOException, CursoException {
        this.utilityService.calcularPopularidadCursos();
        final PieDataset pieDataset = buildPiePopCursosDataset();
        final String title = "Popularidad Cursos por Visitas";
        final boolean legend = true;
        final boolean tooltips = true;

        final PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{0} = {2}");

        final JFreeChart pieChart3D = ChartFactory.createPieChart3D(title, pieDataset, legend, tooltips,
                Locale.getDefault());
        final PiePlot3D piePlot3D = (PiePlot3D) pieChart3D.getPlot();
        piePlot3D.setLabelGenerator(labelGenerator);

        writeChartAsPNGImage(pieChart3D, 750, 450, response);
    }

    @GetMapping("/exportar/piechart/pop_cursos/pdf")
    public ResponseEntity<InputStreamResource> buildPieChart()
            throws IOException {
        String prefijo = this.utilityService.obtenerFechaActualConFormatoParaArchivos();
        final PieDataset pieDataset = buildPiePopCursosDataset();
        final String title = "Popularidad Cursos por Visitas";
        final boolean legend = true;
        final boolean tooltips = true;

        final PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{0} = {2}");

        final JFreeChart pieChart3D = ChartFactory.createPieChart3D(title, pieDataset, legend, tooltips,
                Locale.getDefault());
        final PiePlot3D piePlot3D = (PiePlot3D) pieChart3D.getPlot();
        piePlot3D.setLabelGenerator(labelGenerator);

        final BufferedImage bufferedImage = pieChart3D.createBufferedImage(700, 450);
        ByteArrayInputStream bais = this.cursoService.exportarPiechartPopCursos(bufferedImage);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename = " + prefijo + "_piechart.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bais));
    }

    private PieDataset buildPiePopCursosDataset() {
        final DefaultPieDataset pieDataset = new DefaultPieDataset();
        this.cursoService.getCursos()
                .forEach((curso) -> pieDataset.setValue(curso.getNombre(),
                        curso.getPorcentajePopularidad()));

        return pieDataset;
    }

    private void writeChartAsPNGImage(final JFreeChart chart, final int width, final int height,
                                      HttpServletResponse response) throws IOException {
        final BufferedImage bufferedImage = chart.createBufferedImage(width, height);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        ChartUtils.writeBufferedImageAsPNG(response.getOutputStream(), bufferedImage);
    }
	
}
