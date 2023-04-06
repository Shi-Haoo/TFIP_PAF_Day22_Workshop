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

    public List<RSVP> getRSVPByName(String name){

        List<RSVP> rsvpByName = new ArrayList<>();
        
        //we can construct the full sql sentence in the manner I code below or just write 
        //"select * from rsvp where name like CONCAT( '%',?,'%')" in DBQueries.java. If we do that, we don't need additional 
        //variable partsOfName. Can just SqlRowSet rs = template.queryForRowSet(SELECT_RSVP_BY_NAME, name); instead

        String partsOfName = "%"+name+"%";
        SqlRowSet rs = template.queryForRowSet(SELECT_RSVP_BY_NAME, partsOfName);

        //SqlRowSet will be empty and not NUll if no name matches the substring.
        //If no match, empty list is returned 

        while(rs.next()){
            rsvpByName.add(RSVP.createFromSqlResults(rs));
        }

        return rsvpByName;
    }
}
