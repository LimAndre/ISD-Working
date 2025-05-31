package es.udc.ws.app.test.model.appservice;

import es.udc.ws.app.model.curso.Curso;
import es.udc.ws.app.model.cursoservice.CursoService;
import es.udc.ws.app.model.cursoservice.CursoServiceFactory;
import es.udc.ws.app.model.cursoservice.exceptions.CourseClosedException;
import es.udc.ws.app.model.cursoservice.exceptions.CourseFullException;
import es.udc.ws.app.model.inscripcion.Inscripcion;
import es.udc.ws.app.model.inscripcion.SqlInscripcionDao;
import es.udc.ws.app.model.inscripcion.SqlInscripcionDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

import static es.udc.ws.app.model.util.ModelConstants.APP_DATA_SOURCE;
import static org.junit.jupiter.api.Assertions.*;

public class AppServiceTest {
    private static final long NON_EXISTENT_CURSO_ID = -1L;
    private static final long NON_EXISTENT_INSCRIPCION_ID = -1L;

    private static final String VALID_EMAIL = "user@example.com";
    private static final String INVALID_EMAIL = "";
    private static final String VALID_CARD = "1234567890123456";
    private static final String INVALID_CARD = "123";

    private static CursoService cursoService;
    private static SqlInscripcionDao inscDao;

    @BeforeAll
    public static void init() {
        DataSource ds = new SimpleDataSource();
        DataSourceLocator.addDataSource(APP_DATA_SOURCE, ds);
        cursoService = CursoServiceFactory.getService();
        inscDao = SqlInscripcionDaoFactory.getDao();
    }

    private Curso getValidCurso(String ciudad) {
        LocalDateTime inicio = LocalDateTime.now().plusDays(20).withNano(0);
        return new Curso(ciudad, "Nombre Curso", inicio, null, 100f, 10);
    }

