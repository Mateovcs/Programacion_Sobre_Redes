package tp;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class cifrado {
    private static final String ALGORITMO = "AES";
    private static final byte[] CLAVE = "ClaveSecreta1234".getBytes(StandardCharsets.UTF_8);

    public static String encriptar(String texto) throws Exception {
        if (texto == null || texto.isEmpty()) return "";
        SecretKeySpec keySpec = new SecretKeySpec(CLAVE, ALGORITMO);
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] bytes = cipher.doFinal(texto.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String desencriptar(String textoCifrado) throws Exception {
        if (textoCifrado == null || textoCifrado.isEmpty()) return "";
        SecretKeySpec keySpec = new SecretKeySpec(CLAVE, ALGORITMO);
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] bytes = Base64.getDecoder().decode(textoCifrado);
        return new String(cipher.doFinal(bytes), StandardCharsets.UTF_8);
    }
}
