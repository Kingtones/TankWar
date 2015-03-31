import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;


/**
 * 客户端类
 * @author Yang Haoran
 *
 */

public class TankClient extends Frame{
	/*
	 * 线程睡眠时长，能改变游戏速度
	 * 默认情况的敌人数量
	 * 前景色
	 */
	public static final int Game_width=880,Game_heigth=640;
	public static final int SleepDuration=80;
	public int EnemyCount=5;
	public Color foreground=Color.white;
	public Random ran=new Random();
	

	
	/*
	 * 我方敌方坦克，子弹的初始化
	 */
	
	Tank myTank=new Tank(Game_width/2-10,Game_heigth-10,this);
	List<EnemyTank> enemy=new ArrayList<EnemyTank>();
	List<Bullet> bullets=new ArrayList<Bullet>();
	List<EnemyBullet> enemybullets=new ArrayList<EnemyBullet>();
	
	
	
	/*
	 * 墙和基地的初始化
	 */
	
	Wall w1=new Wall(220,220,30,200,this);
	Wall w2=new Wall(600,220,200,30,this);
	Wall cw1=new Wall(410,590,10,50,this);
	Wall cw2=new Wall(480,590,10,50,this);
	Wall cw3=new Wall(410,580,80,10,this);
	

	Image offScreenImage=null;//双缓冲时使用

	/*
	 * 主窗口，只在刚出现时执行
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
		this.setBackground(Color.white);//底层窗口的颜色，由于缓冲机制所以没有显示
		this.addKeyListener(new KeyMonitor());//键盘的监听器
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		new Thread(new RepaintThread()).start();//重画线程
		
		
	}
	
	/*
	 * paint方法，包含我方敌方坦克，子弹以及墙的draw
	 * (non-Javadoc)
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g){

		myTank.draw(g);
		
		g.drawString("Change the control way with Q and W,and press space to shoot", 3,35);
		g.drawString("press Z to add enemis and press F2 to revive", 3, 50);
		g.drawString("Bullets      count: "+bullets.size(),3, 65);//子弹的计数
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
		
		//我方子弹
		for(int i=0;i<bullets.size();i++){
			Bullet b=bullets.get(i);
			b.isHit(enemy);//判断方法执行以消去被击中的坦克
			b.isHitWall(w1);
			b.isHitWall(w1);
			b.isHitWall(w2);
			b.draw(g);
		}//容器内的存在的对象画出（bullet类的draw方法）
		
		
		//敌方子弹
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
		
		//敌方坦克
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
	
	
	//双缓冲
	public void update(Graphics g){
		if(offScreenImage==null){
			offScreenImage=this.createImage(Game_width, Game_heigth);
		}
		Graphics gOffScreen=offScreenImage.getGraphics();
		Color c=gOffScreen.getColor();
		gOffScreen.setColor(foreground);//窗口显示的真正颜色
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
	}//页面重画的线程在lanuchFrame中引用
	
	
	
	//键盘监听的内部类便于使用成员变量
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
