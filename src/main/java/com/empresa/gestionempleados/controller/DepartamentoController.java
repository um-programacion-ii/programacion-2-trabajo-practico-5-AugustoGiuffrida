package com.empresa.gestionempleados.controller;

import com.empresa.gestionempleados.entity.Departamento;
import com.empresa.gestionempleados.exceptions.DepartamentoNoEncontradoException;
import com.empresa.gestionempleados.service.DepartamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/departamentos")
public class DepartamentoController {
    private final DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService){
        this.departamentoService = departamentoService;
    }

    /**
     * Obtiene todos los departamentos.
     * @return Lista de departamentos
     */
    @GetMapping
    public ResponseEntity<List<Departamento>> findAll(){
        return ResponseEntity.ok(departamentoService.findAll());
    }

    /**
     * Busca un departamento por su ID.
     * @param id ID del departamento
     * @return Departamento encontrado o NOT_FOUND si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Departamento> findById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(departamentoService.findById(id));
        } catch (DepartamentoNoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Crea un nuevo departamento.
     * @param departamento Departamento a crear
     * @return Departamento creado con código CREATED
     */
    @PostMapping
    public ResponseEntity<Departamento> save(@RequestBody Departamento departamento){
        return ResponseEntity.status(HttpStatus.CREATED).body(departamentoService.save(departamento));
    }

    /**
     * Actualiza un departamento existente.
     * @param id ID del departamento a actualizar
     * @param departamento Datos actualizados del departamento
     * @return Departamento actualizado o NOT_FOUND si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Departamento> update(@PathVariable Long id, @RequestBody Departamento departamento){
        try {
            return ResponseEntity.ok(departamentoService.update(id, departamento));
        } catch (DepartamentoNoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Elimina un departamento por su ID.
     * @param id ID del departamento a eliminar
     * @return NO_CONTENT si se elimina correctamente o NOT_FOUND si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        try {
            departamentoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (DepartamentoNoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
