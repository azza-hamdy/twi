package com.thirdwayv.server.packet;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thirdwayv.server.utils.AdditionalInfo;

/**
 * Created by FaridaMohamed
 * Edited by AhmedMohsen, Mostafa Elkhateeb
 */
public class CTwiPacket 
{

    private final static Logger logger = LoggerFactory.getLogger(CTwiPacket.class);

//region Variables
    static final String PROTOCOL_MAGIC_PATTERN = "TW";
    static final short PROTOCOL_VERSION = 0;
    static final short HEADER_FLAGS_LEN = 8;
    static final short HEADER_SRC_LEN = 4;
    static final short HEADER_DST_LEN = 4;

    static final short HEADER_ADDRESSES_LEN = HEADER_SRC_LEN + HEADER_DST_LEN;
    static final short HEADER_MAC_LENGTH=8;

    /* ------ Word 0 ------ */
    public boolean isSAS;
    // TW  0x54 0x57
    public boolean isTFS;
    // 3 bits, for now its value is 0
    public short eqos;
    // Short Address Scheme, if true, TWI SN addresses will be used instead of general 128 bit addresses
    public boolean isAcknowledgment;
    // To or From Server, if true, then only one address will be included in the packet
    public boolean isPriority;
    // Extended Quality Of Service, 3 bits, for now it will be only 0 or 1
    public boolean isLastMessage;
    public boolean isGateway;
    public byte seqNumber;
    public byte ackNumber;
    public AdditionalInfo additionalInfo;
    /* ------ Word 1 ------ */
    public int bytesCount;
    // Sequence Number
    // TFS = 0, SAS = 0 [128 bit]
    public byte[] sourceAddress;
    // Acknowledgement Number
    public byte[] destinationAddress;
    // 11 bit, payload Bytes Count, MAX is 2047 bytes

    /* ------ Addresses ------ */
    // TFS = 0, SAS = 1 [32 bit] (Default)
    public byte[] sourceTwiSN;
    public byte[] destinationTwiSN;
    // TFS = 1, SAS = 0 [128 bit]
    public byte[] nodeAddress;
    // TFS = 1, SAS = 1 [32 bit]
    public byte[] nodeTwiSN;
    public byte[] payload;
    String magicPattern;

    /* ------ payload ------ */
    short protocolVersion;
//endregion

    //region Initiazlization
    public CTwiPacket() {
        magicPattern = PROTOCOL_MAGIC_PATTERN;
        protocolVersion = PROTOCOL_VERSION;
        isSAS = true;
        isTFS = false;
        eqos = 0;
        isAcknowledgment = false;
        isPriority = false;
        isLastMessage = false;
        isGateway = false;
        additionalInfo=AdditionalInfo.ClearMessage;
    }

