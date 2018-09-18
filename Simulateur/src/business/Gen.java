package business;

import java.util.ArrayList;

public class Gen extends AtomicComponent {

	public Gen(String name) {
		super(name);
		outputs.add(IO.JOB);

	}

	/*
	 * public void init() { super.init(); }
	 */

	@Override
	public void delta_int() {
		if (current_state == 0)
			next_state = 0;
		current_state = next_state;
	}

	@Override
	public void delta_ext(ArrayList<IO> inputs) {
		current_state = next_state;
	}

	@Override
	public void delta_con(ArrayList<IO> inputs) {
		current_state = next_state;
	}

	@Override
	public ArrayList<IO> lambda() {
		ArrayList<IO> outputs = new ArrayList<>();
		if (current_state == 0) {
			outputs.add(IO.JOB);
		}
		return outputs;
	}

	@Override
	public double getTa() {
		if (current_state == 0) {
			return 1.0;
		}
		return -1;
	}
}