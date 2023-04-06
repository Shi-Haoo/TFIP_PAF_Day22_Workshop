package sg.edu.nus.iss.workshop22.model;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;

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
        
        //if we just use the pattern stored in mySQL, can just use:
        //rsvp.setConfirmationDate(new DateTime(DateTime.parse(rs.getString("confirmation_date"))));
        //But we need retrieve date in the manner shown below if we want to set date in a certain pattern:
        String confirmationDateStr = rs.getString("confirmation_date");
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        DateTime confirmationDate = formatter.parseDateTime(confirmationDateStr);
        rsvp.setConfirmationDate(confirmationDate);
        rsvp.setComments(rs.getString("comments"));

        return rsvp;
    }

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

}
