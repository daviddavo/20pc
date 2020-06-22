public interface Lock {
	public int takeLock(int pid);
	public int releaseLock(int pid);
}
