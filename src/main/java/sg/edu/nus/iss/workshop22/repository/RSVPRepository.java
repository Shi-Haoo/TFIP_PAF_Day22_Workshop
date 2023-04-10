package sg.edu.nus.iss.workshop22.repository;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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

    public RSVP getRSVPByEmail(String email){
        
        RSVP rsvp = null;
        SqlRowSet rs = template.queryForRowSet(SELECT_RSVP_BY_EMAIL, email);

        //since email is set to unique in sql, there should only be 1 record based on email. 
        //So we can use rs.first() to check if there is result in the first row of rs
        if(rs.first()){
            rsvp = RSVP.createFromSqlResults(rs);
        }

        return rsvp;
        
    }

    public RSVP upsertRSVP(RSVP rsvp){
       
        RSVP newRSVP = getRSVPByEmail(rsvp.getEmail());
        KeyHolder keyholder = new GeneratedKeyHolder();

        //if no existing record, insert new record
        if(newRSVP == null){

            template.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(INSERT_NEW_RSVP, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, rsvp.getName());
                ps.setString(2, rsvp.getEmail());
                ps.setString(3, rsvp.getPhone());
                ps.setTimestamp(4, new Timestamp(rsvp.getConfirmationDate().toDateTime().getMillis()));
                ps.setString(5, rsvp.getComments());
                
                return ps;
            }, keyholder);

            BigInteger primaryKey = (BigInteger) keyholder.getKey();
            rsvp.setId(primaryKey.intValue());
        }

        //else update existing record
        else{
            newRSVP.setName(rsvp.getName());
            newRSVP.setPhone(rsvp.getPhone());
            newRSVP.setConfirmationDate(rsvp.getConfirmationDate());
            newRSVP.setComments(rsvp.getComments());

            template.update(UPDATE_EXISTING_RSVP, newRSVP.getName(),
                                                  newRSVP.getPhone(),
                                                  new Timestamp(newRSVP.getConfirmationDate().toDateTime().getMillis()),
                                                  newRSVP.getComments(),
                                                  newRSVP.getEmail());
            
            rsvp.setId(newRSVP.getId());                                        
        }

        return rsvp;
    }

    public RSVP updateRSVPByEmail(RSVP rsvp, String email){
        
        RSVP updRsvp = getRSVPByEmail(email);

        if(updRsvp!=null){
            updRsvp.setName(rsvp.getName());
            updRsvp.setPhone(rsvp.getPhone());
            updRsvp.setConfirmationDate(rsvp.getConfirmationDate());
            updRsvp.setComments(rsvp.getComments());
            updRsvp.setEmail(rsvp.getEmail());
    
            template.update(UPDATE_EXISTING_RSVP, updRsvp.getName(),
                                                  updRsvp.getPhone(),
                                                  new Timestamp(updRsvp.getConfirmationDate().toDateTime().getMillis()),
                                                  updRsvp.getComments(),
                                                  email);
            
        
        }
        return updRsvp;
                        


    }


    public Long getRSVPCount(){
        
        //Since we are expecting only 1 row of result, can use queryForObject
         
        return template.queryForObject(SELECT_RSVP_COUNT, Long.class);

        //Alternate Method:
        //List<Map<String, Object>> rows = jdbcTemplate.queryForList(SELECT_RSVP_COUNT);
        //return (Long) rows.get(0).get("total_count");
    }

    
}
