package com.empresa.gestionempleados.service;

import com.empresa.gestionempleados.entity.Proyecto;
import com.empresa.gestionempleados.exceptions.ProyectoNoEncontradoException;
import com.empresa.gestionempleados.repository.ProyectoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestProyectoService {

    @Mock
    private ProyectoRepository proyectoRepository;

    @InjectMocks
    private  ProyectoServiceImpl proyectoService;

    @Test
    void saveProyecto(){
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto Test");

        when(proyectoRepository.save(proyecto)).thenReturn(proyecto);

        Proyecto resultado = proyectoService.save(proyecto);
        assertNotNull(resultado);
        assertEquals("Proyecto Test", resultado.getNombre());
        verify(proyectoRepository).save(proyecto);
    }

    @Test
    void updateExistentProyecto(){
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto A");

        when(proyectoRepository.existsById(1L)).thenReturn(true);
        when(proyectoRepository.save(proyecto)).thenReturn(proyecto);

        Proyecto resultado = proyectoService.update(1L, proyecto);

        assertEquals(1L, resultado.getId());
        assertEquals("Proyecto A", resultado.getNombre());
        verify(proyectoRepository).save(proyecto);
    }

    @Test
    void updateNoneExistentProyecto(){
        Proyecto proyecto = new Proyecto();

        when(proyectoRepository.existsById(1L)).thenReturn(false);
        assertThrows(ProyectoNoEncontradoException.class,()-> proyectoService.update(1L,proyecto));
        verify(proyectoRepository, never()).save(any());
    }

    @Test
    void deleteExistentProyecto(){
        when(proyectoRepository.existsById(1L)).thenReturn(true);
        proyectoService.delete(1L);
        verify(proyectoRepository).deleteById(1L);
    }

    @Test
    void deleteNonExistentProyecto(){
        when(proyectoRepository.existsById(1L)).thenReturn(false);
        assertThrows(ProyectoNoEncontradoException.class,()-> proyectoService.delete(1L));
        verify(proyectoRepository, never()).deleteById(any());
    }

    @Test
    void findProyectoById(){
        Proyecto proyecto = new Proyecto();
        proyecto.setId(1L);

        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));

        Proyecto resultado = proyectoService.findById(1L);
        assertEquals(proyecto, resultado);
        verify(proyectoRepository).findById(1L);
    }

    @Test
    void findNonExistentProyectoById(){
        when(proyectoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProyectoNoEncontradoException.class,()->proyectoService.findById(1L));
        verify(proyectoRepository).findById(1L);
    }

    @Test
    void findAll(){
        Proyecto proyecto = new Proyecto();

        when(proyectoRepository.findAll()).thenReturn(List.of(proyecto));

        List<Proyecto> resultado = proyectoService.findAll();
        assertEquals(1, resultado.size());
        verify(proyectoRepository).findAll();
    }

    @Test
    void findByStatus(){
        Proyecto proyecto = new Proyecto();
        proyecto.setEstado("ACTIVO");

        when(proyectoRepository.findByEstado("ACTIVO")).thenReturn(List.of(proyecto));
        List<Proyecto> resultado = proyectoRepository.findByEstado("ACTIVO");
        assertEquals(1, resultado.size());
        verify(proyectoRepository).findByEstado("ACTIVO");
    }
}
