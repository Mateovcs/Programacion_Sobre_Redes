package com.examen;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Se encarga de leer el archivo original juegos.dat y reestructurarlo
 * a un formato estandarizado CSV con separador " ; ".
 * Luego elimina el archivo original.
 */
public class Estandarizador {

    private static final Logger logger = Logger.getLogger(Estandarizador.class.getName());

    static {
        try {
            // Configura el logger para escribir en crash.log sin mostrar nada en consola
            FileHandler fh = new FileHandler("crash.log", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setUseParentHandlers(false); 
        } catch (Exception e) {
            System.err.println("No se pudo inicializar el sistema de logs: " + e.getMessage());
        }
    }

    /**
     * Lee el archivo original (juegos.dat), reemplaza el caracter '+'
     * por el separador " ; ", guarda el resultado como juegos.csv
     * y elimina el archivo original juegos.dat.
     * 
     * @param rutaOriginal ruta completa del archivo juegos.dat a procesar
     */
    public void estandarizar(String rutaOriginal) {
        try {
            Path ruta = Paths.get(rutaOriginal);
            
            // 1. Leer archivo original
            String contenido = Files.readString(ruta);
            
            // 2. Reemplazar + por " ; " (con espacios incluidos)
            contenido = contenido.replace("+", " ; ");
            
            // 3. Escribir nuevo archivo .csv
            String rutaCsv = rutaOriginal.replace(".dat", ".csv");
            Files.writeString(Paths.get(rutaCsv), contenido);
            
            // 4. Eliminar el .dat original
            Files.delete(ruta);
            
        } catch (Exception ex) {
            // Capturar errores y registrarlos en crash.log
            logger.log(Level.SEVERE, "Error al estandarizar", ex);
        }
    }
    
    /**
     * Genera una nueva clave simétrica AES de 256 bits.
     */
    public static SecretKey generarClaveAES() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(256);
            return kg.generateKey(); 
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al generar clave AES", ex);
        }
        return null;
    }
    
    /**
     * Convierte la clave AES a texto (Base64) y la guarda en un archivo.
     */
    public static void guardarClave(SecretKey clave, String rutaArchivo) {
        try {
            String claveEnTexto = Base64.getEncoder().encodeToString(clave.getEncoded());
            Files.writeString(Paths.get(rutaArchivo), claveEnTexto);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al guardar la clave en el archivo", ex);
        }
    }

    /**
     * Lee el archivo de texto y reconstruye la clave AES para poder usarla.
     */
    public static SecretKey recuperarClave(String rutaArchivo) {
        try {
            // 1. Leemos el texto completo (Base64) desde el archivo
            String textoLeido = Files.readString(Paths.get(rutaArchivo)).trim();
            
            // 2. Decodificamos el texto para recuperar los bytes originales
            byte[] bytesClave = Base64.getDecoder().decode(textoLeido);
            
            // 3. Reconstruimos y retornamos la llave AES
            return new SecretKeySpec(bytesClave, 0, bytesClave.length, "AES");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al recuperar la clave del archivo", ex);
        }
        return null;
    }
    
    /**
     * Encripta un texto usando la clave proporcionada.
     */
    public static String encriptar(String datos, SecretKey clave) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, clave);
            byte[] bytesEncriptados = cipher.doFinal(datos.getBytes());
            return Base64.getEncoder().encodeToString(bytesEncriptados);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al encriptar datos", ex);
        }
        return null;
    }
    
    /**
     * Desencripta un texto en Base64 para recuperar los datos originales.
     */
    public static String desencriptar(String datosEncriptados, SecretKey clave) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, clave);
            byte[] bytesDecodificados = Base64.getDecoder().decode(datosEncriptados);
            byte[] bytesDesencriptados = cipher.doFinal(bytesDecodificados);
            return new String(bytesDesencriptados);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al desencriptar datos", ex);
        }
        return null;
    }
}
