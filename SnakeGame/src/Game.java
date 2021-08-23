import javax.swing.*;
import java.awt.event.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.*;

public class Game extends JFrame{
    final int RECTANGLE_WIDTH = 10;
    final int RECTANGLE_HEIGHT = 10;
    final int rectangleCountX = 30;
    final int rectangleCountY = 30;
    private boolean colided = false;
    private int current;
    private int vk;
    private int score = 0;
    private int high_score;
    private int start = (rectangleCountY/2)+(rectangleCountX*rectangleCountX/2);

    private List<Shape> grid;
    private List<Shape> fill;
    private List<Shape> crums;

    private ArrayList<Integer> upper_bounds;
    private ArrayList<Integer> lower_bounds;

    private ArrayList<Integer> snake_list;

    private Crum crum = new Crum(rectangleCountX*rectangleCountY);
    PaintPane ppane;

    public Game(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        ppane = new PaintPane();
        ppane.setBackground(Color.black);
        ppane.requestFocus();
        setContentPane(ppane);
        repaint();
        pack();
        ppane.requestFocusInWindow();
    
    }
    class PaintPane extends JPanel{
        public PaintPane(){
            
            

            setPreferredSize(new Dimension(rectangleCountX*RECTANGLE_WIDTH,rectangleCountY*RECTANGLE_HEIGHT));
            setFocusable(true);
            requestFocus();

            grid = new ArrayList<>(rectangleCountX*rectangleCountY);
            fill = new ArrayList<>(rectangleCountX*rectangleCountY);
            crums = new ArrayList<>(rectangleCountX*rectangleCountY);


            snake_list = new ArrayList<>();


            upper_bounds = new ArrayList<>(rectangleCountX);
            lower_bounds = new ArrayList<>(rectangleCountX);
            for (int i = 0; i < rectangleCountX; i++){
                upper_bounds.add(i*rectangleCountY);
                lower_bounds.add(i*rectangleCountY - 1);
            }
            lower_bounds.add(rectangleCountX*rectangleCountY-1);


            addKeyListener(new KeyAdapter(){
                public void keyPressed(KeyEvent e){
                    int key = e.getKeyCode();
                    if (key == KeyEvent.VK_LEFT){
                        vk = -1;
                    } 
                    if (key == KeyEvent.VK_RIGHT){
                        vk = 1;
                    } 
                    if (key == KeyEvent.VK_UP){
                        vk = 2;
                    } 
                    if (key == KeyEvent.VK_DOWN){
                        vk = -2;
                    } 
  
                }
            });
            for (int i = 0; i < rectangleCountX; i++){
                for (int j = 0; j < rectangleCountY; j++){
                    grid.add(new Rectangle(i*RECTANGLE_WIDTH, j*RECTANGLE_HEIGHT,
                    RECTANGLE_WIDTH, RECTANGLE_HEIGHT));
                }
            }


    
        }
       
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.green);
            for (Shape cell : fill){
                g2d.fill(cell);
            }
            g2d.setColor(Color.red);
            for (Shape cell : crums){
                g2d.fill(cell);
            }

        }
        public void check_crum(int current){
            if (current == crum.get_location()){
                crum.generate_crum();
                crums.clear();
                crums.add(grid.get(crum.get_location()));
                snake_list.add(current);
                score++;
                if (score > high_score){
                    high_score = score;
                }
                setTitle("High Score: "+high_score+" , Current Score: "+score);
                repaint();
            }
        }

    }
    public void check_collision(int current){
        if (snake_list.contains(current)){
            colided = false;
            collide();
        }
    }
    private void collide(){
        snake_list.clear();
        snake_list.add(current);
        fill.clear();
        fill.add(grid.get(current));
        if (score > high_score){
            high_score = score;
        }
        score = 0;
        repaint();
        play();
    }
    private void game_loop(){
        while(!colided){
            int temp_head = current;
            if(vk == 1){
                // go right
                current = (current+rectangleCountY);
                if (current > (rectangleCountX*rectangleCountY-1)){
                    current = temp_head;
                    collide();
                }
 
            } else if (vk == -1){
                // go left
                current = (current-rectangleCountY);
                if (current < 0){
                    current = temp_head;
                    collide();
                }

            } else if (vk == 2){
                // go up
                if (upper_bounds.contains(current)){
                    current = temp_head;
                    collide();
                } else {
                    current = current - 1;
                }
            } else if (vk == -2){
                // go down
                if (lower_bounds.contains(current)){
                    current = temp_head;
                    collide();
                } else {
                    current = current + 1;
                }
            }
            if (current != start){
                //System.out.println("Not start");
                check_collision(current);
                ppane.check_crum(current);
            }
            if (snake_list.size() > 0){
                Collections.rotate(snake_list,1);
                snake_list.set(0,current);
            } else {
                snake_list.add(current);
            }
            fill.clear();
            for (int joint : snake_list){
                fill.add(grid.get(joint));
            }
            repaint();
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public void play(){
        setTitle("Dini's Snake Game");
        vk = 0;
        current = start;
        fill.add(grid.get(start));
        crums.add(grid.get(crum.get_location()));
        repaint();
        game_loop();
    }
    public static void main(String[] args) {
        Game game = new Game();
        game.play();
        
    }    
}
