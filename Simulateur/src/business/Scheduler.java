package business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import chart.Chart;
import chart.ChartFrame;

public class Scheduler {

	private static ArrayList<AtomicComponent> components;
	private static Double temps;
	private static boolean stop; // equivalent à Tmax, mais ici on demandera par IHM si on continue ou non
	private static ArrayList<AtomicComponent> imminentComponents;
	private static ArrayList<AtomicComponent> impactedComponents;
	private static Double tempsMin;
	private static Buf buffer;
	private static Gen generator;
	private static Proc processor;
	private static ArrayList<IOenum> messageList;

	// for choose the mode we want to run : dynamic mode, or until we stop the
	// program (ctrl-C) or with a choosen time ?
	private static boolean dynamic;
	private static double chosenTime;

	public static boolean askContinue() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("\nDo you want to continue the simulation ? (y|n)\n");
		String s = br.readLine();
		if (s.equals("y") || s.equals("Y"))
			return true;
		return false;
	}

	public static void askMode() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean stop = false;
		int chosenNumber = 0;
		//double tmpTime = 0;
		String s ;
		while (!stop)
		{
			System.out.println("Choose a mode :");
			System.out.println("  1 - Dynamic mode (you choose to continue or not at each incrementation of time)");
			System.out.println("  2 - Infinite mode (you must stop manually the program to stop the simulation : ctrl-C)");
			System.out.println("  3 - Time mode (you choose a time, and the simulation will work until this time)");
			System.out.println("Your number : ");
			
			//choosenNumber = NumberUtils.toInt("1234");
			
			s = br.readLine();	
			try {
				chosenNumber = Integer.parseInt(s);
				if (chosenNumber > 0 && chosenNumber < 4)
					stop = true;
			} catch (Exception e) {
				System.out.println("Please, enter a number between 1 and 3 :");
			}
		}
			
		if (chosenNumber == 1)
			dynamic = true;
		else
			dynamic = false;
		
		if(chosenNumber == 2)
			chosenTime = Double.POSITIVE_INFINITY;
		
		if(chosenNumber == 3)
		{
			stop = false;
			while (!stop)
			{
				System.out.println("How many time do you want to play ? :");
				System.out.println("Your number : ");				
				s = br.readLine();	
				try {
					chosenTime = Double.parseDouble(s);
					stop = true;
				} catch (Exception e) {
					System.out.println("This number was not valid. Please, enter a valid number.");
				}
			}
			
		}
		
	}

	private static AtomicComponent impactedComponent(IOenum message) {
		if (message.equals(IOenum.JOB))
			return buffer;
		if (message.equals(IOenum.REQ))
			return processor;
		if (message.equals(IOenum.DONE))
			return buffer;
		return null;
	}

	private static ArrayList<AtomicComponent> impactedComponents(ArrayList<IOenum> messagesList) {
		ArrayList<AtomicComponent> res = new ArrayList<>();
		for (IOenum io : messagesList)
			res.add(impactedComponent(io));
		return res;
	}

	private static void initialisation_Scheduler() {
		components = new ArrayList<>();
		imminentComponents = new ArrayList<>();
		impactedComponents = new ArrayList<>();
		messageList = new ArrayList<>();

		generator = new Gen("Generateur");
		components.add(generator);
		buffer = new Buf("Buffer");
		components.add(buffer);
		processor = new Proc("Processeur");
		components.add(processor);
		stop = false;

	}

	public static void main(String[] args) throws IOException {

		initialisation_Scheduler();
		askMode();
		
		Double tmp;
		ChartFrame cf = new ChartFrame("gbp", "GBP");
		Chart qChart = new Chart("q");
		// qChart.addDataToSeries(0.0, 1.0);
		cf.addToLineChartPane(qChart);
		for (AtomicComponent c : components)
			c.init();
		temps = 0.d;
		
		while (!stop) {
			// on recupere les differents Ta pour savoir le temps du prochain evenement et
			// les composants a executer.
			System.out.println("temps : " + temps.toString());
			imminentComponents.clear();
			tempsMin = components.get(0).getTa();
			// tempsMin = Double.POSITIVE_INFINITY;
			for (AtomicComponent c : components) {
				tmp = c.getTa();
				System.out.println(c.name + ".Ta = " + tmp.toString());
				if (tmp < tempsMin) {
					imminentComponents.clear();
					tempsMin = tmp;
				}
				if (tmp.equals(tempsMin))
					imminentComponents.add(c);
			}
			System.out.println("Le prochain evenement se produira dans t + " + tempsMin);

			// j'execute lambda et recupere les composants impactés
			temps += tempsMin;
			System.out.println(" Temps = " + temps);

			messageList.clear();
			for (AtomicComponent c : imminentComponents) {
				System.out.println(c.toString() + " execute Lambda.");
				messageList.addAll(c.lambda());

			}
			impactedComponents = impactedComponents(messageList);
			System.out.println("Les messages emis sont : " + messageList.toString());
			System.out.println("Lesquels impactent : " + impactedComponents.toString());

			for (AtomicComponent c : components) {
				if (imminentComponents.contains(c)) {
					if (impactedComponents.contains(c))
						c.delta_con(messageList);
					else
						c.delta_int();
				} else { // non imminent
					if (impactedComponents.contains(c))
						c.delta_ext(messageList);
					else
						// temps += tempsMin;
						c.IncrementTime(tempsMin);
				}
			}
			qChart.addDataToSeries(temps, buffer.getQ());
			System.out.println("le q = " + buffer.getQ());
			
			if(dynamic)
				stop = !askContinue();
			else
				if (temps > chosenTime)
					stop = true;
		}
	}
}
