/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcipherblock;

import static java.lang.Math.abs;

/**
 *
 * @author HP
 */
public class NewCipherBlock {
    /*CLASS ATTRIBUTES*/
    private String plaintext;
    private String key;
    private String ciphertext;
    private int[] innerKey;
    
    public NewCipherBlock(){
        plaintext = "";
        key = "";
        ciphertext = "";
        innerKey = new int[16];
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
    
    public int[] getInnerKey(){
        return this.innerKey;
    }
    
    public void setPlaintext(String s){
        this.plaintext = s;
    }
    
    public void setKey(String s){
        this.key = s;
    }
    
    public void setCiphertext(String s){
        this.ciphertext = s;
    }
    
    public void setInnerKey(int[] ik){
        this.innerKey = ik;
    }
    
    /*Parsers*/
    public int[] charToHexa(char c){
        int[] hex = new int[2];
        String h = Integer.toHexString((int) c);
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
    
    //parser char-bit dan parser hexa-bit kayaknya nggak perlu ya..
    
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
    public void encryptor(){
        int n = key.length()*2;     //number of iterations
        for (int i=0; i<n; i++){
            innerKey[i] = charToHexa(key.charAt(i/2))[i%2];
        }
        for (char c: plaintext.toCharArray()){
            int[] lr = charToHexa(c);
            for (int i=0; i<n; i++){
                lr = encFeistel(lr, innerKey[i]);
                innerKey[i] = lr[0];
            }
            ciphertext += "" + hexaToChar(lr);
        }
    }
    
    /*Dekripsi*/
    public void decryptor(){
        int n = key.length()*2;     //number of iterations
        for (int i=0; i<n; i++){
            innerKey[n-i-1] = charToHexa(key.charAt(i/2))[i%2];
        }
        System.out.println(ciphertext);
        for (char c: ciphertext.toCharArray()){
            int[] lr = charToHexa(c);
            for (int i=0; i<n; i++){
                int tmp = lr[0];
                lr = decFeistel(lr, innerKey[i]);
                innerKey[i] = tmp;
            }
            plaintext += "" + hexaToChar(lr);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        NewCipherBlock k = new NewCipherBlock();
        k.setPlaintext("ABDEFZHPLM");
        k.setKey("abcdefgh");
        k.setCiphertext("");
        k.encryptor();
        String cipher = k.getCiphertext();
        System.out.println(cipher);
        k.setPlaintext("");
        k.setCiphertext(cipher);
        k.setKey("abcdefgh");
        k.decryptor();
        String plain = k.getPlaintext();
        System.out.println(plain);
    }
    
}
