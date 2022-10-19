import java.util.*;

/**
 *  This class helps the computer choose the most optimal next move for the normal level.
 *
 *  @author gracejiang
 *  @version May 3, 2021
 */
public class AI
{
    private int[] board;
    private PriorityQueue<Square> pq;

    /**
     * Create a new AI helper
     * @param brd current state of the board
     */
    public AI(int[] brd)
    {
        board = brd;
        pq = new PriorityQueue<Square>(new SquareComparator());

        pq.add(new Square(1));
        pq.add(new Square(2));
        pq.add(new Square(3));
        pq.add(new Square(4));
        pq.add(new Square(5));
        pq.add(new Square(6));
        pq.add(new Square(7));
        pq.add(new Square(8));
        pq.add(new Square(9));
    }

    /**
     * Chooses most beneficial move for computer.
     * @param humanChoice human's previous choice,
     *      which is removed from the PQ so the computer can't choose it.
     * @return tile number for computer's move
     */
    public int chooseMove(int humanChoice)
    {
        Square toBeRemoved = null;
        for (Square s : pq) // find humanChoice and remove it from pq
        {
            if (s.number == humanChoice)
            {
                toBeRemoved = s;
            }
        }
        pq.remove(toBeRemoved);

        int computerChoice = 0;

        // first check for possible computer wins
        if (board[2 - 1] + board[3 - 1] == -2 ||
                board[4 - 1] + board[7 - 1] == -2 ||
                board[5 - 1] + board[9 - 1] == -2)
        {
            computerChoice = 1;
        }
        else if (board[1 - 1] + board[3 - 1] == -2 ||
                board[5 - 1] + board[8 - 1] == -2)
        {
            computerChoice = 2;
        }
        else if (board[1 - 1] + board[2 - 1] == -2 ||
            board[6 - 1] + board[9 - 1] == -2 ||
            board[5 - 1] + board[7 - 1] == -2)
        {
            computerChoice = 3;
        }
        else if (board[5 - 1] + board[6 - 1] == -2 ||
                board[1 - 1] + board[7 - 1] == -2)
        {
            computerChoice = 4;
        }
        else if (board[1 - 1] + board[9 - 1] == -2 ||
            board[7 - 1] + board[3 - 1] == -2 ||
            board[2 - 1] + board[8 - 1] == -2 ||
            board[4 - 1] + board[6 - 1] == -2)
        {
            computerChoice = 5;
        }
        else if (board[4 - 1] + board[5 - 1] == -2 ||
                board[3 - 1] + board[9 - 1] == -2)
        {
            computerChoice = 6;
        }
        else if (board[8 - 1] + board[9 - 1] == -2 ||
                board[1 - 1] + board[4 - 1] == -2 ||
                board[5 - 1] + board[3 - 1] == -2)
        {
            computerChoice = 7;
        }
        else if (board[7 - 1] + board[9 - 1] == -2 ||
            board[2 - 1] + board[5 - 1] == -2)
        {
            computerChoice = 8;
        }
        else if (board[7 - 1] + board[8 - 1] == -2 ||
            board[3 - 1] + board[6 - 1] == -2 ||
            board[1 - 1] + board[5 - 1] == -2)
        {
            computerChoice = 9;
        }

        // otherwise check to block possible human wins
        else if (board[2 - 1] + board[3 - 1] == 2 ||
            board[4 - 1] + board[7 - 1] == 2 ||
            board[5 - 1] + board[9 - 1] == 2)
        {
            computerChoice = 1;
        }

        else if (board[1 - 1] + board[3 - 1] == 2 ||
            board[5 - 1] + board[8 - 1] == 2)
        {
            computerChoice = 2;
        }
        else if (board[1 - 1] + board[2 - 1] == 2 ||
            board[6 - 1] + board[9 - 1] == 2 ||
            board[5 - 1] + board[7 - 1] == 2)
        {
            computerChoice = 3;
        }
        else if (board[5 - 1] + board[6 - 1] == 2 ||
            board[1 - 1] + board[7 - 1] == 2)
        {
            computerChoice = 4;
        }
        else if (board[1 - 1] + board[9 - 1] == 2 ||
            board[7 - 1] + board[3 - 1] == 2 ||
            board[2 - 1] + board[8 - 1] == 2 ||
            board[4 - 1] + board[6 - 1] == 2)
        {
            computerChoice = 5;
        }
        else if (board[4 - 1] + board[5 - 1] == 2 ||
            board[3 - 1] + board[9 - 1] == 2)
        {
            computerChoice = 6;
        }
        else if (board[8 - 1] + board[9 - 1] == 2 ||
            board[1 - 1] + board[4 - 1] == 2 ||
            board[5 - 1] + board[3 - 1] == 2)
        {
            computerChoice = 7;
        }
        else if (board[7 - 1] + board[9 - 1] == 2 ||
            board[2 - 1] + board[5 - 1] == 2)
        {
            computerChoice = 8;
        }
        else if (board[7 - 1] + board[8 - 1] == 2 ||
            board[3 - 1] + board[6 - 1] == 2 ||
            board[1 - 1] + board[5 - 1] == 2)
        {
            computerChoice = 9;
        }

        // next check for "forks"
        else if (board[2 - 1] + board[4 - 1] == -2)
        {
            computerChoice = 1;
        }
        else if (board[2 - 1] + board[6 - 1] == -2)
        {
            computerChoice = 3;
        }
        else if (board[6 - 1] + board[8 - 1] == -2)
        {
            computerChoice = 9;
        }
        else if (board[8 - 1] + board[4 - 1] == -2)
        {
            computerChoice = 7;
        }

        // last resort is choosing the best empty space from the priority queue
        if (computerChoice == humanChoice) // computer cannot choose human move
        {
            computerChoice = pq.remove().number;
        }
        if (computerChoice == 0) // make sure compChoice is something
        {
            computerChoice = pq.remove().number;
        }

        if (isTaken(computerChoice))
        {
            Square toBeRemoved2 = null;
            for (Square s : pq) // find humanChoice and remove it from pq
            {
                if (s.number == humanChoice)
                {
                    toBeRemoved2 = s;
                }
            }
            pq.remove(toBeRemoved2);
            computerChoice = pq.remove().number;
        }

        board[computerChoice - 1] = -1;
        return computerChoice;
    }

    /**
     * Checks if the computer chooses an occupied spot
     * @param compChoice square to check
     * @return true if board[compChoice - 1] == 1 or -1, false otherwise
     */
    private boolean isTaken(int compChoice)
    {
        return board[compChoice - 1] != 0;
    }

    /****** FOR TESTING BELOW ******/

//    public static void main(String[] args)
//    {
//        AI a = new AI(null);
//        PriorityQueue<Square> pq = a.pq;
//
//        while(!pq.isEmpty())
//        {
//            System.out.println(pq.remove());
//        }
//    }
}