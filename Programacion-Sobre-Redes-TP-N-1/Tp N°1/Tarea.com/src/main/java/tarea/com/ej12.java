package tarea.com;

import java.io.*;
import java.util.*;

public class ej12 {

    public void ejecutar() throws Exception {

        BufferedReader lector = Utiles.getLector();
        PrintWriter out = Utiles.getOut();

        ArrayList<String> alumnos = new ArrayList<>();
        HashMap<String, Integer> notas = new HashMap<>();

        out.println("Nombre alumno:");
        String nom = lector.readLine();
        out.println("Nota:");
        int nota = Integer.parseInt(lector.readLine());

        alumnos.add(nom);
        notas.put(nom, nota);

        for (String a : alumnos) {
            int n = notas.get(a);
            String estado = (n >= 6) ? "Aprobado" : "Desaprobado";
            out.println(a + " -> " + n + " (" + estado + ")");
        }
    }
}
