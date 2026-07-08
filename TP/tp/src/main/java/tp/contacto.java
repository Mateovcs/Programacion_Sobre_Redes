package tp;

public class contacto {
    private String nombre;
    private String telefono;
    private String email;
    private String nota;

    public contacto(String nombre, String telefono, String email, String nota) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.nota = nota;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNota() { return nota; }
    public void setNota(String nota) { this.nota = nota; }
}
