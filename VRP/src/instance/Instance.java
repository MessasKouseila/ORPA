package instance;

import java.util.Random;

import javax.swing.JTextArea;

public class Instance {
	private static Instance inst = null;
	public static Random rand = new Random();
	
	// nombre de station dans le graphe
	public int numberOfNode;
	public boolean [][] lien;
	// Flux d'information rentrant dans chaque station
	public double r[];// = {0.4,0.3,0.2,0.7,0.5,0.3};

	// Les temps pour aller d'un sommet à un autre
	public int t[][]; /*= {{0,3,0,0,0,3},
			{3,0,3,0,0,2},
			{0,3,0,2,0,3},
			{0,0,2,0,2,3},
			{0,0,0,2,0,3},
			{3,2,3,3,3,0}

	};*/
	private void initTime() {
		this.t = new int[this.numberOfNode][this.numberOfNode];
		for (int i = 0; i < this.numberOfNode; ++i) {
			for (int j = 0; j < this.numberOfNode; ++j) {
				if (this.lien[i][j]) {
					this.t[i][j] = (int)this.d[i][j] / 100;
				} else {
					this.t[i][j] = 0;
				}
			}
		}
	}

	// La distance entre les stations
	public int d[][];
	private void initDistance() {
		
		this.d = new int[this.numberOfNode][this.numberOfNode];
		for (int i = 0; i < this.numberOfNode; ++i) {
			for (int j = 0; j < this.numberOfNode; ++j) {
				this.d[i][j] = 0;
			}
		}
		
		for (int i = 0; i < this.numberOfNode; ++i) {
			for (int j = 0; j < this.numberOfNode; ++j) {
				if (this.lien[i][j]) {
					this.d[i][j] = (int)Rand(101, 505);
					this.d[j][i] = (int)Rand(101, 505);
				} else {
					this.d[i][j] = 0;
					this.d[j][i] = 0;
				}
			}
		}
	}
	// Les contraintes de ralentissement de transfert d'information pour une station
	public double alpha[][];
	private void initAlpha() {
		this.alpha = new double[this.numberOfNode][this.numberOfNode];
		for (int i = 0; i < this.numberOfNode; ++i) {
			for (int j = 0; j < this.numberOfNode; ++j) {
				if (i == j) {
					this.alpha[i][j] = 1;
				} else {
					this.alpha[i][j] = 0;
				}
			}
		}
	}
	public String printTime() {
		String res = "";
		for (int i = 0; i < this.numberOfNode - 1; ++i) {
			for (int j = i + 1; j < this.numberOfNode; ++j) {
				if(this.t[i][j] != 0) 
					res = res + "station "+ i +" à "+ j + " ==> "+this.t[i][j] + "\n";
			}
		}
		return res;
	}
	
	public String printDistance() {
		String res = "";
		for (int i = 0; i < this.numberOfNode - 1; ++i) {
			for (int j = i + 1; j < this.numberOfNode; ++j) {
				if(this.d[i][j] != 0) 
					res = res + "station "+ i +" vers "+ j + " ==> "+this.d[i][j] + "\n";
			}
		}
		return res;
	}
	public String printInformations() {
		String res = "";
		for (int i = 0; i < this.numberOfNode; ++i) {
			res = res + "station "+ i +" génére ==> "+this.r[i] + "\n";
		}
		return res;
	}

	public void printToScreen(JTextArea time, JTextArea distance, JTextArea info) {
		
		time.removeAll();
		time.setText(this.printTime());
		time.revalidate();
		time.repaint();
		
		distance.removeAll();
		distance.setText(this.printDistance());
		distance.revalidate();
		distance.repaint();
		
		info.removeAll();
		info.setText(this.printInformations());
		info.revalidate();
		info.repaint();
		
	}
	
	
	public void printInfo() {
		System.out.println("<======== DISTANCE =============>");
		for (int i = 0; i < this.numberOfNode - 1; ++i) {
			for (int j = i + 1; j < this.numberOfNode; ++j) {
				if(this.d[i][j] != 0) System.out.println("Node "+ i +" / "+ j + " ==> "+this.d[i][j]);
			}
		}
		
		System.out.println("<======== TIME =============>");
		for (int i = 0; i < this.numberOfNode - 1; ++i) {
			for (int j = i + 1; j < this.numberOfNode; ++j) {
				if(this.t[i][j] != 0) System.out.println("Node "+ i +" / "+ j + " ==> "+this.t[i][j]);
			}
		}
		
		System.out.println("<======== Informations =============>");
		for (int i = 0; i < this.numberOfNode; ++i) {
			System.out.println("Node "+ i +" generate ==> "+this.r[i]);
		}
	}
	// Le nombre maximal de stations que peut 
	public int M = 2;
	// Capacité maximale de transport d'informations pour un véhicule
	public int R = 30;
	// La durée de la simulation
	public int T = 13;

