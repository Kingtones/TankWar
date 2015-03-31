import java.awt.*;
//import java.util.List;

public class EnemyBullet {
	public static final int Xspeed=10,Yspeed=10;
	public static final int BulletWidth=10,BulletHeight=10;
	private boolean live=true;
	
	int x,y;
	EnemyTank.Direction enemydir;
	private TankClient tc;
	
	public EnemyBullet(int x,int y,EnemyTank.Direction enemydir,TankClient tc){
		this.x=x;
		this.y=y;
		this.enemydir=enemydir;
		this.tc=tc;
	}
	
	
	public void draw(Graphics g){
		if(!live){
			tc.enemybullets.remove(this);
			return;
		}
		Color c=g.getColor();
		g.setColor(Color.black);
		g.fillOval(x, y, BulletWidth, BulletHeight);
		g.setColor(c);
		
		
		enemymove();
	}
	
	
	public void enemymove(){
		
		switch(enemydir){
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
	
	
	/**
	 * 
	 * @param 己方的坦克是否被敌人子弹击中
	 * @return 如果被击中返回true，并将血量减少20，如果血量==0则将己方live设为false
	 */
	
	public boolean isHit(Tank t){
		if(this.getRect().intersects(t.getRect())&&t.isLive()&&this.live){
			t.blood-=20;//被打中之后不是直接死亡而是blood减去20
			if(t.blood==0){
				t.setLive(false);
			}
			this.setLive(false);
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param 已经存在的墙
	 * @return 如果碰撞则返回true，子弹live状态设为false
	 */
	
	public boolean isHitWall(Wall w){
		if(this.getRect().intersects(w.getRect())&&this.live){
			this.setLive(false);
			return true;
		}
		return false;
	}
	
	
}

