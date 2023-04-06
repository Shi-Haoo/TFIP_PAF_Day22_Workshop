package sg.edu.nus.iss.workshop22.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.workshop22.model.RSVP;
import static sg.edu.nus.iss.workshop22.repository.DBQueries.*;


@Repository
public class RSVPRepository {
    
    @Autowired 
    JdbcTemplate template;

    public List<RSVP> getAllRSVP(){
        List<RSVP> allRsvps = new ArrayList<>();

        SqlRowSet rs = template.queryForRowSet(SELECT_ALL_RSVP);

        while(rs.next()){
            allRsvps.add(RSVP.createFromSqlResults(rs));
        }

        return allRsvps;
        
    }
}
