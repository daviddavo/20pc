
public class DatosCompartidos {

	volatile public int num;
	volatile public int last;
	volatile public boolean ininc = false;
	volatile public boolean indec = false;
	volatile public Lock lock;
	
	public DatosCompartidos(Lock l, int n) {
		this.num = n;
		this.lock = l;
	}
	
}
