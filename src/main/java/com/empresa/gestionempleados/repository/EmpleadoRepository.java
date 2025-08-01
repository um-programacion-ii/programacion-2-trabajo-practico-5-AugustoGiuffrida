package com.empresa.gestionempleados.repository;

import com.empresa.gestionempleados.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
}
