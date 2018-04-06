package com.iutnc.equichier;

import java.util.Scanner;

import javax.swing.JFrame;

public class Principal {
	public static void main(String[] args) {
		JFrame f = new JFrame();
		Echiquier plateau = new Echiquier();
		plateau.setFenetre(f);
		Scanner sc = new Scanner(System.in);

		f.setContentPane(plateau);
		f.setTitle("Echiquier");
		f.pack();
		f.setSize(515, 545);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);

		f.addMouseListener(plateau);
		f.addMouseMotionListener(plateau);
		
		/*int toX, toY;

		
		while (true) {
			// Lecture des coordonnées de départ
			System.out.print("fromX : ");
			plateau.setCaseSelectedX(lireEntier(sc, 1, 8));
			System.out.print("fromY : ");
			plateau.setCaseSelectedX(lireEntier(sc, 1, 8));
			
			// Lecture des coordonnées d'arrivée
			System.out.print("toX : ");
			toX = lireEntier(sc, 1, 8);
			System.out.print("toY : ");
			toY = lireEntier(sc, 0, 8);
			
			// Déplacement de la pièce
			plateau.movePiece(toX-1, toY-1);
		}*/
	}
	
	public static int lireEntier(Scanner sc, int min, int max) {
		int x = 0;
		do {
			try {
				x = sc.nextInt();
				if (x > max) System.out.println("Maxmimum " + max);
				if (x < min) System.out.println("Minimum " + min);
			} catch (Exception e) {
				System.out.println("Entrez un nombre entier !");
				sc.nextLine();
			}
		} while (x < min || x > max);
		
		return x;
	}
}
