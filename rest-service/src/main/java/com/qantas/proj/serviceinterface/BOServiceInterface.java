package com.qantas.proj.serviceinterface;

import java.util.Collection;

import com.qantas.proj.controller.exceptions.ServiceException;
import com.qantas.proj.pojos.User;

public interface BOServiceInterface {

	public User createUser(User user) throws ServiceException ;

	public Collection<User> getAllUsers() throws ServiceException;

	public User getUser(String userid) throws ServiceException ;

	public User updateUser(User user) throws ServiceException ;

	public void deleteUser(String userId) throws ServiceException;
}
