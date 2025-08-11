package com.empresa.gestionempleados.controller;

import com.empresa.gestionempleados.entity.Departamento;
import com.empresa.gestionempleados.entity.Empleado;
import com.empresa.gestionempleados.exceptions.EmpleadoNoEncontradoException;
import com.empresa.gestionempleados.service.EmpleadoServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmpleadoController.class)
public class TestEmpleadoController {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    EmpleadoServiceImpl empleadoService;

    @Test
    void GETEmpleado_returnsListAndStatus200() throws Exception {
        Empleado empleado1 = new Empleado();
        Empleado empleado2 = new Empleado();
        empleado1.setNombre("Juan");
        empleado2.setNombre("Jose");

        when(empleadoService.findAll()).thenReturn(List.of(empleado1,empleado2));

        mockMvc.perform(get("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[1].nombre").value("Jose"));
    }

    @Test
    void GETEmpleado_returnsEmpleadoAndStatus200() throws Exception{
        Empleado empleado = new Empleado();
        Long id = 1L;
        empleado.setNombre("Juan");
        empleado.setId(id);

        when(empleadoService.findById(id)).thenReturn(empleado);
        mockMvc.perform(get("/api/empleados/"+id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void GETNonExistentEmpleadoById_ReturnsStatus404() throws Exception {
        Empleado empleado = new Empleado();
        Long id = 1L;
        empleado.setId(id);
        when(empleadoService.findById(id)).thenThrow(new EmpleadoNoEncontradoException(id));

        mockMvc.perform(get("/api/empleados/"+id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void POSTEmpleado_CreatesNewEmpleadoAndReturns201() throws Exception {
        Empleado empleado = new Empleado();
        Long id = 1L;
        empleado.setId(id);
        empleado.setNombre("Juan");

        when(empleadoService.save(any(Empleado.class))).thenReturn(empleado);

        mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleado)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void PUTEmpleado_ReturnsEmpleadoAndStatus200() throws Exception {
        Empleado empleado = new Empleado();
        Long id = 1L;
        empleado.setId(id);
        empleado.setNombre("Juan");

        when(empleadoService.update(eq(id),any())).thenReturn(empleado);

        mockMvc.perform(put("/api/empleados/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void PUTNonExistentEmpleado_ReturnsStatus404() throws Exception {
        Empleado empleado = new Empleado();
        Long id = 1L;
        empleado.setId(id);

        when(empleadoService.update(id,empleado)).thenThrow(new EmpleadoNoEncontradoException(id));

        mockMvc.perform(put("/api/empleados/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleado)))
                .andExpect(status().isNotFound());
    }

    @Test
    void DELETEEmpleado_ReturnsStatus204() throws Exception{
        Empleado empleado = new Empleado();
        Long id = 1L;
        empleado.setId(id);
        mockMvc.perform(delete("/api/empleados/"+id))
            .andExpect(status().isNoContent());
    }

    @Test
    void DELETEEmpleado_NotFound_ReturnsStatus404() throws Exception {
        Long id = 1L;

        doThrow(new EmpleadoNoEncontradoException(id)).when(empleadoService).delete(id);

        mockMvc.perform(delete("/api/empleados/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void GETEmpleadoByDepartamentoNombre_returnsListWithDepartmentName() throws Exception {
        Empleado empleado1 = new Empleado();
        Empleado empleado2 = new Empleado();
        Departamento departamento = new Departamento();
        String nombre = "RRHH";
        departamento.setNombre("nombre");
        empleado1.setDepartamento(departamento);
        empleado2.setDepartamento(departamento);

        when(empleadoService.findByNombreDepartamento(nombre)).thenReturn(List.of(empleado1,empleado2));

        mockMvc.perform(get("/api/empleados/departamento/"+nombre)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].departamento.nombre").value("nombre"))
                .andExpect(jsonPath("$[1].departamento.nombre").value("nombre"));
    }

    @Test
    void GETEmpleadoByAverageSalaryFromDepartments_returnsAverageSalary() throws Exception {
        Long id = 1L;
        BigDecimal promedio = new BigDecimal("1000");

        when(empleadoService.findAverageSalaryByDepartamento(id)).thenReturn(promedio);

        mockMvc.perform(get("/api/empleados/promedio-salario/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1000"));
    }

    @Test
    void GETEmpleadosBySalaryRange_returnsListOfEmpleados() throws Exception {
        BigDecimal value0 = new BigDecimal("1500");
        Empleado empleado = new Empleado();

        empleado.setSalario(value0);

        BigDecimal min = new BigDecimal("1000");
        BigDecimal max = new BigDecimal("2000");

        when(empleadoService.findBySalaryRange(min,max)).thenReturn(List.of(empleado));
        mockMvc.perform(get("/api/empleados/rango-salario")
                .param("min", "1000")
                .param("max", "2000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].salario").value(1500));
    }

    @Test
    void GETEmpleadosByHiringDate_returnsListOfEmpleados() throws Exception {
        LocalDate inicio = LocalDate.of(2020, 12, 2);
        LocalDate fin = LocalDate.of(2021, 12, 2);
        LocalDate contratacion = LocalDate.of(2020, 12, 5);
        Empleado empleado = new Empleado();
        empleado.setFechaContratacion(contratacion);

        when(empleadoService.findByHiringDate(inicio, fin)).thenReturn(List.of(empleado));
        mockMvc.perform(get("/api/empleados/fecha-contratacion")
                .param("inicio", "2020-12-02")
                .param("fin", "2021-12-02")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fechaContratacion").value("2020-12-05"));
    }
}

