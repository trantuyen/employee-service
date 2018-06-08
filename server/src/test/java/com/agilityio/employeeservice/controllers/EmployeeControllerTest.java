package com.agilityio.employeeservice.controllers;

import com.agilityio.departmentservice.DepartmentClient;
import com.agilityio.departmentservice.models.Department;
import com.agilityio.employeeservice.EmployeeClient;
import com.agilityio.employeeservice.EmployeeServiceApplication;
import com.agilityio.employeeservice.models.Employee;
import com.agilityio.employeeservice.models.EmployeeItem;
import com.agilityio.employeeservice.repositories.EmployeeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
    classes = EmployeeServiceApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@EnableFeignClients(clients = {
    DepartmentClient.class,
    EmployeeClient.class
})
public class EmployeeControllerTest {

    protected static final Faker faker = new Faker(new Locale("en-US"));

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    protected EmployeeRepository repository;

    @Autowired
    protected DepartmentClient departmentClient;

    @Autowired
    protected EmployeeClient employeeClient;

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
        // Create employees of a department first.
        Employee created = createEmployeesSuccess();

        // Perform get all employees of a department
        mockMvc.perform(get(baseUrlTemplate + "?departmentId=" + created.getDepartmentId()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$.departmentId").value(created.getDepartmentId()))
            .andExpect(jsonPath("$.employeeItems").isArray());
    }

    /**
     * Test finding employees of a department got not found status.
     *
     * @throws Exception Json processing error
     */
    @Test
    public void testFindOneWithInvalidDepartmentId() throws Exception {
        mockMvc.perform(get(baseUrlTemplate + "?departmentId=" + UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    /**
     * Test create employees of a department success
     */
    @Test
    public void testCreateSuccess() throws Exception {
        Department department = createDepartmentSuccess();

        List<EmployeeItem> employeeItems = new ArrayList<>();
        employeeItems.add(generateAnEmployeeItem());

        Employee employee = Employee.builder()
            .departmentId(department.getId())
            .employeeItems(employeeItems)
            .build();

        mockMvc.perform(post(baseUrlTemplate).content(toJson(employee)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    /**
     * Test update employees of a department success
     */
    @Test
    public void testUpdateSuccess() throws Exception {
        Employee employee = createEmployeesSuccess();

        List<EmployeeItem> items = employee.getEmployeeItems();
        items.add(generateAnEmployeeItem());

        mockMvc.perform(post(baseUrlTemplate).content(toJson(employee)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    /**
     * Create a department success.
     *
     * @return Department
     */
    private Department createDepartmentSuccess() {
        // Create department first.
        Department requestDepartment = Department.builder()
            .name(faker.name().name())
            .phoneNumber(faker.phoneNumber().phoneNumber())
            .build();

        ResponseEntity<Department> res = departmentClient.create(requestDepartment);
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getStatusCode(), HttpStatus.OK);

        Department createdDepartment = res.getBody();
        Assert.assertNotNull(createdDepartment);
        Assert.assertNotNull(createdDepartment.getId());
        Assert.assertEquals(createdDepartment.getName(), requestDepartment.getName());
        Assert.assertEquals(createdDepartment.getPhoneNumber(), requestDepartment.getPhoneNumber());

        return createdDepartment;
    }

    /**
     * Create employee success.
     *
     * @return The created employee
     */
    private Employee createEmployeesSuccess() {
        Department createdDepartment = createDepartmentSuccess();

        List<EmployeeItem> employeeInternalList = new ArrayList<>();
        employeeInternalList.add(generateAnEmployeeItem());

        Employee employee = Employee.builder()
            .departmentId(createdDepartment.getId())
            .employeeItems(employeeInternalList)
            .build();

        ResponseEntity<Employee> res = employeeClient.createOrUpdate(employee);

        Assert.assertEquals(res.getStatusCode(), HttpStatus.OK);

        Employee created = res.getBody();
        Assert.assertNotNull(created);
        Assert.assertNotNull(created.getId());
        Assert.assertNotNull(created.getEmployeeItems());
        Assert.assertEquals(created.getEmployeeItems().size(), employeeInternalList.size());

        return created;
    }

    /**
     * Generate an employee item.
     *
     * @return Employee item
     */
    private EmployeeItem generateAnEmployeeItem() {
        return EmployeeItem.builder()
            .id(UUID.randomUUID().toString())
            .name(faker.name().name())
            .email(faker.internet().emailAddress())
            .phoneNumber(faker.phoneNumber().phoneNumber())
            .build();
    }

    /**
     * Get json string from employee object.
     *
     * @param employee Employee
     * @return Json string
     * @throws JsonProcessingException Can not parse the given object
     */
    private String toJson(Employee employee) throws JsonProcessingException {
        return springMvcJacksonConverter.getObjectMapper().writeValueAsString(employee);
    }
}
