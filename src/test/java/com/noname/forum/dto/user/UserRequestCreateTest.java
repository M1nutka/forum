package com.noname.forum.dto.user;

import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import com.noname.forum.fixture.UserRequestCreateFixture;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class UserRequestCreateTest {
    
    private final static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test 
    void shouldHavaValidationRequest() throws Exception{
        UserRequestCreate badRequest = UserRequestCreateFixture.incorrectRequestCreate();

        Set<ConstraintViolation<UserRequestCreate>> violations = validator.validate(badRequest);

        assertThat(violations).isNotEmpty();
        assertThat(violations).hasSize(7);
    }
}
