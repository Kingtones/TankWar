import java.awt.*;
//import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class EnemyTank {
	public static final int Xspeed=4,Yspeed=4;
	
	public static final int TankWidth=40,TankHeight=40;
	
	int x,y;
	int oldX,oldY;
	
	private boolean live=true;
	
	private TankClient tc;
	
	
	/**
	 * 改变方向时的随机数
	 */
	private static Random r=new Random();
	
	private int step=r.nextInt(10)+6;

	enum Direction {LU,RU,RD,LD,L,U,R,D,STOP};
	Direction dir =Direction.STOP;
	Direction barrelDir=Direction.R;
	
	
	public EnemyTank(int x,int y,Direction dir,TankClient tc){
		this.x=x;
		this.y=y;
		this.oldX=x;
		this.oldY=y;
		this.dir=dir;
		this.tc=tc;
	}
	
	
	public void draw(Graphics g){
		if(!live){
			tc.enemy.remove(this);
		}
		Color c=g.getColor();
		g.setColor(Color.red);
		g.fillOval(x, y, TankWidth, TankHeight);
		g.setColor(c);
		
		move();
		switch(barrelDir){//重画时根据坦克方向划线
		case L:
			g.drawLine(x+TankWidth/2, y+TankHeight/2, x, y+TankHeight/2);
			break;
		case U:
			g.drawLine(x+TankWidth/2, y+TankHeight/2, x+TankWidth/2, y);
			break;
		case R:
			g.drawLine(x+TankWidth/2, y+TankHeight/2, x+TankWidth, y+TankHeight/2);
			break;
		case D:
			g.drawLine(x+TankWidth/2, y+TankHeight/2, x+TankWidth/2, y+TankHeight);
			break;
		case STOP:
			break;
		case LU:
			g.drawLine(x+TankWidth/2, y+TankHeight/2, x, y);
			break;
		case RU:
			g.drawLine(x+TankWidth/2, y+TankHeight/2, x+TankWidth, y);
			break;
		case RD:
			g.drawLine(x+TankWidth/2, y+TankHeight/2, x+TankWidth, y+TankHeight);
			break;
		case LD:
			g.drawLine(x+TankWidth/2, y+TankHeight/2, x, y+TankHeight);
			break;
		}
		
		
	}
	
	
	public void move(){
		
		this.oldX=x;
		this.oldY=y;
		
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
		
		if(this.dir!=Direction.STOP){
			this.barrelDir=this.dir;
		}//炮筒方向与坦克方向相关联
		
		//判断坦克的出界
		if(x<3) x=3;
		if(y<35) y=35;
		if(x>TankClient.Game_width-Tank.TankWidth-5) x=TankClient.Game_width-Tank.TankWidth-5;
		if(y>TankClient.Game_heigth-Tank.TankHeight-5) y=TankClient.Game_heigth-Tank.TankHeight-5;
	
		//当随机步数为0时重新随机step，再改变方向
		if(step==0){
			step=r.nextInt(10)+6;
			Direction[] dirs=Direction.values();
			int rn=r.nextInt(dirs.length);
			dir=dirs[rn];
		}
		
		step--;
		//控制子弹密度
		if(r.nextInt(40)>37){
			fire();
		}
		
	}
	
	
	
	/**
	 * 敌人坦克发射子弹的方法
	 */
	public void fire(){
		int x1 = this.x+TankWidth/2-Bullet.BulletWidth/2;
		int y1 = this.y+TankWidth/2-Bullet.BulletHeight/2;
		EnemyBullet b=new EnemyBullet(x1,y1,this.barrelDir,this.tc);
		tc.enemybullets.add(b);//ArrayList容器内装入新的子弹对象
		//return b;//返回值不再产生作用
	}//KeyReleased方法中被调用
	
	
	/*
	public void KeyPressed(KeyEvent e){
		int key=e.getKeyCode();
		
		if(key==KeyEvent.VK_UP){
			y -=5;
		}
		if(key==KeyEvent.VK_DOWN){
			y +=5;
		}
		if(key==KeyEvent.VK_RIGHT){
			x +=5;
		}
		if(key==KeyEvent.VK_LEFT){
			x -=5;
		}
		
	}*/
	
	
	
	
	//碰撞检测新增的三个方法
	public Rectangle getRect(){
		return new Rectangle(x,y,TankWidth,TankHeight);
	}
	
	public boolean isLive(){
		return live;
	}
	public void setLive(boolean live){
		this.live=live;
	}
	
	public void changeDirection(){
		x=this.oldX;
		y=this.oldY;
	}
	
	/**
	 * 敌人坦克与墙的碰撞检测
	 * @param 已经存在的墙
	 * @return 如果碰撞则返回true，并且敌人坦克回到上一步
	 */
	
	public boolean isHitWall(Wall w){
		if(this.getRect().intersects(w.getRect())&&this.live){
			this.changeDirection();
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * 敌人坦克之间的碰撞检测
	 * @param 容器内所有的敌人坦克
	 * @return 如果碰撞返回true，并且各自回到上一步
	 */

	public boolean isHitWithTank(List<EnemyTank> enemy){
		for(int i=0;i<enemy.size();i++){
			EnemyTank e=enemy.get(i);
			if(e!=this){
				if(this.getRect().intersects(e.getRect())&&this.live&&e.isLive()){
					this.changeDirection();
					return true;
				}
			}
		}
		return false;
	}
	
}
