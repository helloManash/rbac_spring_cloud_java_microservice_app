package com.manash.photoapp.api.users.PhotoAppApiUsers.ui.model;


public class AlbumResponseModel
{
	private String albumId;
	private String userId;
	private String albumName;
	private String description;

	public String getAlbumName()
	{
		return albumName;
	}

	public void setAlbumName(String albumName)
	{
		this.albumName = albumName;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getAlbumId()
	{
		return albumId;
	}

	public void setAlbumId(String albumId)
	{
		this.albumId = albumId;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}
