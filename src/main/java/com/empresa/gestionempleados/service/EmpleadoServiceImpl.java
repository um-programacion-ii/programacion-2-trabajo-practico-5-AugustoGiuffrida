package com.empresa.gestionempleados.service;

import com.empresa.gestionempleados.entity.Empleado;
import com.empresa.gestionempleados.exceptions.EmailDuplicadoException;
import com.empresa.gestionempleados.exceptions.EmpleadoNoEncontradoException;
import com.empresa.gestionempleados.repository.EmpleadoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmpleadoServiceImpl implements EmpleadoService{
    private final EmpleadoRepository empleadoRepository;

    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository){
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public Empleado  save(Empleado empleado){
        if(empleadoRepository.findByEmail(empleado.getEmail()).isPresent()){
            throw new EmailDuplicadoException(empleado.getEmail());
        }
        return empleadoRepository.save(empleado);
    }

    @Override
    public  Empleado update(Long id, Empleado empleado){
        if(!empleadoRepository.existsById(id)){
            throw new EmpleadoNoEncontradoException(id);
        }
        Optional<Empleado> existingWithEmail = empleadoRepository.findByEmail(empleado.getEmail());
        if(existingWithEmail.isPresent() && !existingWithEmail.get().getId().equals(id)){
            throw new EmailDuplicadoException(empleado.getEmail());
        }
        empleado.setId(id);
        return  empleadoRepository.save(empleado);
    }

    @Override
    public void delete(Long id){
        if(!empleadoRepository.existsById(id)){
            throw new EmpleadoNoEncontradoException(id);
        }
        empleadoRepository.deleteById(id);
    }

    @Override
    public Empleado findById(Long id){
        return empleadoRepository.findById(id).orElseThrow(()-> new EmpleadoNoEncontradoException(id));
    }

    @Override
    public List<Empleado> findAll(){
        return empleadoRepository.findAll();
    }

    @Override
    public List<Empleado> findByNombreDepartamento(String nombreDepartamento){
        return  empleadoRepository.findByNombreDepartamento(nombreDepartamento);
    }

    @Override
    public BigDecimal findAverageSalaryByDepartamento(Long departamentoId){
        return empleadoRepository.findAverageSalaryByDepartamento(departamentoId).orElse(BigDecimal.ZERO);
    }

    @Override
    public List<Empleado> findBySalaryRange(BigDecimal min, BigDecimal max){
        return empleadoRepository.findBySalarioBetween(min,max);
    }

    @Override
    public  List<Empleado> findByHiringDate(LocalDate inicio, LocalDate fin){
        return empleadoRepository.findByFechaContratacionBetween(inicio, fin);
    }

}
