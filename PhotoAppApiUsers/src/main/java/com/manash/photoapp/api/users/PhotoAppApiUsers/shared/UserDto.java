package com.manash.photoapp.api.users.PhotoAppApiUsers.shared;

import com.manash.photoapp.api.users.PhotoAppApiUsers.ui.model.AlbumResponseModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserDto implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String userId;
	private String encryptedPassword;
	private List<AlbumResponseModel> albums;
}
