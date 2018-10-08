package discret;

import java.util.ArrayList;

import business.AtomicComponent;
import business.IO;
import business.IOenum;

public class Buf extends AtomicComponent {

	private int q;

	public int getQ() {
		return q;
	}

	public Buf(String name) {
		super(name);

		outputs.add(new IO(IOenum.REQ));
		inputs.add(new IO(IOenum.DONE));
		inputs.add(new IO(IOenum.JOB));
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
		current_state = next_state;
		ellapsedTime = 0;
	}

	@Override
	public void delta_ext(ArrayList<IO> inputs) {
		if (current_state == 0 && inputs.contains((Object) IOenum.JOB)) {
			q++;
			// integer_varnames_var.put("q",q);
			next_state = 1;
		} else if (current_state == 1 && inputs.contains((Object) IOenum.JOB)) {
			q++;
			// integer_varnames_var.put("q",q);
			next_state = 1;
		} else if (current_state == 2 && inputs.contains((Object) IOenum.DONE)) {
			if (q > 0)
				next_state = 1;
			if (q == 0)
				next_state = 0;
			System.out.println("done");
		} else if (current_state == 2 && inputs.contains((Object) IOenum.JOB)) {
			q++;
			// integer_varnames_var.put("q",q);
			next_state = 2;
			System.out.println("Job");
		}

		current_state = next_state;
		ellapsedTime = 0;
	}

	@Override
	public ArrayList<IO> lambda() {
		ArrayList<IO> outputs = new ArrayList<>();
		if (current_state == 1) {
			outputs.add(new IO(IOenum.REQ));
		}
		return outputs;
	}

}