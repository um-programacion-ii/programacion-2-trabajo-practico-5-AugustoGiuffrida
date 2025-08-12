package com.empresa.gestionempleados.controller;

import com.empresa.gestionempleados.entity.Proyecto;
import com.empresa.gestionempleados.exceptions.ProyectoNoEncontradoException;
import com.empresa.gestionempleados.service.ProyectoServiceImpl;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProyectoController.class)
public class TestProyectoController {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ProyectoServiceImpl proyectoService;

    @Test
    void GETProyecto_returnsListAndStatus200() throws Exception{
        Proyecto proyecto1 = new Proyecto();
        Proyecto proyecto2 = new Proyecto();
        proyecto1.setNombre("Proyecto A");
        proyecto2.setNombre("Proyecto B");

        when(proyectoService.findAll()).thenReturn(List.of(proyecto1,proyecto2));

        mockMvc.perform(get("/api/proyectos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Proyecto A"))
                .andExpect(jsonPath("$[1].nombre").value("Proyecto B"));
    }

    @Test
    void GETProyecto_returnsProyectoAndStatus200() throws Exception{
        Proyecto proyecto = new Proyecto();
        Long id = 1L;
        proyecto.setId(id);
        proyecto.setNombre("Proyecto A");

        when(proyectoService.findById(id)).thenReturn(proyecto);
        mockMvc.perform(get("/api/proyectos/"+id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Proyecto A"));
    }

    @Test
    void GETNonExistentProyectoById_ReturnsStatus404() throws Exception{
        Proyecto proyecto = new Proyecto();
        Long id = 1L;
        proyecto.setId(id);

        when(proyectoService.findById(id)).thenThrow(new ProyectoNoEncontradoException(id));

        mockMvc.perform(get("/api/proyectos/"+id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void GETProyectoByStatus_returnsProyecto() throws Exception{
        Proyecto proyecto1 = new Proyecto();
        Proyecto proyecto2 = new Proyecto();
        String estado = "ACTIVO";
        proyecto1.setEstado(estado);
        proyecto1.setId(1L);
        proyecto2.setEstado(estado);
        proyecto2.setId(2L);

        when(proyectoService.findByStatus(estado)).thenReturn(List.of(proyecto1,proyecto2));
        mockMvc.perform(get("/api/proyectos/estado/"+estado)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].estado").value("ACTIVO"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].estado").value("ACTIVO"));
    }

    @Test
    void POSTProyecto_CreatesNewProjyectoAndReturns201() throws Exception {
        Proyecto proyecto = new Proyecto();
        Long id = 1L;
        proyecto.setId(id);
        proyecto.setNombre("Proyecto A");

        when(proyectoService.save(any(Proyecto.class))).thenReturn(proyecto);

        mockMvc.perform(post("/api/proyectos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proyecto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Proyecto A"));
    }

    @Test
    void PUTProyecto_ReturnsProjectoAndStatus200() throws Exception {
        Proyecto proyecto = new Proyecto();
        Long id = 1L;
        proyecto.setId(id);
        proyecto.setNombre("Proyecto A");

        when(proyectoService.update(eq(id),any())).thenReturn(proyecto);

        mockMvc.perform(put("/api/proyectos/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proyecto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Proyecto A"));
    }


}
