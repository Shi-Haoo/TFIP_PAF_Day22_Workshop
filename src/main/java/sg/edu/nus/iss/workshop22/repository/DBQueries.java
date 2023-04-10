package sg.edu.nus.iss.workshop22.repository;

public class DBQueries {
    
    public static final String SELECT_ALL_RSVP = "select * from rsvp";

    public static final String SELECT_RSVP_BY_NAME = "select * from rsvp where name like ?";
    
    public static final String SELECT_RSVP_BY_EMAIL = "select * from rsvp where email like ?";

    public static final String INSERT_NEW_RSVP = "insert into rsvp (name, email, phone, confirmation_date, comments) values (?,?,?,?,?)";

    public static final String UPDATE_EXISTING_RSVP = "update rsvp set name = ? , phone = ?, confirmation_date = ?, comments = ? where email = ?";

    public static final String  SELECT_RSVP_COUNT = "select count(*) as total_count from rsvp";
}
