package com.empresa.gestionempleados.service;

import com.empresa.gestionempleados.entity.Empleado;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface EmpleadoService {
    Empleado save(Empleado empleado);
    Empleado update(Long id, Empleado empleado);
    void delete(Long id);
    Empleado findById(Long id);
    List<Empleado> findAll();
    List<Empleado> findByNombreDepartamento(String nombreDepartamento);
    BigDecimal findAverageSalaryByDepartamento(Long departamentoId);
    List<Empleado> findBySalaryRange(BigDecimal min, BigDecimal max);
    List<Empleado> findByHiringDate(LocalDate inicio, LocalDate fin);
}
