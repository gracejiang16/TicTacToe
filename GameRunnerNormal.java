import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

/**
 *  This is where the GUI and some of the game logistics are.
 *  The normal level does not cover all possible game outcomes so the player can win sometimes.
 *
 *  @author gracejiang
 *  @version May 3, 2021
 */
public class GameRunnerNormal extends JFrame implements MouseListener, ActionListener
{

    private JButton mainMenuButton;
    private JButton unbeatButton;

    private int[] board; // holds 1 for human, -1 for computer, 0 for empty
    private JLabel[] tileReferences; // hold references to all squares so we can update them
    private AI ai;

    private boolean aiIsThinking;
    private boolean gameOver;
    private int humanCurrentChoice;

    private JMenuBar menuBar;

    /**
     * Create a new GameRunnerNormal object. Makes the window for the game.
     */
    public GameRunnerNormal()
    {
        super("Normal Mode");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new GridLayout(3, 3));
        setSize(600, 600);

        mainMenuButton = new JButton("Main Menu");
        mainMenuButton.addActionListener(this);
        unbeatButton = new JButton("Unbeatable!");
        unbeatButton.addActionListener(this);

        menuBar = new JMenuBar();
        menuBar.add(mainMenuButton);
        menuBar.add(unbeatButton);

        setJMenuBar(menuBar);

        board = new int[9];
        for (int i = 0; i < 9; i++)
        {
            board[i] = 0; // default empty board
        }

        addMouseListener(this);

        // actual game things:
        tileReferences = new JLabel[9];
        for (int i = 0; i < 9; i++)
        {
            Player p = new Player();
            add(p);
            tileReferences[i] = p;
        }
        ai = new AI(board);
        aiIsThinking = false;
        gameOver = false;

