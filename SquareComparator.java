import java.util.Comparator;

/**
 *  Comparator for the 9 squares on the game board.
 *  The square with the bigger priority is favored by the computer.
 *
 *  @author gracejiang
 *  @version May 8, 2021
 */
public class SquareComparator implements Comparator<Square>
{

    /**
     * The square with the bigger priority is favored by the computer.
     */
    @Override
    public int compare(Square s1, Square s2)
    {
        if (s1.getPriority() < s2.getPriority())
        {
            return 1;
        }
        else if (s1.getPriority() > s2.getPriority())
        {
            return -1;
        }
        return 0;
    }

}
