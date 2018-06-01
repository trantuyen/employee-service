package com.agilityio.employeeservice.controllers;

import com.agilityio.employeeservice.exceptions.NotFoundResourceException;
import com.agilityio.employeeservice.models.Employee;
import com.agilityio.employeeservice.models.EmployeeInternal;
import com.agilityio.employeeservice.models.mappers.EmployeeMapper;
import com.agilityio.employeeservice.repositories.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("v1/employees")
public class EmployeeController {

    private EmployeeRepository repository;

    private EmployeeMapper mapper;

    // TODO:: Implement department service feign client
    //private DepartmentServiceClient departmentServiceClient;

    public EmployeeController(EmployeeRepository repository, EmployeeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Get employees of a given department id.
     *
     * @param departmentId Department id
     * @return The employees of a given department id
     */
    @GetMapping
    public ResponseEntity<Employee> findOne(@Valid @RequestParam(value = "departmentId") String departmentId) {
        List<EmployeeInternal> employees = repository.findAllByDepartmentIdEquals(departmentId);
        Employee employee = new Employee();

        // TODO:: Check department has existed in the system

        if (employees != null && !employees.isEmpty()) {
            employee = mapper.toEmployee(employees.get(0));
        }

        return ResponseEntity.ok(employee);
    }

    /**
     * Create new or update employees of an existing department.
     *
     * @param employee Employee instant with a list of employee items of a department
     * @return Response entity.
     */
    @PostMapping
    public ResponseEntity<?> createOrUpdate(@Valid @RequestBody Employee employee) {
        final String departmentId = employee.getDepartmentId();

        if (StringUtils.isEmpty(departmentId)) {
            throw new NotFoundResourceException("The given department has not existed.");
        }

        // TODO:: Make sure department has existed in the system
        List<EmployeeInternal> employeeInternals = repository.findAllByDepartmentIdEquals(departmentId);
        EmployeeInternal employeeInternal;

        if (employeeInternals == null || employeeInternals.isEmpty()) {
            employeeInternal = EmployeeInternal.builderInternal().buildInternal();
            employeeInternal.setDepartmentId(departmentId);
        } else {
            employeeInternal = employeeInternals.get(0);
        }

        // Update data
        mapper.update(employee, employeeInternal);
        repository.save(employeeInternal);

        return ResponseEntity.ok().build();
    }
}
