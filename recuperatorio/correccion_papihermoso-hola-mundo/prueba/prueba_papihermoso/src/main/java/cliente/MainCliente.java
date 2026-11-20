package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

// Código único otorgado por el servidor del profesor: [PEGAR_CODIGO_AQUI]
public class MainCliente {

    private static String CLAVE = "ClaveSecreta1234";
    private static boolean modoCifrado = false;

    public static void main(String[] args) {
        BufferedReader teclado = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8)
        );

        System.out.println(
                Cifrado.CIAN
                + "====================================\n"
                + "          CLIENTE DE CHAT\n"
                + "===================================="
                + Cifrado.RESET
        );

        try {
            System.out.print(Cifrado.AMARILLO + "Ingrese IP: " + Cifrado.RESET);
            String host = teclado.readLine().trim();

            System.out.print(Cifrado.AMARILLO + "Ingrese puerto: " + Cifrado.RESET);
            int puerto = Integer.parseInt(teclado.readLine().trim());

            AtomicBoolean cerrando = new AtomicBoolean(false);

            try (Socket socket = new Socket(host, puerto);
                 BufferedReader disServidor = new BufferedReader(
                         new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
                 );
                 PrintWriter dosServidor = new PrintWriter(
                         new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),
                         true
                 )) {

                System.out.println(
                        Cifrado.VERDE + "\n[CONECTADO AL SERVIDOR]\n" + Cifrado.RESET
                );

                // Primer mensaje de bienvenida siempre llega en texto plano
                String bienvenida = disServidor.readLine();
                if (bienvenida != null) {
                    System.out.println(
                            Cifrado.VERDE + "[SERVIDOR] " + bienvenida + Cifrado.RESET
                    );
                }

                // Hilo secundario escuchando mensajes del servidor en tiempo real
                Thread recibirMensajes = new Thread(() -> {
                    try {
                        String linea;
                        while (!socket.isClosed() && (linea = disServidor.readLine()) != null) {
                            String textoMostrar = procesarEntrada(linea);
                            System.out.println(
                                    "\n" + Cifrado.MORADO + "\t[SERVIDOR] "
                                    + Cifrado.RESET + textoMostrar
                            );
                            System.out.print(Cifrado.CIAN + "vos> " + Cifrado.RESET);
                        }
                    } catch (IOException e) {
                        if (!cerrando.get()) {
                            System.out.println(
                                    "\n" + Cifrado.ROJO
                                    + "Conexión cerrada por el servidor."
                                    + Cifrado.RESET
                            );
                        }
                    }
                }, "RECEPCION");

                recibirMensajes.start();

                System.out.println(
                        Cifrado.AMARILLO
                        + "Comandos: /hi, /logout, /cifrar (activar/clave AES), o texto libre.\n"
                        + Cifrado.RESET
                );
                System.out.print(Cifrado.CIAN + "vos> " + Cifrado.RESET);

                // Hilo principal leyendo teclado
                String mensaje;
                while ((mensaje = teclado.readLine()) != null) {
                    mensaje = mensaje.trim();

                    if (mensaje.isEmpty()) {
                        System.out.print(Cifrado.CIAN + "vos> " + Cifrado.RESET);
                        continue;
                    }

                    // Permite activar el cifrado y setear la clave si el servidor del profesor la indica
                    if (mensaje.equalsIgnoreCase("/cifrar")) {
                        System.out.print(Cifrado.AMARILLO + "Ingrese clave AES (16 caracteres): " + Cifrado.RESET);
                        CLAVE = teclado.readLine().trim();
                        modoCifrado = !modoCifrado;
                        System.out.println(
                                Cifrado.VERDE + "Modo cifrado: " + (modoCifrado ? "ACTIVADO" : "DESACTIVADO") + Cifrado.RESET
                        );
                        System.out.print(Cifrado.CIAN + "vos> " + Cifrado.RESET);
                        continue;
                    }

                    enviar(dosServidor, mensaje);

                    if (mensaje.equalsIgnoreCase("/logout")) {
                        cerrando.set(true);
                        break;
                    }

                    System.out.print(Cifrado.CIAN + "vos> " + Cifrado.RESET);
                
                
                if(mensaje.equals("/PING_CHECK")) {
                	System.out.println("aaa:" + mensaje);
                	dosServidor.println(Cifrado.encriptar(CLAVE,"/PONG_ALIVE" ));
                	continue;
                }
                	
                }
                if (!cerrando.get()) {
                    enviar(dosServidor, "/logout");
                    cerrando.set(true);
                }

                socket.close();
                recibirMensajes.join();

                System.out.println(
                        Cifrado.AMARILLO + "\nDesconectado del chat." + Cifrado.RESET
                );
            }

        } catch (IOException e) {
            System.err.println(
                    Cifrado.ROJO + "Error de conexión: " + e.getMessage() + Cifrado.RESET
            );
        } catch (NumberFormatException e) {
            System.err.println(
                    Cifrado.ROJO + "Puerto inválido." + Cifrado.RESET
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void enviar(PrintWriter salida, String mensaje) {
        if (modoCifrado) {
            String cifrado = Cifrado.encriptar(CLAVE, mensaje);
            salida.println(cifrado != null ? cifrado : mensaje);
        } else {
            salida.println(mensaje);
        }
    }

    private static String procesarEntrada(String linea) {
        // Intenta desencriptar silenciosamente; si es texto plano devuelve null y muestra el original
        String descifrado = Cifrado.decriptar(CLAVE, linea);
        if (descifrado != null) {
            return descifrado;
        }
        return linea;
    }
}
