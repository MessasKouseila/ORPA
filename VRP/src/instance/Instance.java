package instance;

import java.util.Random;

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
					this.d[i][j] = (int)Rand(101, 801);
					this.d[j][i] = (int)Rand(101, 801);
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
		this.r = new double[this.numberOfNode];
		for (int i = 0; i < this.numberOfNode; ++i) {
			this.r[i] = rand.nextDouble();
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
                    if (nbLien(this.lien[i]) < 2) {
                        this.lien[i][j] = rand.nextDouble() > 0.5;
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
		
		for (int i = 0; i < this.numberOfNode; ++i) {
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
	private Instance(int nbrNode) {
		this.numberOfNode = nbrNode;
		this.initLien();
		this.initFlux();
		this.initDistance();
		this.initAlpha();
		this.initTime();
	}
	public static Instance getInstance() {
		return inst;
	}
	
	public static Instance getNewInstance(int nbrNode) {
		inst = new Instance(nbrNode);
		return inst;
	}
}
