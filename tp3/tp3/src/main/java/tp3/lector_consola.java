package tp3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class lector_consola {
    private static final BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));

    // metodo principal de lectura (retorna String)
    public static String leerTexto() {
        try {
            return lector.readLine().trim();
        } catch (IOException e) {
            System.out.println("\033[31m[Error de lectura en consola]\033[0m");
            return "";
        }
    }

    // Validadores de tipo de dato si es entero, decimal, etc
    public static boolean esEntero(String texto) {
        try {
            Integer.parseInt(texto);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean esDecimal(String texto) {
        try {
            Float.parseFloat(texto);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Conversiones seguras obligando al usuario a ingresar el dato correcto
    public static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = leerTexto();
            if (esEntero(entrada)) {
                return Integer.parseInt(entrada);
            }
            System.out.println("\033[31mError: El valor ingresado no es un número entero válido. Intente de nuevo.\033[0m");
        }
    }

    public static float leerDecimal(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = leerTexto();
            if (esDecimal(entrada)) {
                return Float.parseFloat(entrada);
            }
            System.out.println("\033[31mError: El valor ingresado no es un número decimal válido. Intente de nuevo.\033[0m");
        }
    }
}