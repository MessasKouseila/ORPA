package solver;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;

public class Vrp_solver {
	
	
	/////// Environnement GUROBI //////////////////////////
	private GRBEnv env;
	private GRBModel model;
	////////////////////////////////////////////
	
	//// Variable///////////////////////////////////////////////
	private GRBVar x[][][];
	private GRBVar theta[][][];
	private GRBVar f[][][];
	private GRBVar q[][];
	private GRBVar z[][];
	///////////////////////////////////////////////////////////
	
	
	////// Constantes /////////////////////////////////////////
	private int M;
	private int R;
	private double r[];
	private int d[][];
	private double alpha[][];
	private int nombreStation;
	private boolean routeok[][];
	private boolean range[][];
	private int temps[][];
	private int tempsfin;
	///////////////////////////////////////
	
	///// Resolution ///////////////////////
	private double xresolu[][][];
	private double thetaresolu[][][];
	private double fresolu[][][];
	private double qresolu[][];
	private double zresolu[][];
	private double objectif;
	//////////////////////////
	
	/** config env
	 * GUROBI_HOME		/opt/gurobi702/linux64
	 * PATH				/opt/gurobi702/linux64/bin"
	 * LD_LIBRARY_PATH	/opt/gurobi702/linux64/lib"
	 */
	
	
	/**
	 * 
	 * @param nombreStation Le nombre de station
	 * @param temps Les temps pour aller d'un sommet � un autre
	 * @param tempsfin La dur�e de la simulation
	 * @param M
	 * @param R La capacite maximale  d'information que peut recueillir le vehicule
	 * @param r Le flux d'information entrant pour une station donn�
	 * @param d La distance entre deux noeuds
	 * @param alpha Les contraintes de ralentissement de transfert d'information pour une station
	 * @throws GRBException
	 */
	public Vrp_solver( int nombreStation, int temps[][] , int tempsfin,int M,int R ,double r[],int d[][],double alpha[][]) throws GRBException
	{
		env = new GRBEnv("grb.log");
		model = new GRBModel(env);
		this.nombreStation = nombreStation;
		this.M = M;
		this.R = R;
		this.r = r;
		this.d = d;
		this.alpha = alpha;
		this.temps = temps;
		this.tempsfin = tempsfin;
		routeok = new boolean[nombreStation][nombreStation];
		range = new boolean[nombreStation][nombreStation];
		
		x = new GRBVar[nombreStation][nombreStation][tempsfin];
		theta= new GRBVar[nombreStation][nombreStation][tempsfin];
		f  = new GRBVar[nombreStation][nombreStation][tempsfin];
		q = new GRBVar[nombreStation][tempsfin];
		z= new GRBVar[nombreStation][tempsfin];
		
		
		xresolu = new double[nombreStation][nombreStation][tempsfin];
		thetaresolu= new double[nombreStation][nombreStation][tempsfin];
		fresolu  = new double[nombreStation][nombreStation][tempsfin];
		qresolu = new double[nombreStation][tempsfin];
		zresolu = new double[nombreStation][tempsfin];
		for(int i = 0 ; i < nombreStation ; i++)
			for(int j = 0 ; j < nombreStation ; j++)
			{
				routeok[i][j] = (temps[i][j] > 0);
				
				range[i][j] = (d[i][j] > 0);
				
			}
	
	}
	public void resolve() throws GRBException
	{
		this.createVariable();
		
		this.objectiveFunction();
		
		this.routingConstraint();
		this.transferConstraint();
		this.amountInformationConstraint();
		this.validInequalityConstrain();
		//model.feasRelax(GRB.FEASRELAX_LINEAR, true, false, true);
		model.optimize();
		//model.computeIIS();
		 //model.write("model.ilp");
		for(int i = 0 ; i < nombreStation ; i++)
			for(int j = 0 ; j < nombreStation ; j++)
				for(int g = 0 ; g < tempsfin ; g++ )
					this.xresolu[i][j][g] = x[i][j][g].get(GRB.DoubleAttr.X) ;
		for(int i = 0 ; i <nombreStation ; i++)
			for(int g = 0 ; g < tempsfin ; g++ )
				this.zresolu[i][g] =z[i][g].get(GRB.DoubleAttr.X); 
		
		for(int i = 0 ; i < nombreStation ; i++)
			for(int g = 0 ; g < tempsfin ; g++ )
				this.qresolu[i][g] = q[i][g].get(GRB.DoubleAttr.X);
		
		for(int i = 0 ; i < nombreStation ; i++)
			for(int j = 0 ; j < nombreStation ; j++)
				for(int g = 0 ; g < tempsfin ; g++ )
					this.fresolu[i][j][g] = f[i][j][g].get(GRB.DoubleAttr.X);
		
		for(int i = 0 ; i < nombreStation ; i++)
			for(int j = 0 ; j < nombreStation ; j++)
				for(int g = 0 ; g < tempsfin ; g++ )
					this.thetaresolu[i][j][g] = theta[i][j][g].get(GRB.DoubleAttr.X);
		objectif =  model.get(GRB.DoubleAttr.ObjVal);
		
		model.dispose();
	    env.dispose();
	}
	private void createVariable() throws GRBException
	{
		
		// Variable x_i_j_k tel que i et j sont des noeuds et k un temps
		for(int i = 0 ; i < nombreStation ; i++)
			for(int j = 0 ; j < nombreStation ; j++)
				for(int g = 0 ; g < tempsfin ; g++ )
				x[i][j][g] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "x_"+i+"_"+j+"_"+g);
	
