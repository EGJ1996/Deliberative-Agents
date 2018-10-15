package template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import logist.simulation.Vehicle;
import logist.task.TaskSet;
import template.State.CustomAction;
import logist.plan.Action;

class BFS {
	public Vehicle v;
	public HashMap<PlanBuilder,Integer> bestCost;
	public State initialState;
	
	public BFS(TaskSet allTasks) {
		this.initialState = new State(this.v.getCurrentCity(), this.v.capacity() - this.v.getCurrentTasks().weightSum(), 
				allTasks,this.v.getCurrentTasks());
	}
	
	public ArrayList<Action> getActionsFromCustoms(ArrayList<CustomAction> availActions){
		ArrayList<Action> actions = new ArrayList<Action>();
		for(CustomAction act:availActions)
			actions.add(act.getLogAction());
		return actions;
	}
	public void traverseBFS() {
		Queue<PlanBuilder> q = new LinkedList<PlanBuilder>();
		q.add(new PlanBuilder(null, initialState,v));
		ArrayList<State> visited = new ArrayList<State>();
		
		while(!q.isEmpty()) {
			PlanBuilder plan  = q.poll();
			State cur = plan.finalState;
			ArrayList<CustomAction> availActions = cur.getNextActions();
			for(CustomAction a:availActions) {
				State nextState = cur.getNextState(a);
				if(visited.contains(nextState))
					continue;
				PlanBuilder temp = new PlanBuilder(getActionsFromCustoms(availActions), nextState,v);
				int tempCost = bestCost.getOrDefault(temp, Integer.MAX_VALUE);
				if(temp.getCost() < tempCost)
					bestCost.put(temp, temp.getCost());
			}
		}
	}
}