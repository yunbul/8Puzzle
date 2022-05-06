package simulator;
import algorithm.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Main extends JFrame {

	private final Font font1 = new Font(Font.SERIF, Font.PLAIN, 20);
	private final Font font2 = new Font("Arial", Font.BOLD, 14);
	private JTextField[] inputs;
	
	public static LinkedList<Integer> problem=new LinkedList<Integer>();; //new Tile(i+1,problem.get(i)) i=0->8
	private Table table;
	private String solution;
	public static int usingHeuristic = 2;
	public static boolean useSolutionLen = true;
	public static boolean isUsingHeuristic = false;
	
	private JRadioButton rbHeur1;
	private JRadioButton rbHeur2;
	private JRadioButton rbHeur3;
	private JRadioButton rbUseLen;
	private JRadioButton rbUseHeur;
	
	public Main(){
		super("Eight Puzzle");
		randomize();
		table=new Table();
		solution="";
		//create control and input panel and add it to frame
		add(getCtrlPanel(),BorderLayout.EAST);
		add(getInputPanel(), BorderLayout.WEST);
		add(table, BorderLayout.CENTER);
		JLabel msg = new JLabel("Set Heuristic");
		msg.setFont(font1);
		add(msg, BorderLayout.SOUTH);
		
		setPreferredSize(new Dimension(580, 350));
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private JPanel getInputPanel(){
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(6, 3, 1, 1));//row ,col ,gap,gap
		inputs=new JTextField[9];
		for (int i = 0; i < inputs.length; i++) {
			inputs[i]= new JTextField();
			inputs[i].setFont(font1);
			inputs[i].setEditable(true);
			inputs[i].setPreferredSize(new Dimension(32, 32));
			inputPanel.add(inputs[i]);
		}
		for (int i = 0; i < 6; i++) {
			JTextField dummy = new JTextField();
			dummy.setVisible(false);
			inputPanel.add(dummy);
		}
		rbHeur1 = new JRadioButton("1");
		rbHeur1.setFont(font1);
		rbHeur1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (isUsingHeuristic) {
					usingHeuristic = 1;
					rbHeur2.setSelected(false);
					rbHeur3.setSelected(false);
					System.out.println("using misplacedTiles.");
				}
				else{
					rbHeur1.setSelected(false);
					System.out.println("unlock UseHeur first !");
				}
			}
		});
		rbHeur2 = new JRadioButton("2");
		rbHeur2.setFont(font1);
		rbHeur2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (isUsingHeuristic) {
					usingHeuristic = 2;
					rbHeur1.setSelected(false);
					rbHeur3.setSelected(false);
					System.out.println("using manhattanDistance.");
				}
				else {
					rbHeur2.setSelected(false);
					System.out.println("unlock UseHeur first !");
				}
			}
		});
		rbHeur3 = new JRadioButton("3");
		rbHeur3.setFont(font1);
		rbHeur3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (isUsingHeuristic) {
					usingHeuristic = 3;
					rbHeur1.setSelected(false);
					rbHeur2.setSelected(false);
					System.out.println("using totalHeuristic.");
				}
				else {
					rbHeur3.setSelected(false);
					System.out.println("unlock UseHeur first !");
				}
			}
		});
		inputPanel.add(rbHeur1);
		inputPanel.add(rbHeur2);
		inputPanel.add(rbHeur3);
		return inputPanel;
	}
	
	private JPanel getCtrlPanel(){
		JPanel ctrlPanel = new JPanel();
		ctrlPanel.setLayout(new GridLayout(6 ,2 , 10, 5)); 
		
		JButton btnSave = new JButton("SAVE");
		btnSave.setFont(font2);
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean check = true;
				LinkedList<Integer> checker = new LinkedList<Integer>();
				for (int i = 0; i < inputs.length; i++) {
					try{
					checker.add(Integer.parseInt(inputs[i].getText().trim()));
					}
					catch(NumberFormatException e){
						System.err.println("invalid input(s)");
						check=false;
						break;
					}
				}
				if(check){
					for (int i = 0; i < 9; i++) {
						if(!checker.contains(i)){
							check=false;
							break;
						}
					}
				}
				if (check) {
					problem=new LinkedList<Integer>();
					problem.addAll(checker);
					System.out.println("saved table:");
					for (int i = 0; i < 9; i++) {
						System.out.print(problem.get(i) + " ");
						if (i%3==2) {
							System.out.println();
						}
					}
					refreshTable("SAVE");
				}
				else
					System.err.println("missing tile(s)");
			}
		});
		
		JButton btnClear = new JButton("CLEAR");
		btnClear.setFont(font2);
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (int i = 0; i < inputs.length; i++) {
					inputs[i].setText("");
				}
			}
		});
		
		JButton btnRandom = new JButton("SHUFFLE");
		btnRandom.setFont(font2);
		btnRandom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				randomize();
				refreshTable("SAVE");
			}
		});
		
		JButton btnShow = new JButton("SHOW");
		btnShow.setFont(font2);
		btnShow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showSolution();
			}
		});
				
		JButton btnBFS = new JButton("BFS");
		btnBFS.setFont(font2);
		btnBFS.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				solveWithBFS();
			}
		});
		
		JButton btnUCS = new JButton("UCS");
		btnUCS.setFont(font2);
		btnUCS.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				solveWithUCS();
			}
		});
		
		JButton btnDFS = new JButton("DFS");
		btnDFS.setFont(font2);
		btnDFS.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				solveWithDFS();
			}
		});
		
		JButton btnIDS = new JButton("IDS");
		btnIDS.setFont(font2);
		btnIDS.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				solveWithIDS();
			}
		});
		
		JButton btnGBS = new JButton("GBS");
		btnGBS.setFont(font2);
		btnGBS.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				solveWithGBS();
			}
		});
		
		JButton btnAstar = new JButton("ASTAR");
		btnAstar.setFont(font2);
		btnAstar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				solveWithAStar();
			}
		});
		
		rbUseLen = new JRadioButton("Lenght");
		rbUseLen.setFont(font2);
		rbUseLen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				useSolutionLen=!useSolutionLen;
				if (useSolutionLen) rbUseLen.setSelected(true);
				else rbUseLen.setSelected(false);
			}
		});
		if (useSolutionLen) rbUseLen.setSelected(true);
		
		rbUseHeur = new JRadioButton("UseHeur");
		rbUseHeur.setFont(font2);
		rbUseHeur.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				isUsingHeuristic=!isUsingHeuristic;
				if (isUsingHeuristic){
					usingHeuristic = 2;
					rbUseHeur.setSelected(true);
					rbHeur2.setSelected(true);
				}
				else{
					usingHeuristic = -1;
					rbUseHeur.setSelected(false);
					rbHeur1.setSelected(false);
					rbHeur2.setSelected(false);
					rbHeur3.setSelected(false);
				}
			}
		});
		if (isUsingHeuristic) rbUseHeur.setSelected(true);
		
		ctrlPanel.add(btnSave);
		ctrlPanel.add(btnClear);
		ctrlPanel.add(btnRandom);
		ctrlPanel.add(btnShow);
		ctrlPanel.add(btnBFS);
		ctrlPanel.add(btnUCS);
		ctrlPanel.add(btnDFS);
		ctrlPanel.add(btnIDS);
		ctrlPanel.add(btnGBS);
		ctrlPanel.add(btnAstar);
		ctrlPanel.add(rbUseHeur);
		ctrlPanel.add(rbUseLen);
		return ctrlPanel;
	}
	
	private void solveWithBFS(){
		if (isUsingHeuristic || useSolutionLen) {
			System.err.println("CANCELLED:\nplease deselect 'UseHeur' and 'Lenght'\nto speed up BFS.");
			return;
		}
		System.out.println("BFS----------");
		solution = "SAVE";
		try{
			BFS b = new BFS();
			solution = b.solve();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		refreshTable(solution);
	}
	private void solveWithUCS(){
		if(!useSolutionLen || isUsingHeuristic){
			System.err.println("CANCELLED:\nplease deselect 'UseHeur' and\nselect 'Lenght' to take only\npathcost into consideration.");
			return;
		}
		System.out.println("UCS----------");
		solution = "SAVE";
		try{
			UCS u = new UCS();
			solution = u.solve();
		}
		catch(Exception e){e.printStackTrace();}
		refreshTable(solution);
	}
	private void solveWithDFS(){
		if (isUsingHeuristic || useSolutionLen) {
			System.err.println("CANCELLED:\nplease deselect 'UseHeur' and 'Lenght'\nto speed up DFS.");
			return;
		}
		System.out.println("DFS----------");
		solution = "SAVE";
		try{
			DFS d = new DFS();
			solution = d.solve();
		}
		catch(Exception e){e.printStackTrace();}
		refreshTable(solution);
	}
	private void solveWithIDS(){
		if (isUsingHeuristic || useSolutionLen) {
			System.err.println("CANCELLED:\nplease deselect 'UseHeur' and 'Lenght'\nto speed up IDS.");
			return;
		}
		System.out.println("IDS----------");
		solution = "SAVE";
		try{
			IDS i = new IDS();
			solution = i.solve();
		}
		catch(Exception e){e.printStackTrace();}
		refreshTable(solution);
	}
	private void solveWithGBS(){
		if(useSolutionLen || !isUsingHeuristic){
			System.err.println("CANCELLED:\nplease select 'UseHeur' and\ndeselect 'Lenght' to take only\nheuristic into consideration.");
			return;
		}
		System.out.println("GBS----------");
		solution = "SAVE";
		try{
			GBS g = new GBS();
			solution = g.solve();
		}
		catch(Exception e){e.printStackTrace();}
		refreshTable(solution);
	}
	private void solveWithAStar(){
		if (!isUsingHeuristic || !useSolutionLen) {
			System.err.println("CANCELLED:\nplease select 'UseHeur' and 'Lenght'\nto take them both into consideration.");
			return;
		}
		System.out.println("ASTAR----------");
		solution = "SAVE";
		try{
			AStar a = new AStar();
			solution = a.solve();
		}
		catch(Exception e){e.printStackTrace();}
		refreshTable(solution);
	}
	
	private void showSolution(){
		table.startAnimation();
	}
	
	private boolean reverseOrder = false;
	private void randomize(){
		problem = new LinkedList<Integer>();
		LinkedList<Integer> sampleSpace = new LinkedList<Integer>();
		if (reverseOrder) {
			int j=8;
			for (int i = 0; i < 9; i++) {
				sampleSpace.add(i,j);--j;
			}
		}
		else{
			for (int i = 0; i < 9; i++) {
				sampleSpace.add(i,i);
			}
		}reverseOrder=!reverseOrder;
		for (int i = 0; i < 9; i++) {
			if(sampleSpace.size()==1){
				problem.add(sampleSpace.get(0));
				break;
			}
			int rnd = (int)(Math.random()*(sampleSpace.size()));
			problem.add(sampleSpace.get(rnd));
			sampleSpace.remove(rnd);
		}
		System.out.println("randomize:");
//		for (int i = 0; i < 9; i++) {
//			System.out.print(problem.get(i) + ",");
//		}System.out.println("\n");
		int x=0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(problem.get(x) + " ");
				++x;
			}System.out.println();
		}
	}
	
	private void refreshTable(String solutionStr) {
		remove(table);
		if(solutionStr=="SAVE"){
			table = new Table();
			
		}
		else{
			table = new Table(solutionStr);
			//System.out.println("ready to simulate.");
			System.out.println();
		}
		add(table, BorderLayout.CENTER);
		revalidate();
		repaint();
		pack();
		//System.out.println("table refreshed.");
	}
	
	public static void main(String[] args) {
		new Main();
	}
	

}
