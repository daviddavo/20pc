package prodcons;

public interface MonitorAlmacen {
	public void request_produce() throws InterruptedException;
	public void release_produce() throws InterruptedException;
	public void request_consume() throws InterruptedException;
	public void release_consume() throws InterruptedException;
}
