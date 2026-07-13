package com.examen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Gestiona la coleccion de partidas cargadas desde el archivo CSV estandarizado.
 * Provee metodos para cargar, mostrar, analizar y guardar los datos.
 */
public class GestorPartidas {

    private ArrayList<Partida> partidas;
    private static final Logger logger = Logger.getLogger(GestorPartidas.class.getName());

    static {
        try {
            // Configura el logger para redirigir errores a crash.log silenciosamente
            FileHandler fh = new FileHandler("crash.log", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setUseParentHandlers(false);
        } catch (Exception e) {
            System.err.println("No se pudo inicializar el sistema de logs: " + e.getMessage());
        }
    }

    /**
     * Crea un gestor vacio. Inicializa la lista de partidas.
     */
    public GestorPartidas() {
        this.partidas = new ArrayList<>();
    }

    /**
     * Carga los datos desde el archivo CSV estandarizado.
     * Lee cada linea, la parsea y crea objetos Partida.
     * La primera linea (encabezados) debe ignorarse.
     * <p>
     * Formato esperado: dd/MM ; 1 ; 0 ; 1 ; 4 ; 7 ; 5
     * <p>
     * Los errores deben registrarse en crash.log sin mostrar en consola.
     *
     * @param rutaCsv ruta del archivo CSV a cargar
     */
    public void cargar(String rutaCsv) {
        try {
            List<String> lineas = Files.readAllLines(Paths.get(rutaCsv));
            
            // Si el archivo está vacío o solo tiene cabecera, no hace nada
            if (lineas.size() <= 1) return;

            // Ignoramos la primera línea (index 0) que contiene los encabezados
            for (int i = 1; i < lineas.size(); i++) {
                String linea = lineas.get(i).trim();
                if (linea.isEmpty()) continue;

                // Separamos por " ; " respetando los espacios del estandarizador
                String[] datos = linea.split(" ; ");
                
                // Mapeo de datos (Ajustar según los atributos exactos de tu clase Partida)
                String fecha = datos[0].trim();
                int p1 = Integer.parseInt(datos[1].trim());
                int p2 = Integer.parseInt(datos[2].trim());
                int p3 = Integer.parseInt(datos[3].trim());
                int p4 = Integer.parseInt(datos[4].trim());
                int p5 = Integer.parseInt(datos[5].trim());
                int p6 = Integer.parseInt(datos[6].trim());

                // Creamos el objeto y lo agregamos a la lista
                Partida partida = new Partida(fecha, p1, p2, p3, p4, p5, p6);
                partidas.add(partida);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al cargar el archivo CSV: " + rutaCsv, ex);
        }
    }

    /**
     * Muestra todas las partidas cargadas en consola de forma ordenada.
     * Si no hay partidas, muestra un mensaje indicandolo.
     */
    public void mostrarTodos() {
        if (partidas.isEmpty()) {
            System.out.println("No hay partidas cargadas para mostrar.");
            return;
        }

        System.out.println("=================================================================");
        System.out.printf("%-10s | %-6s | %-6s | %-6s | %-6s | %-6s | %-6s%n", 
                "FECHA", "DATO1", "DATO2", "DATO3", "DATO4", "DATO5", "DATO6");
        System.out.println("=================================================================");
        
        for (Partida p : partidas) {
            // Reemplazar los "get" por las propiedades reales de tu clase Partida
            System.out.printf("%-10s | %-6d | %-6d | %-6d | %-6d | %-6d | %-6d%n",
                    p.getFecha(), p.getDato1(), p.getDato2(), p.getDato3(), 
                    p.getDato4(), p.getDato1(), p.getDato6());
        }
        System.out.println("=================================================================");
    }

    /**
     * Analiza las partidas y determina en que mes hubo mas victorias.
     * La fecha tiene formato dd/MM (el mes esta despues de la barra).
     * <p>
     * En caso de empate, devuelve cualquiera de los meses.
     *
     * @return String con numero de mes (dos digitos, ej: "04").
     *         Si no hay partidas, retorna "SIN DATOS".
     */
    public String mesMasVictorias() {
        if (partidas.isEmpty()) {
            return "SIN DATOS";
        }

        // Mapa para acumular las victorias por cada mes ("01", "02", etc.)
        Map<String, Integer> victoriasPorMes = new HashMap<>();

        for (Partida p : partidas) {
            // Extrae el mes desde el formato dd/MM
            String[] partesFecha = p.getFecha().split("/");
            if (partesFecha.length < 2) continue;
            
            String mes = partesFecha[1].trim();

            // Suposición estándar: El método esVictoria() determina si se ganó.
            // Si en tu modelo la victoria se define por el valor de una columna (ej: dato1 == 1), 
            // cambiá p.esVictoria() por esa condición.
            if (p.esVictoria()) { 
                victoriasPorMes.put(mes, victoriasPorMes.getOrDefault(mes, 0) + 1);
            }
        }

        if (victoriasPorMes.isEmpty()) {
            return "01"; // Retorna un mes por defecto si no hubo ninguna victoria registrada
        }

        // Buscar el mes con el número máximo de victorias
        String mesMax = "SIN DATOS";
        int maxVictorias = -1;

        for (Map.Entry<String, Integer> entrada : victoriasPorMes.entrySet()) {
            if (entrada.getValue() > maxVictorias) {
                maxVictorias = entrada.getValue();
                mesMax = entrada.getKey();
            }
        }

        return mesMax;
    }

    /**
     * Guarda todas las partidas en el archivo CSV.
     * Primero escribe la linea de encabezados y luego cada partida.
     * Si el archivo ya existe, se sobrescribe.
     *
     * @param rutaCsv ruta del archivo CSV donde guardar
     */
    public void guardar(String rutaCsv) {
        try {
            List<String> lineas = new ArrayList<>();
            
            // Escribimos la cabecera simulando el formato original
            lineas.add("FECHA ; DATO1 ; DATO2 ; DATO3 ; DATO4 ; DATO5 ; DATO6");
            
            // Agregamos cada partida formateada con el separador " ; "
            for (Partida p : partidas) {
                String linea = String.format("%s ; %d ; %d ; %d ; %d ; %d ; %d",
                        p.getFecha(), p.getDato1(), p.getDato2(), p.getDato3(),
                        p.getDato4(), p.getDato1(), p.getDato6());
                lineas.add(linea);
            }
            
            // Escribe (y sobrescribe si ya existe) el archivo CSV
            Files.write(Paths.get(rutaCsv), lineas);
            
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error al guardar el archivo CSV: " + rutaCsv, ex);
        }
    }

    /**
     * Elimina una partida de la lista segun el indice indicado.
     * Valida que el indice sea valido antes de eliminar.
     * Si el indice es invalido, muestra un mensaje de error.
     *
     * @param indice posicion de la partida a eliminar (base 0)
     */
    public void eliminar(int indice) {
        if (indice >= 0 && indice < partidas.size()) {
            partidas.remove(indice);
            System.out.println("Partida eliminada correctamente de la posición: " + indice);
        } else {
            System.out.println("Error: El índice " + indice + " es inválido. Rango permitido: 0 a " + (partidas.size() - 1));
        }
    }

    /**
     * @return cantidad de partidas cargadas
     */
    public int cantidadPartidas() {
        return partidas.size();
    }

    /**
     * @return la lista interna de partidas
     */
    public List<Partida> getPartidas() {
        return partidas;
    }
}
