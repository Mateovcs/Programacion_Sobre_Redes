package tp;

import java.util.Scanner;

public class app {
  
    public static void main(String[] args) {
        agenda miAgenda = new agenda();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n" + "========================================"  );
            System.out.println(  "       SISTEMA DE AGENDA SEGURA         " );
            System.out.println( "========================================" );
            System.out.println("1. " +  "Mostrar contactos" );
            System.out.println("2. " +  "Agregar contacto" );
            System.out.println("3. " +  "Editar contacto" );
            System.out.println("4. " +  "Eliminar contacto" );
            System.out.println("5. Salir");
            System.out.println( "========================================" );
            System.out.print( "Seleccione una opción: " );
            
            String opcion = sc.nextLine();

            try {
                switch (opcion) {
                    case "1" -> mostrar(miAgenda);
                    case "2" -> agregar(miAgenda, sc);
                    case "3" -> editar(miAgenda, sc);
                    case "4" -> eliminar(miAgenda, sc);
                    case "5" -> {
                        System.out.println("Saliendo del sistema..." );
                        System.exit(0);
                    }
                    default -> System.out.println( "Alerta: Opción inválida." );
                }
            } catch (Exception e) {
                System.out.println( "Error en la operación: " + e.getMessage() );
            }
        }
    }

    private static void mostrar(agenda miAgenda) {
        var lista = miAgenda.getLista();
        if (lista.isEmpty()) {
            System.out.println("Alerta: La agenda está vacía." );
            return;
        }
        System.out.println("\n"  + "--------------------------------------------------------------------------------------------------" );
        System.out.printf( "%-4s | %-15s | %-12s | %-25s | %-25s\n" , "ID", "Nombre", "Teléfono", "Email", "Nota Privada (Descifrada)");
        System.out.println( "--------------------------------------------------------------------------------------------------" );
        for (int i = 0; i < lista.size(); i++) {
            contacto c = lista.get(i);
            System.out.printf("%-4d | %-15s | %-12s | %-25s | " +  "%-25s\n" , i, c.getNombre(), c.getTelefono(), c.getEmail(), c.getNota());
        }
        System.out.println( "--------------------------------------------------------------------------------------------------");
    }

    private static void agregar(agenda miAgenda, Scanner sc) throws Exception {
        System.out.print("Nombre: "); String nom = sc.nextLine();
        System.out.print("Teléfono: "); String tel = sc.nextLine();
        System.out.print("Email: "); String em = sc.nextLine();
        System.out.print( "Nota Privada (Se cifrará): " ); String nt = sc.nextLine();

        miAgenda.agregar(new contacto(nom, tel, em, nt));
        System.out.println( "Éxito: Contacto guardado de forma atómica." );
    }

    private static void editar(agenda miAgenda, Scanner sc) throws Exception {
        mostrar(miAgenda);
        if (miAgenda.getLista().isEmpty()) return;
        
        System.out.print("ID a editar: " );
        int id = Integer.parseInt(sc.nextLine());

        System.out.print("Nuevo Nombre: "); String nom = sc.nextLine();
        System.out.print("Nuevo Teléfono: "); String tel = sc.nextLine();
        System.out.print("Nuevo Email: "); String em = sc.nextLine();
        System.out.print( "Nueva Nota Privada: " ); String nt = sc.nextLine();

        if (miAgenda.editar(id, new contacto(nom, tel, em, nt))) {
            System.out.println( "Éxito: Contacto modificado."  );
        } else {
            System.out.println( "Error: ID no válido." );
        }
    }

    private static void eliminar(agenda miAgenda, Scanner sc) throws Exception {
        mostrar(miAgenda);
        if (miAgenda.getLista().isEmpty()) return;

        System.out.print(  "ID a eliminar: " );
        int id = Integer.parseInt(sc.nextLine());

        if (miAgenda.eliminar(id)) {
            System.out.println(  "Éxito: Contacto eliminado correctamente." );
        } else {
            System.out.println(  "Error: ID no encontrado." );
        }
    }
}
