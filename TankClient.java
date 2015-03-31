import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;


/**
 * �ͻ�����
 * @author Yang Haoran
 *
 */

public class TankClient extends Frame{
	/*
	 * �߳�˯��ʱ�����ܸı���Ϸ�ٶ�
	 * Ĭ������ĵ�������
	 * ǰ��ɫ
	 */
	public static final int Game_width=880,Game_heigth=640;
	public static final int SleepDuration=80;
	public int EnemyCount=5;
	public Color foreground=Color.white;
	public Random ran=new Random();
	

	
	/*
	 * �ҷ��з�̹�ˣ��ӵ��ĳ�ʼ��
	 */
	
	Tank myTank=new Tank(Game_width/2-10,Game_heigth-10,this);
	List<EnemyTank> enemy=new ArrayList<EnemyTank>();
	List<Bullet> bullets=new ArrayList<Bullet>();
	List<EnemyBullet> enemybullets=new ArrayList<EnemyBullet>();
	
	
	
	/*
	 * ǽ�ͻ��صĳ�ʼ��
	 */
	
	Wall w1=new Wall(220,220,30,200,this);
	Wall w2=new Wall(600,220,200,30,this);
	Wall cw1=new Wall(410,590,10,50,this);
	Wall cw2=new Wall(480,590,10,50,this);
	Wall cw3=new Wall(410,580,80,10,this);
	

	Image offScreenImage=null;//˫����ʱʹ��

	/*
	 * �����ڣ�ֻ�ڸճ���ʱִ��
	 */
	public void launchFrame(){
		for(int i=0;i<EnemyCount;i++){
			enemy.add(new EnemyTank(50+150*i,80+50*i,EnemyTank.Direction.D,this));
		}
		this.setLocation(50, 100);
		this.setSize(Game_width, Game_heigth);
		this.setVisible(true);
		this.setResizable(false);
		this.setTitle("TankWar");
		this.setBackground(Color.white);//�ײ㴰�ڵ���ɫ�����ڻ����������û����ʾ
		this.addKeyListener(new KeyMonitor());//���̵ļ�����
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		new Thread(new RepaintThread()).start();//�ػ��߳�
		
		
	}
	
	/*
	 * paint�����������ҷ��з�̹�ˣ��ӵ��Լ�ǽ��draw
	 * (non-Javadoc)
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g){

		myTank.draw(g);
		
		g.drawString("Change the control way with Q and W,and press space to shoot", 3,35);
		g.drawString("press Z to add enemis and press F2 to revive", 3, 50);
		g.drawString("Bullets      count: "+bullets.size(),3, 65);//�ӵ��ļ���
		g.drawString("EnemyBullets count: "+enemybullets.size(), 3, 80);
		g.drawString("Enemy        count: "+enemy.size(), 3, 95);
		g.drawString("Blood: "+myTank.blood, 3, 110);
//		if(myTank.blood==0){
//			g.drawString("u failed!", Game_width/2, Game_heigth/2);
//		}
		w1.draw(g);
		w2.draw(g);
		cw1.draw(g);
		cw2.draw(g);
		cw3.draw(g);
		
		//�ҷ��ӵ�
		for(int i=0;i<bullets.size();i++){
			Bullet b=bullets.get(i);
			b.isHit(enemy);//�жϷ���ִ������ȥ�����е�̹��
			b.isHitWall(w1);
			b.isHitWall(w1);
			b.isHitWall(w2);
			b.draw(g);
		}//�����ڵĴ��ڵĶ��󻭳���bullet���draw������
		
		
		//�з��ӵ�
		for(int i=0;i<enemybullets.size();i++){
			EnemyBullet bu=enemybullets.get(i);
			bu.isHit(myTank);
			bu.isHitWall(w1);
			bu.isHitWall(w2);
			bu.isHitWall(cw1);
			bu.isHitWall(cw2);
			bu.isHitWall(cw3);
			bu.draw(g);
		}
		
		//�з�̹��
		for(int i=0;i<enemy.size();i++){
			EnemyTank e=enemy.get(i);
			e.isHitWall(w1);
			e.isHitWall(w2);
			e.isHitWall(cw1);
			e.isHitWall(cw2);
			e.isHitWall(cw3);
			e.isHitWithTank(enemy);
			e.draw(g);
		}
	}
	
	
	//˫����
	public void update(Graphics g){
		if(offScreenImage==null){
			offScreenImage=this.createImage(Game_width, Game_heigth);
		}
		Graphics gOffScreen=offScreenImage.getGraphics();
		Color c=gOffScreen.getColor();
		gOffScreen.setColor(foreground);//������ʾ��������ɫ
		gOffScreen.fillRect(0, 0, Game_width, Game_heigth);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TankClient tc=new TankClient();
		tc.launchFrame();
		//tc.paint(g);
	}
	
	private class RepaintThread implements Runnable{

		@Override
		public void run() {
			while(true){
				repaint();
				try {
					Thread.sleep(SleepDuration);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}//ҳ���ػ����߳���lanuchFrame������
	
	
	
	//���̼������ڲ������ʹ�ó�Ա����
	private class KeyMonitor extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			myTank.KeyPressed(e);
		}
		
		public void keyReleased(KeyEvent e){
			myTank.KeyReleased(e);
		}
		
	}

}
