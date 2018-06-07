package com.agilityio.employeeservice;

import com.agilityio.employeeservice.models.Employee;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@FeignClient(name="employee-service")
public interface EmployeeClient {

    /**
     * Get employees of a given department id.
     *
     * @param departmentId Department id
     * @return The employees of a given department id
     */
    @GetMapping("/v1/employees")
    ResponseEntity<Employee> findOne(@Valid @RequestParam(value = "departmentId") String departmentId);

    /**
     * Create new or update employees of an existing department.
     *
     * @param employee Employee instant with a list of employee items of a department
     * @return Response entity.
     */
    @PostMapping("/v1/employees")
    ResponseEntity<Employee> createOrUpdate(@Valid @RequestBody Employee employee);
}
