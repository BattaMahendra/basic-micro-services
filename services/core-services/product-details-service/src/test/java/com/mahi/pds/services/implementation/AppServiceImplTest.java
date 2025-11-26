package com.mahi.pds.services.implementation;

import com.mahi.pds.entity.Employee;
import com.mahi.pds.repositories.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AppServiceImplTest {

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    AppServiceImpl appService;

    @BeforeAll
    public static void setup(){
        System.out.println("Setting up to test the app");

    }

    @Test
    public void getAllEmployeesTest(){
        List<Employee> empList = new ArrayList<>();
        Employee e = new Employee();
        e.setEmp_id(1);
        empList.add(e);
        Mockito.when(employeeRepository.findAll()).thenReturn(empList);
        List<Employee> employeeList = appService.getAllEmployees();
        Assertions.assertEquals(employeeList,empList);
    }
}
