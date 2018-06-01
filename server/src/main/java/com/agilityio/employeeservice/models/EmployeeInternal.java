package com.agilityio.employeeservice.models;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Clock;
import java.time.Instant;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder(builderMethodName = "builderInternal", buildMethodName = "buildInternal")
@Document(collection = "employee-service-employees")
public class EmployeeInternal extends Employee {

    @CreatedDate
    private Instant createdAt = Instant.now(Clock.systemUTC());

    @LastModifiedDate
    private Instant modifiedAt = Instant.now(Clock.systemUTC());

    @Version
    private Long version;
}
