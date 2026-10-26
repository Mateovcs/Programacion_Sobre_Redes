package tp6;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClienteBot {

    private static final String DIRECCION = "127.0.0.1";
    private static final int PUERTO = 6789;
    private static final String CLAVE = "Secreta123456789";

    private static final String ALUMNO = "Juan Martin Romero";

    public static void main(String[] args) {

        List<String> historial = new ArrayList<>();

        imprimir(Cifrado.AMARILLO, "SISTEMA",
                "Cliente iniciado por " + ALUMNO);

        try (Socket socket = new Socket(DIRECCION, PUERTO);
             DataOutputStream salida = new DataOutputStream(
                     socket.getOutputStream());
             DataInputStream entrada = new DataInputStream(
                     socket.getInputStream());
             Scanner teclado = new Scanner(System.in)) {

            imprimir(Cifrado.VERDE, "CONEXIÓN",
                    "Conectado con el servidor.");

            System.out.println("Hola, mi nombre es " + ALUMNO);

            String bienvenida = recibir(entrada);
            imprimir(Cifrado.CIAN, "SERVIDOR", bienvenida);

            boolean continuar = true;

            while (continuar) {

                mostrarMenu();

                System.out.print("\nTu comando o mensaje: ");
                String comando = teclado.nextLine().trim();

                if (comando.isEmpty()) {
                    continue;
                }

                historial.add("Cliente: " + comando);

                if (comando.equalsIgnoreCase("ver historial de chat")) {
                    imprimir(Cifrado.AMARILLO, "HISTORIAL LOCAL",
                            "Mensajes de esta sesión:");

                    for (String linea : historial) {
                        System.out.println("\t" + linea);
                    }
                }

                if (comando.equalsIgnoreCase("subir comprobante")) {

                    enviar(salida, comando);

                    String indicacion = recibir(entrada);
                    imprimir(Cifrado.CIAN, "SERVIDOR", indicacion);

                    System.out.print("Ruta del archivo: ");
                    String ruta = teclado.nextLine().trim();

                    File archivo = new File(ruta);

                    if (!archivo.isFile() || !archivo.canRead()) {
                        imprimir(Cifrado.ROJO, "ERROR",
                                "El archivo no existe o no se puede leer.");
                        break;
                    }

                    if (archivo.length() > 20L * 1024 * 1024) {
                        imprimir(Cifrado.ROJO, "ERROR",
                                "El archivo supera el límite de 20 MiB.");
                        break;
                    }

                    byte[] contenido = Files.readAllBytes(
                            archivo.toPath());

                    byte[] cifrado = Cifrado.encriptarArchivo(
                            CLAVE, contenido);

                    enviar(salida, archivo.getName());

                    enviar(salida, Integer.toString(cifrado.length));
                    salida.write(cifrado);
                    salida.flush();

                    imprimir(Cifrado.VERDE, "ARCHIVO",
                            "Archivo cifrado y enviado: "
                            + archivo.getName());

                    String respuesta = recibir(entrada);
                    historial.add("Servidor: " + respuesta);

                    imprimir(Cifrado.CIAN, "SERVIDOR", respuesta);
                    continue;
                }

                enviar(salida, comando);

                String respuesta = recibir(entrada);
                historial.add("Servidor: " + respuesta);

                imprimir(Cifrado.CIAN, "RESPUESTA", respuesta);

                if (comando.equalsIgnoreCase("salir")) {
                    imprimir(Cifrado.AMARILLO, "CIERRE",
                            "Cerrando conexión y recursos de red.");
                    continuar = false;
                }
            }

        } catch (Exception e) {
            imprimir(Cifrado.ROJO, "ERROR",
                    e.getMessage() == null
                    ? "Error de comunicación." : e.getMessage());
        }

        imprimir(Cifrado.VERDE, "FINAL",
                "Cliente finalizado.");
    }

    private static void mostrarMenu() {

        System.out.println();
        System.out.println("========== BOT DE SOPORTE ==========");
        System.out.println("autenticar - Validar acceso");
        System.out.println("ayuda - Menú de soporte");
        System.out.println("ver stock de Laptop - Consultar stock");
        System.out.println("subir comprobante - Enviar un archivo");
        System.out.println("ver historial de chat - Consultar historial");
        System.out.println("salir - Finalizar sesión");
        System.out.println("====================================");
    }

    private static void enviar(
            DataOutputStream salida, String texto) throws Exception {

        String cifrado = Cifrado.encriptar(CLAVE, texto);

        salida.writeUTF(cifrado);
        salida.flush();

        imprimir(Cifrado.AZUL, "ENVIANDO",
                "Mensaje cifrado enviado (" + cifrado.length()
                + " caracteres).");
    }

    private static String recibir(
            DataInputStream entrada) throws Exception {

        String cifrado = entrada.readUTF();
        String texto = Cifrado.decriptar(CLAVE, cifrado);

        imprimir(Cifrado.MORADO, "DESCIFRADO", texto);
        return texto;
    }

    private static void imprimir(
            String color, String evento, String mensaje) {

        System.out.printf("%s[%-12s] %-65s%s%n",
                color, evento, mensaje, Cifrado.RESET);
    }
}