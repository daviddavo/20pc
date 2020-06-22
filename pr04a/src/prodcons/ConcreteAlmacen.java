package prodcons;

import java.util.concurrent.Semaphore;

public class ConcreteAlmacen implements Almacen {
	private int maxn;
	private int n;
	private int i;
	private int j;
	
	volatile private Producto[] productos;
	
	public ConcreteAlmacen(int maxn) {
		this.maxn = maxn;
		this.i = this.j = this.n = 0;
		this.productos = new Producto[maxn];
	}

	@Override
	public synchronized void almacenar(Producto producto) {
		try {
			while (n == maxn) wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.printf(">>> Almacenado producto %d en %d%n", producto.getId(), j);
		
		productos[j] = producto;
		productos = productos;
		j = (j+1)%maxn;
		n++;
		
		notifyAll();
	}

	@Override
	public synchronized Producto extraer() {
		try {
			while (n == 0) wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Producto p = productos[i];
		productos[i] = null;
		System.out.printf(">>> Sacando producto %d de %d%n", p.getId(), i);
		i = (i+1)%maxn;
		n--;
		
		notifyAll();
		
		return p;
	}

}
