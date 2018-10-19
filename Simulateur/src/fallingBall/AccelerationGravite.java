package fallingBall;

import java.util.ArrayList;

import business.AtomicComponent;
import business.IO;
import business.IOenum;

/*
 * la gravité est une force constante à 9.81.
 * on l'initialise sa force à 0 au début. Nous allons donc lui donner une accélération de 9.81 lors de la 1ere instruction puis de 0. 
 * 
 */
public class AccelerationGravite extends AtomicComponent {
	double gravity_value = -9.81;

	public AccelerationGravite(String name) {
		super(name);
		requiredTime.put(0, 0.0); // l'état 0 doit durer 2 UA de temps.
		requiredTime.put(1, Double.POSITIVE_INFINITY);
	}

	@Override
	public void delta_int() {
		// There are only 2 states, and the first one last only for one iteration. So the next_state is always 1.
		next_state = 1;
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
		outputs.add(new IO(IOenum.JOB, gravity_value, this));
		return outputs;
	}
}