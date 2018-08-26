package com.thirdwayv.ap.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.thirdwayv.ap.server.model.Stream;

@Repository
public interface StreamRepository extends JpaRepository<Stream, Long>  {
	
	@Query(value = "SELECT stream FROM Stream  stream WHERE stream.log.id in (SELECT log.id FROM Log log where log.node.id = :nodeId) order by stream.logTime asc")
	public List<Stream> findAllByNodeId(@Param("nodeId")long nodeId);
	
//	@Modifying
//	@Query(value = "DELETE FROM Stream stream where stream.log.id = :deviceId")
//	public void deleteAllMessagesByDeviceId(@Param("deviceId")long deviceId);
}
