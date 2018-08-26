package com.thirdwayv.ap.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thirdwayv.ap.server.model.Controller;
import com.thirdwayv.ap.server.model.Node;

@Repository
public interface ControllerRepository extends JpaRepository<Controller, Long>{
	public Controller findByDeviceNumber(int deviceNumber);	
}
