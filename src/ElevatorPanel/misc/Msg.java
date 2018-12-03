package ElevatorPanel.misc;


//======================================================================
// Msg
public class Msg {
    private String sender;
    private MBox senderMBox;
    private Type type;
    private String details;
    private Type returnType;

    //------------------------------------------------------------
    // Msg
    public Msg(String sender, MBox senderMBox, Type type, String details) {
        this.sender = sender;
        this.senderMBox = senderMBox;
        this.type = type;
        this.details = details;
    } // Msg

    public Msg(String sender, MBox senderMBox, Type type, String details, Type returnType) {
        this.sender = sender;
        this.senderMBox = senderMBox;
        this.type = type;
        this.details = details;
        this.returnType = returnType;
    } // Msg


    //------------------------------------------------------------
    // getters
    public String getSender() {
        return sender;
    }

    public MBox getSenderMBox() {
        return senderMBox;
    }

    public Type getType() {
        return type;
    }

    public String getDetails() {
        return details;
    }

    public Type getReturnType() { return returnType; }


    //------------------------------------------------------------
    // toString
    public String toString() {
        return "Sender: " + sender + " (" + type + ") -- Content: " + details;
    } // toString


    //------------------------------------------------------------
    // Msg Types
    public enum Type {
        Terminate,    // Terminate the running thread
        SetTimer,       // Set a timer
        CancelTimer,    // Set a timer
        Tick,           // Timer clock ticks
        TimesUp,
        Hello,
        HiHi,
        DoorOpened, // After arrived a floor a the door is opened
        CloseDoor, // After door waiting time, going to close the door
        Svc_Req,
        Svc_Reply,
        Elev_Arr,
        Elev_Dep,
        GUI_Reply,
    } // Type
} // Msg
