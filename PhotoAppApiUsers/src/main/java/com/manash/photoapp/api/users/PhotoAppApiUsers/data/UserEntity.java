package com.manash.photoapp.api.users.PhotoAppApiUsers.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "users")
public class UserEntity implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 50)
	private String firstName;

	@Column(nullable = false, length = 50)
	private String lastName;

	@Column(nullable = false, length = 120, unique = true)
	private String email;

	private String userId;
	private String encryptedPassword;

	@Id
	@GeneratedValue
	private Long id;
}