		//Variable 
			for(int i = 0 ; i <nombreStation ; i++)
				for(int g = 0 ; g < tempsfin ; g++ )
					z[i][g] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "z_"+i+"_"+g);
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
							theta[i][j][g] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "theta_"+i+"_"+j+"_"+g);
	}
	
	private void routingConstraint() throws GRBException
	{
		
		{
			GRBLinExpr lhs = new GRBLinExpr();
			
			for(int i = 0 ; i < nombreStation ; i++)
				if(routeok[0][i])
					lhs.addTerm(1.0, x[0][i][temps[0][i]]);
			model.addConstr(lhs, GRB.EQUAL, 1, "RouteConst1");
		
		}
		
		{
			GRBLinExpr	lhs = new GRBLinExpr();
			
			for(int i = 0 ; i < nombreStation ; i++)
				if(routeok[i][0])
					lhs.addTerm(1.0, x[i][0][ tempsfin - 1 ]);
			model.addConstr(lhs, GRB.EQUAL, 1, "RouteConst2");		
		}
		{
			
			for(int k = 0; k < tempsfin   ; k++)
				for(int j = 0 ; j < nombreStation ; j++)
				{
					GRBLinExpr	lhs = new GRBLinExpr();
					GRBLinExpr	rhs = new GRBLinExpr();
					 lhs.addTerm(1.0, z[j][k]);
					
					for(int p = 0 ; p < nombreStation ; p++)
						if(routeok[j][p] && ( (k + temps[j][p]) < (tempsfin) ) )
							{
								rhs.addTerm(1.0, x[j][p][k + temps[j][p]]);
								
							}
					
							
					model.addConstr(lhs, GRB.LESS_EQUAL, rhs, "RouteConst43_"+j+"_"+k);		
				}
		
		}
		/*{
			
			for(int k = 0; k < tempsfin   ; k++)
				for(int j = 0 ; j < nombreStation ; j++)
				{
					GRBLinExpr	lhs = new GRBLinExpr();
					GRBLinExpr	rhs = new GRBLinExpr();
					 lhs.addTerm(1.0, z[j][k]);
					for(int i = 0 ; i < nombreStation ; i++)
						if(routeok[i][j])
							rhs.addTerm(1.0, x[i][j][k]);
					
					
							
					model.addConstr(lhs, GRB.EQUAL, rhs, "RouteConst3_"+j+"_"+k);		
				}
		
		}*/
		
		{
			for(int k = 0 ; k <  (tempsfin - 1)    ; k++)
				for(int i = 0 ; i < nombreStation ; i++)
				for(int j = 0 ; j < nombreStation ; j++)
				{
					GRBLinExpr lhs = new GRBLinExpr();
					GRBLinExpr rhs = new GRBLinExpr();
					
					lhs.addTerm(1.0, x[i][j][k]);
			
					
			/*	for(int i = 0 ; i < nombreStation ; i++)
						if(routeok[j][i])
							lhs1.addTerm(1.0, x[i][j][k]);
				*/
					for(int p = 0 ; p < nombreStation ; p++)
						if(routeok[j][p] && ( (k + temps[j][p]) < (tempsfin) ) )
							{
								rhs.addTerm(1.0, x[j][p][k + temps[j][p]]);
								
							}
				
					rhs.addTerm(1.0, z[j][k+1]);
			
						model.addConstr(lhs, GRB.LESS_EQUAL, rhs, "RouteConst33_"+i+"_"+j+"_"+k);
				}
		}
		
		/*	{
			for(int k = 0 ; k <  (tempsfin - 1)    ; k++)
				for(int j = 0 ; j < nombreStation ; j++)
				{
					GRBLinExpr lhs1 = new GRBLinExpr();
					GRBLinExpr lhs2 = new GRBLinExpr();
					
					lhs1.addTerm(1.0, z[j][k]);
			
					
				for(int i = 0 ; i < nombreStation ; i++)
						if(routeok[j][i])
							lhs1.addTerm(1.0, x[i][j][k]);
				
					for(int p = 0 ; p < nombreStation ; p++)
						if(routeok[j][p] && ( (k + temps[j][p]) < (tempsfin) ) )
							{
								lhs2.addTerm(1.0, x[j][p][k + temps[j][p]]);
								
							}
				
					
			
						model.addConstr(lhs1, GRB.GREATER_EQUAL, lhs2, "RouteConst4_"+j+"_"+k);
				}
		}	*/
{
			
			for(int k = 1 ; k < tempsfin   ; k++)
				for(int j = 0 ; j < nombreStation ; j++)
					for(int i = 1 ; i < nombreStation ; i++)
					{	if(routeok[i][j]  && (k - temps[i][j]) < 0)
						{
						
							GRBLinExpr	rhs = new GRBLinExpr();
							rhs.addTerm(1, x[i][j][k]);
							
							model.addConstr(rhs, GRB.EQUAL, 0, "RouteConst100_"+i+"_"+j+"_"+k);
						
						}
					}
		}
	/*
		{
			GRBLinExpr	lhs = new GRBLinExpr();
			GRBLinExpr	rhs;
			for(int k = 0 ; k < tempsfin   ; k++)
				for(int j = 0 ; j < nombreStation ; j++)
				{
					 lhs = new GRBLinExpr();
					 rhs = new GRBLinExpr();
					 lhs.addTerm(1.0, z[j][k]);
					for(int i = 0 ; i < nombreStation ; i++)
						if(routeok[i][j])
							rhs.addTerm(1.0, x[i][j][k]);
					model.addConstr(rhs, GRB.LESS_EQUAL, lhs, "RouteConst8_"+j+"_"+k);		
				}
		
		}*/
		
			
		
{
			
			for(int k = 0; k < tempsfin; k++)
			for(int j = 0; j< nombreStation;j++)
			for(int i = 0 ; i < nombreStation ; i++)
			{	GRBLinExpr	lhs = new GRBLinExpr();
				if(!routeok[i][j]){
					lhs.addTerm(1.0, x[i][j][ k ]);
					
					model.addConstr(lhs, GRB.EQUAL, 0, "RouteConst5_"+i+"_"+j+"_"+k);
			
			}
			}
		}
		
		/*{
			GRBLinExpr	lhs = new GRBLinExpr();
			GRBLinExpr	rhs = new GRBLinExpr();
			for(int k = 1 ; k < tempsfin - 1  ; k++)
			{	
				
				for(int j = 0 ; j < nombreStation ; j++)
				{
					
					lhs = new GRBLinExpr();
					rhs = new GRBLinExpr();
					lhs.addTerm(1.0, z[j][k]);
					for(int p = 0 ; p < nombreStation ; p++)
						if(routeok[j][p] && ( (k + temps[j][p]) < (tempsfin) ) )
							{
								lhs.addTerm(1.0, x[j][p][k + temps[j][p]]);
								
							}
					
					for(int i = 0 ;i < nombreStation; i++)
						if(routeok[i][j])
							rhs.addTerm(1.0,x[i][j][k]);
					rhs.addTerm(1.0,z[j][k-1]);
					model.addConstr(lhs, GRB.EQUAL, rhs, "RouteConst7_"+j+"_"+k);	
				}
			}
			
		
		}*/
		 {
		 
			GRBLinExpr	lhs = new GRBLinExpr();
			for(int k = 0; k < tempsfin   ; k++)
			{	
				lhs = new GRBLinExpr();
				for(int j = 0 ; j < nombreStation ; j++)
				{			 
					 lhs.addTerm(1.0, z[j][k]);
						
				}
				for(int j = 0 ; j < nombreStation ; j++)
				{for(int i = 0 ; i < nombreStation ; i++)
					if(routeok[i][j])
					 lhs.addTerm(1.0, x[i][j][k]);
						
				}
				model.addConstr(lhs, GRB.LESS_EQUAL, 1, "RouteConst6_"+k);	
			}
		
		}
		/*
		{
			GRBLinExpr	lhs = new GRBLinExpr();
			for(int k = 0 ; k < tempsfin   ; k++)
			{	
				
				model.addConstr(z[0][0], GRB.EQUAL, 1, "RouteConst11");	
			}
		}*/
	}
	
	private void transferConstraint() throws GRBException
	{
		{
			for(int k = 0 ; k < tempsfin ; k++)
				for(int i = 0 ; i < nombreStation ; i++)
				{
					GRBLinExpr lhs1 = new GRBLinExpr();
					GRBLinExpr lhs2 = new GRBLinExpr();
					
					
					for(int j = 0 ; j < nombreStation ; j++)
						if(range[j][i])
							lhs1.addTerm(1.0, theta[j][i][k]);
					
					lhs2.addTerm(M,z[i][k]);
					
					model.addConstr(lhs1, GRB.LESS_EQUAL, lhs2, "transferConstraint1_"+i+"_"+k);
				}
		}
		
		
		{
			for(int k = 0 ; k < tempsfin ; k++)
					for(int i = 0 ; i < nombreStation ; i++)
						for(int j = 0 ; j < nombreStation ; j++)
					{
						
						if(range[j][i])
						{
							GRBLinExpr lhs1 = new GRBLinExpr();
							GRBLinExpr lhs2 = new GRBLinExpr();
							lhs1.addTerm(1.0,f[j][i][k] );
							lhs2.addTerm( alpha[j][i]*( 1/( 1 + Math.pow( d[j][i] , 2 ) ) )*r[j],theta[j][i][k]);
						 	model.addConstr(lhs1, GRB.LESS_EQUAL, lhs2, "transferConstraint2_"+j+"_"+i+"_"+k);
						}
					}
		}
		
		{
			for(int k = 0 ; k < tempsfin ; k++)
				for(int i = 0 ; i < nombreStation ; i++)
				{
					GRBLinExpr lhs1 = new GRBLinExpr();
					for(int j = 0 ; j < nombreStation ; j++)
						if(range[j][i])
							lhs1.addTerm(1, f[j][i][k]);
					model.addConstr(lhs1, GRB.LESS_EQUAL, R, "transferConstraint3_"+i+"_"+k);
				}
						
		}
	}
	private void amountInformationConstraint() throws GRBException
	{
		{
			for(int k = 0 ; k < tempsfin - 1; k++)
				for(int j = 0 ; j < nombreStation ; j++)
					{
					
							GRBLinExpr lhs1 = new GRBLinExpr();
							GRBLinExpr lhs2 = new GRBLinExpr();
							
							lhs1.addTerm(1.0, q[j][k+1]);
							
							lhs2.addTerm(1.0, q[j][k]);
							lhs2.addConstant(r[j]);
							for(int i = 0 ; i < nombreStation ; i++)
								if(range[j][i])
								{
									lhs2.addTerm(-1.0, f[j][i][k]);
								}
								
							model.addConstr(lhs1, GRB.EQUAL, lhs2, "amountInformationConstraint1_"+j+"_"+k);
					}
		}
		
		
		{
			for(int k = 0 ; k < tempsfin ; k++)
				for(int j = 0 ; j < nombreStation ; j++)
				{
					GRBLinExpr lhs1 = new GRBLinExpr();
					lhs1.addTerm(1.0, q[j][k]);
					model.addConstr(lhs1, GRB.GREATER_EQUAL, r[j], "amountInformationConstraint2_"+j+"_"+k);
				}
		}
	}
	
	private void validInequalityConstrain() throws GRBException
	{
		
		
		{
			for(int k = 0 ; k < tempsfin ; k++)
				for(int i = 0 ; i < nombreStation ; i++)
				for(int j = 0 ; j < nombreStation ; j++)
				{
					GRBLinExpr lhs1 = new GRBLinExpr();
					GRBLinExpr lhs2 = new GRBLinExpr();
					lhs1.addTerm(1.0, theta[j][i][k]);
					lhs2.addTerm(1.0, z[i][k]);
					model.addConstr(lhs1, GRB.LESS_EQUAL, lhs2, "validInequalityConstrain1_"+i+"_"+"_"+k);
				}
		}
		
		
		
		{
			for(int k = 0 ; k < tempsfin ; k++)
				for(int i = 0 ; i < nombreStation ; i++)
				{
					GRBLinExpr lhs1 = new GRBLinExpr();
					GRBLinExpr lhs2 = new GRBLinExpr();
					for(int j = 0 ; j < nombreStation ; j++)
						if(range[j][i])
							lhs1.addTerm(1.0,f[j][i][k]);
					lhs2.addTerm(R, z[i][k]);
				
					model.addConstr(lhs1, GRB.LESS_EQUAL, lhs2, "validInequalityConstrain2_"+i+"_"+k);
				}
		}
		
	}
	
	private void objectiveFunction() throws GRBException
	{
		 GRBLinExpr expr = new GRBLinExpr();
	     for(int j = 0 ;j < nombreStation ; j++) 
	    	 expr.addTerm(1.0, q[j][tempsfin-1]); 
	    model.setObjective(expr, GRB.MINIMIZE);
	}
	public double[][][] deplacementVehicule() {
		return xresolu;
	}
	public double[][][] EnvoiEntreStation() {
		return thetaresolu;
	}
	public double[][][] quantiteEnvoiInformation() {
		return fresolu;
	}
	public double[][] quantiteInformationNoeuds() {
		return qresolu;
	}
	public double[][] arretVehicule() {
		return zresolu;
	}
	public double getObjectif() {
		return objectif;
	}
	private void setObjectif(double objectif) {
		this.objectif = objectif;
	}
	public void prindTrajet()
	{
		for(int k = 0 ; k < tempsfin ; k++)
			for(int i = 0 ; i < nombreStation; i++ )
			{
				if(zresolu[i][k] > 0)
					System.out.println("Au temps "+k+" On est au noeud "+i+" "+zresolu[i][k]);
			}
		for(int k = 0 ; k < tempsfin ; k++)
			for(int i = 0 ; i < nombreStation; i++ )
				for(int j = 0 ; j < nombreStation; j++ )
				{
						if(routeok[i][j] && xresolu[i][j][k] == 1)
						System.out.println("x["+i+"]["+j+"]["+k+"]="+xresolu[i][j][k]);
				}
	}
	
	public void printInformation()
	{
		
		for(int k = 0 ; k < tempsfin ; k++)
		{	for(int i = 0 ; i < nombreStation; i++ )
						System.out.print("z["+i+"]["+k+"]="+zresolu[i][k]);
			System.out.println("");
		}		
	}
}
