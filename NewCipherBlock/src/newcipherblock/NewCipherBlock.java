/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcipherblock;

import static java.lang.Math.abs;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Scanner;


/**
 *
 * @author HP
 */
public class NewCipherBlock {
    /*CLASS ATTRIBUTES*/
    private String plaintext;
    private String key;
    private String ciphertext;
    private int blockLen;       //size of block, number of char in a block
    
    public NewCipherBlock(){
        plaintext = "";
        key = "";
        ciphertext = "";
        blockLen = key.length();
    }
    
    /*Getter & Setter*/
    public String getPlaintext(){
        return this.plaintext;
    }
    
    public String getKey(){
        return this.key;
    }
    
    public String getCiphertext(){
        return this.ciphertext;
    }
    
    public void setPlaintext(String s){
        this.plaintext = s;
    }
    
    public void setKey(String s){
        this.key = s;
        this.blockLen = s.length();
    }
    
    public void setCiphertext(String s){
        this.ciphertext = s;
    }
    
    /*Parsers*/
    public int[] charToHexa(char c){
        int[] hex = new int[2];
        String h = Integer.toHexString((int) c);
        if (h.length() == 1){
            h = "0" + h;
        }
        hex[0] = Integer.parseInt(h.substring(0,1), 16);
        hex[1] = Integer.parseInt(h.substring(1), 16);
        return hex;
    }
    
    public char hexaToChar(int[] i){
        char c;
        String h = "" + Integer.toHexString(i[0]) + Integer.toHexString(i[1]);
        c = (char) Integer.parseInt(h, 16);
        return c;
    }
        
    /*Inner function*/
    public int encInnerFunction(int r, int k){
        return (r+k)%16;
    }
    
    public int decInnerFunction(int r, int k){
        return (r+16-k)%16;
    }
    
    /*Feistel*/
    public int[] encFeistel(int[] lr, int k){
        int[] result = new int[2];
        result[0] = lr[1];
        result[1] = encInnerFunction(lr[1], k);
        result[1] = result[1] ^ lr[0];
        return result;
    }
    
    public int[] decFeistel(int[] lr, int k){
        int[] result = new int[2];
        result[1] = lr[0];
        result[0] = encInnerFunction(lr[0], k);
        result[0] = result[0] ^ lr[1];
        return result;
    }
    
    /*Enkripsi*/
    public String encryptor(String plainBlock){
        int n = blockLen;
        String cipher = "";
        int[] innerKey = new int[n];
        for (int i=0; i<n; i++){
            innerKey[i] = charToHexa(key.charAt(i/2))[i%2];
        }
        for (char c: plainBlock.toCharArray()){
            int[] lr = charToHexa(c);
            for (int i=0; i<n; i++){
                lr = encFeistel(lr, innerKey[i]);
                innerKey[i] = lr[0];
            }
            cipher += hexaToChar(lr);
        }
        return cipher;
    }
    
    /*Dekripsi*/
    public String decryptor(String cipherBlock){
        int n = blockLen;     //number of iterations
        String plain = "";
        int[] innerKey = new int[n];
        for (int i=0; i<n; i++){
            innerKey[n-i-1] = charToHexa(key.charAt(i/2))[i%2];
        }
        for (char c: cipherBlock.toCharArray()){
            int[] lr = charToHexa(c);
            for (int i=0; i<n; i++){
                int tmp = lr[0];
                lr = decFeistel(lr, innerKey[i]);
                innerKey[i] = tmp;
            }
            plain += hexaToChar(lr);
        }
        return plain;
    }
    
    /* ECB Mode */
    public void encECB(){
        //Insert text addtion into plaintext
        int len = plaintext.length();
        if (len % blockLen != 0){
            String str = new Character((char) Integer.parseInt("11111111", 2)).toString();
            for (int i=len%blockLen; i<blockLen; i++){
                plaintext += str;
                len++;
            }
        }
        //Main process
        for (int i=0; i<len; i+=blockLen){
            String plainBlock = plaintext.substring(i, i+blockLen);
            ciphertext += encryptor(plainBlock);
        }
    }
    
    public void decECB(){
        //Insert text addtion into plaintext
        int len = ciphertext.length();
        if (len % blockLen != 0){
            String str = new Character((char) Integer.parseInt("11111111", 2)).toString();
            for (int i=len%blockLen; i<blockLen; i++){
                ciphertext += str;
                len++;
            }
        }
        //Main process
        for (int i=0; i<len; i+=blockLen){
            String cipherBlock = ciphertext.substring(i, i+blockLen);
            plaintext += decryptor(cipherBlock);
        }
    }
    
    /* CBC Mode*/
    public void encCBC(){
        //Initialize the IV (Initialization Vector)
        int seed = 0;
        for (char c: key.toCharArray()){
            seed += (int) c;
        }
        Random rand = new Random(seed);
        String iv = "";
        for (int i=0; i<blockLen; i++){
            int x = abs(rand.nextInt()) % 256;
            iv += (char) x;
        }
        //Insert text addtion into plaintext
        int len = plaintext.length();
        if (len % blockLen != 0){
            String str = new Character((char) Integer.parseInt("11111111", 2)).toString();
            for (int i=len%blockLen; i<blockLen; i++){
                plaintext += str;
                len++;
            }
        }
        //Main process
        for (int i=0; i<len; i+=blockLen){
            String plainBlock = plaintext.substring(i, i+blockLen);
            StringBuilder sb = new StringBuilder();
            for (int j=0; j<blockLen; j++){
                sb.append((char) (plainBlock.charAt(j) ^ iv.charAt(j)));
            }
            iv = encryptor(sb.toString());
            ciphertext += iv;
        }
    }
    
