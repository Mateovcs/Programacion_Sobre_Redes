package tarea.com;

import java.io.*;
import java.util.*;

public class ej5 {

    public void ejecutar() throws Exception {

        BufferedReader lector = Utiles.getLector();
        PrintWriter out = Utiles.getOut();

        out.println("Ingrese una frase:");
        String frase = lector.readLine();

        String[] palabras = frase.split(" ");
        ArrayList<String> lista = new ArrayList<>(Arrays.asList(palabras));

        out.println("Cantidad de palabras: " + lista.size());

        String masLarga = "";
        for (String p : lista) {
            if (p.length() > masLarga.length()) masLarga = p;
        }

        out.println("Palabra más larga: " + masLarga);

        out.println("Letra a buscar:");
        char letra = lector.readLine().charAt(0);

        int c = 0;
        for (String p : lista) {
            if (p.contains(String.valueOf(letra))) c++;
        }

        out.println("Contienen esa letra: " + c);
    }
}