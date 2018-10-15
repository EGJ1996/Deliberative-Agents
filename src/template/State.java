package template;

import logist.plan.Plan;
import logist.task.Task;
import logist.task.TaskSet;
import logist.topology.Topology.City;

import java.util.ArrayList;


import logist.plan.Action;
import logist.plan.Action.Delivery;
import logist.plan.Action.Move;
import logist.plan.Action.Pickup;

class State{

		public City current;
		public TaskSet availableTasks;
		public int rem_cap;
		public Plan cityPlan;
		public ArrayList<Action> actions;
		public double cost;
		public TaskSet pickedTasks;
		public State(City current,int rem_cap,TaskSet availableTasks,TaskSet pickedTasks) {
			this.current = current;
			this.rem_cap = rem_cap;
			this.availableTasks = availableTasks;
			this.pickedTasks = pickedTasks;
		}
		
		public abstract class CustomAction{
			public abstract State changeState();
			public abstract logist.plan.Action getLogAction();
		}
		class MoveAction extends CustomAction{
			public City dest;
			
			public MoveAction(City dest) {
				this.dest  = dest;
			}
			public State changeState() {
				return new State(this.dest,rem_cap,availableTasks,pickedTasks);
			}
			public logist.plan.Action getLogAction(){
				return new Move(this.dest);
			}
		}
		class PickupAction extends CustomAction{
			public Task t;
			public PickupAction(Task t) {
				this.t = t;
			}
			public State changeState() {
				return new State(this.t.pickupCity,rem_cap - this.t.weight,availableTasks,pickedTasks);
			}
			public logist.plan.Action getLogAction(){
				return new Pickup(this.t);
			}
		
		}
		class DeliverAction extends CustomAction{
			public Task t;
			
			public DeliverAction(Task t) {
				this.t = t;
			}
			public State changeState() {
				return new State(this.t.deliveryCity,rem_cap+this.t.weight,availableTasks,pickedTasks);
			}
			public logist.plan.Action getLogAction(){
				return new Delivery(this.t);
			}
		
		}
		public ArrayList<CustomAction> getNextActions(){
			ArrayList<CustomAction> legActions = new ArrayList<CustomAction>();
			
			for(Task t:this.pickedTasks) {
				if(t.weight > this.rem_cap)
					continue;
				if(this.current.equals(t.pickupCity)) {;
					legActions.add(new PickupAction(t));
					pickedTasks.add(t);
					this.rem_cap -= t.weight;
				}
				else if(this.current.equals(t.deliveryCity)) {
					legActions.add(new DeliverAction(t));
					this.rem_cap += t.weight;
				}
				else {
					ArrayList<City> cities = (ArrayList<City>)this.current.pathTo(t.deliveryCity);
					for (City c:cities)
						legActions.add(new MoveAction(c));
					
				}
				
			}
			return legActions;
		}
		
		public State getNextState(CustomAction act) {
			return act.changeState();
		}
		
		public int getHeuristicCost() {
			return this.availableTasks.rewardSum() - this.pickedTasks.rewardSum();
		}
}
		