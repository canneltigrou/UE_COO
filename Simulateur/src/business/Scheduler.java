package business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Scheduler {

	private static ArrayList<AtomicComponent> components;
	private static Double temps;
	private static boolean stop; // equivalent à Tmax, mais ici on demandera par IHM si on continue ou non
	private static ArrayList<AtomicComponent> imminentComponents;
	private static Double tempsMin;

	public static boolean askContinue() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Do you want to continue the simulation ? (y|n)");
		String s = br.readLine();
		if (s.equals("y") || s.equals("Y"))
			return true;
		return false;
	}

	public static void main(String[] args) throws IOException {

		Double tmp;
		components.add(new Gen("Generateur"));
		components.add(new Buf("Buffer"));
		components.add(new Proc("Processeur"));
		stop = false;

		for (AtomicComponent c : components)
			c.init();
		temps = 0.d;
		tempsMin = components.get(0).getTa();
		while (!stop) {
			// on recupere les differents Ta pour savoir le temps du prochain evenement et
			// les composants a executer.
			System.out.println("temps : " + temps.toString());
			for (AtomicComponent c : components) {
				tmp = c.getTa();
				System.out.println(c.name + ".Ta = " + tmp.toString());
				if (tmp < tempsMin) {
					imminentComponents.clear();
					tempsMin = tmp;
				}
				if (tmp == tempsMin)
					imminentComponents.add(c);
			}
			System.out.println("Le prochain evenement se produira dans t + " + tempsMin);

			// j'execute lambda et recupere les composants impactés
			for (AtomicComponent c : imminentComponents)
				c.lambda();

			//

			stop = askContinue();
		}

	}
}
