package com.noname.forum.dto.post;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.noname.forum.fixture.PostRequestFixture;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class PostRequestTest {
    private final static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test 
    void shouldHavaValidationRequest() throws Exception{
        PostRequest postRequest = PostRequestFixture.incorrectPost();

        Set<ConstraintViolation<PostRequest>> violations = validator.validate(postRequest);

        assertThat(violations).isNotEmpty();
        assertThat(violations).hasSize(2);
    }
}
