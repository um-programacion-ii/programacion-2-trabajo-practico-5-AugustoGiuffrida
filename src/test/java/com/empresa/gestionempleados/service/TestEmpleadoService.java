package com.empresa.gestionempleados.service;

import com.empresa.gestionempleados.entity.Departamento;
import com.empresa.gestionempleados.entity.Empleado;
import com.empresa.gestionempleados.exceptions.EmpleadoNoEncontradoException;
import com.empresa.gestionempleados.repository.EmpleadoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestEmpleadoService {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private  EmpleadoServiceImpl empleadoService;


    @Test
    void testSaveEmpleado(){
        Empleado empleado = new Empleado();
        empleado.setEmail("email@.com");

        when(empleadoRepository.findByEmail(empleado.getEmail())).thenReturn(Optional.empty());
        when(empleadoRepository.save(empleado)).thenReturn(empleado);

        Empleado resultado = empleadoService.save(empleado);

        assertNotNull(resultado);
        assertEquals("email@.com", resultado.getEmail());

        verify(empleadoRepository).findByEmail("email@.com");
        verify(empleadoRepository).save(empleado);
    }

    @Test
    void UpdateExistentEmpleado(){
        Empleado empleado = new Empleado();

        when(empleadoRepository.existsById(1L)).thenReturn(true);
        when(empleadoRepository.findByEmail(empleado.getEmail())).thenReturn(Optional.empty());
        when(empleadoRepository.save(empleado)).thenReturn(empleado);

        Empleado resultado = empleadoService.update(1L,empleado);

        assertEquals(1L, resultado.getId());
        verify(empleadoRepository).save(empleado);
    }

    @Test
    void UpdateNonExistentEmpleado(){
        Empleado empleado = new Empleado();
        when(empleadoRepository.existsById(1L)).thenReturn(false);
        assertThrows(EmpleadoNoEncontradoException.class,()->empleadoService.update(1L,empleado));
        verify(empleadoRepository, never()).save(any());
    }

    @Test
    void DeleteExitentEmpleado(){
        when(empleadoRepository.existsById(1L)).thenReturn(true);
        empleadoService.delete(1L);
        verify(empleadoRepository).deleteById(1L);
    }

    @Test
    void DeleteNonExitentEmpleado(){
        when(empleadoRepository.existsById(1L)).thenReturn(false);
        assertThrows(EmpleadoNoEncontradoException.class,()->empleadoService.delete(1L));
        verify(empleadoRepository, never()).deleteById(any());
    }

    @Test
    void findEmpleadoById(){
        Empleado empleado = new Empleado();

        when(empleadoRepository.findById(1L)).thenReturn(Optional.of(empleado));

        Empleado resultado = empleadoService.findById(1L);
        assertEquals(empleado, resultado);
        verify(empleadoRepository).findById(1L);
    }

    @Test
    void findAllEmpleados(){
        Empleado empleado = new Empleado();

        when(empleadoRepository.findAll()).thenReturn(List.of(empleado));

        List<Empleado> resultado = empleadoService.findAll();
        assertEquals(1, resultado.size());
        verify(empleadoRepository).findAll();
    }

    @Test
    void findEmpleadoByNombreDepartamento(){
        Empleado empleado = new Empleado();
        Departamento departamento = new Departamento();
        departamento.setNombre("RRHH");
        empleado.setDepartamento(departamento);

        when(empleadoRepository.findByNombreDepartamento("RRHH")).thenReturn(List.of(empleado));

        List<Empleado> resultado = empleadoService.findByNombreDepartamento("RRHH");
        assertEquals(1, resultado.size());
        verify(empleadoRepository).findByNombreDepartamento("RRHH");
    }

    @Test
    void findAverageSalaryByDepartamento(){
        Empleado empleado1 = new Empleado();
        Empleado empleado2 = new Empleado();
        Departamento departamento = new Departamento();
        Long departamentoId = 1L;
        departamento.setId(departamentoId);
        BigDecimal value = new BigDecimal(1000);
        empleado1.setSalario(value);
        empleado2.setSalario(value);
        empleado1.setDepartamento(departamento);
        empleado2.setDepartamento(departamento);

        when(empleadoRepository.findAverageSalaryByDepartamento(departamentoId)).thenReturn(Optional.of(value));

        BigDecimal resulado = empleadoService.findAverageSalaryByDepartamento(departamento.getId());
        assertEquals(value, resulado);
        verify(empleadoRepository).findAverageSalaryByDepartamento(departamentoId);
    }

    @Test
    void findBySalaryRange(){
        BigDecimal value0 = new BigDecimal("1500");
        Empleado empleado = new Empleado();

        empleado.setSalario(value0);
        BigDecimal min = new BigDecimal("1000");
        BigDecimal max = new BigDecimal("2000");

        when(empleadoRepository.findBySalarioBetween(min,max)).thenReturn(List.of(empleado));

        List<Empleado> resultado = empleadoService.findBySalaryRange(min,max);
        assertEquals(1, resultado.size());
        verify(empleadoRepository).findBySalarioBetween(min,max);
    }

    @Test
    void findByHiringDate(){
        LocalDate inicio = LocalDate.of(2020, 12, 2);
        LocalDate fin = LocalDate.of(2021, 12, 2);
        Empleado empleado = new Empleado();

        when(empleadoRepository.findByFechaContratacionBetween(inicio,fin)).thenReturn(List.of(empleado));

        List<Empleado> resultado = empleadoService.findByHiringDate(inicio,fin);
        assertEquals(1, resultado.size());
        verify(empleadoRepository).findByFechaContratacionBetween(inicio,fin);
    }


}

