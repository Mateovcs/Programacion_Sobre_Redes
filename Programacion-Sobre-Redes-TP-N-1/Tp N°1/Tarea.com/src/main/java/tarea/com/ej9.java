package tarea.com;

import java.io.*;
import java.util.*;

public class ej9 {

    public void ejecutar() throws Exception {

        BufferedReader lector = Utiles.getLector();
        PrintWriter out = Utiles.getOut();

        HashMap<String, String> dic = new HashMap<>();
        dic.put("hola", "hello");
        dic.put("mundo", "world");

        out.println("Ingrese frase:");
        String frase = lector.readLine();

        String[] palabras = frase.split(" ");

        for (String p : palabras) {
            if (dic.containsKey(p))
                out.print(dic.get(p) + " ");
            else
                out.print("[???] ");
        }
    }
}