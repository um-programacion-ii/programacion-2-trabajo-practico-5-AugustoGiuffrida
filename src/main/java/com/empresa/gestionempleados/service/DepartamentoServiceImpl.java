package com.empresa.gestionempleados.service;

import com.empresa.gestionempleados.entity.Departamento;
import com.empresa.gestionempleados.exceptions.DepartamentoNoEncontradoException;
import com.empresa.gestionempleados.repository.DepartamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DepartamentoServiceImpl implements DepartamentoService {
    private final DepartamentoRepository departamentoRepository;

    public DepartamentoServiceImpl(DepartamentoRepository departamentoRepository){
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public Departamento save(Departamento departamento){
        return departamentoRepository.save(departamento);
    }

    @Override
    public Departamento update(Long id, Departamento departamento){
        if(!departamentoRepository.existsById(id)){
            throw new DepartamentoNoEncontradoException(id);
        }
        departamento.setId(id);
        return departamentoRepository.save(departamento);
    }

    @Override
    public  void delete(Long id){
        if (!departamentoRepository.existsById(id)){
            throw new DepartamentoNoEncontradoException(id);
        }
        departamentoRepository.deleteById(id);
    }

    @Override
    public Departamento findById(Long id){
        return departamentoRepository.findById(id).orElseThrow(()->new DepartamentoNoEncontradoException(id));
    }

    @Override
    public List<Departamento> findAll(){
        return  departamentoRepository.findAll();
    }

}
