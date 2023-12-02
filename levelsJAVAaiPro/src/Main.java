
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main extends JFrame implements ActionListener {
    JButton button1, button2;

    public Main() {
        super("Tic Tac Toe");
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        button1 = new JButton("1 players (you VS the computer)");
        button2 = new JButton("2 player (you VS another player)");

        button1.setFont(new Font("Arial", Font.PLAIN, 15));
        button2.setFont(new Font("Arial", Font.PLAIN, 15));

        button1.setBackground(Color.white);
        button2.setBackground(Color.white);

        button1.setForeground(Color.black);
        button2.setForeground(Color.black);

        Dimension d = new Dimension(350,50);
        button1.setPreferredSize(d);
        button2.setPreferredSize(d);

        button1.addActionListener(this);
        button2.addActionListener(this);

        JPanel panel = new JPanel();
        panel.add(button1);
        panel.add(button2);

        getContentPane().add(panel, BorderLayout.CENTER);
        setVisible(true);

        //adjusting the butons location on the frame
        Dimension size1 = button1.getPreferredSize();
        button1.setBounds(50, 145, size1.width, size1.height);
        panel.setLayout(null);
        panel.add(button1);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Dimension size2 = button2.getPreferredSize();
        button2.setBounds(50, 200, size2.width, size2.height);
        panel.setLayout(null);
        panel.add(button2);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button1) {
            new PC();
        } else if (e.getSource() == button2) {
            new humans();
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
