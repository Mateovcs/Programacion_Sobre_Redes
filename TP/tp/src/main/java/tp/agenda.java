package tp;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class agenda {
    private final List<contacto> lista = new ArrayList<>();
    private final Path archivoPrincipal = Paths.get("agenda.dat");

    public agenda() {
        cargar();
    }

    public List<contacto> getLista() {
        return lista;
    }

    public void agregar(contacto c) throws Exception {
        lista.add(c);
        guardar();
    }

    public boolean eliminar(int indice) throws Exception {
        if (indice >= 0 && indice < lista.size()) {
            lista.remove(indice);
            guardar();
            return true;
        }
        return false;
    }

    public boolean editar(int indice, contacto nuevo) throws Exception {
        if (indice >= 0 && indice < lista.size()) {
            lista.set(indice, nuevo);
            guardar();
            return true;
        }
        return false;
    }

    private void cargar() {
        if (!Files.exists(archivoPrincipal)) return;
        try (var reader = Files.newBufferedReader(archivoPrincipal)) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] campos = linea.split(";");
                if (campos.length >= 3) {
                    String nom = campos[0];
                    String tel = campos[1];
                    String em = campos[2];
                    String nCifrada = campos.length == 4 ? campos[3] : "";
                    String nDescifrada = cifrado.desencriptar(nCifrada);
                    lista.add(new contacto(nom, tel, em, nDescifrada));
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar archivo: " + e.getMessage());
        }
    }

    private synchronized void guardar() throws Exception {
        Path archivoTemporal = Files.createTempFile("agenda_temp", ".tmp");
        try {
            List<String> lineas = new ArrayList<>();
            for (contacto c : lista) {
                String nCifrada = cifrado.encriptar(c.getNota());
                lineas.add(String.format("%s;%s;%s;%s", c.getNombre(), c.getTelefono(), c.getEmail(), nCifrada));
            }
            Files.write(archivoTemporal, lineas, StandardOpenOption.WRITE);
            
            // Reemplazo atómico seguro contra fallos eléctricos
            Files.move(archivoTemporal, archivoPrincipal, 
                       StandardCopyOption.REPLACE_EXISTING, 
                       StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            Files.deleteIfExists(archivoTemporal);
            throw e;
        }
    }
}
