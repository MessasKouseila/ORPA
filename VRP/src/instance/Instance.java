package instance;

import java.util.Random;

public class Instance {
	
	public static Random rand = new Random();
	
	// nombre de station dans le graphe
	public int numberOfNode;
	public boolean [][] lien;
	// Flux d'information rentrant dans chaque station
	public double r[];// = {0.4,0.3,0.2,0.7,0.5,0.3};

	// Les temps pour aller d'un sommet à un autre
	public int t[][] ={{0,3,0,0,0,3},
			{3,0,3,0,0,2},
			{0,3,0,2,0,3},
			{0,0,2,0,2,3},
			{0,0,0,2,0,3},
			{3,2,3,3,3,0}

	};

	// La distance entre les stations
	public int d[][] = {{440,1,200,400,400,400},
			{100,0,100,100,100,100},
			{300,1,0,200,300,399},
			{400,1,200,0,200,300},
			{400,1,300,200,0,300},
			{400,1,300,300,300,0}

	};

	// Les contraintes de ralentissement de transfert d'information pour une station
	public double alpha[][] = {{0,1,1,1,1,1},
			{1,0,1,1,1,1},
			{1,1,0,1,1,1},
			{1 ,1,1,0,1,1},
			{1,1,1,1,0,1},
			{1,1,1,1,1,0}

	};
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
	public void initFlux() {
		this.r = new double[this.numberOfNode];
		for (int i = 0; i < this.numberOfNode; ++i) {
			this.r[i] = rand.nextDouble();
		}
	}
	public void initLien() {
		this.lien = new boolean[this.numberOfNode][this.numberOfNode];
		for (int i = 0; i < this.numberOfNode; ++i) {
			for (int j = 0; j < this.numberOfNode; ++j) {

				// une station ne peut pas avoir de chemin vers elle même
				if (i == j) {
					this.lien[i][j] = false;
				} else 
					// on initialise aleatoirement l'existance de liens de la station base 
					if (i == 0) {
						this.lien[i][j] = rand.nextBoolean();					
					} else {
						// si une station i à un chemin vers une station j
						// alors la station j doit avoir un lien avec la station i
						if (j <= i) {
							this.lien[i][j] = this.lien[j][i];
						} else {
							// on initialise aleatoirement l'existance de liens
							this.lien[i][j] = rand.nextBoolean();
						}
					}
			}
			// test chemin, il faut au moins 2 chemins de chaque station
			if (!this.valide(lien[i])) i--;

		}
	}
	/**
	 *  Test de validité des chemin d'une station
	 *  elle doit avoir au moins 2 chemins
	 * @param chemin les chemins de la station
	 * @return si il existe au moins 2 chemins renvoie true, sinon renvoie false 
	 */
	public boolean valide(boolean[] chemin) {
		int i = 0;
		for (boolean val : chemin) {
			if (val) i++;
		}
		return i > 1;
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
	public Instance(int nbrNode) {
		this.numberOfNode = nbrNode;
		this.initLien();
		this.initFlux();
	}
}
