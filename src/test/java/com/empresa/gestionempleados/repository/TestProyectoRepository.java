package com.empresa.gestionempleados.repository;

import com.empresa.gestionempleados.entity.Proyecto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("mysql")
public class TestProyectoRepository {

    @Autowired
    private ProyectoRepository proyectoRepository;

    private Proyecto proyecto1;
    private Proyecto proyecto2;

    @BeforeEach
    void setUp() {
        proyecto1 = new Proyecto();
        proyecto1.setNombre("Proyecto A");
        proyecto1.setDescripcion("Primer proyecto");
        proyecto1.setEstado("Activo");
        proyecto1.setFechaInicio(LocalDate.of(2025, 1, 1));
        proyecto1.setFechaFin(LocalDate.of(2025, 12, 31));

        proyecto2 = new Proyecto();
        proyecto2.setNombre("Proyecto B");
        proyecto2.setDescripcion("Segundo proyecto");
        proyecto2.setEstado("Finalizado");
        proyecto2.setFechaInicio(LocalDate.of(2024, 1, 1));
        proyecto2.setFechaFin(LocalDate.of(2024, 12, 31));
    }


    @Test
    void cuandoGuardarProyecto_entoncesSePersisteCorrectamente() {
        Proyecto guardado = proyectoRepository.save(proyecto1);

        assertNotNull(guardado.getId());
        assertEquals("Proyecto A", guardado.getNombre());
        assertTrue(proyectoRepository.existsById(guardado.getId()));
    }

    @Test
    void cuandoBuscarPorEstado_entoncesRetornaCorrectos() {
        proyectoRepository.save(proyecto1);
        proyectoRepository.save(proyecto2);

        List<Proyecto> activos = proyectoRepository.findByEstado("Activo");
        List<Proyecto> finalizados = proyectoRepository.findByEstado("Finalizado");

        assertEquals(1, activos.size());
        assertEquals("Proyecto A", activos.get(0).getNombre());

        assertEquals(1, finalizados.size());
        assertEquals("Proyecto B", finalizados.get(0).getNombre());
    }

    @Test
    void cuandoBuscarEstadoPorId_entoncesRetornaCorrecto() {
        Proyecto guardado = proyectoRepository.save(proyecto1);

        String estado = proyectoRepository.findEstadoById(guardado.getId());

        assertEquals("Activo", estado);
    }

    @Test
    void cuandoBuscarProyectosVariados_entoncesRetornaTodosCorrectos() {
        proyectoRepository.save(proyecto1);
        proyectoRepository.save(proyecto2);

        List<Proyecto> todos = proyectoRepository.findAll();
        assertEquals(2, todos.size());
    }
}
