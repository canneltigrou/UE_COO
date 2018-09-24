package business;

import java.util.ArrayList;

public class Buf extends AtomicComponent {

	private int q;


	public Buf(String name) {
		super(name);

		outputs.add(IOenum.REQ);
		inputs.add(IOenum.DONE);
		inputs.add(IOenum.JOB);
	}

	@Override
	public void init() {
		super.init();
		q = 0;
		// integer_varnames_var.put("q",q);
		requiredTime.put(0, Double.POSITIVE_INFINITY);
		requiredTime.put(1, 0.0);
		requiredTime.put(2, Double.POSITIVE_INFINITY);
	}

	@Override
	public void delta_int() {
		if (current_state == 1) {
			q--;
			// integer_varnames_var.put("q",q);
			next_state = 2;
		}
		current_state =  next_state;
		ellapsedTime = 0;
	}

	@Override
	public void delta_ext(ArrayList<IOenum> inputs) {
		if (current_state == 0 && inputs.contains(IOenum.JOB)) {
			q++;
			// integer_varnames_var.put("q",q);
			next_state = 1;
		} else if (current_state == 1 && inputs.contains(IOenum.JOB)) {
			q++;
			// integer_varnames_var.put("q",q);
			next_state = 1;
		} else if (current_state == 2 && inputs.contains(IOenum.JOB)) {
			if (q > 0)
				next_state = 1;
			if (q == 0)
				next_state = 0;
		} else if (current_state == 2 && inputs.contains(IOenum.JOB)) {
			q++;
			// integer_varnames_var.put("q",q);
			next_state = 2;
		}

		current_state = next_state;
		ellapsedTime = 0;
	}

	@Override
	public ArrayList<IOenum> lambda() {
		ArrayList<IOenum> outputs = new ArrayList<>();
		if (current_state == 1) {
			outputs.add(IOenum.REQ);
		}
		return outputs;
	}
}