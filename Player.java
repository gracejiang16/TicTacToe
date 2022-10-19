import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *  This class represents both the human and computer players.
 *  The JLabel text updates according to which player chooses the square.
 *
 *  @author gracejiang
 *  @version May 3, 2021
 */
public class Player
    extends JLabel
{

    /**
     * Create a new Player to add onto the board.
     */
    public Player()
    {
        super("");
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setFont(new Font("Helvetica", Font.PLAIN, 90));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
}
