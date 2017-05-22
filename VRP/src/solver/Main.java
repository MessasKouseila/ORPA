package solver;
import gurobi.GRBException;

public class Main {
	public static void main(String[] args) {
		
		double r[] = {0.4,0.3,0.2,0.7,0.5,0.3};
		
		int t[][] ={{0,3,0,0,0,3},
				{3,0,3,0,0,2},
				{0,3,0,2,0,3},
				{0,0,2,0,2,3},
				{0,0,0,2,0,3},
				{3,2,3,3,3,0}
				
		};
		
		int d[][] = {{4,1,2,4,4,4},
					 {1,0,1,1,1,1},
					 {3,1,0,2,3,3},
					 {4,1,2,0,2,3},
					 {4,1,3,2,0,3},
					 {4,1,3,3,3,0}
				
		};
		
		double alpha[][] = {{0,0.1,0.1,0.1,0.1,0.1},
				 			{0.1,0,0.1,0.1,0.1,0.1},
				 			{0.1,0.1,0,0.1,0.1,0.1},
				 			{0.1 ,0.1,0.1,0,0.1,0.1},
				 			{0.1,0.1,0.1,0.1,0,0.1},
				 			{0.1,0.1,0.1,0.1,0.1,0}
			
	};
		int M = 200;
		int R = 300;
		int T = 80;
		try {
			VRP v = new VRP(6,t,T,M,R,r,d,alpha);
			v.resolve();
			v.prindTrajet();
			v.printInformation();
		} catch (GRBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
