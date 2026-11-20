package cliente;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class Servidor implements AutoCloseable {

    private final ServerSocket server;
    private volatile boolean ejecutando = true;
    private final List<ManejadorCliente> clientes = new CopyOnWriteArrayList<>();

    public Servidor(int puerto) throws IOException {
        server = new ServerSocket(puerto);
    }

    public void iniciar() {
        System.out.println(
                Cifrado.VERDE
                + "Servidor escuchando en puerto "
                + server.getLocalPort()
                + Cifrado.RESET
        );

        while (ejecutando) {
            try {
                Socket socket = server.accept();

                System.out.println(
                        Cifrado.CIAN
                        + "\n[CONEXIÓN] "
                        + socket.getInetAddress().getHostAddress()
                        + ":" + socket.getPort()
                        + Cifrado.RESET
                );

                ManejadorCliente cliente = new ManejadorCliente(socket, this);
                clientes.add(cliente);
                cliente.iniciar();

            } catch (IOException e) {
                if (ejecutando) {
                    System.err.println(
                            Cifrado.ROJO
                            + "[ERROR] " + e.getMessage()
                            + Cifrado.RESET
                    );
                }
                break;
            }
        }
    }

    public void difundir(String mensaje) {
        for (ManejadorCliente cliente : clientes) {
            cliente.enviar(mensaje);
        }

        System.out.println(
                Cifrado.AMARILLO
                + "\t[CHAT] " + mensaje
                + Cifrado.RESET
        );
    }

    public void desconectar(ManejadorCliente cliente) {
        if (clientes.remove(cliente)) {
            System.out.println(
                    Cifrado.MORADO
                    + "[DESCONECTADO] Cliente " + cliente.getSock().getPort()
                    + Cifrado.RESET
            );
        }
    }

    @Override
    public void close() {
        ejecutando = false;

        try {
            if (!server.isClosed()) {
                server.close();
            }
        } catch (IOException e) {
            System.err.println("Error cerrando el servidor.");
        }

        for (ManejadorCliente cliente : clientes) {
            cliente.cerrar();
        }

        clientes.clear();

        System.out.println(
                Cifrado.ROJO
                + "Servidor detenido."
                + Cifrado.RESET
        );
    }
}
