package tarea.com;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ej2 {

    public void ejecutar() throws Exception {

        BufferedReader lector = Utiles.getLector();
        PrintWriter out = Utiles.getOut();

        ArrayList<String> nombres = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            out.println("Ingrese un nombre:");
            nombres.add(lector.readLine());
        }

        out.println("Nombre a buscar:");
        String buscar = lector.readLine();

        if (nombres.contains(buscar)) {
            out.println("Existe en la posición: " + nombres.indexOf(buscar));
        } else {
            out.println("\u001B[31mNo existe en la lista\u001B[0m");
        }
    }
}