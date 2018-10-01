package business;

import java.util.ArrayList;

public class Gen extends AtomicComponent {

	public Gen(String name) {
		super(name);
		outputs.add(new IO(IOenum.JOB));
		requiredTime.put(0, 2.0); // l'état 0 doit durer 2 UA de temps.

	}

	/*
	 * public void init() { super.init(); }
	 */

	@Override
	public void delta_int() {
		if (current_state == 0)
			next_state = 0;
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
			outputs.add(new IO(IOenum.JOB));
		}
		return outputs;
	}
}