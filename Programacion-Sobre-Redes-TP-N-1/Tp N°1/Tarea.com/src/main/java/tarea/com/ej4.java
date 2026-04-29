package tarea.com;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class ej4 {

    public void ejecutar() throws Exception {

        BufferedReader lector = Utiles.getLector();
        PrintWriter out = Utiles.getOut();

        ArrayList<String> nombres = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            out.println("Ingrese un nombre:");
            nombres.add(lector.readLine());
        }

        out.println("For clásico:");
        for (int i = 0; i < nombres.size(); i++) {
            out.println("\u001B[34m" + nombres.get(i) + "\u001B[0m");
        }

        out.println("For-each:");
        for (String n : nombres) {
            out.println("\u001B[32m" + n + "\u001B[0m");
        }

        out.println("Iterator:");
        Iterator<String> it = nombres.iterator();
        while (it.hasNext()) {
            out.println("\u001B[35m" + it.next() + "\u001B[0m");
        }
    }
}