        // create a thread that constantly checks if aiIsThinking.
        // That way, the computer can appear to "think" for 1 second.
        Thread t = new Thread()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    yield(); // yields the CPU
                    if (aiIsThinking)
                    {
                        try
                        {
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        aiMove(humanCurrentChoice);
                        aiIsThinking = false;
                    }
                    if (gameOver)
                    {
                        break;
                    }
                }
            }
        };
        t.start();
    }

    /**
     * {@inheritDoc}
     * (we only use this for the player to choose his position)
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        int x = e.getX();
        int y = e.getY();

        humanMove(x, y);
    }

    /**
     * (this catches button clicks to change pages)
     * {@inheritDoc}
     */
    public void actionPerformed(ActionEvent e)
    {
        JButton button = (JButton) e.getSource();
        if (button == mainMenuButton)
        {
            this.dispose();
            (new MainMenu()).setVisible(true);
        }
        else if (button == unbeatButton)
        {
            this.dispose();
            (new GameRunnerMM()).setVisible(true);
        }
    }

    /**
     * The human chooses a spot, which is represented by x and y.
     * After the human goes (if it was a valid move),
     * the aiMove method is called and the computer moves.
     * @param x mouse x coordinate
     * @param y mouse y coordinate
     */
    public void humanMove(int x, int y)
    {
        if (aiIsThinking)
        {
            return;
        }

        int[] gameOverChecker = gameOver();
        if (gameOverChecker[0] != 0)
        {
            return;
        }

        int humansChoice = getTileNumber(x, y);
        if (board[humansChoice - 1] != 0)
        {
            return; // human can't pick occupied squares
        }

        tileReferences[humansChoice - 1].setText("X");
        board[humansChoice - 1] = 1;

        gameOverChecker = gameOver();
        if (gameOverChecker[0] == 1 || gameOverChecker[0] == -2)
        {
            if (gameOverChecker[1] == 1) // human won
            {
                paintWinner(gameOverChecker[2] - 1, gameOverChecker[3] - 1, gameOverChecker[4] - 1, 1);
            }
            else if (gameOverChecker[1] == -1) // computer won
            {
                paintWinner(gameOverChecker[2] - 1, gameOverChecker[3] - 1, gameOverChecker[4] - 1, -1);
            }
            else // tie
            {
                paintWinner(0, 0, 0, 0);
            }
            return;
        }

        humanCurrentChoice = humansChoice;
        aiIsThinking = true;
    }

    /**
     * The computer chooses the best move possible on current board
     * @param humanChoice the human's previous square choice
     */
    public void aiMove(int humanChoice)
    {
        int[] gameOverChecker = gameOver();
        if (gameOverChecker[0] == 1 || gameOverChecker[0] == -2)
        {
            System.out.println("game over");
            gameOver = true;
            return;
        }

        int computersChoice = ai.chooseMove(humanChoice);

        tileReferences[computersChoice - 1].setText("O");

        // now check if the game is over again
        gameOverChecker = gameOver();
        if (gameOverChecker[0] == 1 || gameOverChecker[0] == -2)
        {
            if (gameOverChecker[1] == 1) // human won
            {
                paintWinner(gameOverChecker[2] - 1, gameOverChecker[3] - 1, gameOverChecker[4] - 1, 1);
            }
            else if (gameOverChecker[1] == -1) // computer won
            {
                paintWinner(gameOverChecker[2] - 1, gameOverChecker[3] - 1, gameOverChecker[4] - 1, -1);
            }
            else // tie
            {
                paintWinner(0, 0, 0, 0);
            }
            return;
        }
    }

    /**
     * Checks current board for win.
     * @return int array with result[0] = 1 for win exists, 0 for no win and continue, -2 for tie;
     *      result[1] = -1 for computer's win, 1 for human's win.
     *      The last three elements are the square numbers for the win if there is one.
     */
    private int[] gameOver()
    {
        int[] result = new int[5];
        if (board[1 - 1] + board[2 - 1] + board[3 - 1] == 3)//
        {
            result[0] = 1;
            result[1] = 1;
            result[2] = 1;
            result[3] = 2;
            result[4] = 3;
        }
        else if (board[1 - 1] + board[2 - 1] + board[3 - 1] == -3)//
        {
            result[0] = 1;
            result[1] = -1;
            result[2] = 1;
            result[3] = 2;
            result[4] = 3;
        }

        if (board[4 - 1] + board[5 - 1] + board[6 - 1] == 3)//
        {
            result[0] = 1;
            result[1] = 1;
            result[2] = 4;
            result[3] = 5;
            result[4] = 6;
        }
        else if (board[4 - 1] + board[5 - 1] + board[6 - 1] == -3)//
        {
            result[0] = 1;
            result[1] = -1;
            result[2] = 4;
            result[3] = 5;
            result[4] = 6;
        }

        if (board[7 - 1] + board[8 - 1] + board[9 - 1] == 3)//
        {
            result[0] = 1;
            result[1] = 1;
            result[2] = 7;
            result[3] = 8;
            result[4] = 9;
        }
        else if (board[7 - 1] + board[8 - 1] + board[9 - 1] == -3)//
        {
            result[0] = 1;
            result[1] = -1;
            result[2] = 7;
            result[3] = 8;
            result[4] = 9;
        }

        if (board[1 - 1] + board[4 - 1] + board[7 - 1] == 3)//
        {
            result[0] = 1;
            result[1] = 1;
            result[2] = 1;
            result[3] = 4;
            result[4] = 7;
        }
        else if (board[1 - 1] + board[4 - 1] + board[7 - 1] == -3)//
        {
            result[0] = 1;
            result[1] = -1;
            result[2] = 1;
            result[3] = 4;
            result[4] = 7;
        }

        if (board[2 - 1] + board[5 - 1] + board[8 - 1] == 3)//
        {
            result[0] = 1;
            result[1] = 1;
            result[2] = 2;
            result[3] = 5;
            result[4] = 8;
        }
        else if (board[2 - 1] + board[5 - 1] + board[8 - 1] == -3)//
        {
            result[0] = 1;
            result[1] = -1;
            result[2] = 2;
            result[3] = 5;
            result[4] = 8;
        }

        if (board[3 - 1] + board[6 - 1] + board[9 - 1] == 3)//
        {
            result[0] = 1;
            result[1] = 1;
            result[2] = 3;
            result[3] = 6;
            result[4] = 9;
        }
        else if (board[3 - 1] + board[6 - 1] + board[9 - 1] == -3)//
        {
            result[0] = 1;
            result[1] = -1;
            result[2] = 3;
            result[3] = 6;
            result[4] = 9;
        }

        if (board[1 - 1] + board[5 - 1] + board[9 - 1] == 3)//
        {
            result[0] = 1;
            result[1] = 1;
            result[2] = 1;
            result[3] = 5;
            result[4] = 9;
        }
        else if (board[1 - 1] + board[5 - 1] + board[9 - 1] == -3)//
        {
            result[0] = 1;
            result[1] = -1;
            result[2] = 1;
            result[3] = 5;
            result[4] = 9;
        }

        if (board[3 - 1] + board[5 - 1] + board[7 - 1] == 3)//
        {
            result[0] = 1;
            result[1] = 1;
            result[2] = 3;
            result[3] = 5;
            result[4] = 7;
        }
        else if (board[3 - 1] + board[5 - 1] + board[7 - 1] == -3)//
        {
            result[0] = 1;
            result[1] = -1;
            result[2] = 3;
            result[3] = 5;
            result[4] = 7;
        }
        else if (boardIsFullAndTie())
        {
            result[0] = -2;
            result[1] = 0;
            result[2] = 0;
            result[3] = 0;
            result[4] = 0;
        }
        return result;
    }

    /**
     * checks that the board is full
     * @return true if full, false otherwise
     */
    private boolean boardIsFullAndTie()
    {
        boolean isFull = true;

        for (int i = 0 ; i < 9; i++)
        {
            if (board[i] == 0)
            {
                return false;
            }
        }

        boolean isTie = !( (board[1 - 1] + board[2 - 1] + board[3 - 1] == 3)
            || (board[1 - 1] + board[2 - 1] + board[3 - 1] == -3)
            || (board[4 - 1] + board[5 - 1] + board[6 - 1] == 3)
            || (board[4 - 1] + board[5 - 1] + board[6 - 1] == -3)
            || (board[7 - 1] + board[8 - 1] + board[9 - 1] == 3)
            || (board[7 - 1] + board[8 - 1] + board[9 - 1] == -3)
            || (board[1 - 1] + board[4 - 1] + board[7 - 1] == 3)
            || (board[1 - 1] + board[4 - 1] + board[7 - 1] == -3)
            || (board[2 - 1] + board[5 - 1] + board[8 - 1] == 3)
            || (board[2 - 1] + board[5 - 1] + board[8 - 1] == -3)
            || (board[3 - 1] + board[6 - 1] + board[9 - 1] == 3)
            || (board[3 - 1] + board[6 - 1] + board[9 - 1] == -3)
            || (board[1 - 1] + board[5 - 1] + board[9 - 1] == 3)
            || (board[1 - 1] + board[5 - 1] + board[9 - 1] == -3)
            || (board[3 - 1] + board[5 - 1] + board[7 - 1] == 3)
            || (board[3 - 1] + board[5 - 1] + board[7 - 1] == -3) );

        return isFull && isTie;
    }

    private void paintWinner(int x, int y, int z, int who)
    {
        gameOver = true;
        if (who == 1) // human
        {
            tileReferences[x].setForeground(Color.GREEN);
            tileReferences[y].setForeground(Color.GREEN);
            tileReferences[z].setForeground(Color.GREEN);
        }
        else if (who == -1) // computer
        {
            tileReferences[x].setForeground(Color.RED);
            tileReferences[y].setForeground(Color.RED);
            tileReferences[z].setForeground(Color.RED);
        }
        else if (who == 0) // tie
        {
            for (int i = 0; i < 9; i++)
            {
                tileReferences[i].setForeground(Color.GRAY);
            }
        }

    }

    /**
     * Takes the x and y click coordinates and returns which square that is
     * @param x x coor
     * @param y y coor
     * @return chosen square number
     */
    private int getTileNumber(int x, int y)
    {
        int humansChoice;
        if (y > 0 && y < 200)
        {
            if (x > 0 && x < 200)
            {
                humansChoice = 1;
            }
            else if (x > 200 && x < 400)
            {
                humansChoice = 2;
            }
            else
            {
                humansChoice = 3;
            }
        }
        else if (y > 200 && y < 400)
        {
            if (x > 0 && x < 200)
            {
                humansChoice = 4;
            }
            else if (x > 200 && x < 400)
            {
                humansChoice = 5;
            }
            else
            {
                humansChoice = 6;
            }
        }
        else
        {
            if (x > 0 && x < 200)
            {
                humansChoice = 7;
            }
            else if (x > 200 && x < 400)
            {
                humansChoice = 8;
            }
            else
            {
                humansChoice = 9;
            }
        }
        return humansChoice;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mousePressed(MouseEvent e)
    {/*nothing*/}

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseReleased(MouseEvent e)
    {/*nothing*/}

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseEntered(MouseEvent e)
    {/*nothing*/}

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseExited(MouseEvent e)
    {/*nothing*/}

    public String toString()
    {
        return "\n" + board[0] + " " + board[1] + " " + board[2] + " \n"
            + board[3] + " " + board[4] + " " + board[5] + " \n"
            + board[6] + " " + board[7] + " " + board[8] + " ";
    }

    /****** FOR TESTING BELOW ******/

//    /**
//     * main method; where the game runs
//     * @param args args
//     */
//    public static void main(String[] args)
//    {
//        GameRunnerNormal game = new GameRunnerNormal();
//        game.setVisible(true);
//    }
}
