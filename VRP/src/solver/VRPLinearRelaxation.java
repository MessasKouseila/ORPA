package solver;
import gurobi.GRB;
import gurobi.GRBException;
import gurobi.GRBLinExpr;

public class VRPLinearRelaxation  extends VRP{

	int indexX = 0;
	int indexZ = 0;
	int indexTheta = 0;
	public VRPLinearRelaxation(int nombreStation, int[][] temps, int tempsfin, int M, int R, double[] r2, int[][] d,
			double[][] alpha) throws GRBException {
		super(nombreStation, temps, tempsfin, M, R, r2, d, alpha);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void createVariable() throws GRBException
	{
		
		// Variable x_i_j_k tel que i et j sont des noeuds et k un temps
		for(int i = 0 ; i < nombreStation ; i++)
			for(int j = 0 ; j < nombreStation ; j++)
				for(int g = 0 ; g < tempsfin ; g++ )
				x[i][j][g] = model.addVar(0.0, 1.0, 0.0, GRB.CONTINUOUS, "x_"+i+"_"+j+"_"+g);
	
		//Variable 
			for(int i = 0 ; i <nombreStation ; i++)
				for(int g = 0 ; g < tempsfin ; g++ )
					z[i][g] = model.addVar(0.0, 1.0, 0.0, GRB.CONTINUOUS, "z_"+i+"_"+g);
		//Variable q_j_k
		for(int i = 0 ; i < nombreStation ; i++)
			for(int g = 0 ; g < tempsfin ; g++ )
				q[i][g] = model.addVar(0.0,GRB.INFINITY, 0.0, GRB.CONTINUOUS, "q_"+i+"_"+g);
		
		// Variable f_i_j_k tel que i et j sont des noeuds et k un temps
				for(int i = 0 ; i < nombreStation ; i++)
					for(int j = 0 ; j < nombreStation ; j++)
						for(int g = 0 ; g < tempsfin ; g++ )
							f[i][j][g] = model.addVar(0.0,GRB.INFINITY, 0.0,GRB.CONTINUOUS, "f_"+i+"_"+j+"_"+g);		
				
				// Variable theta_i_j_k tel que i et j sont des noeuds et k un temps
				for(int i = 0 ; i < nombreStation ; i++)
					for(int j = 0 ; j < nombreStation ; j++)
						for(int g = 0 ; g < tempsfin ; g++ )
							theta[i][j][g] = model.addVar(0.0, 1.0, 0.0, GRB.CONTINUOUS, "theta_"+i+"_"+j+"_"+g);
	}
	

	public void fixTrajetVehicule(int i , int j , int k, int fix) throws GRBException
	{
		GRBLinExpr lhs = new GRBLinExpr();
		lhs.addTerm(1.0, x[i][j][k]);
		model.addConstr(lhs, GRB.EQUAL, fix, "FixX"+indexX++);
	}
	
	public void fixPositionVehicule(int i , int j , int fix) throws GRBException
	{
		GRBLinExpr lhs = new GRBLinExpr();
		lhs.addTerm(1.0, z[i][j]);
		model.addConstr(lhs, GRB.EQUAL, fix, "FixZ"+indexZ++);
	}
	public void fixSendMessage(int i , int j , int k , int fix) throws GRBException
	{
		GRBLinExpr lhs = new GRBLinExpr();
		lhs.addTerm(1.0, theta[i][j][k]);
		model.addConstr(lhs, GRB.EQUAL, fix, "FixTheta"+indexTheta++);
	}
}
