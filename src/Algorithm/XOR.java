package Algorithm;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Marcelo d'Almeida on 11/19/2016.
 */
public class XOR {

    private static final long BITS_32 = (long)Math.pow(2, 32);

    public static String generateKey()
    {
        Random random = new Random();
        long key = Math.abs(random.nextLong()) % BITS_32;
        return String.format("%032d", new BigInteger(Long.toBinaryString(key)));
    }

    public static String cypher(String s, String key)
    {
        return base64Encode(xor(s.getBytes(), key.getBytes()));
    }

    public static String decypher(String s, String key)
    {
        return new String(xor(base64Decode(s), key.getBytes()));
    }

    private static byte[] xor(byte[] m, byte[] key)
    {
        byte[] out = new byte[m.length];
        for (int i = 0; i < m.length; i++) {
            out[i] = (byte) (m[i] ^ key[i%key.length]);
        }
        return out;
    }

    private static byte[] base64Decode(String s)
    {
        try
        {
            BASE64Decoder decoder = new BASE64Decoder();
            return decoder.decodeBuffer(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String base64Encode(byte[] bytes)
    {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(bytes).replaceAll("\\s", "");
    }

    private static String binaryStringToString(String binaryString)
    {
        StringBuilder sb = new StringBuilder();
        int size = 8;
        String s;

        for (int start = 0; start < binaryString.length(); start += size) {
            s = binaryString.substring(start, start + size);
            sb.append((char)Long.parseLong(s, 2));
        }
        return sb.toString();
    }
}
