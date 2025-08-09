package com.empresa.gestionempleados.repository;

import com.empresa.gestionempleados.entity.Departamento;
import com.empresa.gestionempleados.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Optional<Empleado> findByEmail(String email);
    List<Empleado> findByFechaContratacionAfter(LocalDate fechaContratacion);
    List<Empleado> findByDepartamento(Departamento departamento);
    List<Empleado> findBySalarioBetween(BigDecimal min, BigDecimal max);
    List<Empleado> findByFechaContratacionBetween(LocalDate inicio, LocalDate fin);

    @Query("SELECT e FROM Empleado e WHERE e.departamento.nombre = :nombreDepartamento")
    List<Empleado> findByNombreDepartamento(@Param("nombreDepartamento") String nombreDepartamento);

    @Query("SELECT AVG(e.salario) FROM Empleado e WHERE e.departamento.id = :departamentoID")
    Optional<BigDecimal> findAverageSalaryByDepartamento(@Param("departamentoID") Long departamentoId);
}
