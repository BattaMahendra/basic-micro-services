package com.sample.pds.controllers;

import com.sample.pds.entity.Employee;
import com.sample.pds.repositories.EmployeeRepository;
import com.sample.pds.services.AppService;
import com.sample.pds.services.implementation.AppServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.util.List;

@RestController //==> @controller + @ResponseBody
public class AppRestController {

    @Autowired
    AppServiceImpl appService;

    @GetMapping("/all")
    public List<Employee> getAllEmployee(){
        return appService.getAllEmployees();
    }

    //this controller accepts json and returns xml
    @PostMapping(value = "/addE",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_XML_VALUE)
    public Employee addAnEmployee(@RequestBody Employee employee){
        return appService.addAnEmployee(employee);
    }
}
