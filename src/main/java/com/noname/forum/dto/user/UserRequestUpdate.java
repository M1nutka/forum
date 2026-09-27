package com.noname.forum.dto.user;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public record UserRequestUpdate(
    @NotEmpty (message = "Email cannot be empty")
    @Email (message = "Email should be valid")
    @Size (min = 5, max = 100, message = "Email must be between 5 and 100 characters")
    String email,

    @NotEmpty (message = "Name cannot be empty")
    @Size (min = 3, max = 50, message = "Name must be between 3 and 50 characters") 
    String name,

    @NotEmpty (message = "Lastname cannot be empty")
    @Size (min = 3, max = 50, message = "Lastname must be between 3 and 50 characters")
    String lastname,

    @NotNull (message = "Born is cannot be null")
    @Past (message = "Born is must be past")
    LocalDate bornIs,
    
    @NotEmpty (message = "Phone cannot be empty")
    @Size (min = 12, max = 12, message = "Phone must be 12 characters") 
    String phone,
    
    @Size (max = 1000, message = "Description must be max 1000 characters")
    String description
) {
} 
