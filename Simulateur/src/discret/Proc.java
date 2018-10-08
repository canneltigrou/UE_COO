package discret;

import java.util.ArrayList;

import business.AtomicComponent;
import business.IO;
import business.IOenum;

public class Proc extends AtomicComponent {

	public enum etat{BUSY,IDLE}
	public Proc(String name) {
		super(name);
		outputs.add(new IO(IOenum.DONE));
		inputs.add(new IO(IOenum.REQ));
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
	public void delta_ext(ArrayList<IO> inputs) {
		if (current_state == 0 && inputs.contains((Object)IOenum.REQ))
			next_state = 1;
		current_state = next_state;
		ellapsedTime = 0;
	}

	@Override
	public ArrayList<IO> lambda() {
		ArrayList<IO> outputs = new ArrayList<>();
		if (current_state == 1) {
			outputs.add(new IO(IOenum.DONE));
		}
		return outputs;
	}
}