    private Curso createCurso(Curso c) {
        try {
            return cursoService.altaCurso(c);
        } catch (InputValidationException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeCurso(Long id) {
        try {
            cursoService.removeCurso(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Inscripcion inscribir(Long cursoId, String email, String card) throws Exception {
        return new Inscripcion(cursoId, email, card, LocalDateTime.now().withNano(0));
    }

    private void removeInscripcion(Long inscId) {
        DataSource ds = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        try (Connection con = ds.getConnection()) {
            inscDao.remove(con, inscId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAltaYFindCurso() throws Exception {
        Curso c = getValidCurso("Madrid");
        Curso creado = null;
        try {
            LocalDateTime before = LocalDateTime.now().withNano(0);
            creado = cursoService.altaCurso(c);
            LocalDateTime after = LocalDateTime.now().withNano(0);

            Curso encontrado = cursoService.findCurso(creado.getCursoId());
            assertEquals(creado, encontrado);
            assertEquals(c.getCiudad(), encontrado.getCiudad());
            assertTrue(!encontrado.getFechaAlta().isBefore(before) && !encontrado.getFechaAlta().isAfter(after));
        } finally {
            if (creado != null) {
                removeCurso(creado.getCursoId());
            }
        }
    }

    @Test
    public void testAltaCursoFechaInvalida() {
        Curso c = getValidCurso("Valencia");
        c.setFechaInicio(LocalDateTime.now().plusDays(5)); // menos de 15 días
        assertThrows(InputValidationException.class, () -> cursoService.altaCurso(c));
    }

    @Test
    public void testFindNonExistentCurso() {
        assertThrows(InstanceNotFoundException.class, () -> cursoService.findCurso(NON_EXISTENT_CURSO_ID));
    }

    @Test
    public void testUpdateCurso() throws Exception {
        Curso c = createCurso(getValidCurso("Sevilla"));
        try {
            c.setCiudad("Granada");
            cursoService.updateCurso(c);
            Curso upd = cursoService.findCurso(c.getCursoId());
            assertEquals("Granada", upd.getCiudad());
        } finally {
            removeCurso(c.getCursoId());
        }
    }

    @Test
    public void testUpdateNonExistentCurso() {
        Curso c = getValidCurso("Bilbao");
        c.setCursoId(NON_EXISTENT_CURSO_ID);
        assertThrows(InstanceNotFoundException.class, () -> cursoService.updateCurso(c));
    }

    @Test
    public void testRemoveCurso() throws Exception {
        Curso c = createCurso(getValidCurso("Zaragoza"));
        cursoService.removeCurso(c.getCursoId());
        assertThrows(InstanceNotFoundException.class, () -> cursoService.findCurso(c.getCursoId()));
    }

    @Test
    public void testRemoveNonExistentCurso() {
        assertThrows(InstanceNotFoundException.class, () -> cursoService.removeCurso(NON_EXISTENT_CURSO_ID));
    }

    @Test
    public void testBuscarCursos() throws Exception {
        Curso c1 = createCurso(getValidCurso("A"));
        Curso c2 = createCurso(getValidCurso("A"));
        Curso c3 = createCurso(getValidCurso("B"));
        try {
            List<Curso> list = cursoService.buscarCursosByFechaYCiuddad("A", LocalDateTime.now());
            assertEquals(2, list.size());
            for (Curso c : list) {
                assertEquals("A", c.getCiudad());
                assertTrue(c.getPlazasDisponibles() <= c.getPlazasMaximas());
            }
        } finally {
            removeCurso(c1.getCursoId());
            removeCurso(c2.getCursoId());
            removeCurso(c3.getCursoId());
        }
    }

    @Test
    public void testInscribirYFindInscripcion() throws Exception {
        Curso c = createCurso(getValidCurso("Toledo"));
        Long inscId = null;
        try {
            inscId = cursoService.inscribirUsuario(c.getCursoId(), VALID_EMAIL, VALID_CARD);
            Inscripcion insc = cursoService.findInscripcion(inscId);
            assertEquals(VALID_EMAIL, insc.getEmailUsuario());
            assertEquals(VALID_CARD, insc.getTarjetaPago());
            assertEquals(c.getCursoId(), insc.getCursoId());
        } finally {
            if (inscId != null) removeInscripcion(inscId);
            removeCurso(c.getCursoId());
        }
    }

    @Test
    public void testInscribirCursoCerrado() throws Exception {
        Curso c = createCurso(getValidCurso("Cadiz"));
        Long cursoId = c.getCursoId();
        // Actualizar fechaInicio a pasado
        c.setFechaInicio(LocalDateTime.now().minusDays(1));
        cursoService.updateCurso(c);
        try {
            assertThrows(CourseClosedException.class,
                    () -> cursoService.inscribirUsuario(cursoId, VALID_EMAIL, VALID_CARD));
        } finally {
            removeCurso(cursoId);
        }
    }

    @Test
    public void testInscribirSinPlazas() throws Exception {
        Curso c = getValidCurso("Cadiz");
        c.setPlazasMaximas(1);
        c = createCurso(c);
        Long cursoId = c.getCursoId();
        Long ins1 = cursoService.inscribirUsuario(cursoId, VALID_EMAIL, VALID_CARD);
        try {
            assertThrows(CourseFullException.class,
                    () -> cursoService.inscribirUsuario(cursoId, "otro@user.com", VALID_CARD));
        } finally {
            removeInscripcion(ins1);
            removeCurso(cursoId);
        }
    }

    @Test
    public void testInscribirDatosInvalidos() throws Exception {
        Curso c = createCurso(getValidCurso("Pamplona"));
        Long cursoId = c.getCursoId();
        try {
            assertThrows(InputValidationException.class,
                    () -> cursoService.inscribirUsuario(cursoId, INVALID_EMAIL, VALID_CARD));
            assertThrows(InputValidationException.class,
                    () -> cursoService.inscribirUsuario(cursoId, VALID_EMAIL, INVALID_CARD));
        } finally {
            removeCurso(cursoId);
        }
    }

    @Test
    public void testFindNonExistentInscripcion() {
        assertThrows(InstanceNotFoundException.class,
                () -> cursoService.findInscripcion(NON_EXISTENT_INSCRIPCION_ID));
    }
}
