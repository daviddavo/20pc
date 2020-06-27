import java.util.concurrent.atomic.AtomicInteger;

public class LockTicketMax implements Lock {
	private int n;
	private int max;
	volatile private AtomicInteger number;
	volatile int next;
	volatile int turn[];
	
	public LockTicketMax(int N, int max) {
		this.n = N;
		this.max = max;
		turn = new int[N];
		number = new AtomicInteger(1);
		next = 1;
		for (int i = 0; i < n; i++) turn[i] = 0;
		turn = turn;
	}
	
	public LockTicketMax(int N) {
		this(N, N);
	}

	@Override
	public int takeLock(int pid) {
		if (pid <= max) {
			turn[pid]= number.getAndAdd(1)%max;
			turn = turn;
			while (turn[pid]!=next);
			
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int releaseLock(int pid) {
		next = (next+1)%max;
		
		return 1;
	}

}
