package www.SRTS.in;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Harshit on 19-Jan-18.
 */

public class Ticket implements Serializable{
    private String Source;
    private String Destination;
    private String VIA;
    private double Fare;
    private String TimeStamp;
    private int Adults;
    private int Children;
    private String Category;
    private String Return;
    private String Status;

    public Ticket(){
        Source = "VASHI";
        Destination = "KHARGAR";
        this.VIA = "";
        Fare = 22;
        TimeStamp = genTimeStamp();
        Adults = 1;
        Children = 0;
        Category = "II ORDINARY";
        Return = "YES";
        Status = "VALID";
    }
    public String genTimeStamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        String stamp = sdf.format(date);
        return stamp;
    }
    public double getHours(){
        double hours=0;
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
             date = sdf.parse(this.TimeStamp);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date != null)
            hours = date.getTime()/3600000.0;
        return hours;
    }
    public Ticket(String source, String destination, String VIA, double fare, int adults, int children, String category, String aReturn, String status) {
        Source = source;
        Destination = destination;
        this.VIA = VIA;
        Fare = fare;

        TimeStamp = genTimeStamp();
        Adults = adults;
        Children = children;
        Category = category;
        Return = aReturn;
        Status = status;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination.toUpperCase();
    }

    public String getVIA() {
        return VIA;
    }

    public void setVIA(String VIA) {
        this.VIA = VIA;
    }

    public double getFare() {
        return Fare;
    }

    public void setFare(double fare) {
        Fare = fare;
    }


    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public int getAdults() {
        return Adults;
    }

    public void setAdults(int adults) {
        Adults = adults;
    }

    public int getChildren() {
        return Children;
    }

    public void setChildren(int children) {
        Children = children;
    }


    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getReturn() {
        return Return;
    }

    public void setReturn(String R) {
        Return = R;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
