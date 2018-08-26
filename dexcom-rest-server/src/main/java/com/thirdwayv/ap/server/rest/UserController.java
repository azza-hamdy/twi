package com.thirdwayv.ap.server.rest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thirdwayv.ap.server.model.Controller;
import com.thirdwayv.ap.server.model.Node;
import com.thirdwayv.ap.server.model.User;
import com.thirdwayv.ap.server.repository.NodeRepository;
import com.thirdwayv.ap.server.service.NodeService;
import com.thirdwayv.ap.server.service.ControllerService;
import com.thirdwayv.ap.server.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/user")
public class UserController {


    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private ControllerService gatewayService;
	@Autowired
    private NodeService ctrDeviceService;
	
	@RequestMapping(value="/login",method = RequestMethod.POST,produces="application/json", consumes="application/json")
	public String login(@RequestBody Map<String,String> json) throws ServletException{
		LOGGER.info("login request with user name { "+ json.get("username")+" }");
		LOGGER.info("login request with password { "+json.get("password")+"}");
		if(json.get("username") == null || json.get("password") == null){
			throw new ServletException("can't find user name or password");
		}
		String userName = json.get("username");
		String password = json.get("password");
		
		User user = userService.findByUserName(userName);
		if(user == null){
			LOGGER.info("login request with password {}","User not found");
			throw new ServletException("User not found");
		}
		if(!password.equals(user.getPassword())){
			LOGGER.info("login request with password {}","User not found");
			throw new ServletException("Invalid Password");
		}
		LOGGER.info("logged in successfully");
		
		 //if it has been specified, let's add the expiration
		int MINUTES = 30;
		long ttlMillis = MINUTES*60*1000;
	    long expMillis = System.currentTimeMillis() + ttlMillis;
	    Date exp = new Date(expMillis);
	   
		return Jwts.builder().setSubject(userName).claim("roles", "user").setIssuedAt(new Date())
				.signWith(SignatureAlgorithm.HS256, "secretkey").compact();//.setExpiration(exp).compact();
	}
	
	
	@RequestMapping(value="/gateway/location",method = RequestMethod.POST,produces="application/json", consumes="application/json")
	public void setGatewayLocation(@RequestBody Map<String,String> json){
		try{
			int gatewayId = Integer.parseInt(json.get("gateway"));
			double lat = Double.parseDouble(json.get("lat"));
			double lng= Double.parseDouble(json.get("lng"));
			LOGGER.info("setLocation with  gateway id:"+gatewayId+", lat:"+lat+", lng:"+lng);
			boolean saved = false;
			Controller gateway =  gatewayService.findGatewayByDeviceNumber(gatewayId);
			if(gateway == null){
				LOGGER.info("new gateway");
				gateway =  new Controller();
				gateway.setDeviceNumber(gatewayId);
				gateway.setLat(lat);
				gateway.setLng(lng);
				gateway.setLtk("");;
				gateway.setSequenceNumber(1);;
				gateway.setDateCreated(LocalDateTime.now());
				saved=gatewayService.saveGateway(gateway);
			} else {
				LOGGER.info("modify gateway ");
				gateway.setLat(lat);
				gateway.setLng(lng);
				gateway.setDateUpdated(LocalDateTime.now());
				saved = gatewayService.saveGateway(gateway);
			}
			if (saved) {
				LOGGER.info("gateway saved successfully");
			} else {
				LOGGER.error("error occurred during saving");
			}
		} catch (Exception e) {
			LOGGER.error("error occurred during saving "+e.getMessage());
			throw new RuntimeException("error");
		}
	}
	
	
	@Autowired
	private NodeRepository nodeRepo;
	@RequestMapping(value="/clean/cgm/{cgmSerialNumber}",method = RequestMethod.GET)
	public String cleanSession(@PathVariable String cgmSerialNumber) throws ServletException{
		try{
//			ctrDeviceService.clearDeviceBySerialNumber(cgmSerialNumber);
			Node node = nodeRepo.findBySerialId(cgmSerialNumber);
			if(node == null) return "the device is not exist in DB";
			nodeRepo.delete(node);
			
		return "CGM cleared successfully";	
		} catch (Exception e) {
			return "error while clearing the CGM";	
		}
	}
}
