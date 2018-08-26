package com.thirdwayv.ap.server.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thirdwayv.ap.server.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	public User findByUserName(String userName);
}