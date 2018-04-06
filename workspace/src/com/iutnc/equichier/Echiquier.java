package com.iutnc.equichier;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Echiquier extends JPanel implements MouseListener, MouseMotionListener {
	// Constantes
	private final int tailleCaseX = 64;
	private final int tailleCaseY = 64;
	private final int taillePlateauX = 8 * tailleCaseX;
	private final int taillePlateauY = 8 * tailleCaseY;

	private final String pathImg = "pions.png";
	private JFrame fenetre;

	// Tableau des pièces sur le plateau
	private Piece[][] pieces;

	// Attributs pour l'animation des mouvements
	private Piece pieceEnDeplacement = null; // La pièce en mouvement
	private float progress = 0.0f; // La progression de l'animation

	// Attributs pour la sélection
	private int selectX = -1, selectY = -1;
	private int caseHoverX = -1, caseHoverY = -1;
	boolean selected = false;
	boolean tourDesBlancs = true;

	/**
	 * Constructeur de l'échiquier
	 */
	public Echiquier() {
		//*/
		pieces = new Piece[][] {
			{new Piece(false, TypePiece.TOUR), 	   new Piece(false, TypePiece.PION), null, null, null, null, new Piece(true, TypePiece.PION), new Piece(true, TypePiece.TOUR)},
			{new Piece(false, TypePiece.CAVALIER), new Piece(false, TypePiece.PION), null, null, null, null, new Piece(true, TypePiece.PION), new Piece(true, TypePiece.CAVALIER)},
			{new Piece(false, TypePiece.FOU),	   new Piece(false, TypePiece.PION), null, null, null, null, new Piece(true, TypePiece.PION), new Piece(true, TypePiece.FOU)},
			{new Piece(false, TypePiece.REINE),    new Piece(false, TypePiece.PION), null, null, null, null, new Piece(true, TypePiece.PION), new Piece(true, TypePiece.REINE)},
			{new Piece(false, TypePiece.ROI), 	   new Piece(false, TypePiece.PION), null, null, null, null, new Piece(true, TypePiece.PION), new Piece(true, TypePiece.ROI)},
			{new Piece(false, TypePiece.FOU), 	   new Piece(false, TypePiece.PION), null, null, null, null, new Piece(true, TypePiece.PION), new Piece(true, TypePiece.FOU)},
			{new Piece(false, TypePiece.CAVALIER), new Piece(false, TypePiece.PION), null, null, null, null, new Piece(true, TypePiece.PION), new Piece(true, TypePiece.CAVALIER)},
			{new Piece(false, TypePiece.TOUR), 	   new Piece(false, TypePiece.PION), null, null, null, null, new Piece(true, TypePiece.PION), new Piece(true, TypePiece.TOUR)}
		};
		/*/
		pieces = new Piece[][] {
			{new Piece(false, TypePiece.PION), new Piece(false, TypePiece.PION), new Piece(false, TypePiece.PION), new Piece(false, TypePiece.PION), new Piece(false, TypePiece.PION), new Piece(false, TypePiece.PION), new Piece(false, TypePiece.PION), new Piece(false, TypePiece.PION), new Piece(false, TypePiece.PION), new Piece(false, TypePiece.PION)},
			{null, null, null, null, null, null, null, null, null, null},
			{new Piece(true, TypePiece.FOU), null, null, null, new Piece(true, TypePiece.FOU), null, null, null, null, null},
			{null, new Piece(true, TypePiece.FOU), null, new Piece(true, TypePiece.FOU), null, null, null, null, null, null},
			{null, null, new Piece(false, TypePiece.FOU), null, null, null, null, null, null, null},
			{null, new Piece(true, TypePiece.FOU), null, new Piece(true, TypePiece.FOU), null, null, null, null, null, null},
			{new Piece(true, TypePiece.FOU), null, null, null, new Piece(true, TypePiece.FOU), null, null, null, null, null},
			{null, null, null, null, new Piece(false, TypePiece.FOU), null, null, null, null, null},
			{null, null, null, null, null, null, null, null, null, null},
			{null, null, null, null, null, null, null, null, null, null}
		};
		/**/
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (fenetre != null)
			fenetre.setTitle(tourDesBlancs?"Tour des blancs":"Tour des noirs");
		dessinerPlateau(g);
		dessinerPions(g);
	}

	/**
	 * Dessine le damier : cases blanches/noires + case sélectionnée + case pointée
	 * @param g le Graphics sur lequel dessiner le damier
	 */
	private void dessinerPlateau(Graphics g) {
		// Calcule la position du plateau d'échecs dans la fenêtre
		int posX = getWidth()/2 - taillePlateauX/2;
		int posY = getHeight()/2 - taillePlateauY/2;
		boolean caseBlanche = true;

		// Dessine chaque case du plateau
		for (int x = 0; x < 8 ; x++) {
			for (int y = 0 ; y < 8 ; y++) {

				if (x == selectX && y == selectY && pieceEnDeplacement == null) {
					// Case sélectionnée
					g.setColor(Color.ORANGE);
					g.fillRect(x*taillePlateauX/8+posX, y*taillePlateauY/8+posY, taillePlateauX/8, taillePlateauY/8);
				}
				else if (x == caseHoverX && y == caseHoverY) {
					// Case pointée avec le curseur
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect(x*taillePlateauX/8+posX, y*taillePlateauY/8+posY, taillePlateauX/8, taillePlateauY/8);
				}
				else if (selected && deplacementPossible(pieces[selectX][selectY], x, y)) {
					// Case déplacement possible
					if (peutManger(pieces[selectX][selectY], x, y))
						g.setColor(Color.RED);
					else
						g.setColor(Color.GREEN);
					g.fillRect(x*taillePlateauX/8+posX, y*taillePlateauY/8+posY, taillePlateauX/8, taillePlateauY/8);
				}
				else {
					// Case blanche / noire
					if (caseBlanche) g.setColor(Color.WHITE);
					else g.setColor(Color.GRAY);
					g.fillRect(x*taillePlateauX/8+posX, y*taillePlateauY/8+posY, taillePlateauX/8, taillePlateauY/8);
				}
				
				caseBlanche = !caseBlanche;
			}
			caseBlanche = !caseBlanche;
		}
	}
	
	private boolean peutManger(Piece mangeur, int toX, int toY) {
		int fromX = getPosXPiece(mangeur);
		int fromY = getPosYPiece(mangeur);
		
		switch (mangeur.getType()) {
			case CAVALIER:
			case FOU:
			case REINE:
			case ROI:
			case TOUR:
			default:
				return pieces[toX][toY] != null && deplacementPossible(mangeur, toX, toY);
			case PION:
				if (Math.abs(fromX - toX) == 1) {
					if (fromX+1 == toX && pieces[toX][toY] != null && couleursDifferente(mangeur, pieces[toX][toY])) {
						if (mangeur.estBlanc() && fromY-1 == toY)
							return true;
						if (!mangeur.estBlanc() && fromY+1 == toY)
							return true;
					}
					else if (fromX-1 == toX && pieces[toX][toY] != null && couleursDifferente(mangeur, pieces[toX][toY])) {
						if (mangeur.estBlanc() && fromY-1 == toY)
							return true;
						if (!mangeur.estBlanc() && fromY+1 == toY)
							return true;
					}
					return false;
				}
			break;
		}
		return true;
	}
	
	private int getPosXPiece(Piece p) {
		// Cherche la position de la pièce dans le plateau
		for (int i = 0 ; i < pieces.length ; i++) {
			for (int j = 0 ; j < pieces[i].length ; j++) {
				if (p == pieces[i][j]) {
					return i;
				}
			}
		}
		return -1;
	}
	
	private int getPosYPiece(Piece p) {
		// Cherche la position de la pièce dans le plateau
		for (int i = 0 ; i < pieces.length ; i++) {
			for (int j = 0 ; j < pieces[i].length ; j++) {
				if (p == pieces[i][j]) {
					return j;
				}
			}
		}
		return -1;
	}
	
	private boolean deplacementPossible(Piece pieceABouger, int toX, int toY) {
		if (pieceABouger == null) return false;
		if (!couleursDifferente(pieceABouger, pieces[toX][toY])) return false;
		
		int fromX = getPosXPiece(pieceABouger);
		int fromY = getPosYPiece(pieceABouger);

		switch (pieceABouger.getType()) {
			case CAVALIER:
				if (!(fromX + 2 == toX && fromY + 1 == toY
					|| fromX + 2 == toX && fromY - 1 == toY
					|| fromX - 2 == toX && fromY + 1 == toY
					|| fromX - 2 == toX && fromY - 1 == toY
					|| fromX + 1 == toX && fromY + 2 == toY
					|| fromX + 1 == toX && fromY - 2 == toY
					|| fromX - 1 == toX && fromY + 2 == toY
					|| fromX - 1 == toX && fromY - 2 == toY)) return false;
				break;
				
			case ROI:
				if (fromX+1 < toX || fromX-1 > toX) return false;
				if (fromY+1 < toY || fromY-1 > toY) return false;
				if (pieceABouger.estBlanc() == tourDesBlancs && roiEnEchec(pieceABouger, toX, toY)) return false;
				// TODO petit/grand roque
				break;
				
			case TOUR:
				if (toX != fromX && toY != fromY) return false;
				if (!horizDispo(fromX, toX, toY)) return false;
				if (!verticDispo(toX, fromY, toY)) return false;
				break;
				
			case FOU:
				return false;
				//if (toX - fromX != toY - fromY && toX - fromX != fromY - toY) return false;
				//if (!diagonaleDispo(toX, toY)) return false;
				//break;
				
			case PION:
				// TODO coup "en passant"
				if (pieceABouger.estBlanc()) {
					if (fromY < toY) return false;
					if (!pieceABouger.hasMoved()) {
						if (fromY-2 > toY || pieces[toX][toY] != null) return false;
					} else {
						if (fromY-1 > toY || pieces[toX][toY] != null) return false;
					}
				} else {
					if (fromY > toY) return false;
					if (!pieceABouger.hasMoved()) {
						if (fromY+2 < toY || pieces[toX][toY] != null) return false;
					} else {
						if (fromY+1 < toY || pieces[toX][toY] != null) return false;
					}
				}
				break;
				
			case REINE:
				// Cas : diagonale
				if (toX - fromX == toY - fromY || toX - fromX == fromY - toY) {
					if (!diagonaleDispo(toX, toY)) {
						return false;
					}
				}
				// Cas : horizontal/vertical
				else if (toX == fromX || toY == fromY) {
					if (toY == fromY && !horizDispo(fromX, toX, toY)) return false;
					if (toX == fromX && !verticDispo(fromX, fromY, toY)) return false;
				}
				// Autres cas : impossible
				else {
					return false;
				}
				break;
		}
		System.out.println(pieceABouger.getType() + " " + (pieceABouger.estBlanc()?"blanc":"noir") + " peut aller en " + toX + " " + toY);
		return true;
	}
	
	private boolean horizDispo(int fromX, int toX, int y) {
		if (fromX > toX) {
			int tmp = fromX;
			fromX = toX;
			toX = tmp;
		}
		for (int i = fromX+1 ; i < toX && i < 8 ; i++) {
			if (pieces[i][y] != null)
				return false;
		}
		return true;
	}
	
	private boolean verticDispo(int x, int fromY, int toY) {
		if (fromY > toY) {
			int tmp = fromY;
			fromY = toY;
			toY = tmp;
		}
		for (int i = fromY+1 ; i < toY && i < 8 ; i++) {
			if (pieces[x][i] != null && i != toY)
				return false;
		}
		return true;
	}
	
	/**
	 * Vérifie que la pièce sélectionnée est différente de celle aux coordonnées passées en paramètres
	 * @return
	 */
	private boolean couleursDifferente(Piece p1, Piece p2) {
		return p1 == null || p2 == null || p1.estBlanc() != p2.estBlanc();
	}
	
	private boolean roiEnEchec(Piece roi, int posX, int posY) {
		if (roi == null || roi.getType() != TypePiece.ROI) return false;

		for (int x = 0 ; x < pieces.length ; x++) {
			for (int y = 0 ; y < pieces[x].length ; y++) {
				Piece p = pieces[x][y];
				if (p != null && couleursDifferente(roi, p)) {
					// Si la pièce peut se déplacer sur la case du roi : il est en échec
					if (deplacementPossible(p, posX, posY)) {
						//System.out.println("NOT OK - " + x + " " + y + " " + p.getType() + " " + p.estBlanc());
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean diagonaleDispo(int toX, int toY) {
		for (int i = 1 ; i < 7 ; i++) {
			if (selectX < toX) {
				if (selectY < toY) {
					// BAS DROIT
					if (selectX+i < 8 && selectY+i<8 && pieces[selectX+i][selectY+i] != null && selectX+i < toX) {
						return false;
					}
				} else {
					// HAUT DROIT
					if (selectX+i < 8 && selectY-i>=0 && pieces[selectX+i][selectY-i] != null && selectX+i < toX) {
						return false;
					}
				}
			} else {
				if (selectY < toY) {
					// BAS GAUCHE
					if (selectX-i >= 0 && selectY+i < 8 && pieces[selectX-i][selectY+i] != null && selectX-i > toX) {
						return false;
					}
				} else {
					// HAUT GAUCHE
					if (selectX-i >= 0 && selectY-i>=0 && pieces[selectX-i][selectY-i] != null && selectX-i > toX) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Dessine les pions sur la fenêtre
	 * @param g le Graphics sur lequel dessiner
	 */
	private void dessinerPions(Graphics g) {
		// Calcule la position du plateau d'échecs dans la fenêtre
		int posX = getWidth()/2 - taillePlateauX/2;
		int posY = getHeight()/2 - taillePlateauY/2;
		
		// Images des pions
		BufferedImage imgPions = null;
		BufferedImage pion = null;

		// Ouvre l'image globale des pions
		try {
			imgPions = ImageIO.read(new File(pathImg));
		} catch (IOException e) {
			System.out.println("Le fichier " + pathImg + " ne peut être lu.\n" + e.getMessage());
			System.exit(-1);
		}

		// Place chacune des pièces sur le plateau
		for (int x = 0; x < 8 ; x++) {
			for (int y = 0 ; y < 8 ; y++) {
				// Récupère l'image du pion correspondante selon le type et la couleur de la pièce
				pion = getImgPiece(pieces[x][y], imgPions);
				if (pion != null)
					g.drawImage(pion, x*taillePlateauX/8+posX, y*taillePlateauY/8+posY, null);
			}
		}

		// Animation du déplacement d'une pièce
		if (pieceEnDeplacement != null) {
			int x = (int)((caseHoverX-selectX)*taillePlateauX/8*progress + selectX*taillePlateauX/8);
			int y = (int)((caseHoverY-selectY)*taillePlateauY/8*progress + selectY*taillePlateauY/8);
			pion = getImgPiece(pieceEnDeplacement, imgPions);
			if (pion != null)
				g.drawImage(pion, x+posX, y+posY, null);
		}
	}

	/**
	 * Renvoie l'image correspondant à la pièce passée en paramètre
	 * @param piece la pièce dont on souhaite l'image
	 * @param imgPieces l'image contenant toutes les pièces
	 * @return l'image de la pièce passée en paramètre
	 */
	public BufferedImage getImgPiece(Piece piece, BufferedImage imgPieces) {
		if (piece == null)
			return null;
		
		int posY = piece.estBlanc() ? 64 : 0;
		
		switch (piece.getType()) {
			case CAVALIER:
				return imgPieces.getSubimage(3*64, posY, 64, 64);
			case FOU:
				return imgPieces.getSubimage(4*64, posY, 64, 64);
			case PION:
				return imgPieces.getSubimage(5*64, posY, 64, 64);
			case REINE:
				return imgPieces.getSubimage(64, posY, 64, 64);
			case ROI:
				return imgPieces.getSubimage(0, posY, 64, 64);
			case TOUR:
				return imgPieces.getSubimage(2*64, posY, 64, 64);
			default:
				return null;
		}
	}

	/**
	 * Renvoie l'abscice de la case au-dessus de laquelle se trouve le curseur
	 * @param e l'event souris
	 * @return abscice de la case (0-7)
	 */
	public int getXSelected(MouseEvent e) {
		int x;
		int posPlateauX = getWidth()/2 - taillePlateauX/2;
		
		for (x = 1 ; x < 8 ; x++) {
			if (e.getX() < x*tailleCaseX+posPlateauX) {
				break;
			}
		}
		return x-1;
	}


	/**
	 * Renvoie l'ordonnée de la case au-dessus de laquelle se trouve le curseur
	 * @param e l'event souris
	 * @return ordonnée de la case (0-7)
	 */
	public int getYSelected(MouseEvent e) {
		int y;
		int posPlateauY = getHeight()/2 - taillePlateauY/2;

		for (y = 1 ; y < 8 ; y++) {
			if (e.getY()-tailleCaseY/2 < y*tailleCaseY+posPlateauY) {
				break;
			}
		}
		return y-1;
	}

	/**
	 * Déplace une pièce de l'échiquier. Si une pièce prend la place d'une autre, l'autre est mangée
	 * @return
	 */
	public void movePiece(Piece p, int to_X, int to_Y) {
		if (selectX < 0 || selectX > 7)
			return;
		
		if (selectY < 0 || selectY > 7)
			return;
		
		// TODO décommenter
		/*
		if (!deplacementPossible(p, to_X, to_Y)) {
			selectX = -1;
			selectY = -1;
			selected = false;
			return;
		}*/
		
		pieceEnDeplacement = pieces[selectX][selectY];
		pieceEnDeplacement.setMoved(true);
		pieces[selectX][selectY] = null;
		caseHoverX = to_X;
		caseHoverY = to_Y;
		animate();
		tourDesBlancs = !tourDesBlancs;
	}

	public void animate() {
        final int animationTime = 300;
        int framesPerSecond = 30;
        int delay = 1000 / framesPerSecond;
        final long start = System.currentTimeMillis();
        final Timer timer = new Timer(delay, null);
        progress = 0;
        timer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final long now = System.currentTimeMillis();
                final long elapsed = now - start;
                
                // sinus pour un déplacement avec accélération/décélération
                progress = (float)Math.sin(Math.PI/2 * (float) elapsed / animationTime);
                
                repaint();
                
            	if (elapsed >= animationTime) {
                	timer.stop();
                	pieces[caseHoverX][caseHoverY] = pieceEnDeplacement;
                	
                	// Réinitialise les variables
                	pieceEnDeplacement = null;
                	selectX = -1;
                	selectY = -1;
                	caseHoverX = -1;
                	caseHoverX = -1;
                }
			}
		});
        timer.start();
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		// Actualise la position
		mouseMoved(e);
		
		// Empêche la sélection si une pièce n'a pas fini d'être déplacée (= animation en cours)
		if (pieceEnDeplacement != null) return;
		
		// Si une case est déjà sélectionnée
		if (selected) {
			selected = false;
			// On déplace la case sélectionnée vers la case sur laquelle le joueur a cliqué
			movePiece(pieces[selectX][selectY], caseHoverX, caseHoverY);
			repaint();
		} else {
			// Si la case sur laquelle le joueur clique contient une pièce
			Piece p = pieces[caseHoverX][caseHoverY];
			if (p != null && (p.estBlanc() && tourDesBlancs || !p.estBlanc() && !tourDesBlancs)) {
				selected = true;
				selectX = caseHoverX;
				selectY = caseHoverY;
				repaint();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// Empêche la sélection si une pièce n'a pas fini d'être déplacée (= animation en cours)
		if (pieceEnDeplacement != null) return;

		caseHoverX = getXSelected(e);
		caseHoverY = getYSelected(e);
		repaint();
	}
	
	/**
	 * Entre 0 et 7
	 * @param caseSelectedX
	 */
	public void setCaseSelectedX(int caseSelectedX) {
		if (caseSelectedX >= 0 && caseSelectedX < 8)
			this.selectX = caseSelectedX;
	}

	/**
	 * Entre 0 et 7
	 * @param caseSelectedY
	 */
	public void setCaseSelectedY(int caseSelectedY) {
		if (caseSelectedY >= 0 && caseSelectedY < 8)
			this.selectY = caseSelectedY;
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}
	
	public void setFenetre(JFrame fenetre) {
		this.fenetre = fenetre;
	}
}
