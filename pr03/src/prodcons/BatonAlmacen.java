package prodcons;

public interface BatonAlmacen {
	public void almacenar(int tipo) throws InterruptedException;
	public Producto extraer(int tipo) throws InterruptedException;
}
