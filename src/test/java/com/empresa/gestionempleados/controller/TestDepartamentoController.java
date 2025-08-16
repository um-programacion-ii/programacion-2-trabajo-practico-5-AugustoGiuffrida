package com.empresa.gestionempleados.controller;

import com.empresa.gestionempleados.entity.Departamento;
import com.empresa.gestionempleados.exceptions.DepartamentoNoEncontradoException;
import com.empresa.gestionempleados.service.DepartamentoServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartamentoController.class)
public class TestDepartamentoController {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    DepartamentoServiceImpl departamentoService;

    @Test
    void GETDepartamento_returnsListAndStatus200() throws Exception {
        Departamento departamento1 = new Departamento();
        Departamento departamento2 = new Departamento();

        departamento1.setNombre("Departamento A");
        departamento2.setNombre("Departamento B");

        when(departamentoService.findAll()).thenReturn(List.of(departamento1,departamento2));

        mockMvc.perform(get("/api/departamentos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Departamento A"))
                .andExpect(jsonPath("$[1].nombre").value("Departamento B"));
    }

    @Test
    void GETDepartamento_returnsDepartamentoAndStatus200() throws Exception{
        Departamento departamento = new Departamento();
        Long id = 1L;
        departamento.setId(id);
        departamento.setNombre("Departamento A");

        when(departamentoService.findById(id)).thenReturn(departamento);

        mockMvc.perform(get("/api/departamentos/"+id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Departamento A"));
    }


    @Test
    void GETNonExistentDepartamentoById_ReturnsStatus404() throws Exception {
        Departamento departamento = new Departamento();
        Long id = 1L;
        departamento.setId(id);

        when(departamentoService.findById(id)).thenThrow(new DepartamentoNoEncontradoException(id));

        mockMvc.perform(get("/api/departamentos/"+id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void POSTDepartamento_CreatesNewDepartamentoAndReturns201() throws Exception {
        Departamento departamento = new Departamento();
        Long id = 1L;
        departamento.setId(id);
        departamento.setNombre("Departamento A");

        when(departamentoService.save(any(Departamento.class))).thenReturn(departamento);

        mockMvc.perform(post("/api/departamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departamento)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Departamento A"));
    }

    @Test
    void PUTProyecto_ReturnsProjectoAndStatus200() throws Exception{
        Departamento departamento = new Departamento();
        Long id = 1L;
        departamento.setId(id);
        departamento.setNombre("Departamento A");

        when(departamentoService.update(eq(id),any())).thenReturn(departamento);

        mockMvc.perform(put("/api/departamentos/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departamento)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Departamento A"));
    }

    @Test
    void PUTNonExistentDepartamento_ReturnsStatus404() throws Exception{
        Departamento departamento = new Departamento();
        Long id = 1L;
        departamento.setId(id);

        when(departamentoService.update(id, departamento)).thenThrow(new DepartamentoNoEncontradoException(id));
        mockMvc.perform(put("/api/departamentos/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departamento)))
                .andExpect(status().isNotFound());
    }

    @Test
    void DELETEDepartamento_ReturnsStatus204() throws Exception {
        Departamento departamento = new Departamento();
        Long id = 1L;
        departamento.setId(id);

        mockMvc.perform(delete("/api/departamentos/"+id))
                .andExpect(status().isNoContent());
    }

    @Test
    void DELETEDepartamento_NotFound_ReturnsStatus404() throws Exception {
        Long id = 1L;

        doThrow(new DepartamentoNoEncontradoException(id)).when(departamentoService).delete(id);

        mockMvc.perform(delete("/api/departamentos/" + id))
                .andExpect(status().isNotFound());
    }

}
