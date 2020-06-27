package prodcons;

import java.util.concurrent.Semaphore;

public class ConcreteBatonAlmacen implements BatonAlmacen {
	private int n;
	private int i;
	private int j;
	// delayed consumidores/productores
	private int dproductores  = 0;
	private int dconsumidores = 0;
	// active productores/consumidores
	private int productores   = 0;
	private int consumidores  = 0;
	private int tipos;
	
	volatile private int[] productos;
	
	private Semaphore mutexBaton;      // e
	private Semaphore mutexProductor;  // w
	private Semaphore mutexConsumidor; // r
	
	public ConcreteBatonAlmacen(int n, int tipos) {
		this.n = n;
		productos = new int[tipos];
		
		mutexBaton      = new Semaphore(1);
		mutexProductor  = new Semaphore(0);
		mutexConsumidor = new Semaphore(0);
	}

	@Override
	public void almacenar(int tipo) throws InterruptedException {
		mutexBaton.acquire();
		
		// No puede haber ningun consumidor en el almacen
		if (consumidores > 0) {
			 dproductores++;
			 mutexBaton.release();
			 mutexProductor.acquire();
		}
		
		productores++;
		
		// SIGNAL
		// (NO despertamos otros productores porque solo puede haber 1)
		mutexBaton.release();
		
		// DB READ
		Thread.sleep(100);
		
		mutexBaton.acquire();
		productores--;
		
		// SIGNAL
		// Cuando termine un productor le dará paso a un productor si hay alguno en espera
		// si no hay ninguno da paso a los consumidores
		if (dproductores > 0){
			dproductores--;
			mutexProductor.release();
		} else if (dconsumidores > 0) {
			dconsumidores--;
			mutexConsumidor.release();
		} else {
			mutexBaton.release();
		}
	}

	@Override
	public Producto extraer(int tipo) throws InterruptedException {
		mutexBaton.acquire();
		if (productores > 0) {
			dconsumidores++;
			mutexBaton.release();
			mutexConsumidor.acquire();
		}
		
		consumidores++;
		
		// SIGNAL (despertar otros consumidores)
		if (dconsumidores > 0) {
			dconsumidores--; 
			mutexConsumidor.release();
		} else {
			mutexBaton.release();
		}
		
		// DB READ
		Thread.sleep(50);

		mutexBaton.acquire();
		consumidores--;
		
		// SIGNAL (despertar productor si es posible)
		if (consumidores == 0 && dproductores > 0) {
			dproductores--; 
			mutexProductor.release();
		} else {
			mutexBaton.release();
		}
		
		return new Producto(i);
	}

}
