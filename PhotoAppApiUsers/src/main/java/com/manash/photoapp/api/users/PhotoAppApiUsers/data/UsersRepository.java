package com.manash.photoapp.api.users.PhotoAppApiUsers.data;

import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<UserEntity,Long>
{
	UserEntity findByEmail(String email);

	UserEntity findByUserId(String userId);
}
