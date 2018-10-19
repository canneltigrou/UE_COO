package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import continu.ContinueEulerScheduler;
import continu.ContinueQssScheduler;
import discret.DiscretScheduler;
import fallingBall.BallScheduler;

public class Main {

	public enum Option {
		DISCRET, EULER, QSS, BALL
	}

	private static Option askOption() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean stop = false;
		int chosenNumber = 0;
		// double tmpTime = 0;
		String s;
		while (!stop) {
			System.out.println("Choose an option :");
			System.out.println("  1 - Version 1 : the discret integrator");
			System.out.println("  2 - Version 2 : the Euler integrator");
			System.out.println("  3 - Version 3 : the QSS integrator");
			System.out.println("  4 - Version 4 : the exemple of a falling ball");
			System.out.println("Your number : ");

			s = br.readLine();
			try {
				chosenNumber = Integer.parseInt(s);
				if (chosenNumber > 0 && chosenNumber < 5)
					stop = true;
			} catch (Exception e) {
				System.out.println("Please, enter a number between 1 and 4 :");
			}
		}

		if (chosenNumber == 1)
			return Option.DISCRET;
		if (chosenNumber == 2)
			return Option.EULER;
		if (chosenNumber == 3)
			return Option.QSS;
		return Option.BALL;
	}

	public static void main(String[] args) throws IOException {
		Option option = askOption();
		if (option.equals(Option.DISCRET))
			new DiscretScheduler();
		else
			if (option.equals(Option.EULER))
				new ContinueEulerScheduler();
			else
				if (option.equals(Option.QSS))
					new ContinueQssScheduler();
				else
					new BallScheduler();

	}

}
