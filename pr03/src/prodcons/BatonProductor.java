package prodcons;

public class BatonProductor extends Thread {
	private long id;
	private static long nextId = 0;
	private BatonAlmacen almacen;
	private long n;
	
	public BatonProductor(BatonAlmacen almacen, int n) {
		this.id = nextId++;
		this.almacen = almacen;
		this.n = n;
		System.out.printf("[P] Creado productor %d (%d productos)%n", this.id, this.n);
	}
	
	public BatonProductor(BatonAlmacen almacen) {
		this(almacen, 10);
	}
	
	@Override
	public void run() {
		for (int i = 0; i < n; i++) {
			try {
				almacen.almacenar(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
