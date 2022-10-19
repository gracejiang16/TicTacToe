import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *  Main Menu for the game. There are three levels of game-play: easy, medium, and unbeatable.
 *
 *  @author gracejiang
 *  @version May 3, 2021
 */
public class MainMenu extends JFrame implements ActionListener
{
    private JButton normal;
    private JButton unbeat;
    private JLabel intro;

    /**
     * The main menu GUI is here.
     *  There are two buttons for a normal and unbeatable level.
     */
    public MainMenu()
    {
        setSize(600, 600);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.PINK);

        normal = new JButton("Normal");
        normal.addActionListener(this);
        normal.setFont(new Font("Comic Sans", Font.PLAIN, 20));
        normal.setBounds(150, 275, 300, 100);
        unbeat = new JButton("Unbeatable!");
        unbeat.addActionListener(this);
        unbeat.setFont(new Font("Comic Sans", Font.PLAIN, 20));
        unbeat.setBounds(150, 400, 300, 100);

        intro = new JLabel("<html>Welcome to TicTacToe!<br><br>"
            + "You can choose a normal or unbeatable level to play against "
            + "the computer!<br><br>"
            + "You are \"X\" and the computer is \"O\"</html>");
        intro.setFont(new Font("Comic Sans", Font.PLAIN, 20));
        intro.setBounds(120, 100, 400, 200);

        // adding components
        add(intro);
        add(normal);
        add(unbeat);
    }

    /**
     * (catches button actions)
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        JButton button = (JButton) e.getSource();
        if (button == normal)
        {
            this.dispose();
            (new GameRunnerNormal()).setVisible(true);
        }
        else if (button == unbeat)
        {
            this.dispose();
            (new GameRunnerMM()).setVisible(true);
        }
    }

    /****** the game starts below ******/

    /**
     * Main method
     * @param args args
     */
    public static void main(String[] args)
    {
        MainMenu m = new MainMenu();
        m.setVisible(true);
    }
}
