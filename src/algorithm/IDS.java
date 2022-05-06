package algorithm;

import java.util.Stack;

import simulator.Main;

public class IDS {

	private Stack<AlgoTable> stck = new Stack<AlgoTable>();
	private AlgoTable table;
	
	public IDS(){
		//find state of table
		int state =0;
		for (int i = 0; i < 9; i++) {
			if(Main.problem.get(i)==0){
				state=i+1;
				break;
			}
		}
		//create table with that state and fill it
		table = new AlgoTable(state);
		for (int i = 0; i < 9; i++) {
			table.add(i,Main.problem.get(i));
		}
	}
	public String solve(){
		int counter = 0;
		int limit = 18000000;
		int maxdepth = 40;
		int iteration=0; //limited depth of tree
		AlgoTable initNode = table;
		
		long start = System.currentTimeMillis();
		for(;iteration < maxdepth && !table.isGoal();++iteration) {
			
			stck.push(initNode);
			counter=0;
			while (!table.isGoal() && !stck.isEmpty()) {
				
				table=stck.pop();
				//num of moves made will be equal to limited depth of tree
				//if selected table is at the bottom of tree, then pop() it
				if (table.getSolution().length()<iteration) {
					for (AlgoTable t : table.getChildren())
						stck.push(t);
				}
				++counter;
			}
			
		}
		
		long finish = System.currentTimeMillis();
		System.out.println("time elapsed: " + (finish-start) + " millis.");
		System.out.println("solution at iteration: " + (iteration-1));
		if(counter == limit || iteration==maxdepth)
			System.err.println("can not find any solution.");
		else
			System.out.println("solution found.");
		String solution = table.getSolution();
		System.out.println(counter + " nodes expanded.");
		System.out.println(solution.length() + " moves made.");
		System.out.println(solution);
		stck = new Stack<>();
		return solution;
	}
}