    public CTwiPacket(String payloadString) {
        super();
        try {

            this.payload = payloadString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.bytesCount = (short) payloadString.length();
    }

    public CTwiPacket(byte[] payload) {
        super();
        this.payload = payload;
        this.bytesCount = (short) payload.length;
    }
//endregion

    //region Override String Function
    /**
     * This is a method to return string representation of CTwiPacket object
     * @return string representation of CTwiPacket
     */

    public String toString() {
        try {
            return ("-------------------------\n" +
                    "Magic Pattern: " + magicPattern + " \nVersion: " + protocolVersion + " \nSAS: " + isSAS + " \nTFS: " + isTFS + " \neqos: " + eqos + "\nAck:" + isAcknowledgment + "\nPriority:" + isPriority + "\nLast Message:" + isLastMessage + "\n\n" +
                    "Seq#:" + seqNumber + "\nAck#:" + ackNumber + "\nGatwway:" + isGateway + "\nBytes Count:" + bytesCount + "\n\nSource [HEX]:" + new String(sourceTwiSN) + "\nDestination [HEX]:" + new String(destinationTwiSN) + "\n\npayload [HEX]:" + new String(payload, "UTF-8") + "\n-------------------------\n\n");
        } catch (Exception e) {
            return "";
        }
    }
    //endregion

    //region Converting from bytes to CTwiPacket & Vice Versa
    /**
     * This method is used to convert CTwiPacket into byte array by calling class static method
     * @param packet This is the packet to be converted
     * @return byte [] representation of CTwiPacket
     */
    public  byte[] toBytesEx(CTwiPacket packet)
    {
        return toBytes(packet);
    }

    /**
     * This is static method and it is used to convert CTwiPacket into byte array
     * @param packet This is the packet to be converted
     * @return byte [] representation of CTwiPacket
     */
    public static byte[] toBytes(CTwiPacket packet) {
        byte[] bytes;


        // get message length
        // 8: header
        // 8: source id + destination id, assuming default version (TFS=0, SAS=1)
        // payload padded to first multiples of 4
        if (packet.payload == null)
            bytes = new byte[HEADER_FLAGS_LEN + HEADER_ADDRESSES_LEN];
        else
            bytes = new byte[HEADER_FLAGS_LEN + HEADER_ADDRESSES_LEN + packet.payload.length];


			/* ----- header flags [word 0] ----- */

        // byte 0, byte 1

        try {
            System.arraycopy(packet.magicPattern.getBytes("UTF-8"), 0, bytes, 0, 2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // byte 2
        boolean[] bits2 = new boolean[8];
        bits2[0] = (packet.protocolVersion & 4) != 0;
        bits2[1] = (packet.protocolVersion & 2) != 0;
        bits2[2] = (packet.protocolVersion & 1) != 0;
        bits2[3] = packet.isSAS;
        bits2[4] = packet.isTFS;
        bits2[5] = (packet.eqos & 4) != 0;
        bits2[6] = (packet.eqos & 2) != 0;
        bits2[7] = (packet.eqos & 1) != 0;

        for (int i = 0; i < bits2.length; i++) {
            if (bits2[i])
                bytes[2] |= (byte) (1 << (7 - i));
        }

        // byte 3
        boolean[] bits3 = new boolean[8];
        bits3[0] = packet.isAcknowledgment;
        bits3[1] = packet.isPriority;
        bits3[2] = packet.isLastMessage;
        bits3[3] = packet.isGateway;

        short aadInfoShort= packet.additionalInfo.getCode();
        bits3 [4] = (( aadInfoShort & 0x8) >> 3) ==1 ;
        bits3 [5] = ((aadInfoShort & 0x4) >> 2) ==1;
        bits3 [6] = ((aadInfoShort& 0x2) >> 1)==1;
        bits3 [7] = (aadInfoShort & 0x1) == 1;

        // BAL bits [3:7] are set to 0 for now

        for (int i = 0; i < bits3.length; i++) {
            if (bits3[i])
                bytes[3] |= (byte) (1 << (7 - i));
        }

			/* ----- header flags [word 1] ----- */

        // Seq. number, Ack. number
        bytes[4] = packet.seqNumber;
        bytes[5] = packet.ackNumber;

        // bytes count
        if (packet.payload != null) {

            short ml = (short) (packet.bytesCount);

            bytes[6] = (byte) (ml >> 3);
            bytes[7] = (byte) (ml << 5);


        }


        // reserved bits [3:7] are set to 0

			/* ----- Source & Destination ------ */

        // 32: source id , destination id, assuming default version (TFS=0, SAS=1)
        System.arraycopy(packet.sourceTwiSN, 0, bytes, HEADER_FLAGS_LEN, HEADER_SRC_LEN);
        System.arraycopy(packet.destinationTwiSN, 0, bytes, HEADER_FLAGS_LEN + HEADER_SRC_LEN, HEADER_DST_LEN);

			/* ----- payload ------ */
        if (packet.payload != null) {
            System.arraycopy(packet.payload, 0, bytes, HEADER_FLAGS_LEN + HEADER_SRC_LEN + HEADER_DST_LEN, packet.payload.length);
        }

        return bytes;
    }

    /**
     * This is a static method that is used to construct CTwiPacket object from byte array
     * @param Bytes is the byte array to construct CTWiPacket from
     * @param neglectAdditionalInfoForDecryption is a flag to set the additional info as clear message since the Decryption module changed the 
     * payload to a decrypted one
     * @return CTwiPacket object
     */
    public static CTwiPacket fromBytes(byte[] Bytes, boolean neglectAdditionalInfoForDecryption) {

        byte[] bytes = new byte[Bytes.length];
        int j = 0;
        // Unboxing byte values. (Byte[] to byte[])
        for (Byte b : Bytes)
            bytes[j++] = b.byteValue();


        CTwiPacket packet = new CTwiPacket();

        int MinHeaderLen = HEADER_FLAGS_LEN + HEADER_ADDRESSES_LEN;


        // byte 3

        BitSet byte3 = BitSet.valueOf(new byte[]{bytes[3]});

        packet.isAcknowledgment = byte3.get(7);
        packet.isPriority = byte3.get(6) ;
        packet.isLastMessage = byte3.get(5);
        packet.isGateway = byte3.get(4);


        short addInfoShort=  (short) ( ((byte3.get(3) ? 1 : 0) << 3) | ((byte3.get(2)  ? 1 : 0) << 2) | ((byte3.get(1)  ? 1 : 0) << 1) | (byte3.get(0)  ? 1 : 0));
        

        if(neglectAdditionalInfoForDecryption == true)
        {
        	packet.additionalInfo = AdditionalInfo.ClearMessage;
        }
        else
        {
        	packet.additionalInfo = AdditionalInfo.get(addInfoShort);
        }
        
        
        if(addInfoShort >  AdditionalInfo.PairingMessage.getCode())
        {
            return null;
        }
        
        
        if( packet.additionalInfo == AdditionalInfo.EncryptedMessage )
        {
            MinHeaderLen = MinHeaderLen + HEADER_MAC_LENGTH;
        }

        // validate bytes length
        if (bytes.length < MinHeaderLen) {


            logger.debug("Invalid message length, minimum valid length is " + MinHeaderLen + " bytes");
            return null;
            //throw new Exception (String.Format ("Invalid message length, minimum valid length is {0} bytes", MinHeaderLen));
            // Min length is 16 bytes (8 header, 8 source & destination 32-bit node address [TFS=1, SAS=1]), assuming no payload
        }

			/* ----- header flags [word 0] ----- */

        // byte 0, byte 1
        try {
            packet.magicPattern = new String(bytes, "UTF-8").substring(0, 2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        //byte 2

        BitSet byte2 = BitSet.valueOf(new byte[]{bytes[2]});

        packet.protocolVersion = (short) ((byte2.get(7) ? 1 : 0) << 2 | (byte2.get(6) ? 1 : 0) << 1 | (byte2.get(5)  ? 1 : 0));
        packet.isSAS = byte2.get(4);
        packet.isTFS = byte2.get(3);
        packet.eqos = (short) ((byte2.get(2)? 1 : 0) << 2 | (byte2.get(1) ? 1 : 0) << 1 | (byte2.get(0)  ? 1 : 0));




        // validate protocol signature
        if (!packet.magicPattern.toString().equals(PROTOCOL_MAGIC_PATTERN)) {

            logger.debug( "Invalid Magic Pattern " + MinHeaderLen);
            return null;
            //throw new Exception ("Invalid Magic Pattern");
        }

        // validate protocol version
        if (packet.protocolVersion != PROTOCOL_VERSION) {
            logger.debug("Invalid Protocol Version " + MinHeaderLen);
            return null;
            //throw new Exception ("Invalid Protocol Version");
        }

			/* ----- header flags [word 1] ----- */

        // Seq. number, Ack. number
        packet.seqNumber = bytes[4];
        packet.ackNumber = bytes[5];

        // bytes count

        packet.bytesCount = ((((int) (bytes[6] << 8)) | (bytes[7] & 0xff)) >> 5) & (0x07FF);

        // validate Remaining Length
        if(packet.additionalInfo == AdditionalInfo.EncryptedMessage)
        {
            if (packet.bytesCount > (bytes.length - HEADER_FLAGS_LEN - HEADER_SRC_LEN - HEADER_DST_LEN - HEADER_MAC_LENGTH)) {

            	logger.debug("CTwiPacket: Invalid encrypted Remaining Length " + MinHeaderLen);
                return null;
            }
        }

        else
        {
            if (packet.bytesCount > (bytes.length - HEADER_FLAGS_LEN - HEADER_SRC_LEN - HEADER_DST_LEN )) {

                logger.debug("CTwiPacket: Invalid plain Remaining Length " + MinHeaderLen);
            return null;
        }

        }


			/* ----- Source & Destination ------ */

        // 32: source id , destination id, assuming default version (TFS=0, SAS=1)

        packet.sourceTwiSN = new byte[HEADER_SRC_LEN];
        packet.destinationTwiSN = new byte[HEADER_DST_LEN];
        System.arraycopy(bytes, HEADER_FLAGS_LEN, packet.sourceTwiSN, 0, HEADER_SRC_LEN);
        System.arraycopy(bytes, HEADER_FLAGS_LEN + HEADER_SRC_LEN, packet.destinationTwiSN, 0, HEADER_DST_LEN);

			/* ----- payload ------ */
        if(packet.additionalInfo == AdditionalInfo.EncryptedMessage) {
            packet.payload = new byte[packet.bytesCount +HEADER_MAC_LENGTH ];
            System.arraycopy(bytes, HEADER_FLAGS_LEN + HEADER_ADDRESSES_LEN, packet.payload, 0, packet.bytesCount+ HEADER_MAC_LENGTH);
        }
        else {
            packet.payload = new byte[packet.bytesCount];
            System.arraycopy(bytes, HEADER_FLAGS_LEN + HEADER_ADDRESSES_LEN, packet.payload, 0, packet.bytesCount);

        }
        return packet;
    }

    /**
     * This is a static method that is used to construct CTwiPacket object from byte array
     * It does not take into consideration the Additional Info packet field and does not adjust packet
     * for encryption, used mainly to get packet representation from bytes to be used as input for encryption.
     * @param Bytes is the byte array to construct CTWiPacket from
     * @return CTwiPacket object
     */

    public static CTwiPacket preparePacket(Byte[] Bytes) {

        byte[] bytes = new byte[Bytes.length];
        int j = 0;
        // Unboxing byte values. (Byte[] to byte[])
        for (Byte b : Bytes)
            bytes[j++] = b.byteValue();

        // validate bytes length
        int MinHeaderLen = HEADER_FLAGS_LEN + HEADER_ADDRESSES_LEN;
        if (bytes.length < MinHeaderLen) {


            logger.debug("Invalid message length, minimum valid length is " + MinHeaderLen + " bytes");
            return null;
            //throw new Exception (String.Format ("Invalid message length, minimum valid length is {0} bytes", MinHeaderLen));
            // Min length is 16 bytes (8 header, 8 source & destination 32-bit node address [TFS=1, SAS=1]), assuming no payload
        }


        CTwiPacket packet = new CTwiPacket();

			/* ----- header flags [word 0] ----- */

        // byte 0, byte 1
        try {
            packet.magicPattern = new String(bytes, "UTF-8").substring(0, 2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        //byte 2

        BitSet byte2 = BitSet.valueOf(new byte[]{bytes[2]});

        packet.protocolVersion = (short) ((byte2.get(7) ? 1 : 0) << 2 | (byte2.get(6) ? 1 : 0) << 1 | (byte2.get(5)  ? 1 : 0));
        packet.isSAS = byte2.get(4);
        packet.isTFS = byte2.get(3);
        packet.eqos = (short) ((byte2.get(2)? 1 : 0) << 2 | (byte2.get(1) ? 1 : 0) << 1 | (byte2.get(0)  ? 1 : 0));

        // byte 3

        BitSet byte3 = BitSet.valueOf(new byte[]{bytes[3]});

        packet.isAcknowledgment = byte3.get(7);
        packet.isPriority = byte3.get(6) ;
        packet.isLastMessage = byte3.get(5);
        packet.isGateway = byte3.get(4);


        short addInfoShort=  (short) ( ((byte3.get(3) ? 1 : 0) << 3) | ((byte3.get(2)  ? 1 : 0) << 2) | ((byte3.get(1)  ? 1 : 0) << 1) | (byte3.get(0)  ? 1 : 0));
        packet.additionalInfo = AdditionalInfo.get(addInfoShort);

        // byte3[3:7] : (BAL) is ignored for now


        // validate protocol signature
        if (!packet.magicPattern.toString().equals(PROTOCOL_MAGIC_PATTERN)) {

            logger.debug( "Invalid Magic Pattern " + MinHeaderLen);
            return null;
            //throw new Exception ("Invalid Magic Pattern");
        }

        // validate protocol version
        if (packet.protocolVersion != PROTOCOL_VERSION) {
            logger.debug("Invalid Protocol Version " + MinHeaderLen);
            return null;
            //throw new Exception ("Invalid Protocol Version");
        }

			/* ----- header flags [word 1] ----- */

        // Seq. number, Ack. number
        packet.seqNumber = bytes[4];
        packet.ackNumber = bytes[5];

        // bytes count

        packet.bytesCount = ((((int) (bytes[6] << 8)) | (bytes[7] & 0xff)) >> 5) & (0x07FF);

        // validate Remaining Length
        // MEM: I think != is better than > here
        if (packet.bytesCount > (bytes.length - HEADER_FLAGS_LEN - HEADER_SRC_LEN - HEADER_DST_LEN)) {

        	logger.debug("CTwiPacket: Invalid Remaining Length " + MinHeaderLen);
            return null;
            //throw new Exception ("Invalid Remaining Length");
        }

        // byte3[3:7] : (Reserved) is ignored for now

			/* ----- Source & Destination ------ */

        // 32: source id , destination id, assuming default version (TFS=0, SAS=1)

        packet.sourceTwiSN = new byte[HEADER_SRC_LEN];
        packet.destinationTwiSN = new byte[HEADER_DST_LEN];
        System.arraycopy(bytes, HEADER_FLAGS_LEN, packet.sourceTwiSN, 0, HEADER_SRC_LEN);
        System.arraycopy(bytes, HEADER_FLAGS_LEN + HEADER_SRC_LEN, packet.destinationTwiSN, 0, HEADER_DST_LEN);

			/* ----- payload ------ */

        packet.payload = new byte[packet.bytesCount];
        System.arraycopy(bytes, HEADER_FLAGS_LEN + HEADER_ADDRESSES_LEN, packet.payload, 0, packet.bytesCount);


        return packet;
    }

    //endregion

    //region Create Ack CTwiPacket
    /**
     * This method is used to construct Ack object as CTwiPacket from original CTWiPacket object
     * @param originalPacket This is the original packet to construct Ack for
     * @param msgPayload This is the response payload
     * @return return the CTwipacket object that represents the Ack object
     */
    public CTwiPacket createACK(CTwiPacket originalPacket,byte[] msgPayload) {
        CTwiPacket packet = new CTwiPacket();

        // header flags [word 0]
        if(originalPacket.eqos == 1)
        	packet.isAcknowledgment = true;
        packet.eqos = 0;
        
        // assuming default version (TFS=0, SAS=1)
        packet.sourceTwiSN = originalPacket.destinationTwiSN;
        packet.destinationTwiSN = originalPacket.sourceTwiSN;

        packet.isLastMessage = originalPacket.isLastMessage;
        
        // seq number, ack number, remaining length (payload len) [word 9]
        packet.seqNumber = 0; // this seq. number will be assigned when sending
        if(packet.isAcknowledgment)
        	packet.ackNumber = ((byte) (originalPacket.seqNumber + 1));

        if(msgPayload == null)
        {
        	packet.bytesCount = 0;
        	packet.payload = null;
        }
        else
        {
        	packet.bytesCount = msgPayload.length;
        	packet.payload = msgPayload;
        }       

        return packet;
    }
//endregion

    //region Setters & Getters
    /**
     * This method is used to return payload of CTwiPacket object
     * @return byte array that represent the payload of CTwiPacket object
     */
    public byte[] getPayload() {
        return payload;
    }

    /**
     * This method is used to return payload of CTwiPacket object as string
     * @return string that represent the payload of CTwiPacket object
     */
    public String getPayloadAsString() {
        try {
            if (payload != null) {
                return new String(payload, "UTF-8");
            } else {
                return "";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }
//endregion

}
