package tarea.com;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class main {

    public static void main(String[] args) throws Exception {

        BufferedReader lector = Utiles.getLector();
        PrintWriter out = Utiles.getOut();

        int opcion;

        do {
            out.println("\n===== MENÚ EJERCICIOS COLECCIONES =====");
            out.println("1  - Lista de nombres");
            out.println("2  - Buscar elemento");
            out.println("3  - Modificar lista");
            out.println("4  - Recorrer lista");
            out.println("5  - Procesar texto");
            out.println("6  - Normalización");
            out.println("7  - Diccionario");
            out.println("8  - Traductor palabra");
            out.println("9  - Traductor de frases");
            out.println("10 - Contador de palabras");
            out.println("11 - Eliminar duplicados");
            out.println("12 - Sistema de alumnos");
            out.println("0  - Salir");
            out.print("Elegí una opción: ");

            opcion = Integer.parseInt(lector.readLine());

            out.println("\n----------------------------------------\n");

            switch (opcion) {
                case 1:
                    new ej1().ejecutar();
                    break;
                case 2:
                    new ej2().ejecutar();
                    break;
                case 3:
                    new ej3().ejecutar();
                    break;
                case 4:
                    new ej4().ejecutar();
                    break;
                case 5:
                    new ej5().ejecutar();
                    break;
                case 6:
                    new ej6().ejecutar();
                    break;
                case 7:
                    new ej7().ejecutar();
                    break;
                case 8:
                    new ej8().ejecutar();
                    break;
                case 9:
                    new ej9().ejecutar();
                    break;
                case 10:
                    new ej10().ejecutar();
                    break;
                case 11:
                    new ej11().ejecutar();
                    break;
                case 12:
                    new ej12().ejecutar();
                    break;
                case 0:
                    out.println("Fin del programa.");
                    break;
                default:
                    out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }
}