package com.examen;

/**
 * Punto de entrada principal del programa.
 * <p>
 * Orquesta la ejecucion de forma automatizada y secuencial sin intervencion del usuario:
 * 1. Inicializa LogManager.
 * 2. Crea un Estandarizador y estandariza juegos.dat.
 * 3. Crea un GestorPartidas y carga los datos.
 * 4. Muestra y analiza las partidas.
 * 5. Guarda las partidas y finaliza.
 */
public class Main {

    public static void main(String[] args) {
        // 1. Inicializar el sistema de logs
        LogManager.inicializar();
        LogManager.registrarInfo("Iniciando ejecución automatizada del programa.");

        System.out.println("=== INICIANDO PROCESAMIENTO ===");

        // 2. Crear Estandarizador y procesar el archivo original
        Estandarizador estandarizador = new Estandarizador();
        String rutaOriginal = "juegos.dat";
        String rutaCsv = "juegos.csv";
        
        System.out.println("[1/5] Estandarizando archivo '" + rutaOriginal + "'...");
        try {
            estandarizador.estandarizar(rutaOriginal);
            System.out.println("      -> Éxito: Archivo convertido a '" + rutaCsv + "' y original eliminado.");
        } catch (Exception e) {
            System.out.println("      -> Nota: No se pudo procesar (posiblemente ya fue estandarizado).");
        }

        // 3. Crear GestorPartidas y cargar la información estandarizada
        GestorPartidas gestor = new GestorPartidas();
        System.out.println("[2/5] Cargando registros en el sistema...");
        gestor.cargar(rutaCsv);
        System.out.println("      -> Total de partidas cargadas: " + gestor.cantidadPartidas());

        // 4. Mostrar y Analizar los datos cargados
        System.out.println("[3/5] Listado de partidas procesadas:");
        gestor.mostrarTodos();

        System.out.println("[4/5] Analizando rendimiento por meses...");
        String mesMax = gestor.mesMasVictorias();
        System.out.println("      -> El mes con mayor número de victorias es: " + mesMax);

        // 5. Guardar los datos de vuelta en el archivo CSV para asegurar la persistencia
        System.out.println("[5/5] Resguardando datos en '" + rutaCsv + "'...");
        gestor.guardar(rutaCsv);
        System.out.println("      -> Persistencia completada correctamente.");

        System.out.println("=== PROCESO FINALIZADO CON ÉXITO ===");
        LogManager.registrarInfo("Ejecución automatizada finalizada correctamente.");
    }
}
