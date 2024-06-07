package com.sample.pds.services.implementation;

import com.sample.pds.entity.Employee;
import com.sample.pds.repositories.EmployeeRepository;
import com.sample.pds.services.AppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AppServiceImpl implements AppService {

    @Autowired
    EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public Employee addAnEmployee(Employee employee){
        return employeeRepository.save(employee);
    }



}
