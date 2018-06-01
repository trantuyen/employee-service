package com.agilityio.employeeservice.repositories;

import com.agilityio.employeeservice.models.EmployeeInternal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends MongoRepository<EmployeeInternal, String> {

    /**
     * Find all employees by department ids.
     *
     * @param departmentId Department id
     * @return Employees of the given department id
     */
    List<EmployeeInternal> findAllByDepartmentIdEquals(String departmentId);
}
