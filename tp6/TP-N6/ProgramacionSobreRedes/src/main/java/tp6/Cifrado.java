package tp6;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Cifrado {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int TAMANO_NONCE = 12;
    private static final int TAMANO_TAG = 128;

    public static final String RESET = "\u001B[0m";
    public static final String VERDE = "\u001B[32m";
    public static final String CIAN = "\u001B[36m";
    public static final String MORADO = "\u001B[35m";
    public static final String ROJO = "\u001B[31m";
    public static final String AMARILLO = "\u001B[33m";
    public static final String AZUL = "\u001B[34m";

    private static SecretKeySpec obtenerClave(String clave)
            throws Exception {

        byte[] hash = MessageDigest.getInstance("SHA-256")
                .digest(clave.getBytes(StandardCharsets.UTF_8));

        return new SecretKeySpec(hash, "AES");
    }

    private static byte[] cifrarBytes(String clave, byte[] datos)
            throws Exception {

        byte[] nonce = new byte[TAMANO_NONCE];
        RANDOM.nextBytes(nonce);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, obtenerClave(clave),
                new GCMParameterSpec(TAMANO_TAG, nonce));

        byte[] cifrado = cipher.doFinal(datos);
        byte[] resultado = new byte[nonce.length + cifrado.length];

        System.arraycopy(nonce, 0, resultado, 0, nonce.length);
        System.arraycopy(cifrado, 0, resultado, nonce.length,
                cifrado.length);

        return resultado;
    }

    private static byte[] descifrarBytes(String clave, byte[] datos)
            throws Exception {

        if (datos.length < TAMANO_NONCE + 16) {
            throw new IllegalArgumentException(
                    "Los datos cifrados están incompletos.");
        }

        byte[] nonce = Arrays.copyOfRange(datos, 0, TAMANO_NONCE);
        byte[] cifrado = Arrays.copyOfRange(
                datos, TAMANO_NONCE, datos.length);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, obtenerClave(clave),
                new GCMParameterSpec(TAMANO_TAG, nonce));

        return cipher.doFinal(cifrado);
    }

    public static String encriptar(String clave, String texto)
            throws Exception {

        byte[] datos = texto.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder()
                .encodeToString(cifrarBytes(clave, datos));
    }

    public static String decriptar(String clave, String textoCifrado)
            throws Exception {

        byte[] datos = Base64.getDecoder().decode(textoCifrado);
        return new String(descifrarBytes(clave, datos),
                StandardCharsets.UTF_8);
    }

    public static byte[] encriptarArchivo(String clave, byte[] archivo)
            throws Exception {

        return cifrarBytes(clave, archivo);
    }

    public static byte[] decriptarArchivo(String clave, byte[] archivo)
            throws Exception {

        return descifrarBytes(clave, archivo);
    }
}
