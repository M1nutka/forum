package com.noname.forum.dto.user;

import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import com.noname.forum.fixture.UserRequestUpdateFixture;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class UserRequestUpdateTest {
    
    private final static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test 
    void shouldHavaValidationRequest() throws Exception{
        UserRequestUpdate badRequest = UserRequestUpdateFixture.incorrectRequest();

        Set<ConstraintViolation<UserRequestUpdate>> violations = validator.validate(badRequest);

        assertThat(violations).isNotEmpty();
        assertThat(violations).hasSize(5);
    }
}
