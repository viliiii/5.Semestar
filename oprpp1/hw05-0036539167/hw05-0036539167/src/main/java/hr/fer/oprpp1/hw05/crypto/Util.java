package hr.fer.oprpp1.hw05.crypto;

import java.util.stream.Stream;

public class Util {

    /**
     * Returns byteArray representation of the
     * given String that represents hex-encoded string.
     * Each two characters from given string are converted into
     * one byte. EXAMPLE: "22" -> byte 34.
     * @param keyText given string
     * @return byte array
     * @throws IllegalArgumentException if string contains
     * characters that can not be converted to hex numbers or string length
     * is odd number.
     */
    public static byte[] hextobyte(String keyText){
        if(keyText.length()%2!=0){
            throw new IllegalArgumentException();
        }

        Stream<Character> charStream = keyText.chars().mapToObj(c -> (char) c);
        if(!charStream.allMatch(c-> (c>='a' && c<='f') || (c>='A' && c<='F')
                        || (c>='0' && c<='9'))){
            throw new IllegalArgumentException();
        }


        byte[] result = new byte[keyText.length()/2];

        for(int i=0; i<keyText.length(); i+=2){

            int upper = Character.digit(keyText.charAt(i), 16);
            int lower = Character.digit(keyText.charAt(i+1), 16);

            result[i/2] = (byte) ((upper<<4) + lower);
        }

        return result;
    }

    /**
     * Returns the string representation of given byte array.
     * Each byte is converted to a string consisting 2 characters.
     * Reverse from method hextobyte().
     * @param bytearray given byte array
     * @return string representation of given byte array
     */
    public static String bytetohex(byte[] bytearray) {

        StringBuilder hex = new StringBuilder();
        for(byte b: bytearray){
            hex.append(String.format("%02x", b));
        }

        return hex.toString();
    }


}
