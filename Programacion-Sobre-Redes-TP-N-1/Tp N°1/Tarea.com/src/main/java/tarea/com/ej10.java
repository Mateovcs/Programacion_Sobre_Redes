package tarea.com;

import java.io.*;
import java.util.*;

public class ej10 {

    public void ejecutar() throws Exception {

        BufferedReader lector = Utiles.getLector();
        PrintWriter out = Utiles.getOut();

        out.println("Ingrese frase:");
        String frase = lector.readLine();

        String[] palabras = frase.split(" ");
        HashMap<String, Integer> cont = new HashMap<>();

        for (String p : palabras) {
            cont.put(p, cont.getOrDefault(p, 0) + 1);
        }

        for (String k : cont.keySet()) {
            out.println(k + ": " + cont.get(k));
        }
    }
}