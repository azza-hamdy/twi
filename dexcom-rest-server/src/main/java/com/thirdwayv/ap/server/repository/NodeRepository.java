package com.thirdwayv.ap.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.thirdwayv.ap.server.model.Node;

@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {
	@Query(value = "SELECT device FROM Node device where device.deleted = false and device.serialId LIKE %:searchText% ORDER BY (case when device.lastActivity IS NULL THEN 0 end) DESC, device.lastActivity DESC")
	public Page<Node> findNodesSortedByLastActivity(Pageable pageable,@Param("searchText")String searchText);
	
	@Modifying
	@Query("update Node device set device.lastActivity = null where device.serialId = :serialId")
	public void setCtrDeviceBySerialId(@Param("serialId")String serialId);
	
	public Node findBySerialId(String serialId);	
	
	@Modifying
	@Query("update Node device set device.deleted = true , device.state = '0' where device.serialId = :serialId")
	public void deleteBySerialId(@Param("serialId")String serialId);
}
