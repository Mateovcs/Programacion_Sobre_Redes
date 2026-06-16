package TP3;

import TP3.InventarioService;
import TP3.ConsolaUtils;

public class InventarioApplication {

    public static void main(String[] args) {
        InventarioService servicio = new InventarioService();
        boolean ejecutar = true;

        while (ejecutar) {
            System.out.println(ConsolaUtils.TITULO + "=====================================" + ConsolaUtils.RESET);
            System.out.println(ConsolaUtils.TITULO + "       SISTEMA DE INVENTARIO         " + ConsolaUtils.RESET);
            System.out.println(ConsolaUtils.TITULO + "=====================================" + ConsolaUtils.RESET);
            System.out.println("\t1. Agregar producto");
            System.out.println("\t2. Mostrar inventario");
            System.out.println("\t3. Editar producto");
            System.out.println("\t4. Eliminar producto");
            System.out.println("\t5. Salir");
            System.out.println(ConsolaUtils.TITULO + "-------------------------------------" + ConsolaUtils.RESET);

            int opcion = ConsolaUtils.leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> servicio.agregarProducto();
                case 2 -> servicio.mostrarInventario();
                case 3 -> servicio.editarProducto();
                case 4 -> servicio.eliminarProducto();
                case 5 -> {
                    System.out.println(ConsolaUtils.EXITO + "\nSaliendo del sistema de forma segura..." + ConsolaUtils.RESET);
                    ejecutar = false;
                }
                default -> System.out.println(ConsolaUtils.ERROR + "Opción inválida. Intente de nuevo." + ConsolaUtils.RESET);
            }
            System.out.println(); // Salto de línea estético
        }
    }
}