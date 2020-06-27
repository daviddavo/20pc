package prodcons;

public class MonitorProductor extends Thread {
	private long id;
	private static long nextId = 0;
	private MonitorAlmacen almacen;
	private long n;
	
	public MonitorProductor(MonitorAlmacen almacen, int n) {
		this.id = nextId++;
		this.almacen = almacen;
		this.n = n;
		System.out.printf("[P] Creado productor %d (%d productos)%n", this.id, this.n);
	}
	
	public MonitorProductor(MonitorAlmacen almacen) {
		this(almacen, 10);
	}
	
	@Override
	public void run() {
		for (int i = 0; i < n; i++) {
			try {
				almacen.request_produce();
				Thread.sleep(70);
				almacen.release_produce();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
