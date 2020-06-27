package prodcons;

import java.util.concurrent.Semaphore;

public class ConcreteMonitorAlmacen implements MonitorAlmacen {
	private int n;
	// active productores/consumidores
	private int productores   = 0;
	private int consumidores  = 0;
	private int tipos;
	
	volatile private int[] productos;
	
	public ConcreteMonitorAlmacen(int n, int tipos) {
		this.n = n;
		productos = new int[tipos];
	}
	
	@Override
	public synchronized void request_produce() throws InterruptedException {
		while (productores > 0 || consumidores > 0) wait();

		productores++;
	}

	@Override
	public synchronized void release_produce() throws InterruptedException {
		productores--;
		this.notifyAll();
	}
	
	@Override
	public synchronized void request_consume() throws InterruptedException {
		while (productores > 0) wait();
		
		consumidores++;
	}

	@Override
	public synchronized void release_consume() throws InterruptedException {
		consumidores--;
		this.notifyAll();
	}
}
