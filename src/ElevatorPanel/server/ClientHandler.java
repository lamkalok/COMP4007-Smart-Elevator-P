package ElevatorPanel.server;


import ElevatorPanel.misc.*;
import ElevatorPanel.ElevatorPanel;

import java.io.*;

public class ClientHandler extends AppThread {

    private InputStreamReader dis;
    private DataOutputStream dos;
    private BufferedReader br;

    public ClientHandler(String id, ElevatorPanel elevatorPanel, InputStreamReader dis, DataOutputStream dos) {
        super(id, elevatorPanel);
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {

        while (true) {

            try {

                // receive the answer from client
                br = new BufferedReader(dis);

                String receivedMsg = br.readLine();

                log.info("Message received from server is " + receivedMsg);

                // split out the message to get the detail data
                String[] message_data = receivedMsg.split(" ");

                String type = message_data[0];

                if(type.equals("GUI_Reply")){

                    elevatorPanel.getThread("ThreadElevatorPanelGUI").getMBox().send(new Msg(id, super.mbox, Msg.Type.GUI_Reply, receivedMsg));

                }



            } catch (IOException e) {
                e.printStackTrace();
                try {
                    br.close();
                    break;
                } catch (Exception ex) {

                }

            }
        }

    }
}