package com.manash.photoapp.api.users.PhotoAppApiUsers.service;

import com.manash.photoapp.api.users.PhotoAppApiUsers.data.AlbumsServiceClient;
import com.manash.photoapp.api.users.PhotoAppApiUsers.data.UserEntity;
import com.manash.photoapp.api.users.PhotoAppApiUsers.data.UsersRepository;
import com.manash.photoapp.api.users.PhotoAppApiUsers.shared.UserDto;
import com.manash.photoapp.api.users.PhotoAppApiUsers.ui.model.AlbumResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService
{
	UsersRepository usersRepository;
	BCryptPasswordEncoder bCryptPasswordEncoder;
	AlbumsServiceClient albumsServiceClient;

	@Autowired
	public UsersServiceImpl (UsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AlbumsServiceClient albumsServiceClient)
	{
		this.usersRepository = usersRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.albumsServiceClient = albumsServiceClient;
	}

	@Override
	public UserDto createUser(UserDto userDetails)
	{
		userDetails.setUserId(UUID.randomUUID().toString());
		userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);


		usersRepository.save(userEntity);
		UserDto returnValue = modelMapper.map(userEntity, UserDto.class);
		return returnValue;
	}

	@Override
	public UserDto getUserDetailsByEmail(String email)
	{
		UserEntity userEntity = usersRepository.findByEmail(email);
		if(userEntity == null){
			throw new UsernameNotFoundException(email);
		}
		return new ModelMapper().map(userEntity, UserDto.class);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		UserEntity userEntity = usersRepository.findByEmail(username);
		if (userEntity == null){
			throw new UsernameNotFoundException(username);
		}
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
	}

	@Override
	public UserDto getUserByUserId(String userId){
		UserEntity userEntity = usersRepository.findByUserId(userId);
		if(userEntity == null){
			throw new UsernameNotFoundException("User not found");
		}
		UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
		List<AlbumResponseModel> albumsList = albumsServiceClient.getAlbums(userId);
		userDto.setAlbums(albumsList);
		return userDto;
	}
}
