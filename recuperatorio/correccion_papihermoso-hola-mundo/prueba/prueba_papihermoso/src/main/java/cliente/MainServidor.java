package cliente;

import java.io.IOException;

// Código único otorgado por el servidor del profesor: [PEGAR_CODIGO_AQUI]
public class MainServidor {

    public static final int PUERTO = 5000;

    public static void main(String[] args) {
        System.out.println(Cifrado.VERDE + "INICIANDO SERVIDOR LOCAL" + Cifrado.RESET);

        try (Servidor servidor = new Servidor(PUERTO)) {

            Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    servidor.close();
                }, "CIERRE-SERVIDOR")
            );

            servidor.iniciar();

        } catch (IOException e) {
            System.err.println(
                Cifrado.ROJO + "No se pudo iniciar el servidor: " + e.getMessage() + Cifrado.RESET
            );
        }
    }
}
