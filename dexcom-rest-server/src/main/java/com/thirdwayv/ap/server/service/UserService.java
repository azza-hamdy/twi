package com.thirdwayv.ap.server.service;

import com.thirdwayv.ap.server.model.User;

/**
 * 
 * @author mostafa.elkhateeb
 *
 */
public interface UserService {
	
	public User findByUserName(String userName);

}

