package com.empresa.gestionempleados.service;

import com.empresa.gestionempleados.entity.Proyecto;
import com.empresa.gestionempleados.exceptions.ProyectoNoEncontradoException;
import com.empresa.gestionempleados.repository.ProyectoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProyectoServiceImpl implements ProyectoService{
    private final ProyectoRepository proyectoRepository;

    public ProyectoServiceImpl(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    @Override
    public Proyecto save(Proyecto proyecto){
        return proyectoRepository.save(proyecto);
    }

    @Override
    public Proyecto update(Long id, Proyecto proyecto){
        if(!proyectoRepository.existsById(id)){
            throw new ProyectoNoEncontradoException(id);
        }
        proyecto.setId(id);
        return proyectoRepository.save(proyecto);
    }

    @Override
    public void delete(Long id){
        if (!proyectoRepository.existsById(id)){
            throw  new ProyectoNoEncontradoException(id);
        }
        proyectoRepository.deleteById(id);
    }

    @Override
    public Proyecto findById(Long id){
        return proyectoRepository.findById(id).orElseThrow(()-> new ProyectoNoEncontradoException(id));
    }

    @Override
    public List<Proyecto> findAll(){
        return proyectoRepository.findAll();
    }

    @Override
    public List<Proyecto> findByStatus(String status){
        return proyectoRepository.findByEstado(status);
    }
}
