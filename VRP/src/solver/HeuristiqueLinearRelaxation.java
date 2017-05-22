package solver;

import gurobi.GRBException;

public class HeuristiqueLinearRelaxation {

	int nombreStation;
	int [][] temps;
	int tempsfin;
	int M;
	int R;
	double [] r2;
	int [][] d;
	double [][] alpha;
	public HeuristiqueLinearRelaxation(int nombreStation, int[][] temps, int tempsfin, int M, int R, double[] r2, int[][] d,	double[][] alpha)
	{
		this.nombreStation = nombreStation;
		this.temps = temps;
		this.tempsfin = tempsfin;
		this.M = M;
		this.R = R;
		this.r2 = r2;
		this.d = d;
		this.alpha = alpha;
		
		
	}
	
	
	public VRPLinearRelaxation  resolve() throws GRBException
	{
		
		int time = 0;
		int pos = 0;
		
		//Initialisation  des variables
		Integer[][][] decisionX = new Integer[this.temps.length][this.temps.length][1];
		Integer[][] decisionZ = new Integer[this.temps.length][1];
		Integer[][][] decisionTheta = new Integer[this.temps.length][this.temps.length][1];
		//Declaration Vrp
		VRPLinearRelaxation v = new VRPLinearRelaxation(this.nombreStation,this.temps,this.tempsfin,this.M,this.R,this.r2,this.d,this.alpha);
		
		for(int i = 0; i< this.d.length ; i++)
		{
			int decision = (i == pos ) ? 1 : 0;
			v.fixPositionVehicule(i, time, 0);
			decisionZ[i][time] = 0;
		}
		for(int j = 0 ; j < this.d.length; j++)
			for(int i = 0 ; i < this.d.length ; i++)
			{
				v.fixTrajetVehicule(i,j , time, 0);
				
				v.fixSendMessage(i, j, time, 0);
				decisionX[i][j][time] = 0;
				decisionTheta[i][j][time] = 0;
			}
		// Resolution VRP
		v.resolve();
		/**
		 * On fixe puis on relaxe les valeurss
		 */
		do
		{
			//Recuperation du deplacement du vehicule
			double x[][][] = v.deplacementVehicule();
			//recuperation de la position
			double z[][] = v.arretVehicule();
			//double theta[][][] = v.quantiteEnvoiInformation();
			v = new VRPLinearRelaxation(this.nombreStation,this.temps,this.tempsfin,this.M,this.R,this.r2,this.d,this.alpha);
			// Prendre le meilleur chemin
			int posdec = bestChemin(x,pos,time);
			System.out.println("pos "+posdec);
			//Si le vehicule ne bouge pas
			if(posdec == -1)
			{
				System.out.println("Erreur");
				break;
			}
			//Si la position du vehicule est plus importante que le changement de position
			/**else if((z[pos][time + 1]) > (x[pos][posdec][(int)(time + this.temps[pos][posdec])]) )
			{
				time = time + 1;
				Integer[][][] decisionXp = new Integer[this.temps.length][this.temps.length][time+1];
				Integer[][] decisionZp = new Integer[this.temps.length][time+1];
				Integer[][][] decisionThetap = new Integer[this.alpha.length][this.alpha.length][time+1];
				this.fixeX(decisionX, decisionXp, v);
				this.fixeZ(decisionZ, decisionZp, v);
				this.fixeTheta(decisionTheta, decisionThetap, v);
				
				for(int i = 0; i< this.d.length ; i++)
				{
					int decision = (i == pos ) ? 1 : 0;
					v.fixPositionVehicule(i, time, decision);
					decisionZp[i][time] = decision;
				}
				for(int j = 0 ; j < x[0].length; j++)
					for(int i = 0 ; i < x.length ; i++)
					{
						v.fixTrajetVehicule(i,j , time, 0);
						
						decisionXp[i][j][time] = 0;
					}
			
				for(int j = 0 ; j < this.d.length; j++)
				for(int i = 0 ; i < this.d.length ; i++)
				{
		
					int dec = (j != pos && i == pos)? 1 : 0;
						v.fixSendMessage(j, i, time, dec);
						decisionThetap[j][i][time] = dec;
					
				}
				
				decisionX = decisionXp;
				decisionZ = decisionZp;
				decisionTheta = decisionThetap;
			}**/
			// Si le changement de vehicule est superieur  � la postion du vehicule 
			else
			{
				System.out.println("time "+time);
				int timedec = time + this.temps[pos][posdec];
				Integer[][][] decisionXp = new Integer[this.temps.length][this.temps.length][timedec+1];
				Integer[][] decisionZp = new Integer[this.temps.length][timedec+1];
				Integer[][][] decisionThetap = new Integer[this.alpha.length][this.alpha.length][timedec+1];
				this.fixeX(decisionX, decisionXp, v);
				this.fixeZ(decisionZ, decisionZp, v);
				this.fixeTheta(decisionTheta, decisionThetap, v);
				this.fixeXWithTime( decisionXp, time + 1, timedec,this.d.length, pos, posdec, v);
				this.fixeThetaWithTime( decisionThetap, time + 1, timedec, this.d.length, pos, posdec, v);
				this.fixeZWithTime(decisionZp, time + 1, timedec, this.d.length, pos, posdec, 0, v);
				pos = posdec;
				time = timedec;
				decisionX = decisionXp;
				decisionZ = decisionZp;
				decisionTheta = decisionThetap;
			}
		
			v.resolve();
			v.prindTrajet();
			v.printInformation();
			v.printSendMessage();
			
		}while(time <= this.tempsfin && ! this.isValid(v.deplacementVehicule()));		
		return v;
	}
	/**
	 * Retour la meilleur direction que la voiture doit prendre
	 * @param x 
	 * @param pos
	 * @param k
	 * @return
	 */
	public int bestChemin(double x[][][],int pos,int k)
	{
		int best = pos;
		for(int i = 0 ;  i < x.length ; i++)
		{
			if(this.temps[pos][i] > 0)
			{
				
				int timebest = (int) (this.temps[pos][best] +k);
				int timever = (int) (this.temps[pos][i] +k);
				boolean canReturn = canReturn(timever,i);
				//pour le commencement
				if(best == pos && canReturn)
				{
					best = i;
				}
				if(timebest < this.tempsfin && timever < this.tempsfin && x[pos][best][timebest] < x[pos][i][timever] && canReturn )
				{
					best = i;
				}
			}
		}
		return (best == pos) ? -1 : best;
	}
	/**
	 * Verifie est qu'on aura le temps de retourner ver la base
	 * @param curTime
	 * @param pos
	 * @return
	 */
	public boolean canReturn(int curTime, int pos)
	{
		if(pos == 0 || this.temps[pos][0] > 0 && (this.temps[pos][0]+curTime) < this.tempsfin)
		{
			return true;
		}
		for(int i = 1 ; i < this.temps.length ; i++)
		{
			if(this.temps[pos][i] > 0)
			{
				 int time = (int)(this.temps[pos][i] + curTime);
				 if(time < this.tempsfin && canReturn(time,i))
					 return true;
			}
		}
		return false;
	}
	
