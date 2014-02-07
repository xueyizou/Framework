/**
 * 
 */
package dominant;

import sim.util.Double2D;
import tools.CONFIGURATION;
import modeling.SAAModel;
import modeling.SAAModelBuilder;
import modeling.uas.UAS;
import ec.EvolutionState;
import ec.Individual;
import ec.Problem;
import ec.simple.SimpleFitness;
import ec.simple.SimpleProblemForm;
import ec.vector.DoubleVectorIndividual;

/**
 * @author Xueyi Zou
 *
 */
public class MaxOscillation extends Problem implements SimpleProblemForm 
{

	/* (non-Javadoc)
	 * @see ec.simple.SimpleProblemForm#evaluate(ec.EvolutionState, ec.Individual, int, int)
	 */
	@Override
	public void evaluate(EvolutionState state, Individual ind, int subpopulation, int threadnum) 
	{
		// TODO Auto-generated method stub
		if (ind.evaluated) return;

        if (!(ind instanceof DoubleVectorIndividual))
            state.output.fatal("Whoa!  It's not a DoubleVectorIndividual!!!",null);        
      
        DoubleVectorIndividual ind2 = (DoubleVectorIndividual)ind;
                
        double selfDestDist= ind2.genome[0];
        double selfSpeed= ind2.genome[1];
        
        boolean headOnSelected = (ind2.genome[2]==1)? true: false;
        double  headOnOffset = ind2.genome[3];
    	boolean headOnIsRightSide = (ind2.genome[4]==1)? true: false;		
		double  headOnSpeed = ind2.genome[5];
		
        boolean crossingSelected = (ind2.genome[6]==1)? true: false;
        double  crossingEncounterAngle = ind2.genome[7];
   		boolean crossingIsRightSide = (ind2.genome[8]==1)? true: false;		
		double  crossingSpeed = ind2.genome[9];
		
        boolean tailApproachSelected = (ind2.genome[10]==1)? true: false;    	
		double  tailApproachOffset = ind2.genome[11];
		boolean tailApproachIsRightSide = (ind2.genome[12]==1)? true: false;
		double  tailApproachSpeed = ind2.genome[13];
		
		
       	long time = System.nanoTime();
		SAAModel simState= new SAAModel(785945568, CONFIGURATION.fieldXVal, CONFIGURATION.fieldYVal, false); 	
		
		CONFIGURATION.selfDestDist = selfDestDist;
		CONFIGURATION.selfSpeed = selfSpeed;
		
		CONFIGURATION.headOnSelected = headOnSelected;
		CONFIGURATION.headOnOffset=headOnOffset;
		CONFIGURATION.headOnIsRightSide= headOnIsRightSide;    		
		CONFIGURATION.headOnSpeed =headOnSpeed;
		
		CONFIGURATION.crossingSelected = crossingSelected;
		CONFIGURATION.crossingEncounterAngle=crossingEncounterAngle;
		CONFIGURATION.crossingIsRightSide= crossingIsRightSide;    		
		CONFIGURATION.crossingSpeed =crossingSpeed;
		
		CONFIGURATION.tailApproachSelected = tailApproachSelected;
		CONFIGURATION.tailApproachOffset= tailApproachOffset;
		CONFIGURATION.tailApproachIsRightSide=tailApproachIsRightSide;
		CONFIGURATION.tailApproachSpeed =tailApproachSpeed;
		
   		SAAModelBuilder sBuilder = new SAAModelBuilder(simState);
		sBuilder.generateSimulation();
		
		
		for(int m=0; m<simState.uasBag.size(); m++)
		{
			UAS uas1 = (UAS)simState.uasBag.get(m);
			for(int n=m+1; n<simState.uasBag.size(); n++)
			{
				UAS uas2 = (UAS)simState.uasBag.get(n);
				if(uas1.getLocation().distance(uas2.getLocation())<=7)
				{
	    			
					 ((SimpleFitness)ind2.fitness).setFitness(  state,            
					            0,/// ...the fitness...
					            false);///... is the individual ideal?  Indicate here...

					 ind2.evaluated = true;
					return;
				}
				
			}
			
		}
		
		simState.start();	

		do
		{
			if (!simState.schedule.step(simState))
			{
				break;
			}
		} while(simState.schedule.getSteps()< 1000);
		
		double totalArea = 0;
		for(int j=1; j<simState.uasBag.size(); j++)
		{
			UAS uas = (UAS)simState.uasBag.get(j);
			System.out.println(uas.getOscillation());
 
			totalArea += uas.getOscillation();
		}

		float rawFitness= (float)totalArea/simState.schedule.getSteps();  
		float fitness = rawFitness;
        
        
        if (!(ind2.fitness instanceof SimpleFitness))
            state.output.fatal("Whoa!  It's not a SimpleFitness!!!",null);
        
        ((SimpleFitness)ind2.fitness).setFitness(   state,            
										            fitness,/// ...the fitness...
										           false);///... is the individual ideal?  Indicate here...
        
        ind2.evaluated = true;
        System.out.println("individual result: selfDestDist("+selfDestDist+ "), selfDestAngle("+selfSpeed+ "), isRightSide("+headOnIsRightSide+"), offset("+ headOnOffset+"), speed("+ headOnSpeed + "); fitness[[ " + fitness +" ]]" );
        System.out.println();
        
        
        //if(fitness >0.9)
        {
        	StringBuilder dataItem = new StringBuilder();
        	dataItem.append(state.generation+",");
        	for (int i=0; i< ind2.genome.length; i++)
        	{
        		dataItem.append(ind2.genome[i]+",");
        		
        	}
        	dataItem.append(fitness+",");
        	dataItem.append(simState.aDetector.getNoAccidents());
        	Simulation.simDataSet.add(dataItem.toString());
        	
        }
        
        MyStatistics.accidents[state.generation]+= simState.aDetector.getNoAccidents();

	}

}
