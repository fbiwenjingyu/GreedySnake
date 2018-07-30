package snake;
 
import java.awt.*;
import java.awt.event.*;
 
public class Apple {
    int w = Yard.BLOCK_SIZE;
    int h = Yard.BLOCK_SIZE;
    int row, col;
    Coordinate c = null;
     
    Apple(Coordinate c) {
        this.c = c;
        this.row = c.y;
        this.col = c.x;
    }
 
    void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.YELLOW);
        g.fillOval(col*w, row*h, w, h);
        g.setColor(c);
    }
 
}