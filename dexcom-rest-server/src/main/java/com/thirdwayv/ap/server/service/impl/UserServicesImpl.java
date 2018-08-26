package com.thirdwayv.ap.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thirdwayv.ap.server.model.User;
import com.thirdwayv.ap.server.repository.UserRepository;
import com.thirdwayv.ap.server.service.UserService;

@Service("userService")
@Transactional
public class UserServicesImpl implements UserService{

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public User findByUserName(String userName){
		return userRepo.findByUserName(userName);
	}
}
