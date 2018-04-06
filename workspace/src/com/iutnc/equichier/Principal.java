package com.iutnc.equichier;

import javax.swing.JFrame;

public class Principal {
	public static void main(String[] args) {
		JFrame f = new JFrame();
		Echiquier plateau = new Echiquier();
		plateau.setFenetre(f);
		
		f.setContentPane(plateau);
		f.setTitle("Echiquier");
		f.pack();
		f.setSize(515, 545);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);

		f.addMouseListener(plateau);
		f.addMouseMotionListener(plateau);
	}
}
