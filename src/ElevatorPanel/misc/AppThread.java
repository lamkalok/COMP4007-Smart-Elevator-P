package ElevatorPanel.misc;


import ElevatorPanel.ElevatorPanel;

import java.util.logging.Logger;


//======================================================================
// AppThread
public abstract class AppThread implements Runnable {
    protected String id;
    protected ElevatorPanel elevatorPanel;
    //protected MBox mbox = null;
    protected Logger log = null;

    //------------------------------------------------------------
    // AppThread
    public AppThread(String id, ElevatorPanel elevatorPanel) {
        this.id = id;
        this.elevatorPanel = elevatorPanel;
        //log = elevatorPanel.getLogger();
       // mbox = new MBox(id, log);
        //elevatorPanel.regThread(this);
        log.fine(id + ": created!");
    } // AppThread


    //------------------------------------------------------------
    // getters

    //public MBox getMBox() { return mbox; }
    public String getID() { return id; }
} // AppThread
