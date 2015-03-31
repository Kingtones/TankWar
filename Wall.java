import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Wall {
	
	int x,y,width,height;
	TankClient tc;
	
	public Wall(int x, int y, int width, int height,TankClient tc) {
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.tc=tc;
	}

	/*
	 * ��ǽ�ķ���
	 */
	
	public void draw(Graphics g){
		Color c=g.getColor();
		g.fillRect(x, y, width, height);
		g.setColor(Color.BLACK);
		g.setColor(c);
	}
	
	/*
	 * ��ײ���ķ���
	 */
	
	public Rectangle getRect(){
		return new Rectangle(x,y,width,height);
	}
	
}
