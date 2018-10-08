package continu;

import java.util.ArrayList;
import java.util.HashMap;

import business.AtomicComponent;
import business.IO;
import business.IOenum;

public class Adder extends AtomicComponent {
	HashMap<AtomicComponent, Object> xi ; // prend en clé le nom du composant, et sa valeur Xi.
	double somme;
	
	public Adder(String name) {
		super(name);
	}
	
	public void init() { 
		super.init();
		xi = new HashMap<>();
		requiredTime.put(0, Double.POSITIVE_INFINITY);
		requiredTime.put(1, 0.0);
	}

	@Override
	public void delta_int() {
		next_state = (current_state + 1)%1;
		current_state = next_state;
		ellapsedTime = 0;
	}

	@Override
	public void delta_ext(ArrayList<IO> inputs) {
		if (current_state == 0)
		{
			next_state = 1;
			// maj des Xi en fonction de ce que j'ai reçu
			for (IO input : inputs)
				xi.put(input.getOrigin(), input.getValue());
		}
		current_state = next_state;
		ellapsedTime = 0;
	}

	/* Ici : reenvoie la somme des valeurs de la hashmap, lesquelles sont des doubles */
	private double compute() {
		double res = 0.0;
		for (Object x : xi.values()) {
			res += (double)x;
		}
		somme = res;
		return res;
	}
	
	
	public double getSomme() {
		return somme;
	}

	public void setSomme(double somme) {
		this.somme = somme;
	}

	@Override
	public ArrayList<IO> lambda() {
		ArrayList<IO> outputs = new ArrayList<>();
		if (current_state == 1) {
			outputs.add(new IO(IOenum.REQ, (Object)compute(), this));
		}
		return outputs;
	}

}
