package algorithm;

import java.util.LinkedList;
import java.util.Queue;

import simulator.Main;

public class BFS {

	private Queue<AlgoTable> que = new LinkedList<AlgoTable>();
	private AlgoTable table;
	
	public BFS(){
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
		int limit = 18000000;
		int counter =0;
		que.add(table);
		long start = System.currentTimeMillis();
		while (!table.isGoal() && counter < limit) {
			
			table=que.poll();
			for (AlgoTable t : table.getChildren()) {
					que.add(t);
			}
			++counter;
		
		}
		long finish = System.currentTimeMillis();
		System.out.println("time elapsed: " + (finish-start) + " millis.");
		if(counter == limit)
			System.err.println("can not find any solution.");
		else
			System.out.println("solution found.");
		String solution = table.getSolution();
		System.out.println(counter + " nodes expanded.");
		System.out.println(solution.length() + " moves made.");
		System.out.println(solution);
		que = new LinkedList<>();
		return solution;
	}

	
}
