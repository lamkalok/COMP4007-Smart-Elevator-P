package ElevatorPanel.GUI;

import ElevatorPanel.ElevatorPanel;
import ElevatorPanel.misc.AppThread;
import ElevatorPanel.misc.Msg;
import ElevatorPanel.server.ClientHandler;
import ElevatorPanel.timer.Timer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;


public class ElevatorPanelGUI extends AppThread {
    private JPanel panel;
    private JComboBox elevatorComboBox;
    private JLabel posLabel, dirLabel, schLabel;
    int totalElevator = 0;
    private double sleepTime = 1;
    private String id;
    private String selectedLift = "A";

    public ElevatorPanelGUI(String id, ElevatorPanel elevatorPanel){
        super(id, elevatorPanel);
//        elevatorComboBox = new JComboBox();
//        panel = new JPanel();
//        posLabel = new JLabel();
//        dirLabel = new JLabel();
//        schLabel = new JLabel();
        totalElevator = elevatorPanel.getNElevators();
        this.id = id;
        elevatorComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(elevatorComboBox.getSelectedItem());
                selectedLift = elevatorComboBox.getSelectedItem().toString();
            }
        });
        start();

    }

    public void start(){

        for(int i = 0, ASCII = 65; i<totalElevator; i++, ASCII++){
            elevatorComboBox.addItem((char)ASCII);
        }

        posLabel.setText(elevatorPanel.getProperty("Bldg.MinFloorNumber"));
        dirLabel.setText("S");
        schLabel.setText("");

        this.sleepTime = Double.parseDouble(elevatorPanel.getProperty("Timer.MSecPerTick"));

        JFrame frame = new JFrame("ElevatorPanel");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(700,100);
        frame.setVisible(true);

    }

    public void run() {
        log.info(id + ": starting...");
        Timer.setSimulationTimer(id, mbox, sleepTime);

        for (boolean quit = false; !quit;) {


            Msg msg = mbox.receive();
            //log.info(id + ": message received: [" + msg + "].");

            switch (msg.getType()) {
                case TimesUp:
                    Timer.setSimulationTimer(id, mbox, sleepTime);
                    elevatorPanel.sendMsgToElevatorServer("GUI_Req " + selectedLift);

                    //log.info(id + ": receiving times up at " + appKickstarter.getSimulationTimeStr());
                    //table.getModel().setValueAt("HELLO" + mCnt++ , 2 ,0);
                    break;
                case GUI_Reply:

                    String[] message_data = msg.getDetails().split(" ");

                    String liftNumber = message_data[1];
                    String currentPosition = message_data[2];
                    String dir = message_data[3];
                    String schedule = "";

                    if(message_data.length > 4){
                        for(int i = 4 ; i < message_data.length; i++){
                            schedule += " " + message_data[i];
                        }
                    }

                    posLabel.setText(currentPosition);
                    dirLabel.setText(dir);
                    schLabel.setText(schedule);

                    break;
            }

        }



        // declaring our departure
        elevatorPanel.unregThread(this);
        log.info(id + ": terminating...");
    } // run
}
