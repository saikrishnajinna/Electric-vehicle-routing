/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


/**
 *
 * @author Sai Krishna Reddy
 */
public class Evrp {

    /**
     * @param args the command line arguments
     */
   public static int v;
   public static int cp;
   public static int ecp;
   public static float ecm;
   public static int dm;
   public static int s;
   public static String st2=" ";

  public static float[][] dist= new float[dm+s][dm+s];
  public static float[][] save= new float[dm+s][dm+s];
  public static float[][] consume= new float[dm+s][dm+s];
  public static int[] capacity= new int[dm];
  public static float[][] saving = new float [0][0];
  public static int [] Xcoord = new int [dm+s];
  public static int [] Ycoord = new int [dm+s];
  
  public static void main(String[] args)throws Exception 
  { 
   
  File file = new File("E-n22-k4.evrp"); 
 
  BufferedReader br = new BufferedReader(new FileReader(file)); 
  
  String st; 

  int i=0,i1=0,i2=0,i3=0,i4,i5,i6;
  

 while ((st = br.readLine()) != null){
    st2 += st;
  
 } 
   
 //System.out.println(st2); 
   i=st2.indexOf("VEHICLE");
   //number_of_vehicles =Integer.parseInt(st2.substring(i+10));
   i1 = st2.indexOf("DIMENSION: ");
   i2 = st2.indexOf("CAPACITY: ");
  
   i3 = st2.indexOf("ENERGY_CAPACITY: ");
   i4 = st2.indexOf("ENERGY_CONSUMPTION: ");
  i5 = st2.indexOf("EDGE_WEIGHT_FORMAT: ");
  i6 = st2.indexOf("STATIONS:");

       v =Integer.parseInt(st2.substring(i+10,i1-1));
       cp = Integer.parseInt(st2.substring(i2+10,i3-1));
       ecp = (int)Math.round(Float.parseFloat( st2.substring(i3+16,i4-1)));
       ecm = Float.parseFloat( st2.substring(i4+20,i5-1));
       dm =  Integer.parseInt(st2.substring(i1+11,i6-1));
       s = Integer.parseInt(st2.substring(i6+10,i2-1));
       int size= dm+s;
      System.out.println("Vehicles:" + v);
      System.out.println("Capacity:" + cp);
      System.out.println("Energy_Capacity:" + ecp);
      System.out.println("Energy_Consumption:" + ecm);
      System.out.println("Dimensions:" + dm);
      System.out.println("Stations:" + s);
      System.out.println("problem size:" +size);
     
      distMatrix();
      capacity();
    //printing x and y coordinates of depot, customers and stations
    //  System.out.println("x-axis coordnates->"+Arrays.toString(Xcoord));
    //  System.out.println("y-axis coordnates->"+Arrays.toString(Ycoord));
   int NoOfCustomers = dm-1;
   int NoOfVehicles = v;
  // int NoOfVehicles = 217;
   int VehicleCap = cp;   
    int Depot_x = Xcoord[0];
    int Depot_y = Ycoord[0];
     // System.out.println(""+NoOfCustomers);
    int TABU_Horizon = 10;
       Node[] Nodes = new Node[NoOfCustomers + 1];
        Node depot = new Node(Depot_x, Depot_y);

        Nodes[0] = depot;
        for (int I = 1; I <= NoOfCustomers; I++) {
            Nodes[I] = new Node(I, //Id ) is reserved for depot
                    Xcoord[I], // Coordinates of customer location
                    Ycoord[I],
                    capacity[I]  // Demand of customer with same id
            );
        }
      double[][] distanceMatrix = new double[NoOfCustomers + 1][NoOfCustomers + 1];
        double Delta_x, Delta_y;
        for ( i = 0; i <= NoOfCustomers; i++) {
            for (int j = i + 1; j <= NoOfCustomers; j++) //The table formed will be symmetric 
            {                                      

                Delta_x = (Nodes[i].Node_X - Nodes[j].Node_X);
                Delta_y = (Nodes[i].Node_Y - Nodes[j].Node_Y);

                double distance = Math.sqrt((Delta_x * Delta_x) + (Delta_y * Delta_y));

                distance = Math.round(distance);                //Distance is Casted in Integer
                

                distanceMatrix[i][j] = distance;
                distanceMatrix[j][i] = distance;
            }
        }
        int printMatrix = 0; //If we want to print distance matrix change 0 to 1

        if (printMatrix == 1){
            for ( i = 0; i <= NoOfCustomers; i++) {
                for (int j = 0; j <= NoOfCustomers; j++) {
                    System.out.print("d"+i+","+j+"-"+distanceMatrix[i][j] + "  ");
                }
                System.out.println();
            }
        }
 System.out.println("Attempting to resolve Vehicle Routing Problem (VRP) for "+NoOfCustomers+
                " Customers and "+NoOfVehicles+" Vehicles"+" with "+VehicleCap + " units of capacity\n");

        Solution s = new Solution(NoOfCustomers, NoOfVehicles, VehicleCap);
        

      // s.GreedySolution(Nodes, distanceMatrix);

      // s.SolutionPrint("Greedy Solution"); 
      s.GreedySolution(Nodes, distanceMatrix);
       s.TabuSearch(TABU_Horizon, distanceMatrix);

        s.SolutionPrint("");
 
      
  }
  public static void distMatrix(){
      
 
   
      int i1=0,i2=0;
     String A;
     i1=st2.indexOf("NODE_COORD_SECTION");
     i2 = st2.indexOf("DEMAND_SECTION ");
      A = st2.substring(i1+19,i2-1);
     // System.out.println(A);
      String x[] , y[];
      int size;
      size = dm+s;
     float[][] d= new float[size][size];
      float[][] s= new float[size][size];
      float[][] con= new float[size][size];
     // d= new float[size][size];
      
      x = new String[size*3];
      y = new String[size];
      int k=0;
      String ss,A1 = A,A2 = " ";
      int ii=0,ii1=0;
       ii = A1.indexOf(" ");//1
       A2+= A1.substring(ii+1); 
       //1a1a145a2a215 2 
     //A1 = A1.replaceAll(" ", "\n");
    
     String[] tokens = A1.split(" ");
String[] ary = new String[tokens.length];
//System.out.println("tokens.length: "+tokens.length);
String[] ary1 = new String[tokens.length/3];
String[] ary2 = new String[tokens.length/3];


int i = 0;
for (String token : tokens){
    ary[i++] = token; 
}
int jj=0;
//System.out.println("98: "+ary.length);

for(int u=0;u<ary.length;u++){
  //System.out.println(u+" : "+ary[u]);
  if(jj<ary1.length){
      ary1[jj]=ary[u+1];//145
      jj++;
  }
  u=u+2;//2
}
//System.out.println("114");

jj=0;
for(int u=0;u<ary.length;u++){
    if(jj<ary1.length){
      ary2[jj]=ary[u+2];//145
      jj++;
  }
  u=u+2;//2
}
//System.out.println("110 : "+ary2.length);
/*
for(int jjj =0;jjj<ary1.length;jjj++){
    System.out.println(jjj+" : "+ary1[jjj]);
}

for(int jjj =0;jjj<ary2.length;jjj++){
   System.out.println(jjj+" : "+ary2[jjj]);
}
*/
     
      int [] X = new int [ary1.length];
      int [] Y = new int [ary2.length];
      for (i=0;i<ary1.length;i++)
      {
      X[i]=Integer.parseInt(ary1[i]);
      Y[i]=Integer.parseInt(ary2[i]);
      }
     // printing coordinates X and Y
     // System.out.println(Arrays.toString(X));
     // System.out.println(Arrays.toString(Y));
      
      Xcoord=X;
      Ycoord=Y;
      for(int m=0;m<X.length;m++)
      {
          for(int n=0;n<X.length;n++)
            {
                d[m][n]=(float) Math.sqrt((X[m]-X[n])*(X[m]-X[n])+(Y[m]-Y[n])*(Y[m]-Y[n]));
          //      System.out.print("d"+m+","+n+"->"+d[m][n]+" ");
                   con[m][n] = d[m][n]*ecm;
            }
        //  System.out.println("");
      }
      //printing consumtion rate for each route
      /*
      for(int m=0;m<X.length;m++)
      {
         
          for(int n=0;n<X.length;n++)
            {
             
                System.out.print(" con"+m+","+n+"->"+con[m][n]);
             
            }
          System.out.println("");
      }
      */
          
      dist=d;
      consume=con;
        for(int m1=1;m1<X.length;m1++)
      {
          for(int n1=m1+1;n1<X.length;n1++)
            {
                s[m1][n1]=d[0][m1]+d[0][n1]-d[m1][n1];
            //   System.out.print("s"+m1+","+n1+"->"+s[m1][n1]+"  " );
            }
          //System.out.println("");
      }
      save=s;
}
  public static void capacity(){
      
 int i1=0,i2=0;
     String Cap;
     i1=st2.indexOf("DEMAND_SECTION ");
     i2 = st2.indexOf("STATIONS_COORD_SECTION ");
     Cap = st2.substring(i1+15,i2);
      
    String ar2[] = new String[dm];
     int check=i1+15,index=0;
   
       String filePath = "E-n22-k4.evrp";
  
  String contents = null;
  try {
   contents = new String(Files.readAllBytes(Paths.get(filePath)));
  } catch (IOException e) {
   e.printStackTrace();
  }

  String[] lines = contents.split("\\r?\\n");
  for (String line : lines) {
            //System.out.println(line);
        }
  
  int start_index = 13+dm+s;
  String[] dms_ar = new String[dm];

  //    System.out.println("indeX: "+index+ "start :" +start_index);//44
  for(int i = start_index;i<(start_index+dm);i++){
      dms_ar[index] = lines[i];
      index++;
  }
  //printing capacity line by line in string
  /*
for(int i = 0;i<dm;i++){
     System.out.println(dms_ar[i]);
    
  }
*/
   int in =0,in2=0;
   String items[] = new String[dm];
  for(int i =0;i<dms_ar.length;i++){
      in = dms_ar[i].indexOf(" ");
      items[i] = dms_ar[i].substring(in+1);
  }
  int [] itemCap = new int [items.length];
  for (int i3=0;i3<items.length;i3++)
      {
      itemCap[i3]=Integer.parseInt(items[i3]);
      }
  //printing customer capacity
  //System.out.println(Arrays.toString(itemCap));
  
  /*
      System.out.println("items-->"+items.length);
            for(int i=0;i<dm;i++){
                System.out.println(i+" capacity "+ items[i]);
            }
   */         
  capacity=itemCap;
      
              }
 


}
    