    public void decCBC(){
        //Initialize the IV (Initialization Vector)
        int seed = 0;
        for (char c: key.toCharArray()){
            seed += (int) c;
        }
        Random rand = new Random(seed);
        String iv = "";
        for (int i=0; i<blockLen; i++){
            int x = abs(rand.nextInt()) % 256;
            iv += (char) x;
        }
        //Main process
        int len = ciphertext.length();
        System.out.println(len);
        for (int i=0; i<len; i+=blockLen){
            String cipherBlock = ciphertext.substring(i, i+blockLen);
            String cipherBlockNew = decryptor(cipherBlock);
            StringBuilder sb = new StringBuilder();
            for (int j=0; j<blockLen; j++){
                sb.append((char) (cipherBlockNew.charAt(j) ^ iv.charAt(j)));
            }
            iv = cipherBlock;
            plaintext += sb.toString();
        }
    }
    
    /* CFB Mode */
    public void encCFB(){
        //Initialize the IV (Initialization Vector) or shift register or queue, with the size of blockLen
        int seed = 0;
        for (char c: key.toCharArray()){
            seed += (int) c;
        }
        Random rand = new Random(seed);
        String iv = "";
        for (int i=0; i<blockLen; i++){
            int x = abs(rand.nextInt()) % 256;
            iv += (char) x;
        }
        //Main process
        //Because we use CFB 8-bit, the operation takes in form of char
        int len = plaintext.length();
        for (int i=0; i<len; i++){
            char p = plaintext.charAt(i);       //get the 8 bits of plaintext
            char msb = encryptor(iv.substring(0, blockLen)).charAt(0);      //get the most left 8 bits of encrypted shift register
            char c = (char) (p ^ msb);          //these are XOR'ed
            iv = iv.substring(1) + c;           //append the 8 bits ciphertext into the shift register, then shift 8 bits of the result to the left
            ciphertext += c;
        }        
    }
    
    public void decCFB(){
        //Initialize the IV (Initialization Vector) or shift register or queue, with the size of blockLen
        int seed = 0;
        for (char c: key.toCharArray()){
            seed += (int) c;
        }
        Random rand = new Random(seed);
        String iv = "";
        for (int i=0; i<blockLen; i++){
            int x = abs(rand.nextInt()) % 256;
            iv += (char) x;
        }
        //Main process
        //Because we use CFB 8-bit, the operation takes in form of char
        int len = ciphertext.length();
        for (int i=0; i<len; i++){
            char c = ciphertext.charAt(i);       //get the 8 bits of ciphertext
            char msb = encryptor(iv.substring(0, blockLen)).charAt(0);      //get the most left 8 bits of encrypted shift register
            char p = (char) (c ^ msb);          //these are XOR'ed
            iv = iv.substring(1) + c;           //append the 8 bits ciphertext into the shift register, then shift 8 bits of the result to the left
            plaintext += p;
        }        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        NewCipherBlock k = new NewCipherBlock();
        System.out.println("What mode will you use?");
        System.out.println("1. ECB");
        System.out.println("2. CBC");
        System.out.println("3. CFB 8-bit");
        System.out.print("Enter your choice: "); int mode = userInput.nextInt();
        System.out.println("Enter plaintext:");
        String plain = userInput.next();
        System.out.println("Enter key:");
        String key = userInput.next();
//        k.setPlaintext("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
//        k.setKey("abcdefgh");
//        k.setCiphertext("");
//        k.encCBC();
//        String cipher = k.getCiphertext();
//        k.setPlaintext("");
//        k.setCiphertext(cipher);
//        k.setKey("abcdefgh");
//        k.decCCB();
//        String plain = k.getPlaintext();
        k.setPlaintext(plain);
        k.setKey(key);
        String cipher = "";
        switch(mode){
            case 1: k.encECB(); 
                    cipher = k.getCiphertext();
                    k.setPlaintext("");
                    k.setCiphertext(cipher);
                    k.decECB();
                    plain = k.getPlaintext();
                    break;
            case 2: k.encCBC(); 
                    cipher = k.getCiphertext();
                    k.setPlaintext("");
                    k.setCiphertext(cipher);
                    k.decCBC();
                    plain = k.getPlaintext();
                    break;
            case 3: k.encCFB(); 
                    cipher = k.getCiphertext();
                    k.setPlaintext("");
                    k.setCiphertext(cipher);
                    k.decCFB();
                    plain = k.getPlaintext();
                    break;
            default: break;
        }
        System.out.println("ciphertext =");
        System.out.println(cipher);
        System.out.println("plaintext = ");
        System.out.println(plain);        
    }
}
