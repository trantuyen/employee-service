package com.agilityio.employeeservice.controllers;

import com.agilityio.departmentservice.DepartmentClient;
import com.agilityio.departmentservice.models.Department;
import com.agilityio.employeeservice.exceptions.NotFoundResourceException;
import com.agilityio.employeeservice.models.Employee;
import com.agilityio.employeeservice.models.EmployeeInternal;
import com.agilityio.employeeservice.models.mappers.EmployeeMapper;
import com.agilityio.employeeservice.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private DepartmentClient departmentClient;

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
        // Check department has existed in the system
        if (isExistedDepartment(departmentId)) {
            List<EmployeeInternal> employees = repository.findAllByDepartmentIdEquals(departmentId);
            Employee employee = new Employee();

            if (employees != null && !employees.isEmpty()) {
                employee = mapper.toEmployee(employees.get(0));
            }

            return ResponseEntity.ok(employee);
        } else {
            throw new NotFoundResourceException("Could not find given department id in the system.");
        }
    }

    /**
     * Create new or update employees of an existing department.
     *
     * @param employee Employee instant with a list of employee items of a department
     * @return Response entity.
     */
    @PostMapping
    public ResponseEntity<Employee> createOrUpdate(@Valid @RequestBody Employee employee) {
        final String departmentId = employee.getDepartmentId();

        // Make sure department has existed in the system
        if (!StringUtils.isEmpty(departmentId) && isExistedDepartment(departmentId)) {
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
            employeeInternal = repository.save(employeeInternal);
            employee = mapper.toEmployee(employeeInternal);

            return ResponseEntity.ok(employee);
        } else {
            throw new NotFoundResourceException("Could not find given department id in the system.");
        }
    }

    /**
     * Check if department is existed in the system.
     *
     * @param departmentId Department id
     * @return True: existed.
     */
    private boolean isExistedDepartment(String departmentId) {
        try {
            ResponseEntity<Department> res = departmentClient.findOne(departmentId);
            return res.getStatusCode() == HttpStatus.OK && res.getBody() != null;
        } catch (Exception e) {
            // TODO:: Log this
            return false;
        }
    }
}
