package com.batallanaval;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteNaval {
    private static final int PUERTO = 5000;
    private static final String ALUMNO = "Mateo Villegas";

    public static final String RESET = "\u001B[0m";
    public static final String VERDE = "\u001B[32m";
    public static final String CIAN = "\u001B[36m";
    public static final String AZUL = "\u001B[34m";
    public static final String ROJO = "\u001B[31m";
    public static final String AMARILLO = "\u001B[33m";

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        System.out.println(AMARILLO + "[SISTEMA] Cliente iniciado por " + ALUMNO + RESET);
        
        System.out.print("IP del servidor (ejemplo 127.0.0.1): ");
        String ip = teclado.nextLine().trim();

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ip, PUERTO), 5000);
            socket.setSoTimeout(0);

            try (DataInputStream entrada = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                 DataOutputStream salida = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()))) {

                System.out.println("Hola mi nombre es " + ALUMNO);
                
                String respuestaRol = entrada.readUTF();
                System.out.println(CIAN + "[SERVIDOR] " + respuestaRol + RESET);

                Tablero miTablero = new Tablero();
                Tablero radarEnemigo = new Tablero();

                posicionarFlotaGuiada(miTablero, teclado);

                salida.writeUTF(miTablero.serializar());
                salida.flush();

                System.out.println(AMARILLO + "\n[ESTADO] Tablero enviado. Esperando confirmación del rival.." + RESET);

                boolean juegoActivo = true;
                while (juegoActivo) {
                    String evento = entrada.readUTF();

                    if (evento.startsWith("START")) {
                        System.out.println("\npera" + VERDE + evento.split(":")[1] + RESET);
                    } 
                    else if (evento.startsWith("ESPERA")) {
                        System.out.println("\nCargando.. " + AMARILLO + evento.split(":")[1] + RESET);
                    } 
                    else if (evento.equals("TU_TURNO")) {
                        System.out.println("\n=============================================");
                        System.out.println(" ESTADO ACTUAL DE TUS TABLEROS:");
                        System.out.println("\n MI CUADRICULA (Ataques recibidos por mi):");
                        imprimirMatriz(miTablero, false);
                        System.out.println("\n MI RADAR (Ataques que le hice al enemigo):");
                        imprimirMatriz(radarEnemigo, true);

                        int f = -1, c = -1;
                        boolean coordenadaValida = false;
                        
                        while (!coordenadaValida) {
                            try {
                                System.out.print("\n Ingrese Fila para bombardear mejor si elige bolivia  (0-9): ");
                                f = Integer.parseInt(teclado.nextLine().trim());
                                System.out.print(" Ingrese Columna de ataque (0-9): ");
                                c = Integer.parseInt(teclado.nextLine().trim());
                                
                                if (f >= 0 && f < 10 && c >= 0 && c < 10) {
                                    coordenadaValida = true;
                                } else {
                                    System.out.println(ROJO + " Error: Las coordenadas van del 0 al 9, se recomienda que aprenda a leer." + RESET);
                                }
                            } catch (NumberFormatException e) {
                                System.out.println(ROJO + " Error: Debe ingresar valores numéricos validos.  >:c" + RESET);
                            }
                        }

                        salida.writeInt(f);
                        salida.writeInt(c);
                        salida.flush();
                    } 
                    else if (evento.startsWith("FEEDBACK")) {
                        String[] partes = evento.split(":");
                        System.out.println("\n " + CIAN + "[DISPARO PROPIO] Resultado: " + partes[1] + RESET);
                        radarEnemigo = Tablero.deserializar(partes[2]);
                    } 
                    else if (evento.startsWith("NOTIFICAR")) {
                        String[] partes = evento.split(":");
                        System.out.println("\n⚠ " + ROJO + "[ATAQUE HITLER] " + partes[1] + RESET);
                        miTablero = Tablero.deserializar(partes[2]);
                    } 
                    else if (evento.startsWith("FIN")) {
                        String[] partes = evento.split(":");
                        System.out.println("\n [FIN DEL JUEGO o GAME OVER] " + partes[2]);
                        if (partes[1].equals("GANASTE o YOU WIN")) {
                            System.out.println(VERDE + " ¡FELICIDADES, HAS GANADO LA PARTIDA menudo bombardero eres tio! " + RESET);
                        } else {
                            System.out.println(ROJO + "Lo sentimos, fuiste el peor jugador de este juego. En compensacion tu madre es mia. Besos" + RESET);
                        }
                        juegoActivo = false;
                    }
                }
            }
        } catch (ConnectException e) {
            System.err.println("No se pudo conectar. O la culpa es nuestra por falta de personal o usted dispone una PC povre.");
        } catch (IOException e) {
            System.err.println("Error en la conexión del cliente: " + e.getMessage());
        }
        System.out.println("Cliente finalizado.");
    }

    private static void posicionarFlotaGuiada(Tablero tablero, Scanner scanner) {
        String[] nombres = {"Montachanchas", "Hiroshima", "Titanic", "Titan", "Destructordehombres"};
        int[] longitudes = {5, 4, 3, 3, 2};

        System.out.println("\n--- FASE DE CONFIGURACIÓN DE BARCOS, DERROTA AL SUCIO DE TU RIVAL CON TU MEJOR ARSENAL >:) ---");
        for (int i = 0; i < nombres.length; i++) {
            boolean exito = false;
            while (!exito) {
                try {
                    imprimirMatriz(tablero, false);
                    System.out.println("\nColocando: " + nombres[i] + " (" + longitudes[i] + " casillas)");
                    System.out.print("Fila de origen (0-9): ");
                    int fila = Integer.parseInt(scanner.nextLine().trim());
                    System.out.print("Columna de origen (0-9): ");
                    int col = Integer.parseInt(scanner.nextLine().trim());
                    System.out.print("En horizontal? (s/n): ");
                    boolean horizontal = scanner.nextLine().trim().equalsIgnoreCase("s");

                    exito = tablero.colocarBarco(nombres[i], fila, col, longitudes[i], horizontal);
                    if (!exito) {
                        System.out.println(ROJO + "X Error: Ubicación no disponible, hay otro trtoyano ocupando este casillero. Reintente." + RESET);
                    }
                } catch (Exception e) {
                    System.out.println(ROJO + "Entrada no válida. Ingrese datos numéricos correctos, se recomienda aprender a leer." + RESET);
                }
            }
        }
        System.out.println(VERDE + "\n¡Todos tus barcos fueron posicionados correctamente, a rezar un felipe nuestro!" + RESET);
    }

    private static void imprimirMatriz(Tablero t, boolean ocultarBarcos) {
        System.out.print("   ");
        for (int c = 0; c < 10; c++) System.out.print(c + " ");
        System.out.println();

        for (int i = 0; i < 10; i++) {
            System.out.print(i + " |");
            for (int j = 0; j < 10; j++) {
                String c = t.grilla.get(i).get(j);
                if (c.equals(Tablero.IMPACTO)) {
                    System.out.print(ROJO + "X " + RESET);
                } else if (c.equals(Tablero.FALLO)) {
                    System.out.print(AZUL + "O " + RESET);
                } else if (c.startsWith(Tablero.BARCO)) {
                    if (ocultarBarcos) {
                        System.out.print(". ");
                    } else {
                        System.out.print(VERDE + "B " + RESET);
                    }
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println("|");
        }
    }
}