class Solution
{
    int NoOfVehicles;
    int NoOfCustomers;
    Vehicle[] Vehicles;
    double Cost;

    
    public Vehicle[] VehiclesForBestSolution;
    double BestSolutionCost;

    public ArrayList<Double> PastSolutions;

    Solution(int CustNum, int VechNum , int VechCap)
    {
        this.NoOfVehicles = VechNum;
        this.NoOfCustomers = CustNum;
        this.Cost = 0;
        Vehicles = new Vehicle[NoOfVehicles];
        VehiclesForBestSolution =  new Vehicle[NoOfVehicles];
        PastSolutions = new ArrayList<>();

        for (int i = 0 ; i < NoOfVehicles; i++)
        {
            Vehicles[i] = new Vehicle(i+1,VechCap);
            VehiclesForBestSolution[i] = new Vehicle(i+1,VechCap);
        }
    }

    public boolean UnassignedCustomerExists(Node[] Nodes)
    {
        for (int i = 1; i < Nodes.length; i++)
        {
            if (!Nodes[i].IsRouted)
                return true;
        }
        return false;
    }

    public void GreedySolution(Node[] Nodes , double[][] CostMatrix) {

        double CandCost,EndCost;
        int VehIndex = 0;

        while (UnassignedCustomerExists(Nodes)) {

            int CustIndex = 0;
            Node Candidate = null;
            double minCost = (float) Double.MAX_VALUE;

            if (Vehicles[VehIndex].Route.isEmpty())
            {
                Vehicles[VehIndex].AddNode(Nodes[0]);
            }

            for (int i = 1; i <= NoOfCustomers; i++) {
                if (Nodes[i].IsRouted == false) {
                    if (Vehicles[VehIndex].CheckIfFits(Nodes[i].demand)) {
                        CandCost = CostMatrix[Vehicles[VehIndex].CurLoc][i];
                        if (minCost > CandCost) {
                            minCost = CandCost;
                            CustIndex = i;
                            Candidate = Nodes[i];
                        }
                    }
                }
            }

            if ( Candidate  == null)
            {
                //If non of the Customers Fits
                if ( VehIndex+1 < Vehicles.length ) //We have more vehicles to assign i.e more routes
                {
                    if (Vehicles[VehIndex].CurLoc != 0) {//End this route
                        EndCost = CostMatrix[Vehicles[VehIndex].CurLoc][0];
                        Vehicles[VehIndex].AddNode(Nodes[0]);
                        this.Cost +=  EndCost;
                    }
                    VehIndex = VehIndex+1; //proceed to next vehicle
                }
                else //We don't have any more vehicle to assign. The problem cannont be solved with those vehicles,
                   // to check for alternative solutions change value of v to any number manually in line 87
                {
                    System.out.println("\n Customers do not fit in given Vehicle\n" +
                            "The problem cannot be resolved under these constrains\n"+"Try increasing number of vehicles");
                    System.exit(0);
                }
            }
            else
            {
                Vehicles[VehIndex].AddNode(Candidate);//If a Customer is found who can fit in the route
                Nodes[CustIndex].IsRouted = true;
                this.Cost += minCost;
            }
        }

        EndCost = CostMatrix[Vehicles[VehIndex].CurLoc][0];
        Vehicles[VehIndex].AddNode(Nodes[0]);
        this.Cost +=  EndCost;

    }


