// Author: Ela K. Shepherd & David Davó

public class Main {

	public static void main(String[] args) throws InterruptedException{
		
			int N = 500;
			// DatosCompartidos dato = new DatosCompartidos(new LockRompeEmpate(2*N), 0);
	    	// DatosCompartidos dato = new DatosCompartidos(new LockTicket(2*N), 0);
			// DatosCompartidos dato = new DatosCompartidos(new LockBakery(2*N), 0);
			DatosCompartidos dato = new DatosCompartidos(new LockTicketMax(2*N, 2*N), 0);
			Thread[] hilos = new Thread[2*N];
	
			
			for(int i = 0; i < N; i++) {
				hilos[2*i] = new HiloIncremento(dato, 2*i);
				hilos[2*i].start();
				hilos[2*i+1] = new HiloDecremento(dato, 2*i+1);
				hilos[2*i+1].start();
			}
			
			for(int i = 0; i < 2*N; i++) {
				hilos[i].join();
			}
			
			System.out.println(dato.num);
			System.err.println("Acaba");
	}
}
