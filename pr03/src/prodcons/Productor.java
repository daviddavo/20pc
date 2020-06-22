package prodcons;

public class Productor extends Thread {
	private long id;
	private static long nextId = 0;
	private Almacen almacen;
	private long n;
	
	public Productor(Almacen almacen, int n) {
		this.id = nextId++;
		this.almacen = almacen;
		this.n = n;
		System.out.printf("[P] Creado productor %d (%d productos)%n", this.id, this.n);
	}
	
	public Productor(Almacen almacen) {
		this(almacen, 10);
	}
	
	@Override
	public void run() {
		for (int i = 0; i < n; i++) {
			if (Math.random() < 0.01) {
				System.out.printf("[P] Problemillas en el productor %d, vamos a tardar un poquito...%n", this.id);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			almacen.almacenar(new Producto());
		}
	}
}
