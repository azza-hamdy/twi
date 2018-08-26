package com.thirdwayv.ap.server.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thirdwayv.ap.server.dto.DeviceRange;
import com.thirdwayv.ap.server.dto.NodeLogs;
import com.thirdwayv.ap.server.model.Controller;
import com.thirdwayv.ap.server.model.Log;
import com.thirdwayv.ap.server.model.Node;
import com.thirdwayv.ap.server.model.Stream;
import com.thirdwayv.ap.server.service.LogService;
import com.thirdwayv.ap.server.service.NodeService;

import io.jsonwebtoken.Claims;


@RestController
@CrossOrigin
@RequestMapping(value = "/api/device")
public class DeviceController {
	
    	private static final Logger LOGGER = LoggerFactory.getLogger(DeviceController.class);

	   @Autowired
	   NodeService ctrDeviceService;
	   
	   @Autowired 
	   LogService logService;

	    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<Page<Node>> findAllDevices(Pageable pageable,String searchText, HttpServletRequest req) {
	    	//to get the user 
//	    	Claims claims = (Claims) req.getAttribute("claims");
	    	if(pageable!=null){
	    		LOGGER.info("get page number="+pageable.getPageNumber()+", with searchTxt="+searchText);
	    	}
	    	searchText = (searchText == null)?"":searchText;
	    	Page<Node> page = ctrDeviceService.findDevicesByPage(pageable,searchText);
	    	ResponseEntity<Page<Node>> response = new ResponseEntity<Page<Node>>(page, HttpStatus.OK);
	        return response;
	    }
	    
	    @RequestMapping(value = "{deviceId}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	    public NodeLogs getDeviceLogs(@PathVariable long deviceId){
	    	NodeLogs nodeLogs = new NodeLogs();
	    	List<Stream> egvLogs = ctrDeviceService.getAllLogs(deviceId);
	    	List<Log> bgData = logService.getLogsByNodeId(deviceId);
	    	nodeLogs.setDeviceId(deviceId);
	    	nodeLogs.setDeviceEGVLogs(egvLogs);
	    	nodeLogs.setDeviceBGLogs(bgData);

	    	return nodeLogs;
	    	
	    }
	    
	  
	    @RequestMapping(value = "/add_range",method = RequestMethod.POST)
	    public int addNewRange(@RequestBody DeviceRange range){
	    	if(range != null){
	    		LOGGER.info("Device Range : "+range);
	    	}
	    	int count = 0;
			if (isValidRange(range)) {
				String cgmTxId = range.getStartRange();
				for (int i = 0; i < range.getSize(); i++) {
					count += ctrDeviceService.createNode(cgmTxId) ? 1 : 0;
					cgmTxId = getNextAlphanumericCounter(cgmTxId);
				}
			}
			if(isValidListOfDevices(range)){
				StringTokenizer stringTokenizer = new StringTokenizer(range.getListOfDevices(), ",");
				while (stringTokenizer.hasMoreTokens()) {
					String cgmTxId = stringTokenizer.nextToken();
					count += ctrDeviceService.createNode(cgmTxId) ? 1 : 0;
				}
			}
	    	return count;
	    }
	    
	    @RequestMapping(value = "/remove_range",method = RequestMethod.POST)
	    public int removeRange(@RequestBody DeviceRange range){
	    	if(range != null){
	    		LOGGER.info("Device Range : "+range);
	    	}
	    	int count = 0;
			if (isValidRange(range)) {
				String cgmTxId = range.getStartRange();
				for (int i = 0; i < range.getSize(); i++) {
					count += ctrDeviceService.removeNode(cgmTxId) ? 1 : 0;
					cgmTxId = getNextAlphanumericCounter(cgmTxId);
				}
			}
			if(isValidListOfDevices(range)){
				StringTokenizer stringTokenizer = new StringTokenizer(range.getListOfDevices(), ",");
				while (stringTokenizer.hasMoreTokens()) {
					String cgmTxId = stringTokenizer.nextToken();
					count += ctrDeviceService.removeNode(cgmTxId) ? 1 : 0;
				}
			}
	    	return count;
	    }
	    
	    private boolean isValidRange(DeviceRange range) {
	    	return range!= null && (range.getStartRange() != null && !range.getStartRange().isEmpty() && range.getSize() > 0);
		}
	    
	    private boolean isValidListOfDevices(DeviceRange range){
	    	return range!= null && range.getListOfDevices() != null && !range.getListOfDevices().isEmpty();
	    }

	    private static String getNextAlphanumericCounter(String current){
			String alpha = "0123456789ABCDEFGHJKLMNPQRSTUWXYZ";
			StringBuilder stringBuilder = new StringBuilder();
			int carry = 1;
			for (int i = current.length() - 1; i >= 0 ; i--) {
				int indexOfNextChar = (alpha.indexOf(current.charAt(i))+carry) % alpha.length();
				stringBuilder.append(alpha.charAt(indexOfNextChar));
				carry = (alpha.indexOf(current.charAt(i))+carry) / alpha.length();
			}		
			if(carry == 1)
				stringBuilder.append('1');
			return stringBuilder.reverse().toString();
		}
}
