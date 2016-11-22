package Algorithm;

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
        return cypher(message, privateKey());
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

    public String decypher(String message)
    {
        return decypher(message, privateKey());
    }

    public String decypher(String message, BigInteger[] publicKey)
    {
        StringBuilder sb = new StringBuilder();

        String[] ms = message.split("\n");

        for (String m: ms)
        {
            sb.append(decrypt(m, publicKey));
        }
        return sb.toString();
    }

    private String encrypt(String message, BigInteger[] key)
    {
        BigInteger modulo = key[0];
        BigInteger exponent = key[1];

        return (new BigInteger(message.getBytes())).modPow(exponent, modulo).toString();
    }

    private String decrypt(String message, BigInteger[] key)
    {
        BigInteger modulo = key[0];
        BigInteger exponent = key[1];

        return new String((new BigInteger(message)).modPow(exponent, modulo).toByteArray());
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

    private BigInteger[] privateKey() {
        return new BigInteger[]{this.n, this.d};
    }
}