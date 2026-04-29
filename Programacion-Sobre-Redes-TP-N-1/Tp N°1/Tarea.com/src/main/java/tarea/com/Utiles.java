package tarea.com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Utiles {

    private static BufferedReader lector;
    private static PrintWriter out;

    static {
        lector = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);
    }

    public static BufferedReader getLector() {
        return lector;
    }

    public static PrintWriter getOut() {
        return out;
    }
}