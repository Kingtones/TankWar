import java.awt.*;
import java.awt.event.KeyEvent;

public class Tank {
	public static final int Xspeed=6,Yspeed=6;
	public static final int TankWidth=40,TankHeight=40;
	public static boolean walkWay=true;//变更控制坦克行走的方式
	
	private boolean live=true;
	private int x,y;
	TankClient tc;
	private BloodStrip bs=new BloodStrip();//血条类对象
	public int blood=60;
	
	enum Direction {LU,RU,RD,LD,L,U,R,D,STOP};
	Direction dir =Direction.STOP;
	Direction barrelDir=Direction.U;
	
	
	
	/*
	 * 构造方法以及重载的构造方法
	 */
	public Tank(int x,int y){
		this.x=x;
		this.y=y;
	}
	
	public Tank(int x,int y,TankClient tc){
		this.x=x;
		this.y=y;
		this.tc=tc;
	}
	
	public void draw(Graphics g){
		if(!live) return;
		Color c=g.getColor();
		g.setColor(Color.blue);
		g.fillOval(x, y,TankWidth, TankHeight);
		g.setColor(c);
		move();
		bs.draw(g);//画血条
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
	}
	
	
	/*
	 * 按键抬起和松开时的事件触发
	 */
	
	public void KeyPressed(KeyEvent e){
		int key=e.getKeyCode();
		
		if(key==KeyEvent.VK_Q){
			Tank.walkWay=false;
		}
		if(key==KeyEvent.VK_W){
			Tank.walkWay=true;
			dir=Direction.STOP;
		}//change the control way of tank
		
		if(key==KeyEvent.VK_UP){
			dir=Direction.U;
		}
		if(key==KeyEvent.VK_DOWN){
			dir=Direction.D;
		}
		if(key==KeyEvent.VK_RIGHT){
			dir=Direction.R;
		}
		if(key==KeyEvent.VK_LEFT){
			dir=Direction.L;
		}
		/*if(key==KeyEvent.VK_UP&&key==KeyEvent.VK_LEFT){
			dir=Direction.LU;
		}
		if(key==KeyEvent.VK_UP&&key==KeyEvent.VK_RIGHT){
			dir=Direction.RU;
		}
		if(key==KeyEvent.VK_DOWN&&key==KeyEvent.VK_RIGHT){
			dir=Direction.RD;
		}
		if(key==KeyEvent.VK_DOWN&&key==KeyEvent.VK_LEFT){
			dir=Direction.LD;
		}*/
		
	}

	
	public void KeyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int key=e.getKeyCode();
		
		if(key==KeyEvent.VK_SPACE){
			fire();//抬起时发出子弹避免画面拥挤
		}
		
		if(key==KeyEvent.VK_A){
			superFire();
		}
		
		if(key==KeyEvent.VK_F2){
			this.live=true;
			this.blood=60;
		}
		
		if(key==KeyEvent.VK_Z){
			for(int i=0;i<tc.EnemyCount;i++){
				tc.enemy.add(new EnemyTank(50+40*(i+1),80,EnemyTank.Direction.D,tc));
			}
			
		}
		
		if(walkWay==true){
		if(key==KeyEvent.VK_UP){
			dir=Direction.STOP;
		}
		if(key==KeyEvent.VK_DOWN){
			dir=Direction.STOP;
		}
		if(key==KeyEvent.VK_RIGHT){
			dir=Direction.STOP;
		}
		if(key==KeyEvent.VK_LEFT){
			dir=Direction.STOP;
		}
		/*if(key==KeyEvent.VK_UP&&key==KeyEvent.VK_LEFT){
			dir=Direction.STOP;
		}
		if(key==KeyEvent.VK_UP&&key==KeyEvent.VK_RIGHT){
			dir=Direction.STOP;
		}
		if(key==KeyEvent.VK_DOWN&&key==KeyEvent.VK_RIGHT){
			dir=Direction.STOP;
		}
		if(key==KeyEvent.VK_DOWN&&key==KeyEvent.VK_LEFT){
			dir=Direction.STOP;
		}*/
		}
	}
	
	
	
	/*
	 * fire方法
	 */
	
	public Bullet fire(){
		if(!live) return null;
		int x1 = x+TankWidth/2-Bullet.BulletWidth/2;
		int y1 = y+TankWidth/2-Bullet.BulletHeight/2;
		Bullet b=new Bullet(x1,y1,barrelDir,tc);
		tc.bullets.add(b);//ArrayList容器内装入新的子弹对象
		return b;//返回值不再产生作用
	}//KeyReleased方法中被调用
	
	public Bullet fire(Direction dir){
		if(!live) return null;
		int x1 = x+TankWidth/2-Bullet.BulletWidth/2;
		int y1 = y+TankWidth/2-Bullet.BulletHeight/2;
		Bullet b=new Bullet(x1,y1,dir,tc);
		tc.bullets.add(b);
		return b;
	}
	
	/**
	 * 调用时向8个方向发射子弹
	 */
	
	public void superFire(){
		Direction[] dirs=Direction.values();
		for(int i=0;i<dirs.length;i++){
			if(dirs[i]!=Direction.STOP){
				fire(dirs[i]);
			}
		}
	}
	
	
	
	
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
	
	private class BloodStrip{
		public void draw(Graphics g){
			Color c=g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y-20, TankWidth, 10);
			int w=TankWidth*blood/60;
			g.fillRect(x, y-20, w, 10);
			g.setColor(c);
		}
	}
	
}
