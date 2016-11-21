/**
 * Created by Marcelo d'Almeida on 11/20/2016.
 */
import java.math.BigInteger;
import java.security.SecureRandom;


public class RSA {

    private BigInteger n;
    private BigInteger d;
    private BigInteger e;

    private int bitLength = 1024;

    public RSA(int bits)
    {
        this.bitLength = bits;
        generateKeys();
    }

    public String cypher(String message)
    {
        return cypher(message, getPublicKey());
    }

    public String cypher(String message, BigInteger[] publicKey)
    {
        StringBuilder sb = new StringBuilder();
        String s;
        int size = bitLength/10;
        int end;

        for (int start = 0; start < message.length(); start += size)
        {
            if (start + size >= message.length())
            {
                end = message.length();
            } else {
                end = start + size;
            }
            s = message.substring(start, end);
            sb.append(encrypt(s, publicKey));
            sb.append("\n");
        }
        return sb.toString();
    }

    private String encrypt(String message, BigInteger[] publicKey)
    {
        BigInteger n = publicKey[0];
        BigInteger e = publicKey[1];
        return (new BigInteger(message.getBytes())).modPow(e, n).toString();
    }

    public String decypher(String message)
    {
        StringBuilder sb = new StringBuilder();

        String[] ms = message.split("\n");

        for (String m: ms)
        {
            sb.append(decrypt(m));
        }
        return sb.toString();
    }

    private String decrypt(String message) {
        return new String((new BigInteger(message)).modPow(d, n).toByteArray());
    }

    public void generateKeys()
    {
        SecureRandom r = new SecureRandom();

        BigInteger p = new BigInteger(this.bitLength / 2, 100, r);
        BigInteger q = new BigInteger(this.bitLength / 2, 100, r);
        BigInteger z = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        this.n = p.multiply(q);
        this.e = new BigInteger("3");

        while (z.gcd(e).intValue() > 1)
        {
            e = e.add(new BigInteger("2"));
        }

        this.d = e.modInverse(z);
    }

    public BigInteger[] getPublicKey() {
        return new BigInteger[]{this.n, this.e};
    }
}
