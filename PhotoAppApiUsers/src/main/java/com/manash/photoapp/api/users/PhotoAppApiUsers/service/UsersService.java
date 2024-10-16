package com.manash.photoapp.api.users.PhotoAppApiUsers.service;

import com.manash.photoapp.api.users.PhotoAppApiUsers.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService extends UserDetailsService
{
	UserDto createUser(UserDto userDetails);
	UserDto getUserDetailsByEmail(String email);
	UserDto getUserByUserId(String userId);
}
