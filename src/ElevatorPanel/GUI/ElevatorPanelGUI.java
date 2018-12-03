package ElevatorPanel.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ElevatorPanelGUI {
    private JPanel panel;
    private JComboBox elevatorComboBox;
    private JLabel posLabel, dirLabel, schLabel;
    int totalElevator = 0;

    public ElevatorPanelGUI(int tmpTotalElevator){
        totalElevator = tmpTotalElevator;
        elevatorComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(elevatorComboBox.getSelectedItem());
            }
        });
    }

    public void start(){
        for(int i = 0; i<totalElevator; i++){
            elevatorComboBox.addItem(i);
        }

        posLabel.setText("10");
        dirLabel.setText("▲▼");
        schLabel.setText("14 20 30");

        JFrame frame = new JFrame("ElevatorPanel");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(700,100);
        frame.setVisible(true);
    }
}
