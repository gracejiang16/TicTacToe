import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;

/**
 *  This is the GUI and game logstics for the unbeatable level.
 *  This level uses an unbeatable AI, so the player can never win.
 *
 *  @author gracejiang
 *  @version May 12, 2021
 */
public class GameRunnerMM extends JFrame implements MouseListener, ActionListener
{
    private int[][] board; // represents current game. holds 1 for human, -1 for computer, 0 for empty
    private JLabel[][] tileReferences; // hold references to all squares so we can update them
    private boolean aiIsThinking;
    private boolean gameOver;
    private AIMM ai;

    private JButton mainMenuButton;
    private JButton normalButton;

    private JMenuBar menuBar;

    /**
     * Create a new GameRunnerNormal object. Makes the window for the game.
     */
    public GameRunnerMM()
    {
        super("Unbeatable Mode");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new GridLayout(3, 3));
        setSize(600, 600);

        mainMenuButton = new JButton("Main Menu");
        mainMenuButton.addActionListener(this);
        normalButton = new JButton("Normal");
        normalButton.addActionListener(this);

        menuBar = new JMenuBar();
        menuBar.add(mainMenuButton);
        menuBar.add(normalButton);

        setJMenuBar(menuBar);

        board = new int[3][3];
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                board[i][j] = 0;
            }
        }

        addMouseListener(this);

        // actual game things:

        tileReferences = new JLabel[3][3];
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                Player p = new Player();
                add(p);
                tileReferences[i][j] = p;
            }
        }
        ai = new AIMM(board);
        aiIsThinking = false;
        gameOver = false;

        Thread t = new Thread()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    yield();

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
                        aiMove();
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
        if (gameOverChecker[0] == 1)
        {
            return; // if there's already a win
        }

        int[] humanCoors = getXYCoorsOnBoard(getTileNumberFromMouseCoors(x, y));
        if (board[humanCoors[0]][humanCoors[1]] != 0)
        {
            return; // human cannot pick occupied spot
        }

        (tileReferences[humanCoors[0]][humanCoors[1]]).setText("X");
        board[humanCoors[0]][humanCoors[1]] = 1;

        gameOverChecker = gameOver();
        if (gameOverChecker[0] == 1 || gameOverChecker[0] == -2)
        {
//            if (gameOverChecker[1] == 1) // human won (not possible)
//            {
//                paintWinner(gameOverChecker[2], gameOverChecker[3], gameOverChecker[4], 1);
//            }
            if (gameOverChecker[1] == -1) // computer won
            {
                paintWinner(gameOverChecker[2], gameOverChecker[3], gameOverChecker[4], -1);
            }
            else // tie
            {
                paintWinner(0, 0, 0, 0);
            }
            return;
        }

        aiIsThinking = true;
    }

    /**
     * The computer chooses the best move possible on current board by calling
     * AI's getBestMove method.
     */
    public void aiMove()
    {
        int[] computerCoors = ai.getBestMove(board);
        tileReferences[computerCoors[0]][computerCoors[1]].setText("O");
        board[computerCoors[0]][computerCoors[1]] = -1; // computer is -1, human is 1

        int[] gameOverChecker = gameOver();
        if (gameOverChecker[0] == 1 || gameOverChecker[0] == -2)
        {
//            if (gameOverChecker[1] == 1) // human won (not possible)
//            {
//                System.out.println("human no more: human");
//                paintWinner(gameOverChecker[2] - 1, gameOverChecker[3] - 1, gameOverChecker[4] - 1, 1);
//            }
            if (gameOverChecker[1] == -1) // computer won
            {
                paintWinner(gameOverChecker[2] - 1, gameOverChecker[3] - 1, gameOverChecker[4] - 1, -1);
            }
            else // tie
            {
                paintWinner(0, 0, 0, 0);
            }
        }
    }

    /**
     * Changes the color of the Xs and Os to signify the winner.
     * Red for computer, green for human, gray for tie.
     * @param x first square
     * @param y second square
     * @param z third square
     * @param who 1 for human, -1 for computer, 0 for tie
     */
    private void paintWinner(int x, int y, int z, int who)
    {
        gameOver = true;
        int[] xCoors = getXYCoorsOnBoard(x + 1);
        int xX = xCoors[0];
        int xY = xCoors[1];
        int[] yCoors = getXYCoorsOnBoard(y + 1);
        int yX = yCoors[0];
        int yY = yCoors[1];
        int[] zCoors = getXYCoorsOnBoard(z + 1);
        int zX = zCoors[0];
        int zY = zCoors[1];

//        if (who == 1) // human (not possible)
//        {
//            tileReferences[xX][xY].setForeground(Color.GREEN);
//            tileReferences[yX][yY].setForeground(Color.GREEN);
//            tileReferences[zX][zY].setForeground(Color.GREEN);
//        }
        if (who == -1) // computer
        {
            tileReferences[xX][xY].setForeground(Color.RED);
            tileReferences[yX][yY].setForeground(Color.RED);
            tileReferences[zX][zY].setForeground(Color.RED);
        }
        else if (who == 0) // tie
        {
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    tileReferences[i][j].setForeground(Color.GRAY);
                }
            }
        }
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
     * (catches button actions to change pages)
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
        else if (button == normalButton)
        {
            this.dispose();
            (new GameRunnerNormal()).setVisible(true);
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
        if (board[0][0] + board[0][1] + board[0][2] == 3) // 123
        {
            result[0] = 1;
            result[1] = 1;
            result[2] = 1;
            result[3] = 2;
            result[4] = 3;
        }
        // check for computer wins (no need to check for player wins)
        else if (board[0][0] + board[0][1] + board[0][2] == -3) // 123
        {
            result[0] = 1;
            result[1] = -1;
            result[2] = 1;
            result[3] = 2;
            result[4] = 3;
            return result;
        }
        else if (board[1][0] + board[1][1] + board[1][2] == -3) // 456
        {
            result[0] = 1;
            result[1] = -1;
            result[2] = 4;
            result[3] = 5;
            result[4] = 6;
        }
        else if (board[2][0] + board[2][1] + board[2][2] == -3) // 789
        {
            result[0] = 1;
            result[1] = -1;
            result[2] = 7;
            result[3] = 8;
            result[4] = 9;
        }
        else if (board[0][0] + board[1][0] + board[2][0] == -3) // 147
        {
            result[0] = 1;
            result[1] = -1;
            result[2] = 1;
            result[3] = 4;
            result[4] = 7;
        }
        else if (board[0][1] + board[1][1] + board[2][1] == -3) // 258
        {
            result[0] = 1;
            result[1] = -1;
            result[2] = 2;
            result[3] = 5;
            result[4] = 8;
        }
        else if (board[0][2] + board[1][2] + board[2][2] == -3) // 369
        {
            result[0] = 1;
            result[1] = -1;
            result[2] = 3;
            result[3] = 6;
            result[4] = 9;
        }
        else if (board[0][0] + board[1][1] + board[2][2] == -3) // 159
        {
            result[0] = 1;
            result[1] = -1;
            result[2] = 1;
            result[3] = 5;
            result[4] = 9;
        }
        else if (board[0][2] + board[1][1] + board[2][0] == -3) // 357
        {
            result[0] = 1;
            result[1] = -1;
            result[2] = 3;
            result[3] = 5;
            result[4] = 7;
        }
        else // check for tie/full board & check for no win and continue game
        {
            if (ai.existsMoreMoves(board))
            {
                result[0] = 0;
            }
            else // exists no more moves => tie
            {
                result[0] = -2;
            }
        }
        return result;
    }

    /**
     * Takes the x and y click coordinates and returns which square that is
     * @param x x coor
     * @param y y coor
     * @return chosen square number (1 - 9)
     */
    private int getTileNumberFromMouseCoors(int x, int y)
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

    private int[] getXYCoorsOnBoard(int n)
    {
        int[] coors = new int[2];

        if (n == 1)
        {
            coors[0] = 0;
            coors[1] = 0;
        }
        else if (n == 2)
        {
            coors[0] = 0;
            coors[1] = 1;
        }
        else if (n == 3)
        {
            coors[0] = 0;
            coors[1] = 2;
        }
        else if (n == 4)
        {
            coors[0] = 1;
            coors[1] = 0;
        }
        else if (n == 5)
        {
            coors[0] = 1;
            coors[1] = 1;
        }
        else if (n == 6)
        {
            coors[0] = 1;
            coors[1] = 2;
        }
        else if (n == 7)
        {
            coors[0] = 2;
            coors[1] = 0;
        }
        else if (n == 8)
        {
            coors[0] = 2;
            coors[1] = 1;
        }
        else if (n == 9)
        {
            coors[0] = 2;
            coors[1] = 2;
        }

        return coors;
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

    /****** FOR TESTING BELOW ******/

//    /**
//     * main method; where the game runs
//     * @param args args
//     */
//    public static void main(String[] args)
//    {
//        GameRunnerMM game = new GameRunnerMM();
//        game.setVisible(true);
//    }
}
