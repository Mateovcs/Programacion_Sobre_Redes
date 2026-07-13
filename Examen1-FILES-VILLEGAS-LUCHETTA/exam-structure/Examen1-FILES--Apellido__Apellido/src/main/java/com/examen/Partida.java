package com.examen;

public class Partida {
    private String fecha; // Formato "dd/MM"
    private int victoria; // 1 = Victoria, 0 = Derrota
    private int primeraSangre;
    private int primeraTorreta;
    private int asesinatos;
    private int muertes;
    private int asistencias;

    public Partida(String fecha, int victoria, int primeraSangre, int primeraTorreta, int asesinatos, int muertes, int asistencias) {
        this.fecha = fecha;
        this.victoria = victoria;
        this.primeraSangre = primeraSangre;
        this.primeraTorreta = primeraTorreta;
        this.asesinatos = asesinatos;
        this.muertes = muertes;
        this.asistencias = asistencias;
    }

    public static Partida desdeCsv(String lineaCsv) {
        String[] datos = lineaCsv.split(";");
        return new Partida(
            datos[0].trim(),
            Integer.parseInt(datos[1].trim()),
            Integer.parseInt(datos[2].trim()),
            Integer.parseInt(datos[3].trim()),
            Integer.parseInt(datos[4].trim()),
            Integer.parseInt(datos[5].trim()),
            Integer.parseInt(datos[6].trim())
        );
    }

    public String toCsv() {
        return String.format("%s;%d;%d;%d;%d;%d;%d", 
            fecha, victoria, primeraSangre, primeraTorreta, asesinatos, muertes, asistencias);
    }

    public String getMes() {
        String[] partes = fecha.split("/");
        return partes.length >= 2 ? partes[1].trim() : "00";
    }

    // Getters y Setters
    public String getFecha() { return fecha; }
    public boolean esVictoria() { return victoria == 1; }
    public int getPrimeraSangre() { return primeraSangre; }
    public int getPrimeraTorreta() { return primeraTorreta; }
    public int getAsesinatos() { return asesinatos; }
    public int getMuertes() { return muertes; }
    public int getAsistencias() { return asistencias; }

	public Object getDato1() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getDato3() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getDato4() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getDato2() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getDato6() {
		// TODO Auto-generated method stub
		return null;
	}
}
