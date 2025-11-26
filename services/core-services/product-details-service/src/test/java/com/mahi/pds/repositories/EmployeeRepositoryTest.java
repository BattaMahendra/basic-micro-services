package com.mahi.pds.repositories;

import com.mahi.pds.entity.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

//@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository orderRepository;


    @Test
    public void injectedComponentsAreNotNull(){
        List<Employee> empList = new ArrayList<>();
        Employee e = new Employee();
        e.setEmp_id(1);
        empList.add(e);
        Employee emp = orderRepository.save(e);
        Assertions.assertEquals(emp,e);
    }
}
