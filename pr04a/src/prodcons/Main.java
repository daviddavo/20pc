package prodcons;

public class Main {
	private static int CAPACIDAD = 5;			// Capacidad del almacén
	private static int NConsumidores = 10;		// Numero de consumidores
	private static int NProductos = 200;		// Numero total de Productos
	private static int NProductores = 5;		// Numero de productores

	public static void main(String[] args) throws InterruptedException {
		MonitorAlmacen almacen = new ConcreteMonitorAlmacen(CAPACIDAD, 64);
		
		MonitorProductor[] productores = new MonitorProductor[NProductores];
		MonitorConsumidor[] consumidores = new MonitorConsumidor[NConsumidores];
		
		/*
		 * El numero de productos a crear por los productores debe ser igual al 
		 * numero de producos a consumir para que no haya un livelock, por lo que
		 * repartiremos los NProductos entre todos los productoes y entre todos
		 * los consumidores
		 */
		
		for (int i = 0; i < NProductores; i++) {
			int nprod = NProductos/NProductores;
			if (NProductos % NProductores != 0)
				nprod = (i == 0)?NProductos%(NProductores-1):NProductos/(NProductores-1);
				
			(productores[i] = new MonitorProductor(almacen, nprod)).start();
		}
		
		for (int i = 0; i < NConsumidores; i++) {
			int nprod = NProductos/NConsumidores;
			if (NProductos % NConsumidores != 0)
				nprod = (i == 0)?NProductos%(NConsumidores-1):NProductos/(NConsumidores-1);
			
			(consumidores[i] = new MonitorConsumidor(almacen, nprod)).start();
		}
		
		System.out.println("<<< Invocados todos los P/C");
		
		for (int i = 0; i < NProductores; i++) {
			productores[i].join();
		}
		
		for (int i = 0; i < NConsumidores; i++) {
			consumidores[i].join();
		}
		
		System.out.println("<<< No hay deadlocks");
	}

}
