package prodcons;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcreteAlmacen implements Almacen {
	private int maxn;
	private int n;
	private int i;
	private int j;
	
	volatile private Producto[] productos;
	private final Lock lock = new ReentrantLock();
	private final Condition noLleno = lock.newCondition();
	private final Condition noVacio = lock.newCondition();
	
	public ConcreteAlmacen(int maxn) {
		this.maxn = maxn;
		this.i = this.j = this.n = 0;
		this.productos = new Producto[maxn];
	}

	@Override
	public void almacenar(Producto producto) {
		lock.lock();
		try {
			while (n == maxn) noLleno.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.printf(">>> Almacenado producto %d en %d%n", producto.getId(), j);
		
		productos[j] = producto;
		productos = productos;
		j = (j+1)%maxn;
		n++;
		
		noVacio.signal();
		lock.unlock();
	}

	@Override
	public Producto extraer() {
		lock.lock();
		try {
			while (n == 0) noVacio.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Producto p = productos[i];
		productos[i] = null;
		System.out.printf(">>> Sacando producto %d de %d%n", p.getId(), i);
		i = (i+1)%maxn;
		n--;
		
		noLleno.signal();
		lock.unlock();
		
		return p;
	}

}
