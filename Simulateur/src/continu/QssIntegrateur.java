package continu;

import java.util.ArrayList;

import business.AtomicComponent;
import business.IO;
import business.IOenum;

public class QssIntegrateur extends AtomicComponent {
	// public enum etat{BUSY,IDLE}
	private double stepQ;
	private double currentQ;
	private double derivativeQ;
	// private double deltaQ;
	private double lastQ;

	public double getCurrentQ() {
		return currentQ;
	}

	public void setCurrentQ(double currentQ) {
		this.currentQ = currentQ;
	}

	public QssIntegrateur(String name, double pas) {
		super(name);
		this.stepQ = pas;
		currentQ = 0;
	}

	public QssIntegrateur(String name, double pas, double initialValue) {
		super(name);
		this.stepQ = pas;
		currentQ = initialValue;
	}

	public void init() {
		super.init();
		requiredTime.put(0, Double.POSITIVE_INFINITY);
		derivativeQ = 0.0;
		lastQ = 0.0;
	}

	@Override
	public void delta_int() {
		lastQ = currentQ;
		// I compute the new value of currentQ. the time 'stepTime' has past.
		currentQ += derivativeQ * requiredTime.get(current_state);
		requiredTime.put(current_state, stepQ / Math.abs(derivativeQ));
		ellapsedTime = 0;
	}

	@Override
	public void delta_ext(ArrayList<IO> inputs) {

		if (inputs.get(0).getType().equals(IOenum.RESET)) {
			// it happens if the ball reach the flour. So Q take the opposite value.
			lastQ = currentQ;
			currentQ = -lastQ;
			requiredTime.put(current_state, 0.0);
			// I keep the same derivative because the others forces didn't change.
			//System.out.println(" I RESET  ! !! ! !");
			return;
		}

		// I compute the new value of currentQ at this time :
		lastQ = currentQ;
		currentQ += ellapsedTime * derivativeQ;

		// Now I update my derivative.
		// I suppose I have only 1 input. So it is in input[0]
		derivativeQ = (double) inputs.get(0).getValue();

		current_state = next_state;

		// so now, I compute the time it remains me to change of state q.
		// It remains me a deltaQ of :
		double remainedQ = stepQ - Math.abs(currentQ - lastQ);
		// so I update the time this state require:
		requiredTime.put(current_state, remainedQ / Math.abs(derivativeQ));
		ellapsedTime = 0;

		//System.out.println(" Me : " + name + " -> I computed Q = " + currentQ);
	}

	@Override
	public ArrayList<IO> lambda() {
		ArrayList<IO> outputs = new ArrayList<>();
		outputs.add(new IO(IOenum.DONE, currentQ + derivativeQ * requiredTime.get(current_state), this));
		return outputs;
	}
}
