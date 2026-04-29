package tarea.com;

import java.io.*;
import java.util.*;

public class ej6 {

    public void ejecutar() throws Exception {

        BufferedReader lector = Utiles.getLector();
        PrintWriter out = Utiles.getOut();

        out.println("Ingrese una frase:");
        String frase = lector.readLine();

        String[] palabras = frase.split(" ");

        for (String p : palabras) {
            String n = p.toLowerCase().trim()
                    .replaceAll("[aeiou]", "*");
            out.println(n);
        }
    }
}