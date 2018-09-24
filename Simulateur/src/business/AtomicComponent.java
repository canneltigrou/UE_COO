package business;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AtomicComponent {
	protected String name;
	protected int current_state;
	protected int next_state;
	protected int previous_state;
	protected ArrayList<IOenum> inputs;
	protected ArrayList<IOenum> outputs;

	protected HashMap<Integer, Double> requiredTime; // affecte le temps requis pour chaque etat
	protected double ellapsedTime;

	public AtomicComponent(String name) {
		this.name = name;
		inputs = new ArrayList<>();
		outputs = new ArrayList<>();
		requiredTime = new HashMap<>();
	}

	/* equivaut à un changement d'état */
	public void delta_int() {

	}

	public void delta_ext(ArrayList<IOenum> inputs) {
		current_state = next_state;
		ellapsedTime = 0;
	}

	public void delta_con(ArrayList<IOenum> inputs) {
		current_state = next_state;
		ellapsedTime = 0;
	}

	public void init() {
		current_state = 0;

	}

	/* get Ta retourne le temps restant */
	public double getTa() {
		return (requiredTime.get(current_state) - ellapsedTime);
	}

	/* retourne les evenements qu'il envoie */
	public ArrayList<IOenum> lambda() {
		return null;
	}

	public void IncrementTime(double time) {
		ellapsedTime += time;
		if (ellapsedTime > requiredTime.get(current_state)) {
			System.out.println("ERROR du scheduller : " + name + " n'a pas été appélé à temps.");// ERROR du scheduller.
			return;
		}
		if (ellapsedTime == requiredTime.get(current_state))
			ellapsedTime = 0;
	}

	@Override
	public String toString() {
		return name;
	}

}
