package tp3;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class inventario {
    private static final String ARCHIVO_INVENTARIO = "Inventario.dat";

    public static void main(String[] args) {
        crearArchivoSiNoExiste();
        int opcion = 0;

        do {
            mostrarMenu();
            String entradaOpcion = lector_consola.leerTexto();
            
            if (lector_consola.esEntero(entradaOpcion)) {
                opcion = Integer.parseInt(entradaOpcion);
                ejecutarOpcion(opcion);
            } else {
                System.out.println("\n[!] Por favor, ingrese un número de opción válido.");
            }
        } while (opcion != 5);
    }

    private static void mostrarMenu() {
        System.out.println("\n========================================");
        System.out.println("    SISTEMA DE GESTIÓN DE INVENTARIO    ");
        System.out.println("========================================");
        System.out.println(" 1. Agregar producto");
        System.out.println(" 2. Mostrar productos");
        System.out.println(" 3. Editar producto");
        System.out.println(" 4. Eliminar producto");
        System.out.println(" 5. Salir");
        System.out.println("----------------------------------------");
        System.out.print("Seleccione una opción: ");
    }

    private static void ejecutarOpcion(int opcion) {
        System.out.println(); //salto de linea
        switch (opcion) {
            case 1: agregarProducto (); break;
            case 2: mostrarProductos(); break;
            case 3: editarProducto(); break;
            case 4: eliminarProducto(); break;
            case 5: System.out.println("[i] sistema finalizado"); break;
            default: System.out.println("[!] opcion invalida");
        }
    }

    // ================= MÉTODOS DE PERSISTENCIA Y LÓGICA ================= //

    private static void crearArchivoSiNoExiste() {
        try {
            File archivo = new File(ARCHIVO_INVENTARIO);
            if (archivo.createNewFile()) {
                System.out.println("[i] Archivo Inventario hecho");
            }
        } catch (IOException e) {
            System.out.println("[Error] Al gestionar el archivo: " + e.getMessage());
        }
    }

    private static void agregarProducto() {
        System.out.println("--- AGREGAR NUEVO PRODUCTO ---");
        System.out.print("Nombre del producto: ");
        String nombre = lector_consola.leerTexto();
        
        float pCompra = lector_consola.leerDecimal("Precio de Compra: ");
        float pVenta = lector_consola.leerDecimal("Precio de Venta: ");
        int stock = lector_consola.leerEntero("Cantidad en Stock: ");

        producto nuevoProducto = new producto(nombre, pCompra, pVenta, stock);

        try (FileWriter fw = new FileWriter(ARCHIVO_INVENTARIO, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            
            out.println(nuevoProducto.aFormatoArchivo());
            System.out.println("\n[+] Producto registrado correctamente.");
        } catch (IOException e) {
            System.out.println("[Error] Al guardar: " + e.getMessage());
        }
    }

    private static void mostrarProductos() {
        System.out.println("--- LISTADO DE PRODUCTOS ---");
        System.out.println(String.format("%-15s | \t%-13s | \t%-13s | \t%s", "NOMBRE", "P. COMPRA", "P. VENTA", "STOCK"));
        System.out.println("-------------------------------------------------------------------------");
        
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_INVENTARIO))) {
            String linea;
            boolean hayProductos = false;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 4) {
                    producto p = new producto(partes[0], Float.parseFloat(partes[1]), Float.parseFloat(partes[2]), Integer.parseInt(partes[3]));
                    System.out.println(p.aFormatoConsola());
                    hayProductos = true;
                }
            }
            if (!hayProductos) {
                System.out.println("[i] No hay productos registrados en el inventario.");
            }
        } catch (IOException e) {
            System.out.println("[Error] Al leer el archivo: " + e.getMessage());
        }
    }

    private static void editarProducto() {
        System.out.println("--- EDITAR PRODUCTO ---");
        System.out.print("Ingrese el nombre exacto del producto a editar: ");
        String nombreBusqueda = lector_consola.leerTexto();
        
        List<String> lineas = new ArrayList<>();
        boolean encontrado = false;

        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_INVENTARIO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes[0].equalsIgnoreCase(nombreBusqueda)) {
                    encontrado = true;
                    System.out.println("[i] Producto encontrado. Ingrese los nuevos datos:");
                    float pCompra = lector_consola.leerDecimal("Nuevo Precio de Compra: ");
                    float pVenta = lector_consola.leerDecimal("Nuevo Precio de Venta: ");
                    int stock = lector_consola.leerEntero("Nuevo Stock: ");
                    
                    producto pEditado = new producto(partes[0], pCompra, pVenta, stock);
                    lineas.add(pEditado.aFormatoArchivo());
                } else {
                    lineas.add(linea);
                }
            }
        } catch (IOException e) {
            System.out.println("[Error] al leer el archivo");
            return;
        }

        if (encontrado) {
            reescribirArchivo(lineas);
            System.out.println("\n[*] producto actualizado correctamente");
        } else {
            System.out.println("[!] producto no encontrado");
        }
    }

    private static void eliminarProducto() {
        System.out.println("--- ELIMINAR PRODUCTO ---");
        System.out.print("Ingrese el nombre exacto del producto a eliminar: ");
        String nombreBusqueda = lector_consola.leerTexto();
        
        List<String> lineas = new ArrayList<>();
        boolean encontrado = false;

        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_INVENTARIO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (!partes[0].equalsIgnoreCase(nombreBusqueda)) {
                    lineas.add(linea);
                } else {
                    encontrado = true;
                }
            }
        } catch (IOException e) {
            System.out.println("[Error] al leer el archivo");
            return;
        }

        if (encontrado) {
            reescribirArchivo(lineas);
            System.out.println("\n[-] producto eliminado correctamente");
        } else {
            System.out.println("[!] producto no encontrado");
        }
    }

    private static void reescribirArchivo(List<String> lineas) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_INVENTARIO))) {
            for (String linea : lineas) {
                pw.println(linea);
            }
        } catch (IOException e) {
            System.out.println("[Error] Al actualizar el archivo.");
        }
    }
}