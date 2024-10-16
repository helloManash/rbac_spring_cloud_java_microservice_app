package com.manash.photoapp.api.users.PhotoAppApiUsers.ui.controllers;

import com.manash.photoapp.api.users.PhotoAppApiUsers.service.UsersService;
import com.manash.photoapp.api.users.PhotoAppApiUsers.shared.UserDto;
import com.manash.photoapp.api.users.PhotoAppApiUsers.ui.model.CreateUserRequestModel;
import com.manash.photoapp.api.users.PhotoAppApiUsers.ui.model.CreateUserResponseModel;
import com.manash.photoapp.api.users.PhotoAppApiUsers.ui.model.UserResponseModel;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController
{
	@Autowired
	UsersService usersService;

	@GetMapping("/status/check")
	public String status()
	{
		return "OK";
	}

	@PostMapping(value = "signup",consumes = {
		MediaType.APPLICATION_JSON_VALUE,
		MediaType.APPLICATION_XML_VALUE,
	}, produces = {
		MediaType.APPLICATION_JSON_VALUE,
		MediaType.APPLICATION_XML_VALUE,
	})
	public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetails){

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		UserDto userDto = modelMapper.map(userDetails, UserDto.class);

		UserDto createdUser = usersService.createUser(userDto);

		CreateUserResponseModel responseModel = modelMapper.map(createdUser, CreateUserResponseModel.class);

		return new ResponseEntity<>(responseModel,HttpStatus.CREATED);
	}

		@GetMapping(value = "{userId}", produces = {
			MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE,
		})
		public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId){
			UserDto userDto = usersService.getUserByUserId(userId);
			UserResponseModel returnValue = new ModelMapper().map(userDto, UserResponseModel.class);
			return new ResponseEntity<>(returnValue, HttpStatus.OK);
		}
//	@GetMapping(value = "{userId}")
//	public ResponseEntity<String> getUser(@PathVariable Long userId)
//	{
//		return new ResponseEntity<>("Hi", HttpStatus.OK);
//	}

}
