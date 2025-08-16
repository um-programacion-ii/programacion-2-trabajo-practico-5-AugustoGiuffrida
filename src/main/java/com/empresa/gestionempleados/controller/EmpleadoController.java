package com.empresa.gestionempleados.controller;

import com.empresa.gestionempleados.entity.Empleado;
import com.empresa.gestionempleados.exceptions.EmpleadoNoEncontradoException;
import com.empresa.gestionempleados.service.EmpleadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/empleados")
@Validated
public class EmpleadoController {
    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService){
        this.empleadoService = empleadoService;
    }

    /**
     * Obtiene todos los empleados.
     * @return Lista de empleados
     */
    @GetMapping
    public ResponseEntity<List<Empleado>> findAll(){
        return ResponseEntity.ok(empleadoService.findAll());
    }

    /**
     * Busca un empleado por su ID.
     * @param id ID del empleado
     * @return Empleado encontrado o NOT_FOUND si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Empleado> findById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(empleadoService.findById(id));
        } catch (EmpleadoNoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Crea un nuevo empleado.
     * @param empleado Empleado a crear
     * @return Empleado creado con código CREATED
     */
    @PostMapping
    public ResponseEntity<Empleado> save(@RequestBody Empleado empleado){
        return ResponseEntity.status(HttpStatus.CREATED).body(empleadoService.save(empleado));
    }

    /**
     * Actualiza un empleado existente.
     * @param id ID del empleado a actualizar
     * @param empleado Datos actualizados del empleado
     * @return Empleado actualizado o NOT_FOUND si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Empleado> update(@PathVariable Long id, @RequestBody Empleado empleado){
        try {
            return ResponseEntity.ok(empleadoService.update(id, empleado));
        } catch (EmpleadoNoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    /**
     * Elimina un empleado por su ID.
     * @param id ID del empleado a eliminar
     * @return NO_CONTENT si se elimina correctamente o NOT_FOUND si no existe
     */
    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> delete(@PathVariable Long id){
        try {
            empleadoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EmpleadoNoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Lista empleados por nombre de departamento.
     * @param nombreDepartamento Nombre del departamento
     * @return Lista de empleados o NOT_FOUND si no existen
     */
    @GetMapping("/departamento/{nombreDepartamento}")
    public ResponseEntity<List<Empleado>> findByNombreDepartamento(@PathVariable String nombreDepartamento){
        try {
            return ResponseEntity.ok(empleadoService.findByNombreDepartamento(nombreDepartamento));
        } catch (EmpleadoNoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Obtiene el salario promedio de un departamento.
     * @param departamentoId ID del departamento
     * @return Salario promedio o NOT_FOUND si no existen empleados
     */
    @GetMapping("/promedio-salario/{departamentoId}")
    public ResponseEntity<BigDecimal> findAverageSalaryByDepartamento(@PathVariable Long departamentoId){
        try {
            return ResponseEntity.ok(empleadoService.findAverageSalaryByDepartamento(departamentoId));
        } catch (EmpleadoNoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Lista empleados dentro de un rango de salarios.
     * @param min Salario mínimo
     * @param max Salario máximo
     * @return Lista de empleados o NOT_FOUND si no existen
     */
    @GetMapping("/rango-salario")
    public ResponseEntity<List<Empleado>> findBySalaryRange(@RequestParam BigDecimal min, @RequestParam BigDecimal max){
        try {
            return ResponseEntity.ok(empleadoService.findBySalaryRange(min, max));
        } catch (EmpleadoNoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Lista empleados contratados dentro de un rango de fechas.
     * @param inicio Fecha de inicio
     * @param fin Fecha de fin
     * @return Lista de empleados o NOT_FOUND si no existen
     */
    @GetMapping("/fecha-contratacion")
    public ResponseEntity<List<Empleado>> findByHiringDate(@RequestParam LocalDate inicio, @RequestParam LocalDate fin){
        try {
            return ResponseEntity.ok(empleadoService.findByHiringDate(inicio, fin));
        } catch (EmpleadoNoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
