package business;

import java.util.ArrayList;

public class Proc extends AtomicComponent {

	public enum etat{BUSY,IDLE}
	public Proc(String name) {
		super(name);
		outputs.add(IOenum.DONE);
		inputs.add(IOenum.REQ);
	}

	public void init() { 
		super.init();
		
		requiredTime.put(0, Double.POSITIVE_INFINITY);
		requiredTime.put(1, 3.0);
	}

	@Override
	public void delta_int() {
		if (current_state == 1)
			next_state = 0;
		current_state = next_state;
		ellapsedTime = 0;
	}

	@Override
	public void delta_ext(ArrayList<IOenum> inputs) {
		if (current_state == 0 && inputs.contains(IOenum.REQ))
			next_state = 1;
		current_state = next_state;
		ellapsedTime = 0;
	}

	@Override
	public ArrayList<IOenum> lambda() {
		ArrayList<IOenum> outputs = new ArrayList<>();
		if (current_state == 1) {
			outputs.add(IOenum.DONE);
		}
		return outputs;
	}
}