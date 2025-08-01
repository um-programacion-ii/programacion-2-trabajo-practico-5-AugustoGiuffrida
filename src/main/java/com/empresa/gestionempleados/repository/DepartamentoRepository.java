package com.empresa.gestionempleados.repository;

import com.empresa.gestionempleados.entity.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
}
