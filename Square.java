/**
 *  Represents 1 of the 9 squares on the game grid.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 *
 *  @author gracejiang
 *  @version May 8, 2021
 */
public class Square
{
    /**
     * Denotes which of the 9 squares this one is.
     * The squares are numbered left to right, starting from the top left:
     *
     * <br>[1] [2] [3]
     * <br>[4] [5] [6]
     * <br>[7] [8] [9]
     */
    public final int number;

    /**
     * This "priority number" comes from the number of
     *      winning combinations this square is a part of.
     */
    private int priorityNum;

    /**
     * Create a new Square.
     * @param num used to identify which square this is
     */
    public Square(int num)
    {
        number = num;
        if (number == 5)
        {
            priorityNum = 4;
        }
        else if (number == 1 || number == 3 || number == 7 || number == 9)
        {
            priorityNum = 3;
        }
        else
        {
            priorityNum = 2;
        }
    }

    /**
     * Returns this square's priority number.
     * The more wins associated with a square, the higher its number will be
     * @return priority number
     */
    public int getPriority()
    {
        return priorityNum;
    }

    /****** FOR TESTING BELOW ******/

//    /**
//     * For testing
//     * {@inheritDoc}
//     */
//    public String toString()
//    {
//        return number + "";
//    }
}
