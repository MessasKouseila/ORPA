package solver;

import gurobi.GRBException;

public class Heuristique {

	public VRP  resolve(int nombreStation, int[][] temps, int tempsfin, int M, int R, double[] r2, int[][] d,double[][] alpha) throws GRBException
	{
		VRP vrp;
		if(tempsfin <= 20)
		{
			vrp = new VRP(nombreStation,temps,tempsfin,M,R,r2,d,alpha);
			vrp.resolve();
		}
		else
		{
			int cut = 12;
			for(int i = 8 ; i <= 12; i++)
				if((tempsfin % i ) == 0 )
				{
					cut = i;
					break;
				}
				else if((tempsfin % i ) >= 8)
				{
					cut = i;
				}
			int tempsfixe = cut;
			vrp = new VRP(nombreStation,temps,tempsfixe,M,R,r2,d,alpha);
			vrp.resolve();
			do{
				int tempsprec = tempsfixe;
				tempsfixe = ((tempsfixe+cut) > tempsfin)? tempsfin : tempsfixe+cut; 
				VRPHeuristique vrpexp = new VRPHeuristique(nombreStation,temps,tempsfixe,M,R,r2,d,alpha,tempsprec,vrp.quantiteInformationNoeuds(),vrp.deplacementVehicule(),vrp.EnvoiEntreStation(),vrp.quantiteEnvoiInformation(),vrp.arretVehicule()); 
				vrpexp.resolve();
				vrp = vrpexp;
				
			}while(tempsfixe != tempsfin);
		}
		return vrp;
	}
}
