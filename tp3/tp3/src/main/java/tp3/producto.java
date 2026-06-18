package tp3;

public class producto {
    private String nombre;
    private float precioCompra;
    private float precioVenta;
    private int stock;

    public producto(String nombre, float precioCompra, float precioVenta, int stock) {
        this.nombre = nombre;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.stock = stock;
    }

    public String getNombre() { return nombre; }
    
    // Formato para guardar en Inventario.dat
    public String aFormatoArchivo() {
        return nombre + ";" + precioCompra + ";" + precioVenta + ";" + stock;
    }

    // Formato tabulado para mostrar en consola
    public String aFormatoConsola() {
        return String.format("%-15s | \t$%-12.2f | \t$%-12.2f | \t%d", nombre, precioCompra, precioVenta, stock);
    }
}