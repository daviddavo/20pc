package prodcons;

public class MonitorConsumidor extends Thread {
	private static long nextId = 0;
	private long id;
	private MonitorAlmacen almacen;
	private long n;
	
	public MonitorConsumidor(MonitorAlmacen almacen, int n) {
		this.id = nextId++;
		this.almacen = almacen;
		this.n = n;
		System.out.printf("[C] creado consumidor %d (%d productos)%n", this.id, this.n);
	}
	
	public MonitorConsumidor(MonitorAlmacen almacen) {
		this(almacen, 100);
	}
	
	@Override
	public void run() {
		for (int i = 0; i < n; i++) {
			try {
				almacen.request_consume();
				Thread.sleep(30);
				almacen.release_consume();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
