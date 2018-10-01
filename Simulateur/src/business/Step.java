package business;

import java.util.ArrayList;

public class Step extends AtomicComponent{
	double xi;
	double xf;
	double ts;
	
	public Step(String name, double xi, double xf, double ts) {
		super(name);
		this.xi = xi;
		this.xf = xf;
		this.ts = ts;
		
		requiredTime.put(0, 0.0); // l'état 0 doit durer 2 UA de temps.
		requiredTime.put(1, ts); 
		requiredTime.put(2, Double.POSITIVE_INFINITY);
	}
	
	@Override
	public void delta_int() {
		if (current_state < 2)
			next_state = current_state + 1;
		current_state = next_state;
		ellapsedTime = 0;
	}

	@Override
	public void delta_ext(ArrayList<IO> inputs) {
		
		current_state = next_state;
		ellapsedTime = 0;
	}

	@Override
	public ArrayList<IO> lambda() {
		ArrayList<IO> outputs = new ArrayList<>();
		if (current_state == 0) {
			outputs.add(new IO(IOenum.JOB, xi, this));
		}
		if (current_state == 1)
			outputs.add(new IO(IOenum.JOB, xf, this));
		return outputs;
	}
}