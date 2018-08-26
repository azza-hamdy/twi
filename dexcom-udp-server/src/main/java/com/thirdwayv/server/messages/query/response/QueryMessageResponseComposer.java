package com.thirdwayv.server.messages.query.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thirdwayv.persistence.dao.NodeDao;
import com.thirdwayv.persistence.dao.impl.NodeDaoImpl;
import com.thirdwayv.persistence.entity.LogData_CGM;
import com.thirdwayv.persistence.entity.Node;
import com.thirdwayv.persistence.entity.QueryMessage;
import com.thirdwayv.persistence.entity.QueryResponseMessage;
import com.thirdwayv.persistence.entity.QueryResponseMessage.QueryResponseRecord;
import com.thirdwayv.server.udp.PacketStorer;
import com.thirdwayv.server.utils.ByteOperationsHandlers;
import com.thirdwayv.server.utils.NodeState;
import com.thirdwayv.server.utils.QueryMessageService;

public class QueryMessageResponseComposer {

	final int OFFSET_GW_ID = 0;
	final int OFFSET_MSG_TYPE = 1;
	final int OFFSET_FLAGS = 2;
	final int OFFSET_SIZE = 4;
	final int OFFSET_CGM_SEQUENCENUMBER = 6;
	final int OFFSET_RECORD = 8;

	QueryResponseMessage responseMessage;
	QueryMessage queryMessageParsed;
	
	static Random rand = new Random();
	
	private final static Logger logger = LoggerFactory.getLogger(QueryMessageResponseComposer.class);
	
	public QueryMessageResponseComposer(QueryMessage QueryMessageParsed) {
		this.queryMessageParsed = QueryMessageParsed;
	}

	public Byte [] getQueryResponse()
	{
		composeResponseMessageObject();
		
		return composeResponse();
	}
	
