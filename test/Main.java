package Test;

import Algorithm.RSA;
import Algorithm.XOR;
import java.math.BigInteger;

/**
 * Created by Marcelo d'Almeida on 11/19/2016.
 */

public class Main
{
    public static void main(String[] args)
    {
        String message = "Niterói Cidade Sorriso. Lar de grandes mentes.\n";

        //message = "12 sd sd3bitLength/2bitLength/2bitLength/2bitLength/2bitLength/2bitLength/2456789012345678901234567890123456789012345678901234567890123456789012z vznzz3456789012345vzxv zxvz mxvnz mxvnzx mvzvzxvxzv678901234sc scn scns cnsm csmc nsnmc 56789012345678901234567890dgs s z sd a3456789012345678901234567890123456789012345678901234567890123456789012z vznzz3456789012345vzxv zxvz mxvnz mxvnzx mvzvzxvxzv678901234sc scn scns cnsm csmc nsnmc 56789012345678901234567890dgs s z sd a3456789012345678901234567890123456789012345678901234567890123456789012z vznzz3456789012345vzxv zxvz mxvnz mxvnzx mvzvzxvxzv678901234sc scn scns cnsm csmc nsnmc 56789012345678901234567890dgs s z sd a";

        String key = XOR.generateKey();
        String encoded = XOR.cypher(message, key);
        String decoded = XOR.decypher(encoded, key);

        System.out.println("XOR");
        System.out.println(message);
        System.out.println(encoded);
        System.out.println(decoded);
        System.out.println(key);
        System.out.println();


        RSA rsa = new RSA(1024);
        encoded = rsa.cypher(message);
        decoded = rsa.decypher(encoded);
        BigInteger[] pk = rsa.getPublicKey();
        System.out.println("PK: "+pk[0]+" //// "+pk[1]);
        

        System.out.println("RSA");
        System.out.println(message);
        System.out.println(encoded);
        System.out.println(decoded);
    }
}