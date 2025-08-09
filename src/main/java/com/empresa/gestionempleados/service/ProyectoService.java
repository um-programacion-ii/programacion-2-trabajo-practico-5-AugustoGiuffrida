package com.empresa.gestionempleados.service;

import com.empresa.gestionempleados.entity.Proyecto;

import java.util.List;

public interface ProyectoService {
    Proyecto save(Proyecto proyecto);
    Proyecto update(Long id, Proyecto proyecto);
    void delete (Long id);
    Proyecto findById(Long id);
    List<Proyecto> findAll();
    List<Proyecto> findByStatus(String status);
}
