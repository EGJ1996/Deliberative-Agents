package template;

import logist.plan.Action;
import logist.plan.Plan;
import logist.simulation.Vehicle;

import java.util.ArrayList;

class PlanBuilder{
	ArrayList<Action> planActions;
	State finalState;
	public int cost,hCost;
	public Vehicle v;
	public PlanBuilder(ArrayList<Action> actions,State s, Vehicle v) {
		this.planActions = actions;
		this.finalState = s;
		this.cost = this.hCost = 0;
		this.v = v;
	}
	public PlanBuilder(ArrayList<Action> actions, State s,int cost,int hCost) {
		this.planActions = actions;
		this.finalState = s;
		this.cost = cost;
		this.hCost = hCost;
	}
	
	public int getCCost() {
		Plan p  = new Plan(this.finalState.current,this.planActions);
		return (int)p.totalDistance() * v.costPerKm();
	}
	
	public int getHCost() {
		return this.finalState.getHeuristicCost();
	}
	
	public int getCost() {
		return this.cost + this.hCost;
	}
}