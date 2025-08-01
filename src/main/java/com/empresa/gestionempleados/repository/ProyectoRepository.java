package com.empresa.gestionempleados.repository;

import com.empresa.gestionempleados.entity.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
}
