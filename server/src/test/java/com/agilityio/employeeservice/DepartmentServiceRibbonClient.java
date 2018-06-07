package com.agilityio.employeeservice;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class DepartmentServiceRibbonClient {
    public static final String NAME = "department-service";

    @Bean
    public ServerList<Server> ribbonServerList() {
        return new StaticServerList<>(
            new Server("localhost", 9009)
        );
    }
}
