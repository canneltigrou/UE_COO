package fallingBall;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import business.AtomicComponent;
import business.IO;
import business.IOenum;
import chart.Chart;
import chart.ChartFrame;
import continu.Adder;
import continu.QssIntegrateur;

public class BallScheduler {

	private ArrayList<AtomicComponent> components;
	private Double temps;
	private boolean stop; // equivalent à Tmax, mais ici on demandera par IHM si on continue ou non
	private ArrayList<AtomicComponent> imminentComponents;
	private ArrayList<AtomicComponent> impactedComponents;
	private Double tempsMin;
	private AccelerationGravite gravite;
	// private Adder adder;
	private QssIntegrateur acceleration2speed;
	private QssIntegrateur speed2position;
	private GroundAnswer ground;

	private ArrayList<IO> messageList;

	// for choose the mode we want to run : dynamic mode, or until we stop the
	// program (ctrl-C) or with a chosen time ?
	private boolean dynamic;
	private double chosenTime;

	public boolean askContinue() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("\nDo you want to continue the simulation ? (y|n)\n");
		String s = br.readLine();
		if (s.equals("y") || s.equals("Y"))
			return true;
		return false;
	}

	public void askMode() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean stop = false;
		int chosenNumber = 0;
		// double tmpTime = 0;
		String s;
		while (!stop) {
			System.out.println("Choose a mode :");
			System.out.println("  1 - Dynamic mode (you choose to continue or not at each incrementation of time)");
			System.out.println(
					"  2 - Infinite mode (you must stop manually the program to stop the simulation : ctrl-C)");
			System.out.println("  3 - Time mode (you choose a time, and the simulation will work until this time)");
			System.out.println("Your number : ");

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

		if (chosenNumber == 2)
			chosenTime = Double.POSITIVE_INFINITY;

		if (chosenNumber == 3) {
			stop = false;
			while (!stop) {
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

	public double askBallHeight() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean stop = false;
		String s;
		double chosenHigh = 10;
		{
			stop = false;
			while (!stop) {
				System.out.println("How high is the ball ? :");
				System.out.println("Your number : ");
				s = br.readLine();
				try {
					chosenHigh = Double.parseDouble(s);
					stop = true;
				} catch (Exception e) {
					System.out.println("This number was not valid. Please, enter a valid number.");
				}
			}
		}
		return chosenHigh;

	}

	private AtomicComponent impactedComponent(IO message) {
		// Here we have 2 QssIntegrator... So I prefere determine the impacted component
		// in function of whom the message come from.
		if (message.getOrigin().equals(gravite))
			return acceleration2speed;
		if (message.getOrigin().equals(acceleration2speed))
			return speed2position;
		
		if (message.getOrigin().equals(speed2position))
		{		
			//System.out.println("HEY ! The ball is " + message.getValue() + " meters in height");
			return ground;
		}
		if (message.getOrigin().equals(ground))
			return acceleration2speed;
		return null;
	}

	private ArrayList<AtomicComponent> impactedComponents(ArrayList<IO> messagesList) {
		ArrayList<AtomicComponent> res = new ArrayList<>();
		for (IO io : messagesList)
			res.add(impactedComponent(io));
		return res;
	}

	private void initialisation_Scheduler() throws IOException {
		components = new ArrayList<>();
		imminentComponents = new ArrayList<>();
		impactedComponents = new ArrayList<>();
		messageList = new ArrayList<>();
		double pas = 0.005;

		gravite = new AccelerationGravite("graviteAcceleration");
		components.add(gravite);

		acceleration2speed = new QssIntegrateur("acceleration2speed", 0.01);
		components.add(acceleration2speed);

		speed2position = new QssIntegrateur("speed2position", 0.0001, askBallHeight());
		components.add(speed2position);
		
		ground = new GroundAnswer("Ground");
		components.add(ground);

		stop = false;

	}

	private void run() throws IOException {

		initialisation_Scheduler();
		askMode();

		Double tmp;
		ChartFrame cf = new ChartFrame("gbp", "GBP");
		Chart speedChart = new Chart("speed");
		Chart positionChart = new Chart("position");
		// qChart.addDataToSeries(0.0, 1.0);
		cf.addToLineChartPane(speedChart);
		cf.addToLineChartPane(positionChart);
		for (AtomicComponent c : components)
			c.init();
		temps = 0.d;

		while (!stop) {
			// on recupere les differents Ta pour savoir le temps du prochain evenement et
			// les composants a executer.
			//System.out.println("temps : " + temps.toString());
			imminentComponents.clear();
			tempsMin = components.get(0).getTa();
			for (AtomicComponent c : components) {
				tmp = c.getTa();
				//System.out.println(c.name + ".Ta = " + tmp.toString());
				if (tmp < tempsMin) {
					imminentComponents.clear();
					tempsMin = tmp;
				}
				if (tmp.equals(tempsMin))
					imminentComponents.add(c);
			}
			//System.out.println("Le prochain evenement se produira dans t + " + tempsMin);

			// j'execute lambda et recupere les composants impactés
			temps += tempsMin;
			//System.out.println(" Temps = " + temps);

			messageList.clear();
			for (AtomicComponent c : imminentComponents) {
				//System.out.println(c.toString() + " execute Lambda.");
				messageList.addAll(c.lambda());

			}
			impactedComponents = impactedComponents(messageList);
			//System.out.println("Les messages emis sont : " + messageList.toString());
			//System.out.println("Lesquels impactent : " + impactedComponents.toString());

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
			speedChart.addDataToSeries(temps, acceleration2speed.getCurrentQ());
			positionChart.addDataToSeries(temps, speed2position.getCurrentQ());
			// System.out.println("le q = " + buffer.getQ());

			if (dynamic)
				stop = !askContinue();
			else if (temps > chosenTime)
				stop = true;
		}
	}

	public BallScheduler() {
		try {
			run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
