package com.batallanaval;

import java.util.ArrayList;

public class Tablero {
    public static final String AGUA = ".";
    public static final String BARCO = "B";
    public static final String IMPACTO = "X";
    public static final String FALLO = "O";
    
    public ArrayList<ArrayList<String>> grilla;
    public ArrayList<String> nombresBarcos;
    public ArrayList<Integer> vidasBarcos;
    public static final int TAMANIO = 10;

    public Tablero() {
        this.grilla = new ArrayList<>();
        this.nombresBarcos = new ArrayList<>();
        this.vidasBarcos = new ArrayList<>();
        inicializarTablero();
    }

    private void inicializarTablero() {
        for (int i = 0; i < TAMANIO; i++) {
            ArrayList<String> fila = new ArrayList<>();
            for (int j = 0; j < TAMANIO; j++) {
                fila.add(AGUA);
            }
            this.grilla.add(fila);
        }
    }

    public boolean colocarBarco(String nombre, int fila, int col, int longitud, boolean horizontal) {
        if (horizontal && (col + longitud > TAMANIO)) return false;
        if (!horizontal && (fila + longitud > TAMANIO)) return false;

        for (int i = 0; i < longitud; i++) {
            int r = horizontal ? fila : fila + i;
            int c = horizontal ? col + i : col;
            if (!this.grilla.get(r).get(c).equals(AGUA)) {
                return false;
            }
        }

        for (int i = 0; i < longitud; i++) {
            int r = horizontal ? fila : fila + i;
            int c = horizontal ? col + i : col;
            this.grilla.get(r).set(c, BARCO + "-" + nombresBarcos.size());
        }
        
        this.nombresBarcos.add(nombre);
        this.vidasBarcos.add(longitud);
        return true;
    }

    public String registrarDisparo(int fila, int col) {
        if (fila < 0 || fila >= TAMANIO || col < 0 || col >= TAMANIO) {
            return "FUERA_RANGO";
        }

        String celda = this.grilla.get(fila).get(col);
        if (celda.equals(IMPACTO) || celda.equals(FALLO)) {
            return "REPETIDO";
        }

        if (celda.startsWith(BARCO)) {
        	int indiceBarco = Integer.parseInt(celda.split("-")[1]);
            this.grilla.get(fila).set(col, IMPACTO);
            
            int vidaRestante = this.vidasBarcos.get(indiceBarco) - 1;
            this.vidasBarcos.set(indiceBarco, vidaRestante);

            if (vidaRestante == 0) {
                return "HUNDIDO:" + this.nombresBarcos.get(indiceBarco);
            }
            return "TOCADO";
        }

        this.grilla.get(fila).set(col, FALLO);
        return "AGUA";
    }

    public boolean todosHundidos() {
        if (vidasBarcos.isEmpty()) return false;
        for (int vida : vidasBarcos) {
            if (vida > 0) return false;
        }
        return true;
    }

    public String serializar() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TAMANIO; i++) {
            for (int j = 0; j < TAMANIO; j++) {
                sb.append(this.grilla.get(i).get(j)).append(",");
            }
        }
        return sb.toString();
    }

    public static Tablero deserializar(String datos) {
        Tablero t = new Tablero();
        String[] celdas = datos.split(",");
        int k = 0;
        for (int i = 0; i < TAMANIO; i++) {
            for (int j = 0; j < TAMANIO; j++) {
                t.grilla.get(i).set(j, celdas[k++]);
            }
        }
        return t;
    }
}
