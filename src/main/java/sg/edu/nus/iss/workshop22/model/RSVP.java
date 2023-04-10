package sg.edu.nus.iss.workshop22.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class RSVP {

    private Integer id;
    private String name;
    private String email;
    private String phone;
    private DateTime confirmationDate;
    private String comments;

    public RSVP() {
    }

    public RSVP(Integer id, String name, String email, String phone, DateTime confirmationDate, String comments) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.confirmationDate = confirmationDate;
        this.comments = comments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public DateTime getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(DateTime confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "RSVP [id=" + id + ", name=" + name + ", email=" + email + ", phone=" + phone + ", confirmationDate="
                + confirmationDate + ", comments=" + comments + "]";
    }

    public static RSVP createFromSqlResults(SqlRowSet rs){

        RSVP rsvp = new RSVP();

        rsvp.setId(rs.getInt("id"));
        rsvp.setName(rs.getString("name"));
        rsvp.setEmail(rs.getString("email"));
        rsvp.setPhone(rs.getString("phone"));
        
        
        // Alternate method: rsvp.setConfirmationDate(new DateTime(DateTime.parse(rs.getString("confirmation_date"))));
        
        //purpose of code below: retrieve the confirmation_date as String then convert it into DateTime
        //To convert string to DateTime successfully,DateTimeFormat.forPattern() must match 
        //the pattern of the String of confirm_date which in this case is "yyyy-MM-dd'T'HH:mm".

        String confirmationDateStr = rs.getString("confirmation_date");
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm");
        DateTime confirmationDate = formatter.parseDateTime(confirmationDateStr);
        rsvp.setConfirmationDate(confirmationDate);
        rsvp.setComments(rs.getString("comments"));

        return rsvp;
    }

    //Since we have parsed the String into a DateTime object in createFromSqlResults(SqlRowSet rs) method, 
    //we can now use DateTimeFormat.forPattern() to convert it to String in the pattern we desire

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("id", this.getId())
                .add("name", this.getName())
                .add("email", this.getEmail())
                .add("phone", this.getPhone())
                .add("Confirmation_Date", this.getConfirmationDate().toString(DateTimeFormat.forPattern("dd-MM-yyyy")))
                .add("comment", this.getComments())
                .build();
    }

    public static RSVP createFromJson(String json) throws IOException{

        RSVP rsvp = new RSVP();

        //convert Json String to Json Object
        try(InputStream is = new ByteArrayInputStream(json.getBytes())){
            JsonReader reader = Json.createReader(is);
            JsonObject o = reader.readObject();
            
            //set values in Json Object to Java Object
            rsvp.setName(o.getString("name"));
            rsvp.setEmail(o.getString("email"));
            rsvp.setPhone(o.getString("phone"));
            
            //convert String in the format of "dd-MM-yyyy" to DateTime Object and 
            //set it into RSVP confirmationDate
            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
            DateTime date = formatter.parseDateTime(o.getString("confirmationDate"));
            rsvp.setConfirmationDate(date);
            
            rsvp.setComments(o.getString("comments"));
        }

        return rsvp;

    }

}
