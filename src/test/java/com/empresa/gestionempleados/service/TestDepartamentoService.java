package com.empresa.gestionempleados.service;

import com.empresa.gestionempleados.entity.Departamento;
import com.empresa.gestionempleados.exceptions.DepartamentoNoEncontradoException;
import com.empresa.gestionempleados.repository.DepartamentoRepository;
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
public class TestDepartamentoService {

    @Mock
    private DepartamentoRepository departamentoRepository;

    @InjectMocks
    private DepartamentoServiceImpl departamentoService;

    @Test
    void saveDepartamento(){
        Departamento departamento = new Departamento();
        departamento.setNombre("Departamento Test");

        when(departamentoRepository.save(departamento)).thenReturn(departamento);

        Departamento resultado = departamentoService.save(departamento);
        assertNotNull(resultado);
        assertEquals("Departamento Test", resultado.getNombre());
    }

    @Test
    void updateExistentDepartamento(){
        Departamento departamento = new Departamento();
        departamento.setNombre("Departamento Test");

        when(departamentoRepository.existsById(1L)).thenReturn(true);
        when(departamentoRepository.save(departamento)).thenReturn(departamento);

        Departamento resultado = departamentoService.update(1L, departamento);

        assertNotNull(resultado);
        assertEquals("Departamento Test", resultado.getNombre());
        verify(departamentoRepository).save(departamento);
    }

    @Test
    void updateNonExistentDepartamento(){
        Departamento departamento = new Departamento();
        when(departamentoRepository.existsById(1L)).thenReturn(false);
        assertThrows(DepartamentoNoEncontradoException.class,()->departamentoService.update(1L,departamento));
        verify(departamentoRepository, never()).save(any());
    }

    @Test
    void deleteExistentDepartamento(){
        when(departamentoRepository.existsById(1L)).thenReturn(true);
        departamentoService.delete(1L);
        verify(departamentoRepository).deleteById(1L);
    }

    @Test
    void deleteNonExitentDepartamento(){
       when(departamentoRepository.existsById(1L)).thenReturn(false);
       assertThrows(DepartamentoNoEncontradoException.class,()->departamentoService.delete(1L));
       verify(departamentoRepository, never()).deleteById(any());
    }

    @Test
    void findDepartamentoById(){
       Departamento departamento = new Departamento();

       when(departamentoRepository.findById(1L)).thenReturn(Optional.of(departamento));
       Departamento resultado = departamentoService.findById(1L);

       assertEquals(departamento,resultado);
       verify(departamentoRepository).findById(1L);
    }

    @Test
    void findNonExistentDepartamentoById(){
        when(departamentoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DepartamentoNoEncontradoException.class,()->departamentoService.findById(1L));
        verify(departamentoRepository).findById(1L);
    }

    @Test
    void findAll(){
        Departamento departamento = new Departamento();

        when(departamentoRepository.findAll()).thenReturn(List.of(departamento));
        List<Departamento> resultado = departamentoService.findAll();

        assertEquals(1, resultado.size());
        verify(departamentoRepository).findAll();
    }
}
