package com.examen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Sistema de registro de errores que escribe en el archivo crash.log.
 * <p>
 * Ningun error debe mostrarse por consola, todos deben registrarse aqui.
 * Utiliza FileWriter y PrintWriter para escribir en el archivo.
 */
public class LogManager {

    private static final String ARCHIVO_LOG = "crash.log";
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Inicializa el sistema de log.
     * Crea o verifica que el archivo crash.log existe y esta listo para escribir.
     * Este metodo debe llamarse una unica vez al iniciar el programa.
     */
    public static void inicializar() {
        try {
            File archivo = new File(ARCHIVO_LOG);
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
            // Escribimos una línea de inicio para comprobar que funciona de forma segura
            registrarInfo("--- Sistema de Log Inicializado ---");
        } catch (IOException e) {
            // No se muestra por consola según requerimiento estricto del enunciado
        }
    }

    /**
     * Registra un error en crash.log.
     * Escribe la fecha/hora, el mensaje de contexto y el stack trace de la excepcion.
     *
     * @param mensaje descripcion del contexto donde ocurrio el error
     * @param e       la excepcion capturada
     */
    public static void registrarError(String mensaje, Exception e) {
        // Abrimos el FileWriter en modo 'append' (true) para no borrar los errores anteriores
        try (FileWriter fw = new FileWriter(ARCHIVO_LOG, true);
             PrintWriter pw = new PrintWriter(fw)) {
            
            String fechaHora = LocalDateTime.now().format(FORMATO_FECHA);
            
            pw.println("[" + fechaHora + "] [ERROR] " + mensaje);
            if (e != null) {
                pw.println("Excepción: " + e.toString());
                pw.println("Stack Trace:");
                // PrintWriter permite volcar el stack trace directamente al archivo de texto
                e.printStackTrace(pw);
            }
            pw.println("-----------------------------------------------------------------");
            
        } catch (IOException ex) {
            // Ignorado por consola de forma obligatoria
        }
    }

    /**
     * Registra un mensaje informativo en crash.log.
     *
     * @param mensaje mensaje informativo a registrar
     */
    public static void registrarInfo(String mensaje) {
        try (FileWriter fw = new FileWriter(ARCHIVO_LOG, true);
             PrintWriter pw = new PrintWriter(fw)) {
            
            String fechaHora = LocalDateTime.now().format(FORMATO_FECHA);
            pw.println("[" + fechaHora + "] [INFO] " + mensaje);
            
        } catch (IOException ex) {
            // Ignorado por consola de forma obligatoria
        }
    }
}
