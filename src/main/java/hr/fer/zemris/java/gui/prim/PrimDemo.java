package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import java.awt.*;

/**
 *  View of list of prim numbers
 */
public class PrimDemo extends JFrame {

    /**
     * Constructing new View
     */
    public PrimDemo() {
        super();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 200);
        initGUI();
    }

    /**
     * Initializing view components
     */
    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        PrimListModel model = new PrimListModel();

        JPanel p = new JPanel(new GridLayout(1, 0));

        JList<Integer> listLeft = new JList<>(model);
        JList<Integer> listRight = new JList<>(model);

        p.add(new JScrollPane(listLeft));
        p.add(new JScrollPane(listRight));

        cp.add(p, BorderLayout.CENTER);

        JButton nextPrimeButton = new JButton("Next prime");
        nextPrimeButton.addActionListener(l -> model.next());

        cp.add(nextPrimeButton, BorderLayout.PAGE_END);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new PrimDemo().setVisible(true);
        });
    }
}
