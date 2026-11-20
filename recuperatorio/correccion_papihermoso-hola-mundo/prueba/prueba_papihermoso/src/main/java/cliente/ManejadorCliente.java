package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class ManejadorCliente implements Runnable {

    private static final String BIENVENIDA = "BIENVENIDO AL SERVIDOR LOCAL";
    private static String CLAVE = "ClaveSecreta1234";

    private Socket sock;
    private BufferedReader entrada;
    private PrintWriter salida;
    private AtomicBoolean conectado = new AtomicBoolean(true);
    private Servidor server;
    private Thread hilo;

    public ManejadorCliente(Socket soc, Servidor ser) throws IOException {
        this.sock = soc;
        this.server = ser;
        this.entrada = new BufferedReader(
                new InputStreamReader(sock.getInputStream(), StandardCharsets.UTF_8)
        );
        this.salida = new PrintWriter(
                new OutputStreamWriter(sock.getOutputStream(), StandardCharsets.UTF_8),
                true
        );
    }

    public void iniciar() {
        hilo = new Thread(this, "CLIENTE-" + sock.getPort());
        hilo.start();
    }

    @Override
    public void run() {
        try {
            // El primer mensaje de bienvenida siempre viaja en texto plano
            enviar(BIENVENIDA);

            String linea;
            while (conectado.get() && (linea = entrada.readLine()) != null) {
                // Si viene cifrado con formato IV:Mensaje lo descifra; si viene plano lo usa tal cual
                String desencriptado = Cifrado.decriptar(CLAVE, linea);
                String msg = (desencriptado != null ? desencriptado : linea).trim();

                if (msg.isEmpty()) {
                    continue;
                }

                if (msg.startsWith("/")) {
                    if (!procesarComando(msg)) {
                        break;
                    }
                } else {
                    // Mensajes comunes se reenvían a todos los conectados (incluido el emisor)
                    server.difundir(msg);
                }
            }
        } catch (IOException e) {
            if (conectado.get()) {
                System.err.println(
                        Cifrado.ROJO + "[ALERTA] Conexión cerrada abruptamente." + Cifrado.RESET
                );
            }
        } finally {
            cerrar();
        }
    }

    private boolean procesarComando(String msg) {
        String minusculas = msg.toLowerCase(Locale.ROOT);

        if (minusculas.equals("/hi")) {
            enviar(BIENVENIDA);
            return true;
        }

        if (minusculas.equals("/logout")) {
            enviar("Desconexion aprobada. ¡Adios!");
            return false;
        }

        enviar("COMANDO DESCONOCIDO");
        return true;
    }

    public synchronized boolean enviar(String msg) {
        if (!conectado.get()) {
            return false;
        }
        salida.println(msg);
        return !salida.checkError();
    }

    public void cerrar() {
        if (!conectado.compareAndSet(true, false)) {
            return;
        }

        try {
            sock.close();
        } catch (IOException e) {
            System.err.println("Error cerrando el socket.");
        }

        server.desconectar(this);
    }

    public Socket getSock() {
        return sock;
    }
}
