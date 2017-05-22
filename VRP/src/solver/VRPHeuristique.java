package solver;
import gurobi.GRB;
import gurobi.GRBException;
import gurobi.GRBLinExpr;

public class VRPHeuristique extends VRP{

	double [][] qprec;
	double [][][] xprec;
	double [][] zprec;
	double [][][] thetaprec;
	double [][][] fprec;
	int limiteprec = 0;
	public VRPHeuristique(int nombreStation, int[][] temps, int tempsfin, int M, int R, double[] r2, int[][] d,
			double[][] alpha,int limiteprec,double qprec[][],double xprec[][][],double  thetaprec[][][], double fprec[][][],double zprec[][]) throws GRBException {
		super(nombreStation, temps, tempsfin, M, R, r2, d, alpha);
		this.qprec = qprec;
		this.limiteprec = limiteprec;
		this.xprec = xprec;
		this.zprec = zprec;
		this.thetaprec = thetaprec;
		this.fprec = fprec;
		// TODO Auto-generated constructor stub
	}
	protected void amountInformationConstraint() throws GRBException
	{
		super.amountInformationConstraint();
		{
			for(int k = 0 ; k < limiteprec ; k++)
				for(int j = 0 ; j < nombreStation ; j++)
				{
					GRBLinExpr lhs1 = new GRBLinExpr();
					lhs1.addTerm(1.0, q[j][k]);
					model.addConstr(lhs1, GRB.EQUAL, qprec[j][k], "amountInformationConstraint3_"+j+"_"+k);
				}
		}
	}
	protected void routingConstraint() throws GRBException
	{
		super.routingConstraint();
		for(int k = 0; k < limiteprec   ; k++)
			for(int j = 0 ; j < nombreStation ; j++)
				for(int i = 0 ; i < nombreStation ; i++)
			{
				GRBLinExpr	lhs = new GRBLinExpr();
				 lhs.addTerm(1.0, x[i][j][k]);
				model.addConstr(lhs, GRB.EQUAL, xprec[i][j][k], "RouteConst444_"+i+"_"+j+"_"+k);		
			}
		
		for(int k = 0; k < limiteprec   ; k++)
				for(int i = 0 ; i < nombreStation ; i++)
			{
				GRBLinExpr	lhs = new GRBLinExpr();
				 lhs.addTerm(1.0, z[i][k]);
				model.addConstr(lhs, GRB.EQUAL, zprec[i][k], "RouteConst445_"+i+"_"+k);		
			}
		
	}
	public void resolve() throws GRBException{
		super.resolve();
	}
	
	protected void transferConstraint() throws GRBException
	{
		super.transferConstraint();
		for(int k = 0 ; k < limiteprec ; k++)
			for(int i = 0 ; i < nombreStation ; i++)
				for(int j = 0 ; j < nombreStation ; j++)
			{
				GRBLinExpr lhs = new GRBLinExpr();
				
				lhs.addTerm(1.0, theta[i][j][k]);
				
				
				
				model.addConstr(lhs, GRB.EQUAL, thetaprec[i][j][k], "transferConstraint441_"+i+"_"+j+"_"+k);
			}
		for(int k = 0 ; k < limiteprec ; k++)
			for(int i = 0 ; i < nombreStation ; i++)
				for(int j = 0 ; j < nombreStation ; j++)
			{
				GRBLinExpr lhs = new GRBLinExpr();
				
				lhs.addTerm(1.0, f[i][j][k]);
				
				
				
				model.addConstr(lhs, GRB.EQUAL, fprec[i][j][k], "transferConstraint442_"+i+"_"+j+"_"+k);
			}
	}
	
}
