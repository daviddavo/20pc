package prodcons;

public class Main {
	private static final int CAPACIDAD = 10;			// Capacidad del almacén
	private static final int NCONSUMIDORES = 110;		// Numero de consumidores
	private static final int NPRODUCTOS = 1500;		// Numero total de Productos
	private static final int NPRODUCTORES = 120;		// Numero de productores

	public static void main(String[] args) throws InterruptedException {
		Almacen almacen = new ConcreteAlmacen(CAPACIDAD);
		
		Productor[] productores = new Productor[NPRODUCTORES];
		Consumidor[] consumidores = new Consumidor[NCONSUMIDORES];
		
		/*
		 * El numero de productos a crear por los productores debe ser igual al 
		 * numero de producos a consumir para que no haya problemas, por lo que
		 * repartiremos los NProductos entre todos los productoes y entre todos
		 * los consumidores. También podríamos hacer que cada consumidor supiese
		 * que ya no hay más productos, y acabase, para ello podríamos modificar
		 * Almacen con un metodo isEmpty()
		 */
		
		for (int i = 0; i < NPRODUCTORES; i++) {
			int nprod = NPRODUCTOS/NPRODUCTORES;
			if (NPRODUCTOS % NPRODUCTORES != 0)
				nprod = (i == 0)?NPRODUCTOS%(NPRODUCTORES-1):NPRODUCTOS/(NPRODUCTORES-1);
				
			(productores[i] = new Productor(almacen, nprod)).start();
		}
		
		for (int i = 0; i < NCONSUMIDORES; i++) {
			int nprod = NPRODUCTOS/NCONSUMIDORES;
			if (NPRODUCTOS % NCONSUMIDORES != 0)
				nprod = (i == 0)?NPRODUCTOS%(NCONSUMIDORES-1):NPRODUCTOS/(NCONSUMIDORES-1);
			
			(consumidores[i] = new Consumidor(almacen, nprod)).start();
		}
		
		System.out.println("<<< Invocados todos los P/C");
		
		for (int i = 0; i < NPRODUCTORES; i++) {
			productores[i].join();
		}
		
		for (int i = 0; i < NCONSUMIDORES; i++) {
			consumidores[i].join();
		}
		
		System.out.println("<<< No hay deadlocks");
	}

}
