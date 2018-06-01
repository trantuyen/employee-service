package com.agilityio.employeeservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class EmployeeItem {

    /**
     * The employee id
     */
    @Id
    private String id;

    /**
     * The employee name
     */
    @NotBlank
    private String name;

    /**
     * The employee phone number
     */
    @NotBlank
    private String phoneNumber;

    /**
     * Employee's email
     */
    @Email
    private String email;
}
