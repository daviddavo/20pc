package prodcons;

public class BatonConsumidor extends Thread {
	private static long nextId = 0;
	private long id;
	private BatonAlmacen almacen;
	private long n;
	
	public BatonConsumidor(BatonAlmacen almacen, int n) {
		this.id = nextId++;
		this.almacen = almacen;
		this.n = n;
		System.out.printf("[C] creado consumidor %d (%d productos)%n", this.id, this.n);
	}
	
	public BatonConsumidor(BatonAlmacen almacen) {
		this(almacen, 100);
	}
	
	@Override
	public void run() {
		for (int i = 0; i < n; i++) {
			try {
				almacen.extraer(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
