package solver;
import gurobi.GRBException;

public class MainHeuristique {
	public static void main(String[] args) {
		
		double r[] = {0.4,0.3,0.2,0.7,0.5,0.3};
		
		int t[][] ={{0,3,0,0,0,3},
				{3,0,3,0,0,2},
				{0,3,0,2,0,3},
				{0,0,2,0,2,3},
				{0,0,0,2,0,3},
				{3,2,3,3,3,0}
				
		};
		
		int d[][] = {{440,1,200,400,400,400},
					 {100,0,100,100,100,100},
					 {300,1,0,200,300,399},
					 {400,1,200,0,200,300},
					 {400,1,300,200,0,300},
					 {400,1,300,300,300,0}
				
		};
		
		double alpha[][] = {{0,1,1,1,1,1},
				 			{1,0,1,1,1,1},
				 			{1,1,0,1,1,1},
				 			{1 ,1,1,0,1,1},
				 			{1,1,1,1,0,1},
				 			{1,1,1,1,1,0}
			
	};
		int M = 2;
		int R = 30;
		int T = 80;
		try {
			Heuristique h = new Heuristique();
			VRP v = h.resolve(6,t,T,M,R,r,d,alpha);
			
			v.prindTrajet();
			v.printInformation();
		} catch (GRBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