    public void TabuSearch(int TABU_Horizon, double[][] CostMatrix) {

        //We use 1-0 exchange move
        ArrayList<Node> RouteFrom;
        ArrayList<Node> RouteTo;

        int MovingNodeDemand = 0;

        int VehIndexFrom,VehIndexTo;
        double BestNCost,NeigthboorCost;

        int SwapIndexA = -1, SwapIndexB = -1, SwapRouteFrom =-1, SwapRouteTo=-1;

        int MAX_ITERATIONS = 2;
        int iteration_number= 0;

        int DimensionCustomer = CostMatrix[1].length;
        int TABU_Matrix[][] = new int[DimensionCustomer+1][DimensionCustomer+1];

        BestSolutionCost = this.Cost; //Initial total distance

        boolean Termination = false;

        while (!Termination)
        {
            iteration_number++;
            BestNCost = Double.MAX_VALUE;

            for (VehIndexFrom = 0;  VehIndexFrom <  this.Vehicles.length;  VehIndexFrom++) {
                RouteFrom =  this.Vehicles[VehIndexFrom].Route;
                int RoutFromLength = RouteFrom.size();
                for (int i = 1; i < RoutFromLength - 1; i++) { //Not possible to move depot!

                    for (VehIndexTo = 0; VehIndexTo <  this.Vehicles.length; VehIndexTo++) {
                        RouteTo =   this.Vehicles[VehIndexTo].Route;
                        int RouteTolength = RouteTo.size();
                        for (int j = 0; (j < RouteTolength - 1); j++) {//Not possible to move after last Depot!

                            MovingNodeDemand = RouteFrom.get(i).demand;

                            if ((VehIndexFrom == VehIndexTo) ||  this.Vehicles[VehIndexTo].CheckIfFits(MovingNodeDemand)) {
                                //If we assign to a different route check capacity constrains
                                //if in the new route is the same no need to check for capacity

                                if (((VehIndexFrom == VehIndexTo) && ((j == i) || (j == i - 1))) == false)  // Not a move that Changes solution cost
                                {
                                    double MinusCost1 = CostMatrix[RouteFrom.get(i - 1).NodeId][RouteFrom.get(i).NodeId];
                                    double MinusCost2 = CostMatrix[RouteFrom.get(i).NodeId][RouteFrom.get(i + 1).NodeId];
                                    double MinusCost3 = CostMatrix[RouteTo.get(j).NodeId][RouteTo.get(j + 1).NodeId];

                                    double AddedCost1 = CostMatrix[RouteFrom.get(i - 1).NodeId][RouteFrom.get(i + 1).NodeId];
                                    double AddedCost2 = CostMatrix[RouteTo.get(j).NodeId][RouteFrom.get(i).NodeId];
                                    double AddedCost3 = CostMatrix[RouteFrom.get(i).NodeId][RouteTo.get(j + 1).NodeId];

                                    //Check if the move is a Tabu! - If it is Tabu break
                                    if ((TABU_Matrix[RouteFrom.get(i - 1).NodeId][RouteFrom.get(i+1).NodeId] != 0)
                                            || (TABU_Matrix[RouteTo.get(j).NodeId][RouteFrom.get(i).NodeId] != 0)
                                            || (TABU_Matrix[RouteFrom.get(i).NodeId][RouteTo.get(j+1).NodeId] != 0)) {
                                        break;
                                    }

                                    NeigthboorCost = AddedCost1 + AddedCost2 + AddedCost3
                                            - MinusCost1 - MinusCost2 - MinusCost3;

                                    if (NeigthboorCost < BestNCost) {
                                        BestNCost = NeigthboorCost;
                                        SwapIndexA = i;
                                        SwapIndexB = j;
                                        SwapRouteFrom = VehIndexFrom;
                                        SwapRouteTo = VehIndexTo;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (int o = 0; o < TABU_Matrix[0].length;  o++) {
                for (int p = 0; p < TABU_Matrix[0].length ; p++) {
                    if (TABU_Matrix[o][p] > 0)
                    { TABU_Matrix[o][p]--; }
                }
            }

            RouteFrom =  this.Vehicles[SwapRouteFrom].Route;
            RouteTo =  this.Vehicles[SwapRouteTo].Route;
            this.Vehicles[SwapRouteFrom].Route = null;
            this.Vehicles[SwapRouteTo].Route = null;

            Node SwapNode = RouteFrom.get(SwapIndexA);

            int NodeIDBefore = RouteFrom.get(SwapIndexA-1).NodeId;
            int NodeIDAfter = RouteFrom.get(SwapIndexA+1).NodeId;
            int NodeID_F = RouteTo.get(SwapIndexB).NodeId;
            int NodeID_G = RouteTo.get(SwapIndexB+1).NodeId;

            Random TabuRan = new Random();
            int RendomDelay1 = TabuRan.nextInt(5);
            int RendomDelay2 = TabuRan.nextInt(5);
            int RendomDelay3 = TabuRan.nextInt(5);

            TABU_Matrix[NodeIDBefore][SwapNode.NodeId] = TABU_Horizon + RendomDelay1;
            TABU_Matrix[SwapNode.NodeId][NodeIDAfter]  = TABU_Horizon + RendomDelay2 ;
            TABU_Matrix[NodeID_F][NodeID_G] = TABU_Horizon + RendomDelay3;

            RouteFrom.remove(SwapIndexA);

            if (SwapRouteFrom == SwapRouteTo) {
                if (SwapIndexA < SwapIndexB) {
                    RouteTo.add(SwapIndexB, SwapNode);
                } else {
                    RouteTo.add(SwapIndexB + 1, SwapNode);
                }
            }
            else
            {
                RouteTo.add(SwapIndexB+1, SwapNode);
            }


            this.Vehicles[SwapRouteFrom].Route = RouteFrom;
            this.Vehicles[SwapRouteFrom].load -= MovingNodeDemand;

            this.Vehicles[SwapRouteTo].Route = RouteTo;
            this.Vehicles[SwapRouteTo].load += MovingNodeDemand;

            PastSolutions.add(this.Cost);

            this.Cost  += BestNCost;

            if (this.Cost <   BestSolutionCost)
            {
                SaveBestSolution();
            }

            if (iteration_number == MAX_ITERATIONS)
            {
                Termination = true;
            }
        }

        this.Vehicles = VehiclesForBestSolution;
        this.Cost = BestSolutionCost;

       
    }

    public void SaveBestSolution()
    {
        BestSolutionCost = Cost;
        for (int j=0 ; j < NoOfVehicles ; j++)
        {
            VehiclesForBestSolution[j].Route.clear();
            if (! Vehicles[j].Route.isEmpty())
            {
                int RoutSize = Vehicles[j].Route.size();
                for (int k = 0; k < RoutSize ; k++) {
                    Node n = Vehicles[j].Route.get(k);
                    VehiclesForBestSolution[j].Route.add(n);
                }
            }
        }
    }



    public void SolutionPrint(String Solution_Label)//Print Solution 
    {
        System.out.println("=========================================================");
        System.out.println(Solution_Label+"\n");
        ArrayList<Integer> beforeCharge = new ArrayList<Integer>();
        int c1=0,c2=0,c3=0,c4=0;
        float csmRate=0;
        float high=0;
        String r="";
        float remCsm = Evrp.ecp;
        int m=Evrp.dm;
       int size=Evrp.dm+Evrp.s;
       int mIndex=0;
       int totaldist=(int) this.Cost;
      // System.out.println("cost"+totaldist);
        
       //for (int j=0 ; j < 1 ; j++)
        for (int j=0 ; j < NoOfVehicles ; j++)
        {
            if (! Vehicles[j].Route.isEmpty())
            {   System.out.print("Vehicle " + j + ":");
                int RoutSize = Vehicles[j].Route.size();
                for (int k = 0; k < RoutSize ; k++) {
                    if (k == RoutSize-1)
                    { 
                       // System.out.print(Vehicles[j].Route.get(k).NodeId );  
                       //System.out.println("a");
                    }
                    else
                    { 
                       // System.out.print(Vehicles[j].Route.get(k).NodeId+ "->"); 
                        beforeCharge.add(Vehicles[j].Route.get(k).NodeId);
                        //System.out.print("a");
                    }
                }
                System.out.println();
            }
            
            for(int qq = 0; qq<=beforeCharge.size()-1; qq++){
            
            c1 = beforeCharge.get(qq);//0//14//16//13//11//8//10
            if(qq==beforeCharge.size()-1){
                c2 = 0;
            }
            else
            c2 = beforeCharge.get(qq+1);
            
            
            csmRate = Evrp.consume[c1][c2];
                  
               
            if(remCsm >2*csmRate){
            remCsm = remCsm-csmRate;
          
           //System.out.print("648:"+Vehicles[j].Route.get(qq).NodeId+ "->"); 
                //System.out.println("Evrp.consume[][]");
            }
            else if(c1==0){
                high=0;
            }
            else{
               // System.out.println("c1 "+c1);
                c4= beforeCharge.get(qq-1);
                //System.out.println("c4"+c4);
                
                for(m=Evrp.dm;m<=size-1;m++)
                {
                   if(high<Evrp.save[c1][m])
                       
                   {
                       high=Evrp.save[c1][m];
                      
                       //System.out.println("m"+m);
                       c3=m;
                      // System.out.println(mIndex+"  dashbd" + Evrp.save[c1][m]);
                   }
                   
                }
                
                r+=c3+"->";
                remCsm = Evrp.ecp;
                remCsm = remCsm-Evrp.consume[c3][qq];
                totaldist=(int) (totaldist-Evrp.dist[c1][c4]+(Evrp.dist[c1][c3]+Evrp.dist[c3][c4]));
                //System.out.println("c1c4"+Evrp.dist[c1][c3]);
                //System.out.println("m"+c3);
                high=0;
            }
           
             r+=Vehicles[j].Route.get(qq).NodeId+ "->";
            }
          
            System.out.println(r+"0");
        c1=0;
        c2=0;
       
        csmRate=0;
        r="";
        remCsm = Evrp.ecp;
        beforeCharge.clear();
        }
        
        System.out.println("\n Total distance without battery stations "+this.Cost);
        System.out.println(" Total distance after visiting battery stations "+totaldist);
        
    }
}

class Node
{
    public int NodeId;
    public int Node_X ,Node_Y; 
    public int demand; 
    public boolean IsRouted;
    private boolean IsDepot; 

    public Node(int depot_x,int depot_y) //Constructor for depot
    {
        this.NodeId = 0;
        this.Node_X = depot_x;
        this.Node_Y = depot_y;
        this.IsDepot = true;
    }

    public Node(int id ,int x, int y, int demand) //Constructor for Customers
    {
        this.NodeId = id;
        this.Node_X = x;
        this.Node_Y = y;
        this.demand = demand;
        this.IsRouted = false;
        this.IsDepot = false;
    }
}

class Vehicle
{
    public int VehId;
    public ArrayList<Node> Route = new ArrayList<Node>();
    public int capacity;
    public int load;
    public int CurLoc;
    public boolean Closed;

    public Vehicle(int id, int cap)
    {
        this.VehId = id;
        this.capacity = cap;
        this.load = 0;
        this.CurLoc = 0; //In depot Initially
        this.Closed = false;
        this.Route.clear();
    }

    public void AddNode(Node Customer )//Add Customer to Vehicle Route
    {
        Route.add(Customer);
        this.load +=  Customer.demand;
        this.CurLoc = Customer.NodeId;
    }

    public boolean CheckIfFits(int dem) //Check if we have Capacity Violation
    {
        return ((load + dem <= capacity));
    }

}
