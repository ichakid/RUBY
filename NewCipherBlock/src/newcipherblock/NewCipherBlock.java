/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcipherblock;

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
    public int charToHexa(char c){
        int hex;
        //Put some code here
        return hex;
    }
    
    public char hexaToChar(int i){
        char c;
        //Put some code here
        return c;
    }
    
    //parser char-bit dan parser hexa-bit kayaknya nggak perlu ya..
    
    /*Inner function*/
    public int encInnerFunction(){
        
    }
    
    public int decInnerFunction(){
        
    }
    
    /*Feistel*/
    public char encFeistel(){
        
    }
    
    public char decFeistel(){
        
    }
    
    /*Enkripsi*/
    public void encryptor(){
        
    }
    
    /*Dekripsi*/
    public void decryptor(){
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
