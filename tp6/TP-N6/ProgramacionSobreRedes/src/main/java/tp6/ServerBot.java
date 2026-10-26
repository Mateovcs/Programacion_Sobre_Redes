package tp6;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ServerBot {

    private static final int PUERTO = 6789;
    private static final String CLAVE = "Secreta123456789";

    private static final String ALUMNO = "Juan Martin Romero";

    private static final String[] PRODUCTOS = {
        "Laptop", "Smartphone", "Auriculares", "Monitor"
    };

    private static final int[] CANTIDADES = {
        15, 30, 50, 10
    };

    public static void main(String[] args) {

        imprimir(Cifrado.VERDE, "SISTEMA",
                "Servidor Bot iniciado por " + ALUMNO);

        try (ServerSocket servidor = new ServerSocket(PUERTO)) {

            imprimir(Cifrado.VERDE, "ESCUCHA",
                    "Servidor activo en el puerto " + PUERTO);

            while (true) {
                imprimir(Cifrado.AMARILLO, "ESPERA",
                        "Esperando una conexión...");

                try (Socket cliente = servidor.accept();
                     DataInputStream entrada = new DataInputStream(
                             cliente.getInputStream());
                     DataOutputStream salida = new DataOutputStream(
                             cliente.getOutputStream())) {

                    imprimir(Cifrado.VERDE, "CONEXIÓN",
                            "Cliente conectado desde "
                            + cliente.getRemoteSocketAddress());

                    atenderCliente(entrada, salida);

                } catch (EOFException e) {
                    imprimir(Cifrado.ROJO, "DESCONEXIÓN",
                            "El cliente cerró la conexión abruptamente.");
                } catch (Exception e) {
                    imprimir(Cifrado.ROJO, "ERROR",
                            e.getMessage() == null
                            ? "Error de comunicación." : e.getMessage());
                }
            }

        } catch (IOException e) {
            imprimir(Cifrado.ROJO, "ERROR FATAL", e.getMessage());
        }
    }

    private static void atenderCliente(
            DataInputStream entrada,
            DataOutputStream salida) throws Exception {

        List<String> historial = new ArrayList<>();
        boolean autenticado = false;

        imprimir(Cifrado.CIAN, "IDENTIDAD",
                "Hola, mi nombre es " + ALUMNO);

        enviar(salida, "Hola, mi nombre es " + ALUMNO
                + ". Bienvenido al servidor de soporte.");

        boolean conectado = true;

        while (conectado) {

            String mensaje = recibir(entrada);

            imprimir(Cifrado.AZUL, "RECIBIDO", mensaje);
            historial.add("Cliente: " + mensaje);

            String respuesta;

            if (mensaje.equalsIgnoreCase("salir")) {

                respuesta = "Conexión finalizada. ¡Gracias por usar "
                        + "nuestro soporte!";
                enviar(salida, respuesta);
                historial.add("Servidor: " + respuesta);
                conectado = false;
                continue;

            } else if (mensaje.equalsIgnoreCase("autenticar")) {

                autenticado = true;
                respuesta = "Autenticación exitosa. Ahora puede "
                        + "realizar consultas operativas.";

            } else if (mensaje.equalsIgnoreCase("ayuda")
                    || mensaje.toLowerCase().contains("hola")) {

                respuesta = "Menú de Soporte: 1. Estado de Envío"
                        + " | 2. Devoluciones | 3. Reclamos."
                        + " Por favor, introduzca un comando válido.";

            } else if (mensaje.toLowerCase()
                    .startsWith("ver stock de ")) {

                if (!autenticado) {
                    respuesta = "ERROR: Debe ejecutar primero "
                            + "el comando 'autenticar'.";

                } else {
                    String buscado = mensaje.substring(13).trim();
                    respuesta = consultarStock(buscado);
                }

            } else if (mensaje.equalsIgnoreCase(
                    "ver historial de chat")) {

                StringBuilder sb = new StringBuilder(
                        "--- HISTORIAL DE SESIÓN ---\n");

                for (String linea : historial) {
                    sb.append(linea).append("\n");
                }

                respuesta = sb.toString();

            } else if (mensaje.equalsIgnoreCase("subir comprobante")) {

                enviar(salida, "Indique la ruta del archivo "
                        + "que desea enviar desde el cliente.");

                String nombre = recibir(entrada);
                nombre = new File(nombre).getName();

                if (nombre.isBlank() || nombre.contains("..")) {
                    throw new IOException("Nombre de archivo inválido.");
                }

                long tamano = Long.parseLong(recibir(entrada));

                if (tamano < 28 || tamano > 20L * 1024 * 1024 + 28) {
                    throw new IOException("Tamaño de archivo no permitido.");
                }

                byte[] cifrado = new byte[(int) tamano];
                entrada.readFully(cifrado);

                byte[] archivo = Cifrado.decriptarArchivo(
                        CLAVE, cifrado);

                File destino = new File("recibidos");
                Files.createDirectories(destino.toPath());

                File archivoDestino = new File(destino, nombre);
                Files.write(archivoDestino.toPath(), archivo);

                imprimir(Cifrado.VERDE, "ARCHIVO",
                        "Comprobante recibido: "
                        + archivoDestino.getAbsolutePath());

                respuesta = "Comprobante recibido correctamente: "
                        + nombre;

            } else if (mensaje.equalsIgnoreCase("1")) {

                respuesta = "Estado de envío: su consulta será "
                        + "derivada al área de logística.";

            } else if (mensaje.equalsIgnoreCase("2")) {

                respuesta = "Devoluciones: conserve el comprobante "
                        + "y comuníquese con atención al cliente.";

            } else if (mensaje.equalsIgnoreCase("3")) {

                respuesta = "Reclamos: describa su inconveniente "
                        + "para registrar el caso.";

            } else {

                respuesta = "Comando no reconocido. Escriba "
                        + "'ayuda' para desplegar el menú guiado.";
            }

            historial.add("Servidor: " + respuesta);
            enviar(salida, respuesta);

            imprimir(Cifrado.VERDE, "RESPUESTA", respuesta);
        }
    }

    private static String consultarStock(String buscado) {

        for (int i = 0; i < PRODUCTOS.length; i++) {
            if (PRODUCTOS[i].equalsIgnoreCase(buscado)) {
                return "El stock actual de '" + PRODUCTOS[i]
                        + "' es de " + CANTIDADES[i] + " unidades.";
            }
        }

        return "El producto '" + buscado
                + "' no existe en el catálogo.";
    }

    private static void enviar(
            DataOutputStream salida, String texto) throws Exception {

        String cifrado = Cifrado.encriptar(CLAVE, texto);
        salida.writeUTF(cifrado);
        salida.flush();

        imprimir(Cifrado.MORADO, "ENVIANDO",
                "Mensaje cifrado enviado (" + cifrado.length()
                + " caracteres).");
    }

    private static String recibir(
            DataInputStream entrada) throws Exception {

        String cifrado = entrada.readUTF();
        String texto = Cifrado.decriptar(CLAVE, cifrado);

        imprimir(Cifrado.CIAN, "DESCIFRADO", texto);
        return texto;
    }

    private static void imprimir(
            String color, String evento, String mensaje) {

        System.out.printf("%s[%-12s] %-65s%s%n",
                color, evento, mensaje, Cifrado.RESET);
    }
}