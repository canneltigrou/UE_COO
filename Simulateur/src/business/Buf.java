package business;

import java.util.ArrayList;

public class Buf extends AtomicComponent {

	private int q;

	public Buf(String name) {
		super(name);

		outputs.add(IO.REQ);
		inputs.add(IO.DONE);
		inputs.add(IO.JOB);
	}

	@Override
	public void init() {
		super.init();
		q = 0;
		// integer_varnames_var.put("q",q);
	}

	@Override
	public void delta_int() {
		if (current_state == 1) {
			q--;
			// integer_varnames_var.put("q",q);
			next_state = 2;
		}
		current_state = next_state;
	}

	@Override
	public void delta_ext(ArrayList<IO> inputs) {
		if (current_state == 0 && inputs.contains(IO.JOB)) {
			q++;
			// integer_varnames_var.put("q",q);
			next_state = 1;
		} else if (current_state == 1 && inputs.contains(IO.JOB)) {
			q++;
			// integer_varnames_var.put("q",q);
			next_state = 1;
		} else if (current_state == 2 && inputs.contains(IO.JOB)) {
			if (q > 0)
				next_state = 1;
			if (q == 0)
				next_state = 0;
		} else if (current_state == 2 && inputs.contains(IO.JOB)) {
			q++;
			// integer_varnames_var.put("q",q);
			next_state = 2;
		}

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
			outputs.add(IO.REQ);
		}
		return outputs;
	}

	@Override
	public double getTa() {

		if (current_state == 0) {
			return Double.POSITIVE_INFINITY;
		} else if (current_state == 1) {
			return 0.0;
		} else if (current_state == 2) {
			return Double.POSITIVE_INFINITY;
		}

		return 0;
	}
}