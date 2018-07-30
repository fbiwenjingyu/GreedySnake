package snake;
 
import java.awt.*;
import java.awt.event.*;
 
public class Yard extends Frame {
    static int X_0 = 200;
    static int Y_0 = 0;
    static int ROWS = 60;
    static int COLUMNS = 60;
    static int BLOCK_SIZE = 10;//QUESTION：只有静态变量才能被其他类使用？
     
    Image offScreenImage = null;
     
    Snake snake = new Snake();
     
    public void launch() {//画出底层框架背景
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);//QUESTION:这里有必要先调用setvisible()吗？
            }
        });
        this.addKeyListener(new KeyMonitor());
        this.setBounds(X_0, Y_0, ROWS*BLOCK_SIZE, COLUMNS*BLOCK_SIZE);//
        this.setBackground(Color.GRAY);
        this.setTitle("Snake");
        this.setVisible(true);
         
        new Thread(new PaintThread()).start();//作为子进程来运行，能够让背景不用多次重画
    }
     
    public void paint(Graphics g) {
        //super.paint(g);QUESTION：有�?么用�?
        snake.draw(g);//QUESTION：这个擦除问题还是不好解�?//先画蛇，使蛇在最底层运动
         
        g.setColor(Color.GRAY);//擦除重叠的分数，以保证显示清晰�??
        g.fillRect(60, 50, 10, 10);
        Color c = g.getColor();
        g.setColor(Color.BLACK);
        for(int i=0; i<COLUMNS; i++ ) {//画出行列的线条�??
            g.drawLine(i*BLOCK_SIZE, 0, i*BLOCK_SIZE, COLUMNS*BLOCK_SIZE);
        }
        g.setColor(Color.BLACK);
        for(int i=0; i<ROWS; i++) {
            g.drawLine(0, i*BLOCK_SIZE, ROWS*BLOCK_SIZE, i*BLOCK_SIZE);
        }
        g.setColor(Color.WHITE);//用以显示分数�?
        g.drawString("Score:" + snake.score, 30, 60);
        g.setColor(c);
    }
 
    public void update(Graphics g) {//QUESTION:双缓冲如何实现？
        if(offScreenImage == null) {//QUESTION:哪里来的NULLPOINTEREXCEPTION�?
            offScreenImage = this.createImage(ROWS*BLOCK_SIZE, COLUMNS*BLOCK_SIZE);
        }
        Graphics gOff = offScreenImage.getGraphics();
        paint(gOff);
        g.drawImage(offScreenImage, 0, 0, null);
    }
     
    public static void main(String[] args) {
        new Yard().launch();
    }
 
     
     
    private class PaintThread implements Runnable {
        public void run() {//QUESTION：private类里的public方法的访问权限是怎么样的呢？
            while(true) {
                repaint();
                try {
                    Thread.sleep(100);//ATTENTION!
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
 
     
     
     
    private class KeyMonitor extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            snake.keyPressed(e);
        }
    }
}