package business;

import java.util.ArrayList;

public class Proc extends AtomicComponent {

	public Proc(String name) {
		super(name);
		outputs.add(IO.DONE);
		inputs.add(IO.REQ);
	}

	/*
	 * public void init() { current_state = 0; }
	 */

	@Override
	public void delta_int() {
		if (current_state == 1)
			next_state = 0;
		current_state = next_state;
	}

	@Override
	public void delta_ext(ArrayList<IO> inputs) {
		if (current_state == 0 && inputs.contains(IO.REQ))
			next_state = 1;
		current_state = next_state;
	}

	@Override
	public void delta_con(ArrayList<IO> inputs) {
		current_state = next_state;
	}

	@Override
	public ArrayList<IO> lambda() {
		ArrayList<IO> outputs = new ArrayList<>();
		if (current_state == 1) {
			outputs.add(IO.DONE);
		}
		return outputs;
	}

	@Override
	public double getTa() {
		if (current_state == 0) {
			return Double.POSITIVE_INFINITY;
		} else if (current_state == 1) {
			return 2.0;
		}
		return -1;
	}
}