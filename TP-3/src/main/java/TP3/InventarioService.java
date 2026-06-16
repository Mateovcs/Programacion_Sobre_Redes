package TP3;

import TP3.Producto;
import TP3.ConsolaUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InventarioService {
    private static final String FILE_NAME = "Inventario.dat";

    public InventarioService() {
        crearArchivoSiNoExiste();
    }

    private void crearArchivoSiNoExiste() {
        File file = new File(FILE_NAME);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println(ConsolaUtils.ERROR + "Error al inicializar el archivo." + ConsolaUtils.RESET);
        }
    }

    // Cargar todos los productos desde el archivo a memoria
    private List<Producto> cargarProductos() {
        List<Producto> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] campos = linea.split(";");
                if (campos.length == 4) {
                    String nombre = campos[0];
                    float pCompra = Float.parseFloat(campos[1]);
                    float pVenta = Float.parseFloat(campos[2]);
                    int stock = Integer.parseInt(campos[3]);
                    lista.add(new Producto(nombre, pCompra, pVenta, stock));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println(ConsolaUtils.ERROR + "Error al leer los datos." + ConsolaUtils.RESET);
        }
        return lista;
    }

    // Guardar la lista completa de regreso al archivo
    private void guardarTodosLosProductos(List<Producto> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Producto p : lista) {
                bw.write(p.toFileFormat());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println(ConsolaUtils.ERROR + "Error al escribir en el archivo." + ConsolaUtils.RESET);
        }
    }

    public void agregarProducto() {
        System.out.println("\n" + ConsolaUtils.TITULO + "--- REGISTRAR NUEVO ARTÍCULO ---" + ConsolaUtils.RESET);
        String nombre = ConsolaUtils.leerStringObligatorio("\tNombre: ");
        float pCompra = ConsolaUtils.leerDecimal("\tPrecio de Compra: ");
        float pVenta = ConsolaUtils.leerDecimal("\tPrecio de Venta: ");
        int stock = ConsolaUtils.leerEntero("\tStock: ");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            Producto nuevo = new Producto(nombre, pCompra, pVenta, stock);
            bw.write(nuevo.toFileFormat());
            bw.newLine();
            System.out.println(ConsolaUtils.EXITO + "\n\t¡Producto agregado con éxito!" + ConsolaUtils.RESET);
        } catch (IOException e) {
            System.out.println(ConsolaUtils.ERROR + "\tNo se pudo guardar el producto." + ConsolaUtils.RESET);
        }
    }

    public void mostrarInventario() {
        List<Producto> lista = cargarProductos();
        System.out.println("\n" + ConsolaUtils.TITULO + "-------------------------------------------------------------");
        System.out.printf("%-20s %-15s %-15s %-10s\n", "NOMBRE", "P. COMPRA", "P. VENTA", "STOCK");
        System.out.println("-------------------------------------------------------------" + ConsolaUtils.RESET);

        if (lista.isEmpty()) {
            System.out.println("\t[El inventario está vacío]");
        } else {
            for (Producto p : lista) {
                System.out.printf("%-20s $%-14.2f $%-14.2f %-10d\n", 
                        p.getNombre(), p.getPrecioCompra(), p.getPrecioVenta(), p.getStock());
            }
        }
        System.out.println(ConsolaUtils.TITULO + "-------------------------------------------------------------\n" + ConsolaUtils.RESET);
    }

    public void editarProducto() {
        System.out.println("\n" + ConsolaUtils.TITULO + "--- EDITAR PRODUCTO ---" + ConsolaUtils.RESET);
        String nombreBuscar = ConsolaUtils.leerString("Ingrese el nombre del producto a editar: ");
        List<Producto> lista = cargarProductos();
        boolean encontrado = false;

        for (Producto p : lista) {
            if (p.getNombre().equalsIgnoreCase(nombreBuscar)) {
                encontrado = true;
                System.out.println(ConsolaUtils.EXITO + "\tProducto encontrado. Ingrese los nuevos datos:" + ConsolaUtils.RESET);
                p.setPrecioCompra(ConsolaUtils.leerDecimal("\tNuevo Precio de Compra: "));
                p.setPrecioVenta(ConsolaUtils.leerDecimal("\tNuevo Precio de Venta: "));
                p.setStock(ConsolaUtils.leerEntero("\tNuevo Stock: "));
                break;
            }
        }

        if (encontrado) {
            guardarTodosLosProductos(lista);
            System.out.println(ConsolaUtils.EXITO + "\t¡Producto modificado correctamente!" + ConsolaUtils.RESET);
        } else {
            System.out.println(ConsolaUtils.ERROR + "\tProducto no encontrado." + ConsolaUtils.RESET);
        }
    }

    public void eliminarProducto() {
        System.out.println("\n" + ConsolaUtils.TITULO + "--- ELIMINAR PRODUCTO ---" + ConsolaUtils.RESET);
        String nombreBuscar = ConsolaUtils.leerString("Ingrese el nombre del producto a eliminar: ");
        List<Producto> lista = cargarProductos();
        boolean eliminado = lista.removeIf(p -> p.getNombre().equalsIgnoreCase(nombreBuscar));

        if (eliminado) {
            guardarTodosLosProductos(lista);
            System.out.println(ConsolaUtils.EXITO + "\t¡Producto eliminado correctamente del archivo!" + ConsolaUtils.RESET);
        } else {
            System.out.println(ConsolaUtils.ERROR + "\tProducto no encontrado." + ConsolaUtils.RESET);
        }
    }
}