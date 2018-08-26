package com.thirdwayv.server.messages.query.parser;

import com.thirdwayv.persistence.entity.Node;
import com.thirdwayv.persistence.entity.QueryMessage;
import com.thirdwayv.server.utils.ByteReader;

public class QueryMessageParser {

	final int OFFSET_GW_ID = 0;
	final int OFFSET_MSG_TYPE = 1;
	final int OFFSET_FLAGS = 2;
	final int OFFSET_SIZE = 4;
	final int OFFSET_RECORD = 6;

	final int CGM_ID_LENGTH = 6;
	final int BLE_ID_LENGTH = 6;
	final int RECORD_SIZE = CGM_ID_LENGTH + BLE_ID_LENGTH;

	private byte[] payload;
	private QueryMessage QueryMessageParsed;

	public QueryMessageParser(byte[] payload) {

		this.payload = payload;
		QueryMessageParsed = new QueryMessage();
	}

	public QueryMessage GetParsedQueryMessage() {

		ParseQueryMessageHeader();
		ParseQueryMessageRecords();

		return QueryMessageParsed;
	}

	private void ParseQueryMessageHeader() {

		try {
			QueryMessageParsed.setGatewayID(ByteReader.getString(payload, OFFSET_GW_ID, 1).charAt(0));
			QueryMessageParsed.setMessageType(ByteReader.getUnsignedByte8(payload, OFFSET_MSG_TYPE));
			QueryMessageParsed.setFlags(ByteReader.getUnsignedShort(payload, OFFSET_FLAGS, false));
			QueryMessageParsed.setSize(ByteReader.getUnsignedShort(payload, OFFSET_SIZE, false));
		} catch (Exception e) {
			System.out.println("error in parsing query message header " + e.getMessage());
		}

	}

	private void ParseQueryMessageRecords() {

		try {

			if (QueryMessageParsed.getSize() > 0) {// records exist

				int recordsCount = QueryMessageParsed.getSize() / RECORD_SIZE;
				int currentRecordOffset = OFFSET_RECORD;

				String serialId = null;
				byte[] ascii = new byte[6];
				
				for (int i = 0; i < recordsCount; i++) {

					Node cgmRecordData = new Node();

					System.arraycopy(payload, currentRecordOffset, ascii, 0, CGM_ID_LENGTH);
					serialId =ByteReader.getString (ascii);
					cgmRecordData.setSerialId(serialId);
					currentRecordOffset += CGM_ID_LENGTH;

					cgmRecordData.setBleAddress(ByteReader.getLong48(payload, currentRecordOffset,false));
					currentRecordOffset += BLE_ID_LENGTH;

					QueryMessageParsed.getCGM_Records().add(cgmRecordData);

				}

			}
		} catch (Exception e) {
			System.out.println("error in parsing query message records " + e.getMessage());
		}

	}

}
