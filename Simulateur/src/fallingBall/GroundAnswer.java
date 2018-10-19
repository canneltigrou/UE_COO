package fallingBall;

import java.util.ArrayList;

import business.AtomicComponent;
import business.IO;
import business.IOenum;

/*
 * It receives the position of the ball. If the position is equal to 0 : it means the ball reach the flour.
 * So the direction of the vector speed change. (The ground answer with a reverse force that it received)
 * 
 */
public class GroundAnswer extends AtomicComponent {
	double reach_value = 0;
	//boolean has_reached = false;

	public GroundAnswer(String name) {
		super(name);
		requiredTime.put(0, Double.POSITIVE_INFINITY);
		requiredTime.put(1, 0.0); // the case where I reached the floor. So it last only 1 UA
	}

	@Override
	public void delta_int() {
		// There are only 2 states, and the second one last only for one iteration. So the next_state is always 0.
		next_state = 0;
		current_state = next_state;
		ellapsedTime = 0;
	}
	
	@Override
	public void delta_ext(ArrayList<IO> inputs) {
		boolean has_reached = ((double) (inputs.get(0).getValue()) <= 0.0);
		if((double) (inputs.get(0).getValue()) <= 0.0)
			current_state = 1;
		ellapsedTime = 0;
		//System.out.println("ground : I detect : " + has_reached);
		//System.out.println("I'm in state " + current_state);
	}

	@Override
	public ArrayList<IO> lambda() {
		ArrayList<IO> outputs = new ArrayList<>();
		if (current_state == 1)
			outputs.add(new IO(IOenum.RESET, 0.0, this));
		return outputs;
	}
}