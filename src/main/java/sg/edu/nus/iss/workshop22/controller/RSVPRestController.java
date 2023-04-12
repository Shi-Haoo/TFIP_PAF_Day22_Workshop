package sg.edu.nus.iss.workshop22.controller;

import java.io.IOException;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping(path="/rsvp", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> upsertViaJson(@RequestBody String json) throws IOException{

        //convert Json to Java Object
        RSVP rsvp = RSVP.createFromJson(json);

        //If no existing record is found
        Boolean recordExists = svc.getRSVPByEmail(rsvp.getEmail())==null ? false : true;
        System.out.println("isExisting >>>>> " + recordExists);

        RSVP newRSVP = svc.upsertRSVP(rsvp);

        String message = recordExists ? "%s >>>>%s's record is updated".formatted(newRSVP.getId(),newRSVP.getName()) : 
           "%s >>>>%s's record is inserted".formatted(newRSVP.getId(), newRSVP.getName());
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Json.createObjectBuilder()
                            .add("Message", message)
                            .build()
                            .toString());
        
        
        
        

    }


    @PostMapping(path="/rsvp/form", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    //use @ModelAttribute to retrieve the values in RSVP object which we previously 
    //added into the model to bind to the form in RSVPController.java
    public ResponseEntity<String> upsertViaForm(@ModelAttribute RSVP rsvp, String date){

        DateTime confirmationDate = RSVP.convertToDateTimeFromForm(date);
        rsvp.setConfirmationDate(confirmationDate);
        
        //If no existing record is found
        Boolean recordExists = svc.getRSVPByEmail(rsvp.getEmail())==null ? false : true;
        System.out.println("isExisting >>>>> " + recordExists);

        RSVP newRSVP = svc.upsertRSVP(rsvp);

        String message = recordExists ? "%s >>>>%s's record is updated".formatted(newRSVP.getId(),newRSVP.getName()) : 
           "%s >>>>%s's record is inserted".formatted(newRSVP.getId(), newRSVP.getName());
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Json.createObjectBuilder()
                            .add("Message", message)
                            .build()
                            .toString());
        
        
        
        

    }

    @PutMapping(path="/rsvp/{email}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateByEmail(@PathVariable String email, @RequestBody String json) throws IOException{

        //convert from Json to Java Object
        RSVP rsvp = RSVP.createFromJson(json);

        //Check if RSVP based on this particular email exist
        RSVP currentRSVP = svc.getRSVPByEmail(email);

        //If not exist
        if(currentRSVP == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Json.createObjectBuilder()
                            .add("Error", "Error: RSVP with %s not found".formatted(email))
                            .build()
                            .toString());
        }

        //Update RSVP
        RSVP updRSVP = svc.updateByEmail(rsvp, email);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Json.createObjectBuilder()
                        .add("Message", "RSVP for email %s has been updated".formatted(updRSVP.getEmail()))
                        .build()
                        .toString());
                
    }

    @GetMapping(path ="rsvps/count")
    public ResponseEntity<String> getTotalRSVPCount(){
        Long totalCount = svc.getRSVPCount();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Json.createObjectBuilder()
                        .add("total_rsvp_count:", totalCount)
                        .build()
                        .toString());
    }
    
}
    

