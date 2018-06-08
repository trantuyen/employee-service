package com.agilityio.employeeservice.configurations;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

//@Profile("localhost")
@Configuration
public class LocalRibbonConfiguration {

    public static class DepartmentService {
        public static final String NAME = "department-service";

        @Bean
        public ServerList<Server> ribbonServerList() {
            return new StaticServerList<>(
                new Server("localhost", 9009)
            );
        }
    }

    public static class EmployeeService {
        public static final String NAME = "employee-service";

        @Bean
        public ServerList<Server> ribbonServerList() {
            return new StaticServerList<>(
                new Server("localhost", 9008)
            );
        }
    }
}
