package tarea.com;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ej3 {

    public void ejecutar() throws Exception {

        BufferedReader lector = Utiles.getLector();
        PrintWriter out = Utiles.getOut();

        ArrayList<String> nombres = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            out.println("Ingrese un nombre:");
            nombres.add(lector.readLine());
        }

        // Reemplazar tercer elemento
        nombres.set(2, "Reemplazado");

        out.println("Nombre a eliminar:");
        String eliminar = lector.readLine();
        nombres.remove(eliminar);

        out.println("Lista final:");
        out.println(nombres);
    }
}