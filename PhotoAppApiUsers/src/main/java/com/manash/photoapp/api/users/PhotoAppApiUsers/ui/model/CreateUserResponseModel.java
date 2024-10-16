package com.manash.photoapp.api.users.PhotoAppApiUsers.ui.model;

import lombok.Data;

@Data
public class CreateUserResponseModel
{
	private String userId;
	private String firstName;
	private String lastName;
	private String email;
}
