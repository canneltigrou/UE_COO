package continu;

import java.util.ArrayList;

import business.IO;
import business.IOenum;
import discret.Proc;

public class EulerIntegrateur extends Proc{
	//public enum etat{BUSY,IDLE}
	private double pas;
	private double currentX;
	public double getCurrentX() {
		return currentX;
	}

	public void setCurrentX(double currentX) {
		this.currentX = currentX;
	}

	private double derivativeX;
	
	public EulerIntegrateur(String name, double pas) {
		super(name);
		this.pas = pas;
	}

	public void init() { 
		super.init();
		requiredTime.put(0, pas);
		currentX = 0.0;
		derivativeX = 0.0;
	}

	@Override
	public void delta_int() {
		// I compute the new value of currentX. the time 'pas' has past.
		currentX += pas*derivativeX;
		ellapsedTime = 0;
	}

	@Override
	public void delta_ext(ArrayList<IO> inputs) {
		// I compute the new value of currentX at this time :
		currentX += ellapsedTime * derivativeX;
		
		// Now I update my derivative.
		// I suppose I have only 1 input. So it is in input[0]
		derivativeX = (double)inputs.get(0).getValue();
		
		current_state = next_state;
		ellapsedTime = 0;
	}

	@Override
	public ArrayList<IO> lambda() {
		ArrayList<IO> outputs = new ArrayList<>();
		outputs.add(new IO(IOenum.DONE, currentX, this));
		return outputs;
	}
}

