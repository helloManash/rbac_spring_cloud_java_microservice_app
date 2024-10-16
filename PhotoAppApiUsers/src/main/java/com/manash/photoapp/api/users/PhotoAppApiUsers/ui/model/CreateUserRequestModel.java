package com.manash.photoapp.api.users.PhotoAppApiUsers.ui.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequestModel
{
	@NotNull(message = "First name cannot be null")
	@Size(min = 2, message = "First name must not be less than two characters")
	private String firstName;

	@NotNull(message = "Last name cannot be null")
	@Size(min = 2, message = "Last name must not be less than two characters")
	private String lastName;

	@Email
	@NotNull(message = "Email cannot be null")
	private String email;

	@NotNull(message = "password cannot be null")
	@Size(min = 8, max = 16, message = "Password must be equal or greater than 8 characters and less than 16 characters")
	private String password;

}
