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

    @GetMapping
    public ResponseEntity<List<Empleado>> findAll(){
        return ResponseEntity.ok(empleadoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> findById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(empleadoService.findById(id));
        } catch (EmpleadoNoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @PostMapping
    public ResponseEntity<Empleado> save(@RequestBody Empleado empleado){
        return ResponseEntity.status(HttpStatus.CREATED).body(empleadoService.save(empleado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> update(@PathVariable Long id, @RequestBody Empleado empleado){
        try {
            return ResponseEntity.ok(empleadoService.update(id, empleado));
        } catch (EmpleadoNoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> delete(@PathVariable Long id){
        try {
            empleadoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EmpleadoNoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/departamento/{nombreDepartamento}")
    public ResponseEntity<List<Empleado>> findByNombreDepartamento(@PathVariable String nombreDepartamento){
        try {
            return ResponseEntity.ok(empleadoService.findByNombreDepartamento(nombreDepartamento));
        } catch (EmpleadoNoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/promedio-salario/{departamentoId}")
    public ResponseEntity<BigDecimal> findAverageSalaryByDepartamento(@PathVariable Long departamentoId){
        try {
            return ResponseEntity.ok(empleadoService.findAverageSalaryByDepartamento(departamentoId));
        } catch (EmpleadoNoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/rango-salario")
    public ResponseEntity<List<Empleado>> findBySalaryRange(@RequestParam BigDecimal min, @RequestParam BigDecimal max){
        try {
            return ResponseEntity.ok(empleadoService.findBySalaryRange(min, max));
        } catch (EmpleadoNoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/fecha-contratacion")
    public ResponseEntity<List<Empleado>> findByHiringDate(@RequestParam LocalDate inicio, @RequestParam LocalDate fin){
        try {
            return ResponseEntity.ok(empleadoService.findByHiringDate(inicio, fin));
        } catch (EmpleadoNoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
