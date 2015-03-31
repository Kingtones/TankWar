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
		}//�������򲻻�����������������Ƴ�
	}
	
	
	/*
	 *���·���������ײ����Լ����������ж� 
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
	 * @param ������̹���Ƿ񱻵����ӵ�����
	 * @return ��������з���true������Ѫ������20�����Ѫ��==0�򽫼���live��Ϊfalse
	 */
	
	public boolean isHit(Tank t){
		if(this.getRect().intersects(t.getRect())&&t.isLive()&&this.live){
			t.blood-=20;//������֮����ֱ����������blood��ȥ20
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
	 * @param �Ѿ����ڵ�ǽ
	 * @return �����ײ�򷵻�true���ӵ�live״̬��Ϊfalse
	 */
	
	public boolean isHitWall(Wall w){
		if(this.getRect().intersects(w.getRect())&&this.live){
			this.setLive(false);
			return true;
		}
		return false;
	}
	
	
}

