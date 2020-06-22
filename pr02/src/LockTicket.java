import java.util.concurrent.atomic.AtomicInteger;

public class LockTicket implements Lock{
	private int n;
	volatile private AtomicInteger number;
	volatile int next;
	volatile int turn [];
	public LockTicket(int N) {
		n = N;
		turn = new int [N];
		number = new AtomicInteger(1);
		next = 1;
		for (int i = 0; i < n; i++) turn[i]=0;
		turn = turn;
		
	}
	@Override
	public int takeLock(int pid) {
			turn[pid]= number.getAndAdd(1);
			turn = turn;
			while (turn[pid]!=next);
			
		//System.out.println("Lock " + Integer.toString(pid) + " taken");
		return 1;
	}
	
	@Override
	public int releaseLock(int pid) {
		next+=1;
		
		//System.out.println("Lock " + Integer.toString(pid) + " released");
		return 1;
	}
}
