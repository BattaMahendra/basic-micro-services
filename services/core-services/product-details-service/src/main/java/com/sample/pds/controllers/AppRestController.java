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

    @GetMapping(value = "/all",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> getAllEmployee(){
        System.out.println("Returns JSON mapping");
        return appService.getAllEmployees();
    }
    @GetMapping(value = "/all2",produces = MediaType.APPLICATION_XML_VALUE)
    public List<Employee> getAllEmployeeInXMLFormat(){
        System.out.println("Returns XML mapping");
        return appService.getAllEmployees();
    }

    /*
    * Here although we didn't specify any produces statement it by default returns xml because in build.gradle
    * we have added jackson xml dependency which forces rest controller default behaviour to xml format
    *
    * Actually in spring boot the default behaviour of Rest Controller is json format if you don't specify jackson xml dependency*/
    @GetMapping(value = "/all3")
    public List<Employee> getAllEmployeeInDefaultFormat(){
        System.out.println("Returns XML mapping");
        return appService.getAllEmployees();
    }

    //this controller accepts json and returns xml
    @PostMapping(value = "/addE",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_XML_VALUE)
    public Employee addAnEmployee(@RequestBody Employee employee){
        return appService.addAnEmployee(employee);
    }

}
