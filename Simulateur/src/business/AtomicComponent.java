package business;


import java.util.ArrayList;

public abstract class AtomicComponent {
	protected String name;
	protected int current_state;
	protected int next_state;
	protected int previous_state;
	protected ArrayList<IO> inputs;
	protected ArrayList<IO> outputs;

	public AtomicComponent(String name) {
		this.name = name;
	}

	public void delta_int() {

	}

	public void delta_ext(ArrayList<IO> inputs) {

	}
	
	public void delta_con(ArrayList<IO> inputs) {
		
	}

	public void init() {
		current_state = 0;
	}

	public double getTa() {
		return (0);
	}

	public ArrayList<IO> lambda() {
		return null;
	}
}
