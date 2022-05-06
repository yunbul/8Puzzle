package simulator;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;

public class Tile {
	
	public int pos;//=1->9
	public Integer id;//=0->8
	private int size=70;
	private int tileIndent=5;//for all tiles
	private int labelIndent=25;//for label in tile
	private int fontSize=38;
	private int squareIndent=3;//for lines in tile
	private Point position;
	private Point positionBM;//positionBeforeMove
	public boolean moveDone= false;
	
	public Tile(int _pos, int _id){
		pos=_pos;
		id=_id;
		//find where tile should be drawn
		int posY = size*(pos%3==0 ? ((int)(pos/3))-1 : ((int)(pos/3)) ); //rows{0,1,2} - posY{0,100,100}
		int posX = size*(pos%3==0 ? 2 : (pos%3)-1); //colums{0,1,2} - posX{0,100,100}
		position = new Point(posX+tileIndent,posY+tileIndent);
		positionBM = new Point(position.x, position.y);
		moveDone=false;
	}
	public void drawMe(Graphics2D g2d){
		if(id==0) return; //TODO: comment out later
		g2d.setColor(Color.BLACK);	
		g2d.drawLine(position.x+squareIndent, position.y+squareIndent, position.x+size-squareIndent, position.y+squareIndent);//U>
		g2d.drawLine(position.x+squareIndent, position.y+squareIndent, position.x+squareIndent, position.y+size-squareIndent);//Lv
		g2d.drawLine(position.x+squareIndent, position.y+size-squareIndent, position.x+size-squareIndent, position.y+size-squareIndent);//D>
		g2d.drawLine(position.x+size-squareIndent, position.y+squareIndent, position.x+size-squareIndent, position.y+size-squareIndent);//Rv
		g2d.setFont(new Font("Arial", Font.BOLD, fontSize));
		g2d.drawString(id.toString(), position.x+labelIndent, position.y+labelIndent+labelIndent);
	}
	public void move(char direction){
		if(id==0) return;
		if(moveDone) return;
		//this tile goes to opposite direction of empty tile (0)
		//if solution string is U then empty tile is moving up 
		//and this tile should move down
		//make movement
		if(direction=='U'){
			//move downwards
			++position.y;
		}
		else if(direction=='D'){
			//move upwards
			--position.y;
		}
		else if(direction=='L'){
			//move right
			++position.x;
		}
		else if(direction=='R'){
			//move left
			--position.x;
		}
		//check if move completed -> update position
		if(direction=='U' || direction=='D'){
			if(Math.abs(position.y-positionBM.y)==size){
				pos=(position.y>positionBM.y)? pos+3:pos-3;
				positionBM.x =position.x;
				positionBM.y =position.y;
				moveDone=true;
				//System.out.println("tile: " + id + " moved to: " + pos);
			}
		}
		else if(Math.abs(position.x-positionBM.x)==size){
			pos=(position.x>positionBM.x)?pos+1:pos-1;
			positionBM.x =position.x;
			positionBM.y =position.y;
			moveDone=true;
			//System.out.println("tile: " + id + " moved to: " + pos);
		}
	}
}
