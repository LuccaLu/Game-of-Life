import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JFrame;

// Extends JFrame to create a window for the simulation
public class Sim453 extends JFrame{

    // Static variables to store command line arguments
    static int iterations;

    static char patternType;

    // Constructor for Sim453
    public Sim453(){
        // Add the main panel where the game will be displayed
        add(new LifePanel(iterations,patternType));

        // Set the size of the window and make it visible
        setSize(980,980);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Main method, the entry point of the program
    public static void main(String[] args) {
        // Check if the correct number of arguments are provided
        if (args.length < 2){
            System.out.println("Usage: java Sim453 <Iterations> <Pattern Type>");
            return;
        }

        // Parse the command line arguments
        iterations = Integer.parseInt(args[0]);
        patternType = args[1].charAt(0);

        // Validate the number of iterations
        if (iterations < 0){
            System.out.println("Iterations can not less than 0!");
            return;
        }

        // Create and display the main frame
        Sim453 frame = new Sim453();
        frame.setVisible(true);
    }
}

// JPanel class to handle the life simulation
class LifePanel extends JPanel implements ActionListener{

    // Panel size and cell size variables
    int xPanel = 980;
    int yPanel = 980;
    int size = 7;
    int xWidth = xPanel/size;
    int yHeight = yPanel/size;

    // Variables for the simulation
    int iterations;
    Random rand = new Random();
    int[][] life = new int[xWidth][yHeight];
    int[][] beforeLife = new int[xWidth][yHeight];
    boolean starts = true;

    // Constructor for LifePanel
    public LifePanel(int iterations, char patternType){
        this.iterations = iterations;
        setSize(980,980);
        setLayout(null);
        initializaCells(patternType);
        new Timer(120,this).start();
    }

    // Initialize cells based on the selected pattern type
    private void initializaCells(char patternType){
        switch (patternType){
            case 'R':
                // Random pattern initialization
                for (int x = 0; x < life.length; x++){
                    for (int y = 0; y < life.length; y++){
                        if ((int)(Math.random()*5) == 0){
                            beforeLife[x][y] = 1;
                        }
                    }
                }
                break;

            case 'B':
                // The By Flop oscillator initialization 
                int centerX = 5;
                int centerY = 5;

                beforeLife[centerX-1][centerY] = 1;
                beforeLife[centerX-2][centerY] = 1;

                beforeLife[centerX][centerY] = 1;
                beforeLife[centerX+1][centerY] = 1;
                beforeLife[centerX+2][centerY] = 1;

                beforeLife[centerX-1][centerY -2] = 1;
                beforeLife[centerX-1][centerY +2] = 1;

                beforeLife[centerX+1][centerY +2] = 1;
                beforeLife[centerX+1][centerY -2] = 1;
                beforeLife[centerX+1][centerY +3] = 1;
                beforeLife[centerX+1][centerY -3] = 1;

                beforeLife[centerX+3][centerY -1] = 1;
                beforeLife[centerX+3][centerY +1] = 1;

                break;

            case 'C':
                // The Crab glider initialization
                int startX = 12;
                int startY = 12;

                beforeLife[startX][startY] = 1;
                beforeLife[startX][startY + 1] = 1;
                beforeLife[startX][startY - 7] = 1;

                beforeLife[startX+ 1][startY - 1 ] = 1;
                beforeLife[startX+ 1][startY + 2] = 1;
                beforeLife[startX+ 1][startY - 7] = 1;
                beforeLife[startX+ 1][startY - 8] = 1;

                beforeLife[startX+ 2][startY - 1] = 1;
                beforeLife[startX+ 2][startY - 2] = 1;
                beforeLife[startX+ 2][startY - 6] = 1;
                beforeLife[startX+ 2][startY - 8] = 1;
                beforeLife[startX+ 2][startY + 1] = 1;

                beforeLife[startX+ 3][startY - 4] = 1;
                beforeLife[startX+ 4][startY - 5] = 1;

                beforeLife[startX+ 5][startY - 2] = 1;
                beforeLife[startX+ 5][startY - 5] = 1;

                beforeLife[startX- 2][startY + 2] = 1;
                beforeLife[startX- 2][startY + 3] = 1;
                beforeLife[startX- 3][startY + 2] = 1;
                beforeLife[startX- 3][startY + 3] = 1;

                beforeLife[startX- 5][startY + 1] = 1;
                beforeLife[startX- 5][startY - 1] = 1;
                beforeLife[startX- 6][startY - 1] = 1;
                beforeLife[startX- 6][startY] = 1;
                beforeLife[startX- 7][startY] = 1;

                break;

            case 'D':
                int playX = xWidth/2;
                int playY = yHeight/2;

                beforeLife[playX][playY] = 1;

                beforeLife[playX-1][playY] = 1;
                beforeLife[playX+1][playY] = 1;

                beforeLife[playX][playY-1] = 1;

                beforeLife[playX-1][playY+1] = 1;
                beforeLife[playX+1][playY+1] = 1;

                break;

            default:
                throw new IllegalArgumentException("Unknown Pattern Type!");

        }
    }

    // Method to paint the component
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        setBackground(Color.BLACK);
        grid(g);
        display(g);
    }

    // Draw the grid lines
    private void grid(Graphics g){

        g.setColor(Color.DARK_GRAY);

        for (int i = 0; i < life.length; i ++ ){
            g.drawLine(0,i*size,xPanel,i*size); //row
            g.drawLine(i*size,0,i*size,yPanel);//column
        }

    }

    // Display the cells
    private void display(Graphics g){
        g.setColor(Color.ORANGE);
        copyArray();

        for (int x = 0; x < life.length; x++) {
            for (int y = 0; y < life.length; y++) {

                if (life[x][y] == 1) {
                    g.fillRect(x * size, y * size, size, size);
                }
            }
        }
    }

    // Copy the array for the next iteration
    private void copyArray(){
        for (int x = 0; x < life.length; x++) {
            for (int y = 0; y < life.length; y++) {
                life[x][y] = beforeLife[x][y];
            }
        }
    }

    // Check the surrounding cells and return the number of live cells
    private int check(int x,int y){
        int alive = 0;
        alive += life[(x+xWidth-1) % xWidth][(y+yHeight-1) % yHeight];
        alive += life[(x+xWidth) % xWidth][(y+yHeight-1) % yHeight];

        alive += life[(x+xWidth+1) % xWidth][(y+yHeight-1) % yHeight];
        alive += life[(x+xWidth-1) % xWidth][(y+yHeight) % yHeight];

        alive += life[(x+xWidth+1) % xWidth][(y+yHeight) % yHeight];
        alive += life[(x+xWidth-1) % xWidth][(y+yHeight+1) % yHeight];

        alive += life[(x+xWidth) % xWidth][(y+yHeight+1) % yHeight];
        alive += life[(x+xWidth+1) % xWidth][(y+yHeight+1) % yHeight];

        return alive;
    }

    // Apply the game of life's rules
    public void actionPerformed(ActionEvent e){
        // Game logic for a single iteration
        for (int x = 0; x < life.length; x++) {
            for (int y = 0; y < life.length; y++) {
                int alive = check(x, y);

                if (alive == 3) {
                    beforeLife[x][y] = 1;
                } else if (alive == 2 && life[x][y] == 1) {
                    beforeLife[x][y] = 1;
                } else {
                    beforeLife[x][y] = 0;
                }

            }
        }
        copyArray();
        repaint();
        
        // Decrement the iterations count
        iterations--;
        
        // Stop the timer if iterations have completed
        if (iterations <= 0) {
            ((Timer) e.getSource()).stop();
        }
    }
}

