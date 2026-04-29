package tarea.com;

import java.io.*;
import java.util.*;

public class ej11 {

    public void ejecutar() throws Exception {

        BufferedReader lector = Utiles.getLector();
        PrintWriter out = Utiles.getOut();

        ArrayList<Integer> nums = new ArrayList<>();

        out.println("Ingrese números (0 corta):");
        int n;
        while ((n = Integer.parseInt(lector.readLine())) != 0) {
            nums.add(n);
        }

        HashSet<Integer> sinRep = new HashSet<>(nums);

        out.println("Original: " + nums);
        out.println("Sin repetidos: " + sinRep);
    }
}