package tarea.com;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ej1 {

    public void ejecutar() throws Exception {

        BufferedReader lector = Utiles.getLector();
        PrintWriter out = Utiles.getOut();

        ArrayList<String> nombres = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            out.println("Ingrese un nombre:");
            nombres.add(lector.readLine());
        }

  
        out.println("Lista completa:");
        out.println(nombres);

        out.println("Cantidad de elementos: " + nombres.size());

        out.println("Primero: " + nombres.get(0));
        out.println("Último: " + nombres.get(nombres.size() - 1));

        out.println("Nombres en mayúsculas:");
        for (String n : nombres) {
            out.println(n.toUpperCase());
        }
    }
}