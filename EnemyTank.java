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
	 * �ı䷽��ʱ�������
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
		switch(barrelDir){//�ػ�ʱ����̹�˷�����
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
		}//��Ͳ������̹�˷��������
		
		//�ж�̹�˵ĳ���
		if(x<3) x=3;
		if(y<35) y=35;
		if(x>TankClient.Game_width-Tank.TankWidth-5) x=TankClient.Game_width-Tank.TankWidth-5;
		if(y>TankClient.Game_heigth-Tank.TankHeight-5) y=TankClient.Game_heigth-Tank.TankHeight-5;
	
		//���������Ϊ0ʱ�������step���ٸı䷽��
		if(step==0){
			step=r.nextInt(10)+6;
			Direction[] dirs=Direction.values();
			int rn=r.nextInt(dirs.length);
			dir=dirs[rn];
		}
		
		step--;
		//�����ӵ��ܶ�
		if(r.nextInt(40)>37){
			fire();
		}
		
	}
	
	
	
	/**
	 * ����̹�˷����ӵ��ķ���
	 */
	public void fire(){
		int x1 = this.x+TankWidth/2-Bullet.BulletWidth/2;
		int y1 = this.y+TankWidth/2-Bullet.BulletHeight/2;
		EnemyBullet b=new EnemyBullet(x1,y1,this.barrelDir,this.tc);
		tc.enemybullets.add(b);//ArrayList������װ���µ��ӵ�����
		//return b;//����ֵ���ٲ�������
	}//KeyReleased�����б�����
	
	
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
	
	
	
	
	//��ײ�����������������
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
	 * ����̹����ǽ����ײ���
	 * @param �Ѿ����ڵ�ǽ
	 * @return �����ײ�򷵻�true�����ҵ���̹�˻ص���һ��
	 */
	
	public boolean isHitWall(Wall w){
		if(this.getRect().intersects(w.getRect())&&this.live){
			this.changeDirection();
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * ����̹��֮�����ײ���
	 * @param ���������еĵ���̹��
	 * @return �����ײ����true�����Ҹ��Իص���һ��
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
