
public class LockBakery implements Lock{
	public int n;
	volatile int turn [];
	
	public LockBakery(int N) {
		n = N;
		turn = new int [N];
		for(int i = 0; i < n; i++) turn[i] = 0;
	}

	private boolean op(int a,int b, int c, int d) {
		boolean r=false;
		if (a > c || (a==c && b>d)) r = true;
		return r;
	}
	
	@Override
	public int takeLock(int pid) {
		turn[pid]=1;
		turn = turn;
		turn[pid] = max() + 1;	
		turn = turn;
		for (int i = 0; i < n; i++) {
			while((turn[i]!=0) && op(turn[pid], pid, turn[i], i));
		}
		
		//System.out.println("Lock " + Integer.toString(pid) + " taken");
		return 1;
	}
	
	@Override
	public int releaseLock(int pid) {
			turn[pid]=0;
			turn = turn;
			
		//System.out.println("Lock " + Integer.toString(pid) + " released");
		return 1;
	}
	
	private int max() {
		int max = turn[0];
		
		for (int i = 0; i < turn.length; i++) {
			if (turn[i] > max) max = turn[i];
		}
		return max;
	}
	
}
