package com.agilityio.employeeservice.models.mappers;

import com.agilityio.employeeservice.models.Employee;
import com.agilityio.employeeservice.models.EmployeeInternal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    Employee toEmployee(EmployeeInternal employeeInternal);

    @Mapping(target = "id", ignore = true)
    EmployeeInternal toEmployeeInternal(Employee employee);

    @Mapping(target = "id", ignore = true)
    void update(Employee employee, @MappingTarget EmployeeInternal employeeInternal);
}