	private void composeResponseMessageObject() {
		QueryMessageService messageService  = new QueryMessageService ();

		int totalPayloadSize = 0;

		responseMessage = new QueryResponseMessage();

		responseMessage.setGatewayID('S');
		responseMessage.setMessageType((short) 1);
		responseMessage.setFlags(0);
		
		responseMessage.setMessageSequence (messageService.getNodesCount());

		logger.info("Response Sequence is : "+responseMessage.getMessageSequence());
		
		totalPayloadSize = 2;

		// gateway is asking about some IDs
		if (queryMessageParsed.getCGM_Records().size() > 0) {

			List<QueryResponseRecord> QueryResponseRecords = new ArrayList<QueryResponseRecord>();
			int queryMessageCGMRecordsSize = queryMessageParsed.getCGM_Records().size();

			// looping on gateway requests
			for (int i = 0; i < queryMessageCGMRecordsSize; i++) {

				// get current gateway request
				Node currentGatewayRequest = queryMessageParsed.getCGM_Records().get(i);

				logger.info("Gateway request number ("+i+") Serial Id: " + currentGatewayRequest.getSerialId()+" BLE Address: "+currentGatewayRequest.getBleAddress());
				
				
				
				if(currentGatewayRequest.getSerialId().startsWith("0000"))
				{
					QueryResponseRecord matchedRecord = getMatchedCGMRecords(currentGatewayRequest.getSerialId(),currentGatewayRequest.getBleAddress());
	
					// CGM ID + BLE ID + Count of possible Ids = 13 bytes
					if(totalPayloadSize + 13 + (matchedRecord.getCountOfPossibleIDs() * 4) > 840)
						break;
					
					totalPayloadSize += 13;
					totalPayloadSize += matchedRecord.getCountOfPossibleIDs() * 4;
	
					QueryResponseRecords.add(matchedRecord);
				}
				else
				{
					//Gateway asking about specific ID
					
					NodeDao nodeDao = new NodeDaoImpl();
					Node fetchedNode = messageService.getNodeBySerial(currentGatewayRequest.getSerialId());

					if(fetchedNode!=null)
						nodeDao.updateNodeBLEAddress(fetchedNode.getId(), currentGatewayRequest.getBleAddress());
					
					
					if(fetchedNode != null && fetchedNode.getState() != NodeState.DONE)
					{
						fetchedNode.setBleAddress(currentGatewayRequest.getBleAddress());
	
						System.out.println("### updating node BLE address with value "+currentGatewayRequest.getSerialId() +" BLE add"+currentGatewayRequest.getBleAddress());
						//updating BLE address for node as it was sent by the gateway for this specific id
						
						
						
						if(totalPayloadSize + 13 + 4 > 840)
							break;
						
						totalPayloadSize += 13;
						totalPayloadSize += 4; // Last TX time
						
						System.out.println("### fetching last sync time for node "+fetchedNode.getSerialId());
						long lastSyncTime =  nodeDao.getLastAddedStreamHeaderTime(fetchedNode.getId());
						System.out.println("### last sync time is: "+lastSyncTime +"for "+fetchedNode.getSerialId());
						
						QueryResponseRecord specificCGMRecordResponse = new QueryResponseMessage().new QueryResponseRecord();
					
						specificCGMRecordResponse.setBLEAddress(currentGatewayRequest.getBleAddress());
						specificCGMRecordResponse.setCgmID(currentGatewayRequest.getSerialId());
						specificCGMRecordResponse.setSpecificRecordTxTime(lastSyncTime);
						
						QueryResponseRecords.add(specificCGMRecordResponse);
					}
					else
					{
						totalPayloadSize += 13;
						
						QueryResponseRecord specificCGMRecordResponse = new QueryResponseMessage().new QueryResponseRecord();
						
						specificCGMRecordResponse.setBLEAddress(currentGatewayRequest.getBleAddress());
						specificCGMRecordResponse.setCgmID(currentGatewayRequest.getSerialId());
						specificCGMRecordResponse.setSpecificRecordTxTime(-1);
						
						QueryResponseRecords.add(specificCGMRecordResponse);
					}
					
				}

			}

			logger.info("Response Sequence: "+responseMessage.getMessageSequence());
			responseMessage.setPayloadSize(totalPayloadSize);
			responseMessage.setQueryResponseRecords(QueryResponseRecords);
		}

	}

	public QueryResponseRecord getMatchedCGMRecords(String CGMIDDigits, long BLEAddress) {

		// access database to fetch records with matching 2 digit IDs
		// compose a list of List <QueryResponseRecord>QueryResponseRecords
		// by looping on records then adding them to the list with their count

		// compose QueryResponseRecord
		QueryResponseMessage.QueryResponseRecord record = new QueryResponseMessage().new QueryResponseRecord();
		
		record.setCgmID(CGMIDDigits);
		
		record.setBLEAddress(BLEAddress);

		List<String> DbFetchedCGMId = new ArrayList<String>();
		
		QueryMessageService messageService  = new QueryMessageService ();
		
		List<Node> matchingNodes ;
		
		if(CGMIDDigits.startsWith("0000") == true)
		{
			matchingNodes=  messageService.getNodesByPartialSerial(CGMIDDigits.replace("0000", ""));

			//DbFetchedCGMId should be between 1 to 253 maximum
			// loop on database result
			
			  for (int i = 0; i < matchingNodes.size(); i++) {
				  
				  //String of CGM ID should contain only first 4 charachters
				  DbFetchedCGMId.add(matchingNodes.get(i).getSerialId().substring(0,4));
				  
			  }
			 
			record.setPossibleCGMIDs(DbFetchedCGMId);

		}
		
		
		return record;
	}

