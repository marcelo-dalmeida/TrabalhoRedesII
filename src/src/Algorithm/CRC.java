package Algorithm;

/**
 * @author Juan Lucas Vieira
 */
public class CRC {

    public static long calculateCRC(String message) {
        byte polynomial = (byte) 0x07; //CRC-8-CCITT based on polynomial x⁸+x²+x+1 -> 1(00000111) -> x^n bit omitted -> 0x07
        long crcResult = crc8(message.getBytes(), polynomial);
        return crcResult;
    }

    private static long crc8(byte[] msg, byte polynomial) {
        long crc = 0;                                                 //Char initial value = 0000 0000
        for (int i = 0; i < msg.length; i++) {
            crc = crc ^ msg[i];                                         //XOR operation between char and polynomial
            for (int j = 0; j < 8; j++) {
                if ((crc & 0x80) != 0) {                     //verify if the first bit of the intermediate crc is 1 (bits of crc AND bits of 10000000)
                    crc = (byte) ((crc << 1) ^ polynomial);
                } else {
                    crc = crc << 1;
                }
            }
        }
        return crc;
    }
}
