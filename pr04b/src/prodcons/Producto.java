package prodcons;

public class Producto {
	private static long nextid = 0;
	private long id;
	
	public Producto() {
		id = nextid++;	
	}
	
	public long getId() {
		return id;
	}
}
