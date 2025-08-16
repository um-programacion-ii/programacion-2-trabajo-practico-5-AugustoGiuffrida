package com.empresa.gestionempleados.repository;

import com.empresa.gestionempleados.entity.Departamento;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("postgres")
public class TestDepartamentoRepository {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    private Departamento departamento1;
    private Departamento departamento2;

    @BeforeEach
    void setUp() {
        departamento1 = new Departamento();
        departamento1.setNombre("Recursos Humanos");
        departamento1.setDescripcion("Departamento de RRHH");

        departamento2 = new Departamento();
        departamento2.setNombre("Tecnología");
        departamento2.setDescripcion("Departamento de IT");
    }

    @Test
    void cuandoGuardarDepartamento_entoncesSePersisteCorrectamente() {
        Departamento guardado = departamentoRepository.save(departamento1);

        assertNotNull(guardado.getId());
        assertEquals("Recursos Humanos", guardado.getNombre());
        assertTrue(departamentoRepository.existsById(guardado.getId()));
    }

    @Test
    void cuandoBuscarTodosDepartamentos_entoncesRetornaCorrectos() {
        departamentoRepository.save(departamento1);
        departamentoRepository.save(departamento2);

        List<Departamento> todos = departamentoRepository.findAll();

        assertEquals(2, todos.size());
    }

    @Test
    void cuandoBuscarPorId_entoncesRetornaCorrecto() {
        Departamento guardado = departamentoRepository.save(departamento1);

        Optional<Departamento> encontrado = departamentoRepository.findById(guardado.getId());

        assertTrue(encontrado.isPresent());
        assertEquals("Recursos Humanos", encontrado.get().getNombre());
    }

    @Test
    void cuandoActualizarDepartamento_entoncesCambiaValores() {
        Departamento guardado = departamentoRepository.save(departamento1);
        guardado.setNombre("RRHH Actualizado");

        Departamento actualizado = departamentoRepository.save(guardado);

        assertEquals("RRHH Actualizado", actualizado.getNombre());
    }

    @Test
    void cuandoEliminarDepartamento_entoncesNoExisteMas() {
        Departamento guardado = departamentoRepository.save(departamento1);
        Long id = guardado.getId();

        departamentoRepository.deleteById(id);

        assertFalse(departamentoRepository.findById(id).isPresent());
    }
}
