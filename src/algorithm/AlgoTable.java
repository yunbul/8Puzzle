package algorithm;

import java.util.ArrayList;

import simulator.Main;

@SuppressWarnings("serial")
public class AlgoTable extends ArrayList<Integer> {

	private int usingHeuristic = Main.usingHeuristic;
	private boolean isUsingHeuristic = Main.isUsingHeuristic;
	private boolean addSolutionLenght = Main.useSolutionLen;
	private StringBuilder moves = new StringBuilder("");
	private int state;
	private int heuristic = 0;
	
	public AlgoTable(int _state){
		state=_state;
	}
	public int getHeuristic(){
		return heuristic;
	}
	private void calculateHeuristic(){
		if (isUsingHeuristic) {
			if (usingHeuristic==1) heuristic = numOfMisplaced();
			else if (usingHeuristic==2) heuristic = manhattanDistance();
			else if (usingHeuristic==3) heuristic = totalHeuristic();
		}
		if (addSolutionLenght) heuristic+=moves.length();
	}
	private int numOfMisplaced(){
		int heuristic1=0;
		for (int i = 0; i < size()-1; i++) {
			if(get(i)!=i+1)++heuristic1;
		}
		if(get(8)!=0) ++heuristic1;
		return heuristic1;
	}
	private int manhattanDistance(){
		int heuristic2=0;
		for (int i = 0; i < size(); i++) {
			if(get(i)==0) continue;
			int desiredRow = get(i)%3==0 ? ((int)(get(i)/3))-1 : ((int)(get(i)/3)) ; //rows{0,1,2}
			int desiredCol = get(i)%3==0 ? 2 : (get(i)%3)-1; //columns{0,1,2}
			int pos = i+1;
			int actualRow = pos%3==0 ? ((int)(pos/3))-1 : ((int)(pos/3)) ; //rows{0,1,2}
			int actualCol = pos%3==0 ? 2 : (pos%3)-1; //columns{0,1,2}
			heuristic2+=Math.abs(actualCol-desiredCol) + Math.abs(actualRow-desiredRow);
		}
		return heuristic2;
	}
	private int totalHeuristic(){
		return numOfMisplaced() + manhattanDistance();
	}
	public boolean isGoal(){
		for (int i = 0; i < size()-1; i++) {
			if(get(i)!=i+1)return false;
		}
		return true;
	}
	public void print(){
		System.out.println("state: " + state);
		int x=0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(get(x) + " ");
				++x;
			}System.out.println();
		}
		System.out.println("_______________");
	}
	public String getSolution(){
		return moves.toString();
	}
	
	private void makeMove(char direction){
		switch (direction) {
		case 'U':
			set(state-1,get(state-4));
			set(state-4,0);
			state-=3;
			break;
		case 'D':
			set(state-1,get(state+2));
			set(state+2,0);
			state+=3;
			break;
		case 'L':
			set(state-1,get(state-2));
			set(state-2,0);
			--state;
			break;
		case 'R':
			set(state-1,get(state));
			set(state,0);
			++state;
			break;
		default:
			break;
		}
		moves.append(direction);
	}
	
	public AlgoTable[] getChildren(){
		StringBuilder possibleChild = new StringBuilder("");
		//get row and column info of zero tile
		int row = state%3==0 ? ((int)(state/3))-1 : ((int)(state/3)) ; //rows{0,1,2}
		int col = state%3==0 ? 2 : (state%3)-1; //columns{0,1,2}
		//get last move to prevent loops
		char lastMove = moves.length()==0 ? 'X' : moves.charAt(moves.length()-1);
		
		if(row==0) {
			if(lastMove!='U') possibleChild.append('D');
		}
		else if(row==1){
			if(lastMove!='U') possibleChild.append('D');
			if(lastMove!='D') possibleChild.append('U');
		}
		else if(lastMove!='D') possibleChild.append('U');
		
		if(col==0) {
			if(lastMove!='L') possibleChild.append('R');
		}
		else if(col==1){
			if(lastMove!='L') possibleChild.append('R');
			if(lastMove!='R') possibleChild.append('L');
		}
		else if(lastMove!='R') possibleChild.append('L');
		
		AlgoTable[] ts = new AlgoTable[possibleChild.length()];
		for (int i = 0; i < possibleChild.length(); i++) {
			//inform new(child) table about previous(parent) state
			AlgoTable t = new AlgoTable(state);
			//create same table as previous
			t.addAll(this);
			t.moves.append(moves);
			//let table to swap tiles and update state itself
			t.makeMove(possibleChild.charAt(i));
			//if there is a heuristic it will be calculated
			t.calculateHeuristic();
			ts[i] = t;
		}
		return ts;
	}
}
