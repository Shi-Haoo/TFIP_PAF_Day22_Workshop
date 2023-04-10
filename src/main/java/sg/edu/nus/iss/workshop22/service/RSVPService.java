package sg.edu.nus.iss.workshop22.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.workshop22.model.RSVP;
import sg.edu.nus.iss.workshop22.repository.RSVPRepository;

@Service
public class RSVPService {
    
    @Autowired
    RSVPRepository repo;

    public List<RSVP> getAllRSVP(){
        return repo.getAllRSVP();
    }

    public List<RSVP> getRSVPByName(String name){
        return repo.getRSVPByName(name);
    }

    public RSVP getRSVPByEmail (String email){
        return repo.getRSVPByEmail(email);
    }

    public RSVP upsertRSVP(RSVP rsvp){

        return repo.upsertRSVP(rsvp);

    }

    public RSVP updateByEmail(RSVP rsvp, String email){
        return repo.updateRSVPByEmail(rsvp, email);
    }

    public Long getRSVPCount(){
        return repo.getRSVPCount();
    }
}
