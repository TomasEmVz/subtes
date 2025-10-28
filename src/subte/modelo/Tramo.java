package subte.modelo;

public class Tramo {

	private Estacion estacion1;
	private Estacion estacion2;
	private int tiempo;
	private int congestion;
	
	public Tramo() {	
	}

	public Tramo(Estacion estacion1, Estacion estacion2, int tiempo, int congestion) {
		super();
		this.estacion1 = estacion1;
		this.estacion2 = estacion2;
		this.tiempo = tiempo;
		this.congestion = congestion;
	}

	public Estacion getEstacion1() {
		return estacion1;
	}

	public void setEstacion1(Estacion estacion1) {
		this.estacion1 = estacion1;
	}

	public Estacion getEstacion2() {
		return estacion2;
	}

	public void setEstacion2(Estacion estacion2) {
		this.estacion2 = estacion2;
	}

	public int getTiempo() {
		return tiempo;
	}

	public void setTiempo(int tiempo) {
		this.tiempo = tiempo;
	}

	public int getCongestion() {
		return congestion;
	}

	public void setCongestion(int congestion) {
		this.congestion = congestion;
	}

	@Override
	public String toString() {
		return "Tramo [estacion1=" + estacion1 + ", estacion2=" + estacion2
				+ ", tiempo=" + tiempo + ", congestion=" + congestion + "]";
	}
	
	
}
