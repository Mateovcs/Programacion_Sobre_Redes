package tarea.com;

import java.io.*;
import java.util.*;

public class ej7 {

    public void ejecutar() throws Exception {

        BufferedReader lector = Utiles.getLector();
        PrintWriter out = Utiles.getOut();

        HashMap<String, String> dic = new HashMap<>();

        for (int i = 0; i < 5; i++) {
            out.println("Palabra en español:");
            String esp = lector.readLine();
            out.println("Traducción:");
            String ing = lector.readLine();
            dic.put(esp, ing);
        }

        out.println("Diccionario completo: " + dic.entrySet());
        out.println("Claves: " + dic.keySet());
        out.println("Valores: " + dic.values());
    }
}