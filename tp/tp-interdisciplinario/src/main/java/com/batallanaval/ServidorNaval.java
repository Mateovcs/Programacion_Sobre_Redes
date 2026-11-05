package com.batallanaval;

import java.io.*;
import java.net.*;

public class ServidorNaval {
    private static final int PUERTO = 5000;
    private static final String ALUMNO = "Mateo Villegas";

    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("   SERVIDOR BATALLA NAVAL INICIADO");
        System.out.println("   Desarrollado por: " + ALUMNO);
        System.out.println("   Puerto TCP: " + PUERTO);
        System.out.println("======================================");

        try (ServerSocket servidor = new ServerSocket()) {
            servidor.setReuseAddress(true);
            servidor.bind(new InetSocketAddress(PUERTO));

            while (true) {
                System.out.println("[ESPERA] Esperando al Jugador 1 (Hiroshima)...");
                Socket j1 = servidor.accept();
                System.out.println("[CONEXIÓN] Jugador 1 listo desde: " + j1.getInetAddress().getHostAddress());

                System.out.println("[ESPERA] Esperando al Jugador 2 (Nagasaki)...");
                Socket j2 = servidor.accept();
                System.out.println("[CONEXIÓN] Jugador 2 listo desde: " + j2.getInetAddress().getHostAddress());

                Thread hilo = new Thread(new ManejadorPartida(j1, j2), "Partida-" + j1.getPort());
                hilo.start();
            }
        } catch (IOException e) {
            System.err.println("Error jodido en el servidor: " + e.getMessage());
        }
    }
}

class ManejadorPartida implements Runnable {
    private final Socket s1;
    private final Socket s2;

    ManejadorPartida(Socket j1, Socket j2) {
        this.s1 = j1;
        this.s2 = j2;
    }

    @Override
    public void run() {
        try (DataInputStream in1 = new DataInputStream(new BufferedInputStream(s1.getInputStream()));
             DataOutputStream out1 = new DataOutputStream(new BufferedOutputStream(s1.getOutputStream()));
             DataInputStream in2 = new DataInputStream(new BufferedInputStream(s2.getInputStream()));
             DataOutputStream out2 = new DataOutputStream(new BufferedOutputStream(s2.getOutputStream()))) {

            out1.writeUTF("ROL:1:Conectado. Ahora representas a Hiroshima.");
            out2.writeUTF("ROL:2:Conectado. Ahora representas a Nagasaki");
            out1.flush(); out2.flush();

            Tablero tableroJ1 = Tablero.deserializar(in1.readUTF());
            System.out.println("[INFO] Tablero del Jugador 1 recibido correctamente.");

            Tablero tableroJ2 = Tablero.deserializar(in2.readUTF());
            System.out.println("[INFO] Tablero del Jugador 2 recibido correctamente.");

            out1.writeUTF("START:Troya en posicion. Comienza la GUERRA");
            out2.writeUTF("START:Esparta en espera. Comienza la GUERRA");
            out1.flush(); out2.flush();

            boolean partidaActiva = true;
            int turno = 1;

            while (partidaActiva) {
                if (turno == 1) {
                    out1.writeUTF("TU_TURNO (Ojala pierdas)");
                    out2.writeUTF("ESPERA:Turno de Hiroshima");
                    out1.flush(); out2.flush();

                    int fila = in1.readInt();
                    int col = in1.readInt();

                    String resultado = tableroJ2.registrarDisparo(fila, col);

                    out1.writeUTF("FEEDBACK:" + resultado + ":" + tableroJ2.serializar());
                    out2.writeUTF("NOTIFICAR:El Hiroshima atacó en (" + fila + "," + col + "). Resultado: " + resultado + ":" + tableroJ2.serializar());
                    out1.flush(); out2.flush();

                    if (tableroJ2.todosHundidos()) {
                        out1.writeUTF("FIN:GANASTE:¡Felicitaciones! Hundiste por completo a tu enemigo alias nemesis o al integrante estrella de tu lista negra.");
                        out2.writeUTF("FIN:PERDISTE:Por tu culpa todos murieron, y tenes la desgracia de ser el PERDEDOR, vive con el fracaso, podes jugar una revancha pero quedate con el FRACASSO DE POR VIDA. Fin del juego.");
                        out1.flush(); out2.flush();
                        partidaActiva = false;
                    } else {
                        turno = 2;
                    }
                } else {
                    out2.writeUTF("TU_TURNO");
                    out1.writeUTF("ESPERA:Turno de Nagasaki");
                    out1.flush(); out2.flush();

                    int fila = in2.readInt();
                    int col = in2.readInt();

                    String resultado = tableroJ1.registrarDisparo(fila, col);

                    out2.writeUTF("FEEDBACK:" + resultado + ":" + tableroJ1.serializar());
                    out1.writeUTF("NOTIFICAR:Nagasaki ataco en (" + fila + "," + col + "). Resultado: " + resultado + ":" + tableroJ1.serializar());
                    out2.flush(); out1.flush();

                    if (tableroJ1.todosHundidos()) {
                        out2.writeUTF("FIN:GANASTE:¡Felicitaciones! Evitaste la bomba de Hiroshima.");
                        out1.writeUTF("FIN:PERDISTE:Perder es señal de fracaso....y vos te convertiste en el significado e ejemplo visual de como perder, vive con la derrota y si vas por una revancha, eso no quita el fracaso que sos..");
                        out2.flush(); out1.flush();
                        partidaActiva = false;
                    } else {
                        turno = 1;
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("[PARTIDA FINALIZADA] Desconexión de jugadores o error en hilos: " + e.getMessage());
        } finally {
            try {
                s1.close();
                s2.close();
            } catch (IOException ignored) {}
        }
    }
}
