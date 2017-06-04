package app.adie.reservation.entity;

public class AlertDialogAction {
    public String action;
    public String key;
    public String type;

    public AlertDialogAction(String action) {
        this.action = action;
    }

    public AlertDialogAction(String action, String key) {
        this.action = action;
        this.key = key;
    }

    public AlertDialogAction(String action, String key, String type) {
        this.action = action;
        this.key = key;
        this.type = type;
    }
}
