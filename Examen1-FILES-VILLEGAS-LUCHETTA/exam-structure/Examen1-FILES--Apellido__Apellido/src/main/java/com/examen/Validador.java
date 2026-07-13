package com.examen;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Proporciona metodos para validar datos ingresados por el usuario
 * a traves de la consola.
 * <p>
 * La validacion minima requerida es que el dato ingresado no este vacio.
 * No se permite el uso de la clase Scanner.
 */
public class Validador {

    /**
     * Lee un texto ingresado por el usuario y valida que no sea vacio.
     * Si el usuario ingresa una cadena vacia, se muestra un mensaje de error
     * y se solicita nuevamente el ingreso.
     * <p>
     * Utiliza BufferedReader (NO Scanner).
     *
     * @param reader  BufferedReader conectado a System.in
     * @param mensaje mensaje informativo o de solicitud a mostrar al usuario
     * @return el texto ingresado (garantizado no vacio)
     */
    public static String leerNoVacio(BufferedReader reader, String mensaje) {
        String entrada = "";
        boolean valido = false;

        while (!valido) {
            try {
                // Si el mensaje tiene contenido, lo imprimimos para orientar al usuario
                if (mensaje != null && !mensaje.trim().isEmpty()) {
                    System.out.print(mensaje);
                }
                
                // Leemos la línea desde el BufferedReader
                String linea = reader.readLine();
                
                // Controlamos si la entrada es nula (fin de flujo de consola)
                if (linea == null) {
                    System.out.println("\nError: Entrada interrumpida.");
                    return "";
                }
                
                entrada = linea.trim();

                // Validamos que no sea una cadena vacía
                if (entrada.isEmpty()) {
                    System.out.println("Error: El dato no puede estar vacío. Intente nuevamente.");
                } else {
                    valido = true;
                }
                
            } catch (IOException e) {
                LogManager.registrarError("Error de lectura en Validador", e);
                System.out.println("Ocurrió un error al leer la consola. Intente de nuevo.");
            }
        }
        return entrada;
    }

    /**
     * Variante que permite leer una línea sin necesidad de imprimir un mensaje previo.
     * Reutiliza la lógica principal pasando una cadena vacía como mensaje.
     *
     * @param reader BufferedReader conectado a System.in
     * @return el texto ingresado (garantizado no vacio)
     */
    public static String leerNoVacio(BufferedReader reader) {
        return leerNoVacio(reader, "");
    }
}
