package com.sample.pds.repositories;

import com.sample.pds.entity.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
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
