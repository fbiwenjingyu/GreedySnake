package snake;
 
import java.awt.*;
import java.awt.event.*;
import java.util.*;
 
import snake.Coordinate;
import snake.Yard;
 
public class Snake {
    int score;//用以计数
    Node head = null;
    Node tail = null;
     
    Apple apples = null;
    ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();//用来存放�?有位置的坐标
    //Yard yard = new Yard();QUESTION:StackOutError是�?�么产生的？
     
    Snake() {//初始化的蛇是�?个有�?头一尾向左运动的链表
        Node head = new Node(Yard.ROWS/2, Yard.COLUMNS/2, Dir.L);
        Node tail = new Node(Yard.ROWS/2+1, Yard.COLUMNS/2, Dir.L);
        this.head = head;
        this.tail = tail;
        head.next = tail;
        tail.last = head;
         
        apples = new Apple(new Coordinate(10, 10));//蛇自带一个苹�?
        for(int y = 4; y <= Yard.ROWS - 2; y ++) {//QUESTION：有没有更加快捷的方法把�?有元素添加进去呢�?//将所有坐标都放入�?个数组中
            for(int x = 2; x <= Yard.COLUMNS - 2; x ++) {
                coordinates.add(new Coordinate(x, y));
            }
        }
    }
     
    void addToTail() {
        Node next = null;//ATTENTION:NULLPOINTEREXCEPTION!
        switch(tail.dir) {
        case L :
            next = new Node(tail.row, tail.col+1, Dir.L);
            break;
        case R :
            next = new Node(tail.row, tail.col-1, Dir.R);
            break;
        case U :
            next = new Node(tail.row-1, tail.col, Dir.U);
            break;
        case D :
            next = new Node(tail.row+1, tail.col, Dir.D);
            break;
        }
        tail.next = next;//QUESTION:双向回环链表怎么搞？
        next.last = tail;
        tail = next;
    }
     
    void addToHead() {//QUSTION:这个方向还是有点不太清楚�?
        Node last = null;
        switch(head.dir) {
        case L :
            last = new Node(head.row, head.col-1, Dir.L);
            break;
        case R :
            last = new Node(head.row, head.col+1, Dir.R);
            break;
        case U :
            last = new Node(head.row-1, head.col, Dir.U);
            break;
        case D :
            last = new Node(head.row+1, head.col, Dir.D);
            break;
        }
        head.last = last;
        last.next = head;
        head = last;
    }
     
    void deleteFromTail() {
        tail = tail.last;
        tail.next = null;
    }
 
    void deleteFromHead() {
        head = head.next;
        head.last = null;
    }
     
    void move() {
        addToHead();
        deleteFromTail();
        if((head.row == apples.row && head.col == apples.col) == true) {//用以判定是否吃到苹果
            score ++;
            addToTail();
            generate();
        }
    }
     
    boolean dead() {//判断是否死亡
        boolean smash = false;
        for(Node n = head.next; n != tail; n = n.next) {//遍历除tail以外所有节点的位置，判断是否与head的坐标重叠?
            if(head.coo.x == n.coo.x && head.coo.y == n.coo.y) {
                smash = true;
            }
        }
        if((head.coo.x < 2 || head.coo.x > Yard.COLUMNS - 2 ||//判断头部是否出界或�?�与其他节点坐标重合
                head.coo.y < 4 || head.coo.y > Yard.ROWS - 2 || smash) == true)
            return true;
        else
            return false;
    }
     
    void run() {
        if(dead() == false)//如果确认死亡，停止移�?
            move();
    }
     
    void generate() {//用以产生新的apples
        int num;
        ArrayList<Coordinate> s1 = new ArrayList<Coordinate>();
        ArrayList<Coordinate> s2 = new ArrayList<Coordinate>();
        for(Node n = head; n != null; n=n.next) {
            s1.add(n.coo);
        }
        s2 = coordinates;
        s2.removeAll(s1);
        num = (int)(s2.size()*Math.random());
        apples = new Apple(s2.get(num));
    }
     
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
            switch(keyCode) {
        case KeyEvent.VK_UP :
            if(head.dir != Dir.D)
                head.dir = Dir.U;
            break;
        case KeyEvent.VK_DOWN :
            if(head.dir != Dir.U)
                head.dir = Dir.D;
            break;
        case KeyEvent.VK_LEFT :
            if(head.dir != Dir.R)
                head.dir = Dir.L;
            break;
        case KeyEvent.VK_RIGHT :
            if(head.dir != Dir.L)
                head.dir = Dir.R;
            break;
        }
    }
     
    void draw(Graphics g) {
        run();
         
        for(Node n=head; n!=null; n=n.next) {
            n.draw(g);
            if(n == tail)//每次都将tail擦去可以有效地消除蛇的移动痕�?
                n.erase(g);
        }
        if(dead() == true) {
            g.setColor(Color.RED);
            g.setFont(Font.getFont("Blackadder ITC"));
            g.drawString("GAME OVER", 300, 300);
        }
        apples.draw(g);//之所以apples不用重画，是因为蛇吃到apples以后，tail的erase方法会顺带将apples擦除
    }
     
     
     
    private class Node {
        int w = Yard.BLOCK_SIZE;
        int h = Yard.BLOCK_SIZE;
        int row, col;
        Coordinate coo;
        Dir dir = Dir.D;
        Node next;//next和last可以将snake构成�?个双向链�?
        Node last;
         
        Node(int row, int col, Dir dir) {
            this.row = row;
            this.col = col;
            this.dir = dir;
            coo = new Coordinate(col, row);
        }
         
        void draw(Graphics g) {
            Color c = g.getColor();
            g.setColor(Color.BLACK);
            g.fillRect(col*w, row*h, w, h);
            g.setColor(c);
        }
         
        void erase(Graphics g) {
            Color c = g.getColor();
            g.setColor(Color.GRAY);
            g.fillRect(col*w, row*h, w, h);
            g.setColor(c);
        }
    }
     
}