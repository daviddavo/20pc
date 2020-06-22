package prodcons;

import java.util.concurrent.Semaphore;

public class ConcreteAlmacen implements Almacen {
	private int n;
	private int i;
	private int j;
	
	volatile private Producto[] productos;
	
	private Semaphore mutexProductor;
	private Semaphore mutexConsumidor;
	private Semaphore semLleno;
	private Semaphore semVacio;
	
	public ConcreteAlmacen(int n) {
		this.n = n;
		this.i = this.j = 0;
		this.productos = new Producto[n];
		
		this.mutexProductor = new Semaphore(1);
		this.mutexConsumidor = new Semaphore(1);
		this.semLleno = new Semaphore(0);
        // La unica diferencia entre 1 y n productos
        // this.semVacio = new Semaphore(1);
		this.semVacio = new Semaphore(n);
	}

	@Override
	public void almacenar(Producto producto) {
		try {
			semVacio.acquire();
			mutexProductor.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.printf(">>> Almacenado producto %d en %d%n", producto.getId(), j);
		
		productos[j] = producto;
		productos = productos;
		j = (j+1)%n;
		
		mutexProductor.release();
		semLleno.release();
	}

	@Override
	public Producto extraer() {
		try {
			semLleno.acquire();
			mutexConsumidor.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			
		}
		
		Producto p = productos[i];
		productos[i] = null;
		System.out.printf(">>> Sacando producto %d de %d%n", p.getId(), i);
		i = (i+1)%n;
		
		mutexConsumidor.release();
		semVacio.release();
		
		return p;
	}

}
