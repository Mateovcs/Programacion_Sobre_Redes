package tarea.com;

import java.io.*;
import java.util.*;

public class ej8 {

    public void ejecutar() throws Exception {

        BufferedReader lector = Utiles.getLector();
        PrintWriter out = Utiles.getOut();

        HashMap<String, String> dic = new HashMap<>();
        dic.put("hola", "hello");
        dic.put("chau", "bye");

        out.println("Palabra a traducir:");
        String p = lector.readLine();

        if (dic.containsKey(p)) {
            out.println("Traducción: " + dic.get(p));
        } else {
            out.println("No existe en el diccionario");
        }
    }
}