	private void fixeX(Integer [][][] decisionX, Integer [][][] decisionXp,VRPLinearRelaxation v) throws GRBException
	{
		for(int k = 0 ; k < decisionX[0][0].length ; k++)
			for(int j = 0 ; j < decisionX[0].length; j++)
				for(int i = 0 ; i < decisionX.length ; i++)
				{
					v.fixTrajetVehicule(i, j, k, decisionX[i][j][k]);
					decisionXp[i][j][k] = decisionX[i][j][k];
				}
	
	}
	private void fixeTheta(Integer [][][] decisionTheta, Integer [][][] decisionThetap,VRPLinearRelaxation v) throws GRBException
	{
		for(int k = 0 ; k < decisionTheta[0][0].length ; k++)
			for(int j = 0 ; j < decisionTheta[0].length; j++)
				for(int i = 0 ; i < decisionTheta.length ; i++)
				{
					v.fixSendMessage(i, j, k, decisionTheta[i][j][k]);
					decisionThetap[i][j][k] = decisionTheta[i][j][k];
				}
	
	}
	private void fixeZ(Integer [][] decisionZ, Integer [][] decisionZp,VRPLinearRelaxation v) throws GRBException
	{
		for(int k = 0 ; k < decisionZ[0].length; k++)
			for(int i = 0 ; i < decisionZ.length ; i++)
			{
				v.fixPositionVehicule(i, k, decisionZ[i][k]);
				decisionZp[i][k] = decisionZ[i][k];
				if(decisionZp[i][k] == 1)
				System.out.println("Je m arrete � la position" +i + " au temps "+k);
			}
	
	}
	
	private void fixeXWithTime(Integer [][][] decisionXp,int debut,int fin, int taille,int pos, int posdec,VRPLinearRelaxation v) throws GRBException
	{
		
		for(int j = 0 ; j < taille; j++)
			for(int k = debut; k <= fin; k++)
			{
			for(int i = 0 ; i < taille ; i++)
			{
				int decx = (k == fin && i == pos && j == posdec)?1 : 0;
				//int dect = (k == fin && j == pos && this.alpha[j][i] > 0 )?1 : 0;
				v.fixTrajetVehicule(i,j , k, decx);
				decisionXp[i][j][k] = decx;
				if(decx == 1)
				System.out.println(i+" "+j+" "+k+" "+decx);
			}
			}
	}
	private void fixeThetaWithTime( Integer [][][] decisionThetap,int debut,int fin, int taille,int pos, int posdec,VRPLinearRelaxation v) throws GRBException
	{
		
		for(int j = 0 ; j < taille; j++)
			for(int k = debut; k <= fin; k++)
			{
			for(int i = 0 ; i < taille ; i++)
			{
				
				
				
				v.fixSendMessage(i, j, k, 0);
				decisionThetap[i][j][k] = 0;
			
			}
			}
	}
	
	private void fixeZWithTime( Integer [][] decisionZp,int debut,int fin, int taille,int pos, int posdec,int value,VRPLinearRelaxation v) throws GRBException
	{
		for(int i = 0; i< taille ; i++)
			for(int k = debut; k <= fin; k++)
			{
				
				v.fixPositionVehicule(i, k, 0);
				decisionZp[i][k] = value;
			}

	}
	
	private boolean isValid( double x[][][])
	{
		for(int k = 0 ; k <x[0][0].length ; k++)
			for(int j = 0; j < x[0].length; j++)
				for(int i = 0; i < x.length; i++ )
					if(x[i][j][k] > 0 && x[i][j][k] < 1)
						return false;
		return true;
	}
}
      