package prodcons;

public class Producto {
	private static long nextid = 0;
	private long id;
	private int tipo;
	
	public Producto() {
		id = nextid++;	
	}
	
	public Producto(int tipo) {
		this();
		this.tipo = tipo;
	}
	
	public long getId() {
		return id;
	}
}
