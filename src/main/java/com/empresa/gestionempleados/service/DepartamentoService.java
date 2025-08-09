package com.empresa.gestionempleados.service;

import com.empresa.gestionempleados.entity.Departamento;
import java.util.List;

public interface DepartamentoService {
    Departamento save(Departamento departamento);
    Departamento update(Long id, Departamento departamento);
    void delete(Long id);
    Departamento findById(Long id);
    List<Departamento> findAll();
}
