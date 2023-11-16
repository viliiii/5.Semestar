package hr.fer.oprpp1.hw05.crypto;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Objects;
import java.util.Scanner;

public class Crypto {

    public static void checksha(String[] args){
        System.out.println("Please provide expected sha-256 digest for hw05test.bin:");
        System.out.print(">");

        Scanner scanner = new Scanner(System.in);
        String expSha = scanner.nextLine();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            try(FileInputStream inputStream = new FileInputStream("src/main/java/hr/fer/oprpp1/hw05/crypto/" + args[1])) {
                byte[] buffer = new byte[8192];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    md.update(buffer, 0, bytesRead);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            byte[] digested = md.digest();

            if(expSha.compareTo(Util.bytetohex(digested)) == 0){
                System.out.println("Digesting completed. Digest of " + args[1] + " matches expected digest.");
            }else {
                System.out.println("Digesting completed. Digest of " + args[1] + " does not match the expected digest. Digest\n" +
                        "was:");
                System.out.println(Util.bytetohex(digested));
            }

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Caught NoSuchAlgorithmException");
        }
    }

    public static void crypt(String[] args, boolean encrypt){
        System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):");
        System.out.print("> ");
        Scanner scanner = new Scanner(System.in);
        String keyText = scanner.nextLine();

        System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits):");
        System.out.print("> ");
        String ivText = scanner.nextLine();

        SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(keyText), "AES");
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(ivText));

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);

            try(FileInputStream inputStream = new FileInputStream("src/main/java/hr/fer/oprpp1/hw05/crypto/" + args[1]);
                FileOutputStream outputStream = new FileOutputStream("src/main/java/hr/fer/oprpp1/hw05/crypto/" + args[2])) {
                byte[] buffer = new byte[8192];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byte[] outputBytes = cipher.update(buffer, 0, bytesRead);
                    outputStream.write(outputBytes);
                }

                byte[] finalBytes = cipher.doFinal();
                outputStream.write(finalBytes);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }catch (Exception e){

        }
        System.out.println(encrypt ? "Encryption completed. Generated file" +args[2]+ "based on file" +args[1] : "Decryption completed. Generated file" +args[2]+ "based on file" +args[1] );
    }

    //File paths work if run with run configurations from IDE!!!
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        if(Objects.equals(args[0], "checksha")){
            //DIGESTING----------------------------------------------------------------
            checksha(args);
        }else if(Objects.equals(args[0], "encrypt")){
            //ENCRYPTING or DECRYPTING depending on boolean encrypt (and change input and output files)----------------------------------------------------------------
            crypt(args, true);
        }else if(Objects.equals(args[0], "decrypt")){
            //ENCRYPTING or DECRYPTING depending on boolean encrypt (and change input and output files)----------------------------------------------------------------
            crypt(args, false);
        }else {
            throw new IllegalArgumentException("Invalid option: " + args[0]);
        }
    }
}
