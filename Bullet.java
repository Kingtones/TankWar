import java.awt.*;
import java.util.List;


public class Bullet {
	public static final int Xspeed=10,Yspeed=10;
	public static final int BulletWidth=10,BulletHeight=10;
	private boolean live=true;
	
	int x,y;
	Tank.Direction dir;
	private TankClient tc;

	public Bullet(int x, int y, Tank.Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		
	}
	
	public Bullet(int x,int y,Tank.Direction dir,TankClient tc){
		this(x,y,dir);
		this.tc=tc;
	}//重载的构造方法便于在move时直接传入当前tc对象
	
	
	public void draw(Graphics g){
		if(!live){
			tc.bullets.remove(this);
			return;
		}
		Color c=g.getColor();
		g.setColor(Color.red);
		g.fillOval(x, y, BulletWidth, BulletHeight);
		g.setColor(c);
		
		move();
		
	}

	private void move() {
		// TODO Auto-generated method stub
		switch(dir){
		case L:
			x-=Xspeed;break;
		case U:
			y-=Yspeed;break;
		case R:
			x+=Xspeed;break;
		case D:
			y+=Yspeed;break;
		case STOP:
			break;
		case LU:
			x-=Xspeed;
			y-=Yspeed;break;
		case RU:
			x+=Xspeed;
			y-=Yspeed;break;
		case RD:
			x+=Xspeed;
			y+=Yspeed;break;
		case LD:
			x-=Xspeed;
			y+=Yspeed;break;
		}
		
	
		if(x<0||y<0||x>TankClient.Game_width||y>TankClient.Game_width){
			live=false;
		}//若出界则不画。将对象从容器中移除
	}
	
	/*
	 *以下方法用于碰撞检测以及生存与否的判断 
	 * */
	 
	public boolean isLive(){
		return live;
	}
	
	public void setLive(boolean live){
		this.live=live;
	}
	
	public Rectangle getRect(){
		return new Rectangle(x,y,BulletWidth,BulletHeight);
	}
	
	public boolean isHit(EnemyTank e){
		if(this.getRect().intersects(e.getRect())&&e.isLive()&&this.live){
			e.setLive(false);//一旦打中，将敌方坦克的状态改为false
			this.setLive(false);
			return true;
		}
		return false;
	}
	
	public boolean isHit(List<EnemyTank> enemy){
		for(int i=0;i<enemy.size();i++){
			if(isHit(enemy.get(i))){
				return true;
			}
			
		}
		return false;
	}
	
	public boolean isHitWall(Wall w){
		if(this.getRect().intersects(w.getRect())&&this.live){
			this.setLive(false);
			return true;
		}
		return false;
	}
	
	
	
}
