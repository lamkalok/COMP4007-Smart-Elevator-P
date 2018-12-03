package ElevatorPanel;

import ElevatorPanel.GUI.ElevatorPanelGUI;
import ElevatorPanel.misc.AppThread;
import ElevatorPanel.misc.LogFormatter;
import ElevatorPanel.server.ClientHandler;
import ElevatorPanel.timer.Timer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ElevatorPanel {

    private String cfgFName = null;
    private Properties cfgProps = null;
    private Hashtable<String, AppThread> appThreads = null;
    private String id = null;
    private Logger log = null;
    private ConsoleHandler logConHd = null;
    private FileHandler logFileHd = null;


    private int NElevators;


    private ServerSocket ss;
    private Socket s;
    private InputStreamReader dis;
    private DataOutputStream dos;

    private Timer timer = null;

    private ElevatorPanelGUI elevatorPanelGUI;

    private ClientHandler threadClientHandler;

    public static void main(String[] args) {

        ElevatorPanel elevatorPanel = new ElevatorPanel("ElevatorPanel", "etc/SmartElevator.cfg");
        elevatorPanel.startApp();
    }

    private ElevatorPanel(String id) {
        this(id, "etc/SmartElevator.cfg");
    }

    private ElevatorPanel(String id, String cfgFName) {
        this(id, cfgFName, false);
    }

    private ElevatorPanel(String id, String cfgFName, boolean append) {
        this.id = id;
        this.cfgFName = cfgFName;
        logConHd = null;
        logFileHd = null;
        id = getClass().getName();

        // set my thread name
        Thread.currentThread().setName(this.id);

        try {
            cfgProps = new Properties();
            FileInputStream in = new FileInputStream(cfgFName);
            cfgProps.load(in);
            in.close();
            logConHd = new ConsoleHandler();
            logConHd.setFormatter(new LogFormatter());
            logFileHd = new FileHandler(cfgProps.getProperty("SESvr.FileLogger"), append);
            logFileHd.setFormatter(new LogFormatter());
        } catch (FileNotFoundException e) {
            System.out.println("Failed to open config file (" + cfgFName + ").");
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("Error reading config file (" + cfgFName + ").");
            System.exit(-1);
        }

        // get and configure logger
        log = Logger.getLogger(id);
        log.addHandler(logConHd);
        log.addHandler(logFileHd);
        log.setUseParentHandlers(false);
        log.setLevel(Level.FINER);
        logConHd.setLevel(Level.parse(cfgProps.getProperty("SESvr.ConsLoggerLevel")));
        logFileHd.setLevel(Level.parse(cfgProps.getProperty("SESvr.FileLoggerLevel")));
        appThreads = new Hashtable<String, AppThread>();

    }

    private void startApp() {
// start our application
        log.info("");
        log.info("");
        log.info("============================================================");
        log.info(id + ": Application Starting...");

        log.info(id + "Server Port: " + cfgProps.getProperty("Server.Port"));
        int port = Integer.parseInt(cfgProps.getProperty("Server.Port"));
        String address = cfgProps.getProperty("Server.IP");
        NElevators = Integer.parseInt(cfgProps.getProperty("Bldg.NElevators"));

        timer = new Timer("timer", this);
        new Thread(timer).start();

        elevatorPanelGUI = new ElevatorPanelGUI("ThreadElevatorPanelGUI", this);
        new Thread(elevatorPanelGUI).start();
        System.out.println("Create");

        try {

            s = new Socket(address, port);

            dis = new InputStreamReader(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());



            // create ClientHandler object
            threadClientHandler = new ClientHandler("ThreadClientHandler", this, dis, dos);
            // Invoking the start() method
            new Thread(threadClientHandler).start();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public void sendMsgToElevatorServer(String msg){
        try{
            dos.flush();
            PrintWriter outMsg = new PrintWriter(dos, true);
            log.severe(id + " : message send to ElevatorServer: " + msg);
            outMsg.println(msg);
        } catch (Exception e){
            e.printStackTrace();
        }
    }



    private void stopApp() {
        log.info("");
        log.info("");
        log.info("============================================================");
        log.info(id + ": Application Stopping...");

        Set<String> keys = appThreads.keySet();
//        for(String key: keys){
//            appThreads.get(key).getMBox().send(new Msg(id, null, Msg.Type.Terminate, "Terminate now!"));
//        }

    } // stopApp


    //------------------------------------------------------------
    // regThread
    public void regThread(AppThread appThread) {
        log.fine(id + ": registering " + appThread.getID());
        synchronized (appThreads) {
            appThreads.put(appThread.getID(), appThread);
        }
    } // regThread


    //------------------------------------------------------------
    // unregThread
    public void unregThread(AppThread appThread) {
        log.fine(id + ": unregistering " + appThread.getID());
        synchronized (appThreads) {
            appThreads.remove(appThread.getID());
        }
    } // unregThread


    //------------------------------------------------------------
    // getThread
    public AppThread getThread(String id) {
        synchronized (appThreads) {
            return appThreads.get(id);
        }
    } // getThread

    public Logger getLogger() {
        return log;
    } // getLogger

    //------------------------------------------------------------
    // getLogConHd
    public ConsoleHandler getLogConHd() {
        return logConHd;
    }
    // getLogConHd


    //------------------------------------------------------------
    // getLogFileHd
    public FileHandler getLogFileHd() {
        return logFileHd;
    } // getLogFileHd


    //------------------------------------------------------------
    // getProperty
    public String getProperty(String property) {
        String s = cfgProps.getProperty(property);

        if (s == null) {
            log.severe(id + ": getProperty(" + property + ") failed.  Check the config file (" + cfgFName + ")!");
        }
        return s;
    } // getProperty

    //------------------------------------------------------------
    // getSimulationTimeStr
    public String getSimulationTimeStr() {
        long t = timer.getSimulationTime();
        int s = (int) t % 60;
        int m = (int) (t / 60) % 60;
        int h = (int) (t / 3600) % 60;

        return String.format("%02d:%02d:%02d", h, m, s);
    } // getSimulationTimeStr

    public int getNElevators(){
        return NElevators;
    }

}
