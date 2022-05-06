package simulator;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Table extends JPanel{

	private ArrayList<Tile> tiles;//tiles.get(position of a tile)
	private String solution;
	private int emptyTilePos;
	private int movingTilesPos;
	private char moveToDo;
	private final int sleepTime=5;
	
	private void initTiles(){
		tiles = new ArrayList<Tile>();
		tiles.add(0, null);
		for (int i = 1; i <= 9; i++) {
			tiles.add(i,new Tile(i,Main.problem.get(i-1)));
		}
	}
	private void initEmptyPos(){
		for (int i = 1; i <= 9; i++) {
			if(tiles.get(i).id==0){
				emptyTilePos = i;//alternatively > tiles.get(i).pos
				break;
			}
		}
	}
	private void setMoveToDo(){
		if(solution.isEmpty()) return;
		if(solution.length()==1){
			moveToDo=solution.charAt(0);
			solution = new String();
			return;
		}
		moveToDo=solution.charAt(0);
		solution=solution.substring(1);
	}
	private void setMovingTilesPos(){
		switch (moveToDo) {
		case 'U':
			movingTilesPos=emptyTilePos-3;
			break;
		case 'D':
			movingTilesPos=emptyTilePos+3;
			break;
		case 'L':
			movingTilesPos=emptyTilePos-1;
			break;
		case 'R':
			movingTilesPos=emptyTilePos+1;
			break;
		default:
			break;
		}
	}
	
	public Table(){
		initTiles();
		movingTilesPos=0;
		setPreferredSize(new Dimension(220, 220));
	}
	public Table(String _solution){
		solution=_solution;
		initTiles();
		initEmptyPos();
		setMoveToDo();
		setMovingTilesPos();
		setPreferredSize(new Dimension(220, 220));
	}
	
	public void startAnimation() {
		Thread simulationThread = new Thread(new Runnable() {
			public void run() {
				if (movingTilesPos<1) {
					return;
				}
				while (!(solution.isEmpty()&&tiles.get(movingTilesPos).moveDone)) {
					if (tiles.get(movingTilesPos).moveDone) {
						tiles.get(movingTilesPos).moveDone=false;
						setMoveToDo();
						Tile tmp = tiles.get(emptyTilePos); //hold empty tile
						tiles.set(emptyTilePos, tiles.get(movingTilesPos)); //overwrite empty tile
						tiles.set(movingTilesPos, tmp); //add empty tile to new position
						emptyTilePos=movingTilesPos; //just overwrite it, movingTPos updates auto
						setMovingTilesPos();
					}
					tiles.get(movingTilesPos).move(moveToDo);
					try {
						Thread.sleep(sleepTime);
					} catch (Exception e) {System.err.println("error in simulationThread");}
				}
				//System.out.println("simulation done.");
			}
		});
		Thread repaintThread = new Thread(new Runnable() {
			public void run() {
				while (simulationThread.isAlive()){
					repaint();
					try {
						Thread.sleep(sleepTime-2);
					} catch (InterruptedException e) {System.err.println("error in repaintThread");}
				}
				//System.out.println("repaint done.");
			}
		});
		repaintThread.start();
		simulationThread.start();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		for (int i = 1; i <= 9; i++) {
			tiles.get(i).drawMe(g2d);
		}
	}
}
