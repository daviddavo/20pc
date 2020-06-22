package prodcons;

public class Consumidor extends Thread {
	private static long nextId = 0;
	private long id;
	private Almacen almacen;
	private long n;
	
	public Consumidor(Almacen almacen, int n) {
		this.id = nextId++;
		this.almacen = almacen;
		this.n = n;
		System.out.printf("[C] creado consumidor %d (%d productos)%n", this.id, this.n);
	}
	
	public Consumidor(Almacen almacen) {
		this(almacen, 100);
	}
	
	@Override
	public void run() {
		for (int i = 0; i < n; i++) {
			almacen.extraer();
		}
	}
}
