package com.agilityio.employeeservice.configurations;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;

@RibbonClients({
    @RibbonClient(
        value = LocalRibbonConfiguration.DepartmentService.NAME,
        configuration = LocalRibbonConfiguration.DepartmentService.class),

    @RibbonClient(
        value = LocalRibbonConfiguration.EmployeeService.NAME,
        configuration = LocalRibbonConfiguration.EmployeeService.class
    )
})
public @interface LocalRibbonClients {
}
