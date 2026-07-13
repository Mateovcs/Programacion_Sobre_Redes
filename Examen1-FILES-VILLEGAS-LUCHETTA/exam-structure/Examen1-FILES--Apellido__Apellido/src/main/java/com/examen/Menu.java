package com.examen;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Menu principal de la aplicacion.
 * Presenta opciones al usuario y ejecuta las acciones correspondientes.
 * <p>
 * NO se permite usar la clase Scanner. Se utiliza BufferedReader + InputStreamReader.
 * <p>
 * Opciones:
 * 1 - Mostrar todas las partidas
 * 2 - Eliminar 1 partida elegida por el usuario
 * 3 - Ver mes con mayor cantidad de victorias
 * 4 - Salir (guarda datos y cierra)
 */
public class Menu {

    private GestorPartidas gestor;
    private BufferedReader reader;

    /**
     * Crea el menu asociado a un gestor de partidas.
     * Inicializa el BufferedReader para leer desde la consola.
     *
     * @param gestor el gestor de partidas
     */
    public Menu(GestorPartidas gestor) {
        this.gestor = gestor;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        // Se cargan las partidas inicialmente al arrancar el menú
        this.gestor.cargar("juegos.csv");
    }

    /**
     * Inicia el bucle principal del menu.
     * Muestra las opciones, solicita la eleccion al usuario y ejecuta la accion.
     * El bucle se repite hasta que el usuario elija la opcion 4 (Salir).
     */
    public void iniciar() {
        String opcion = "";
        
        do {
            System.out.println("\n=================================");
            System.out.println("   SISTEMA DE GESTIÓN DE PARTIDAS ");
            System.out.println("=================================");
            System.out.println("1 - Mostrar todas las partidas");
            System.out.println("2 - Eliminar 1 partida");
            System.out.println("3 - Ver mes con mayor cantidad de victorias");
            System.out.println("4 - Salir");
            System.out.print("Seleccione una opción: ");

            try {
                // Se utiliza el método estático de la clase Validador según la instrucción
                opcion = Validador.leerNoVacio(reader).trim();

                switch (opcion) {
                    case "1":
                        gestor.mostrarTodos();
                        break;
                        
                    case "2":
                        System.out.print("Ingrese el índice de la partida a eliminar (0 a " + (gestor.cantidadPartidas() - 1) + "): ");
                        String indiceTexto = Validador.leerNoVacio(reader).trim();
                        try {
                            int indice = Integer.parseInt(indiceTexto);
                            gestor.eliminar(indice);
                        } catch (NumberFormatException e) {
                            System.out.println("Error: El índice debe ser un número entero válido.");
                        }
                        break;
                        
                    case "3":
                        String mes = gestor.mesMasVictorias();
                        if ("SIN DATOS".equals(mes)) {
                            System.out.println("No hay suficientes datos o victorias registradas.");
                        } else {
                            System.out.println("El mes con mayor cantidad de victorias es: " + mes);
                        }
                        break;
                        
                    case "4":
                        System.out.println("Guardando cambios en 'juegos.csv'...");
                        gestor.guardar("juegos.csv");
                        System.out.println("Datos guardados con éxito. Saliendo de la aplicación.");
                        break;
                        
                    default:
                        System.out.println("Opción inválida. Intente de nuevo ingresando un número del 1 al 4.");
                        break;
                }
            } catch (Exception e) {
                LogManager.registrarError("Error crítico durante la ejecución del menú", e);
                System.out.println("Ocurrió un error inesperado al procesar la opción. Registrado en crash.log.");
            }

        } while (!"4".equals(opcion));
    }
}
