package com.agilityio.employeeservice.controllers;

import com.agilityio.employeeservice.EmployeeServiceApplication;
import com.agilityio.employeeservice.repositories.EmployeeRepository;
import com.github.javafaker.Faker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Locale;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
    classes = EmployeeServiceApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
public class EmployeeControllerTest {

    protected static final Faker faker = new Faker(new Locale("en-US"));

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    protected EmployeeRepository repository;

    @Autowired
    protected MappingJackson2HttpMessageConverter springMvcJacksonConverter;

    protected MockMvc mockMvc;

    private String baseUrlTemplate = "/v1/employees";

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Clean database
        repository.deleteAll();
    }

    /**
     * Test finding employees of a department success.
     *
     * @throws Exception Json processing error
     */
    @Test
    public void testFindOneSuccess() throws Exception {
        // TODO:: Create department first.
        // TODO:: Create employees of the department

        // Perform get all employees
        // TODO:: Get by department
        mockMvc.perform(get(baseUrlTemplate))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$").isArray());
    }

    /**
     * Test finding employees of a department got not found status.
     *
     * @throws Exception Json processing error
     */
    @Test
    public void testFindOneWithInvalidDepartmentId() throws Exception {
        // Perform get all departments
        mockMvc.perform(get(baseUrlTemplate + "?departmentId=" + UUID.randomUUID().toString()))
            .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND));
    }

    /**
     * Test create employees of a department success
     */
    @Test
    public void testCreateSuccess() {
        // TODO:: Create department first.
        // TODO:: Create employees of the department
    }

    /**
     * Test update employees of a department success
     */
    @Test
    public void testUpdateSuccess() {
        // TODO:: Create department first.
        // TODO:: Create employees of the department
    }
}
