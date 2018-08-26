package com.thirdwayv.ap.server.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.thirdwayv.ap.server.model.Log;

@Repository
public interface LogRepository extends  JpaRepository<Log, Long> {
	
	@Query(value = "SELECT log FROM Log  log WHERE log.node.id in (SELECT node.id FROM Node node where node.id = :nodeId) order by log.logTime asc")
	public List<Log> findAllByDeviceId(@Param("nodeId")long nodeId);
//	
//	@Modifying
//	@Query(value = "DELETE FROM LogData log WHERE log.logMessage.id in (SELECT message.id FROM LogMessage message where message.ctrDevice.id = :deviceId)")
//	public void deleteAllLogsByDeviceId(@Param("deviceId")long deviceId);


}