	private Byte[] composeResponse() {

		
		// Composing response header
		// read every attribute of the QueryResponseMessage then insert it in
		// its location in the payload.

		List<Byte> composedPayload = new ArrayList<Byte>();

		if (responseMessage != null) {

			// GatewayID
			composedPayload.add((byte) responseMessage.getGatewayID());
			
			// Msg type
			composedPayload.add((byte) responseMessage.getMessageType()); 

			// flags
			byte[] flags = ByteOperationsHandlers.intToByteArray(responseMessage.getFlags(), 2);
			composedPayload = ByteOperationsHandlers.addByteRangeToList(composedPayload, flags); 

			// Size
			byte[] size = ByteOperationsHandlers.intToByteArray(responseMessage.getPayloadSize(), 2);
			composedPayload = ByteOperationsHandlers.addByteRangeToList(composedPayload, size); 

			// Sequence number
			byte[] sequenceNumber = ByteOperationsHandlers.intToByteArray(responseMessage.getMessageSequence(), 2);
			composedPayload = ByteOperationsHandlers.addByteRangeToList(composedPayload, sequenceNumber); 

			
			
			for (int i = 0; i < responseMessage.getQueryResponseRecords().size(); i++) {

				QueryResponseRecord currentQueryResponseRecord = responseMessage.getQueryResponseRecords().get(i);

				// RECORD addition

				// CGMID
				byte[] CGMID = currentQueryResponseRecord.getCgmID().getBytes();
				composedPayload = ByteOperationsHandlers.addByteRangeToList(composedPayload, CGMID); 

				 // BLEID
				byte[] BLEID = ByteOperationsHandlers.longToByteArray(currentQueryResponseRecord.getBLEAddress(), 6);
				composedPayload = ByteOperationsHandlers.addByteRangeToList(composedPayload, BLEID);

				
				short possibleIdCount = currentQueryResponseRecord.getCountOfPossibleIDs(); 
				logger.info("ResponseComposition: Possible IDs count for CGMId:  "+currentQueryResponseRecord.getCgmID() +" BLE address: "+currentQueryResponseRecord.getBLEAddress()+" Count:"+ possibleIdCount);
				
				
				
				//gateway is asking about a specific id so the value should be its
				// last TX time)
				
				if(currentQueryResponseRecord.getCgmID().startsWith("0000") == false)
				{
					if(currentQueryResponseRecord.getSpecificRecordTxTime() != -1)
					{
						possibleIdCount = 255;
					}
					
					if(currentQueryResponseRecord.getSpecificRecordTxTime() == -1) //node not found
					{
						possibleIdCount = 0;
					}
				}
				
				
				byte countOfPossible = (byte) possibleIdCount;
				composedPayload.add(countOfPossible); // Count of possible
				
				
				if (possibleIdCount > 0 && possibleIdCount <= 253) {
					// possible CGMIds
					List<String> possibleCGMIds = currentQueryResponseRecord.getPossibleCGMIDs();

					for (int j = 0; j < possibleCGMIds.size(); j++) {

						byte[] possibleId = possibleCGMIds.get(j).getBytes();
						// PossibleID
						logger.info("+Adding id "+possibleCGMIds.get(j));
						composedPayload = ByteOperationsHandlers.addByteRangeToList(composedPayload, possibleId);
						
					}
				} else if (possibleIdCount == 255) {
					
					//fetch last TX time and add it to					
					// case of = 255 (id passed has all bytes set and gateway is
					// asking about a specific id so the value should be its
					// last TX time)
					
					byte [] lastTXTime = ByteOperationsHandlers.intToByteArray((int)currentQueryResponseRecord.getSpecificRecordTxTime(), 4);
					
					System.out.println("### last tx time in hex is : "+ByteOperationsHandlers.bytesToHexString(lastTXTime));
					
					composedPayload = ByteOperationsHandlers.addByteRangeToList(composedPayload, lastTXTime);
										
				}

			}

			logger.info("Response payload size is: "+composedPayload.size());
			
			
			return composedPayload.toArray(new Byte[composedPayload.size()]);

		} else {
			return null;
		}

	}

}
