/**
 *  AI logic helper for the "unbeatable" level.
 *  This uses the Minimax algorithm, hence the AI"MM".
 *
 *  @author gracejiang
 *  @version May 11, 2021
 */
public class AIMM
{
    /**
     * Create a new AI helper that uses the minimax algorithm
     * @param board
     */
    public AIMM(int[][] board)
    {
        // nothing
    }

    /**
     * Returns best computer move using minimax algorithm.
     * @param brd current state of game
     * @return square number of chosen move
     */
    public int[] getBestMove(int[][] brd)
    {
        int maxValue = Integer.MIN_VALUE;
        int[] finalMove = {0, 0};

        for (int r = 0; r < 3; r++)
        {
            for (int c = 0; c < 3; c++)
            {
                if (brd[r][c] == 0)
                {
                    brd[r][c] = -1; // do the move
                    int hypotheticalBestValue = minimax(brd, 0, false);
                    brd[r][c] = 0; // undo the move

                    if (hypotheticalBestValue > maxValue)
                    {
                        maxValue = hypotheticalBestValue;
                        finalMove[0] = r;
                        finalMove[1] = c;
                    }
                }
            }
        }
        return finalMove;
    }

    /**
     * Evaluates a specific state of a game and assigns it a value
     * @param brd one state of the game
     * @return 10 for computer win, -10 for human win, 0 otherwise
     */
    public int value(int[][] brd)
    {
        if ((brd[0][0] + brd[0][1] + brd[0][2] == 3) || // 123
            (brd[1][0] + brd[1][1] + brd[1][2] == 3) || // 456
            (brd[2][0] + brd[2][1] + brd[2][2] == 3) || // 789
            (brd[0][0] + brd[1][0] + brd[2][0] == 3) || // 147
            (brd[0][1] + brd[1][1] + brd[2][1] == 3) || // 258
            (brd[0][2] + brd[1][2] + brd[2][2] == 3) || // 369
            (brd[0][0] + brd[1][1] + brd[2][2] == 3) || // 159
            (brd[0][2] + brd[1][1] + brd[2][0] == 3) // 357
            )
        {
            return -10; // negative bc the human won
        }
        else if ((brd[0][0] + brd[0][1] + brd[0][2] == -3) || // 123
                (brd[1][0] + brd[1][1] + brd[1][2] == -3) || // 456
                (brd[2][0] + brd[2][1] + brd[2][2] == -3) || // 789
                (brd[0][0] + brd[1][0] + brd[2][0] == -3) || // 147
                (brd[0][1] + brd[1][1] + brd[2][1] == -3) || // 258
                (brd[0][2] + brd[1][2] + brd[2][2] == -3) || // 369
                (brd[0][0] + brd[1][1] + brd[2][2] == -3) || // 159
                (brd[0][2] + brd[1][1] + brd[2][0] == -3) // 357
            )
        {
            return 10; // positive bc the computer won
        }

        return 0; // if no one won
    }

    /**
     * Recursively goes through each possible outcome and chooses best move for computer.
     * @param board1
     * @param depth
     * @param maximizingPlayer
     * @return a value that judges each possible outcome
     */
    public int minimax(int[][] board1, int depth, boolean maximizingPlayer)
    {
        int score = value(board1);

        if (score == 10 || score == -10)
        {
            return score; // if someone won already
        }

        if (!existsMoreMoves(board1))
        {
            return 0; // tie
        }

        if (depth == 9 /* BOARD IN TERMINAL STATE */)
        {
            return 0;
        }

        if (maximizingPlayer)
        {
            int currentMax = Integer.MIN_VALUE;

            for (int i = 0; i <3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    if (board1[i][j] == 0)
                    {
                        board1[i][j] = -1;
                        int newMax = minimax(board1, depth + 1, false);
                        currentMax = Math.max(currentMax, newMax);
                        board1[i][j] = 0;
                    }
                }
            }
            return currentMax;
        }

        else // if (!maximizingPlayer)
        {
            int currentMin = Integer.MAX_VALUE;
            for (int i = 0; i <3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    if (board1[i][j] == 0)
                    {
                        board1[i][j] = 1;
                        int newMin = minimax(board1, depth + 1, true);
                        currentMin = Math.min(currentMin, newMin);
                        board1[i][j] = 0;
                    }
                }
            }
            return currentMin;
        }
    }

    /**
     * Checks if there are any empty spots.
     * @param board1 current state of game
     * @return true if there is at least one empty spot, false otherwise
     */
    public boolean existsMoreMoves(int[][] board1)
    {
        for (int i = 0; i <3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                if (board1[i][j] == 0)
                {
                    return true;
                }
            }
        }
        return false;
    }
}