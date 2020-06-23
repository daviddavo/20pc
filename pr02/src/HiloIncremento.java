// Author: Ela K. Shepherd & David Davó

public class HiloIncremento extends Thread {
	private DatosCompartidos dato;
	public int id;
	
	public HiloIncremento(DatosCompartidos dato, int id) {
		this.dato = dato;
		this.id = id;
	}
	
	public void run(){
		for(int i = 0; i < 3000; i++) {
			dato.lock.takeLock(id);
			dato.num++;
		//	System.out.println(Integer.toString(dato.num) + " " + Integer.toString(i));
			dato.lock.releaseLock(id);
		}
		
	}
}
