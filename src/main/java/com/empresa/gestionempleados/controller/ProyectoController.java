package com.empresa.gestionempleados.controller;

import com.empresa.gestionempleados.entity.Proyecto;
import com.empresa.gestionempleados.exceptions.ProyectoNoEncontradoException;
import com.empresa.gestionempleados.service.ProyectoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/proyectos")
public class ProyectoController {
    private final ProyectoService proyectoService;

    public ProyectoController( ProyectoService proyectoService){
        this.proyectoService = proyectoService;
    }

    @GetMapping
    public ResponseEntity<List<Proyecto>> findAll(){
        return ResponseEntity.ok(proyectoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> findById(@PathVariable Long id){
        try {
          return ResponseEntity.ok(proyectoService.findById(id));
        } catch (ProyectoNoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("estado/{status}")
    public ResponseEntity<List<Proyecto>> findByStatus(@PathVariable String status){
            return ResponseEntity.ok(proyectoService.findByStatus(status));
    }

    @PostMapping
    public ResponseEntity<Proyecto> save(@RequestBody Proyecto proyecto){
        return ResponseEntity.status(HttpStatus.CREATED).body(proyectoService.save(proyecto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proyecto> update(@PathVariable Long id, @RequestBody Proyecto proyecto){
        try {
            return ResponseEntity.ok(proyectoService.update(id, proyecto));
        } catch (ProyectoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        try {
            proyectoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ProyectoNoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
