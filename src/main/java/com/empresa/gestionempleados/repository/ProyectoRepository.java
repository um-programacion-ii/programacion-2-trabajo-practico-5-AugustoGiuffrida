package com.empresa.gestionempleados.repository;

import com.empresa.gestionempleados.entity.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    List<Proyecto> findByEstado(String estado);

    @Query("SELECT p.estado FROM Proyecto p WHERE p.id = :idProyecto")
    String findEstadoById(@Param("idProyecto") Long idProyecto);
}