	/**
	 *  genere le Flux d'informations rentrant dans chaque station
	 * @return
	 */
	private void initFlux() {
		double tmp = 0;
		this.r = new double[this.numberOfNode];
		for (int i = 0; i < this.numberOfNode; ++i) {
			while ((tmp = rand.nextDouble()) == 0);
			this.r[i] = tmp;
		}
	}
	private void initLien() {
		this.lien = new boolean[this.numberOfNode][this.numberOfNode];
		for (int i = 0; i < this.numberOfNode; ++i) {
            for (int j = 0; j < this.numberOfNode; ++j) {
                if (i == j) {
                    this.lien[i][j] = false;
                    this.lien[j][i] = false;
                    // on initialise aleatoirement l'existance de liens de la station base
                } else if (i == 0) {
                    if (nbLien(this.lien[i]) < 3) {
                        this.lien[i][j] = rand.nextDouble() > 0.4;
                        this.lien[j][i] = this.lien[i][j];
                    }
                } else {
                    if (j < i) {
                        this.lien[i][j] = this.lien[j][i];
                    } else {
                        // on initialise aleatoirement l'existance de liens
                        if (nbLien(this.lien[i]) < 2) {
                            this.lien[i][j] = rand.nextDouble() > 0.5;
                            this.lien[j][i] = this.lien[i][j];
                        }
                    }
                }
            }
            if (!this.valide(lien[i])) {
                int tmp = i;
                while (i == tmp) {
                    tmp = rand.nextInt(this.numberOfNode);
                }
                this.lien[i][tmp] = true;
                this.lien[tmp][i] = true;
            }
        }
        this.clean();
        this.path();
    }
	
	private void clean() {
		
		for (int i = 1; i < this.numberOfNode; ++i) {
			if (nbLien(this.lien[i]) > 1) {
				for (int j = 0; j < this.numberOfNode; ++j) {
					if (this.lien[i][j] == true && nbLien(this.lien[j]) > 1) {
						this.lien[i][j] = false;
						this.lien[j][i] = false;
						if (nbLien(this.lien[i]) < 2) break;
					}
				}
			}
		}
	}
	
	private void path() {
		for (int i = 0; i < this.numberOfNode - 1; ++i) {
			this.lien[i][i+1] = true;
			this.lien[i + 1][i] = true;
		}
	}
	private int nbLien(boolean [] mat) {
		int res = 0;
		for (Boolean a:mat) {
			if (a != null && a == true) res++;
		}
		return res;
	}
	/**
	 *  Test de validité des chemin d'une station
	 *  elle doit avoir au moins 2 chemins
	 * @param chemin les chemins de la station
	 * @return si il existe au moins 2 chemins renvoie true, sinon renvoie false 
	 */
	private boolean valide(boolean[] chemin) {
		int i = 0;
		for (boolean val : chemin) {
			if (val) i++;
		}
		return i > 0;
	}
	/**
	 * 
	 * @param low
	 * @param high
	 * @return
	 */
	public static float Rand(int low, int high) {
		return rand.nextInt(high - low) + low;
	}
	/**
	 * 
	 * @param nbrNode
	 */
	private Instance(int nbrNode, int time) {
		this.numberOfNode = nbrNode;
		this.initLien();
		this.initFlux();
		this.initDistance();
		this.initAlpha();
		this.initTime();
		this.T = time;
	}
	public static Instance getInstance() {
		return inst;
	}
	
	public static Instance getNewInstance(int nbrNode, int time) {
		inst = new Instance(nbrNode, time);
		return inst;
	}
}
