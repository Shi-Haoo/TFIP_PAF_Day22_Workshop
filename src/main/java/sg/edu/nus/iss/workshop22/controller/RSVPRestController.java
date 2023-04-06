package sg.edu.nus.iss.workshop22.controller;

import java.util.List;

import javax.xml.namespace.QName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import sg.edu.nus.iss.workshop22.model.RSVP;
import sg.edu.nus.iss.workshop22.service.RSVPService;

@RestController
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class RSVPRestController {

    @Autowired
    RSVPService svc;

    @GetMapping(path="/rsvps")
    public ResponseEntity<String> getAllRSVP(){
        List<RSVP> allRsvps = svc.getAllRSVP();

        JsonArrayBuilder jab = Json.createArrayBuilder();
        

        for(RSVP r : allRsvps){
            jab.add(r.toJson());
        }

        JsonArray jarr = jab.build();
        
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jarr.toString());
        }

    @GetMapping(path="/rsvp")
    public ResponseEntity<String> getRSVPByName(@RequestParam(required = true) String q){

        List<RSVP> rsvpByName = svc.getRSVPByName(q);

        if(rsvpByName.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("Error: No record found for name you requested for");
        }

        JsonArrayBuilder jab = Json.createArrayBuilder();

        for(RSVP r : rsvpByName){

            jab.add(r.toJson());
        }

        return ResponseEntity   
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jab.build().toString());

    }
    
}
    

