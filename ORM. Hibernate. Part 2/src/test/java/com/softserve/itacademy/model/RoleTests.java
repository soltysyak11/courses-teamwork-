package com.softserve.itacademy.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RoleTests {

    @Test
    void constraintViolationOnEmptyRoleName() {
        Role emptyRole = new Role();
        emptyRole.setName("");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Role>> violations = validator.validate(emptyRole);
        assertEquals(1, violations.size(), "Validation should fail when name is empty");
    }

    @Test
    void toStringTest() {
        Role role = new Role();
        role.setName("NEW_ROLE");
        String expected = "Role {name = NEW_ROLE}";
        assertEquals(expected, role.toString(), "toString should return the correct string representation");
    }

    @Test
    void testSetAndGetName() {
        Role role = new Role();
        role.setName("Admin");
        assertEquals("Admin", role.getName(), "Name should be set and retrieved correctly");
    }

    @Test
    void constraintViolationOnNullRoleName() {
        Role nullRole = new Role();
        nullRole.setName(null);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Role>> violations = validator.validate(nullRole);
        assertEquals(1, violations.size(), "Validation should fail when name is null");
    }

    @Test
    void noConstraintViolationOnValidRoleName() {
        Role validRole = new Role();
        validRole.setName("ValidName");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Role>> violations = validator.validate(validRole);
        assertTrue(violations.isEmpty(), "No violations should be present for a valid name");
    }
}
