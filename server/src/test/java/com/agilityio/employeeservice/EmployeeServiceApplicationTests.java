package com.agilityio.employeeservice;

import com.agilityio.employeeservice.configurations.LocalRibbonClients;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@LocalRibbonClients
public class EmployeeServiceApplicationTests {

    @Test
    public void contextLoads() {
    }

}
