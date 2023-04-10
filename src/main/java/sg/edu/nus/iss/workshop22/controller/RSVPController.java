package sg.edu.nus.iss.workshop22.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.nus.iss.workshop22.model.RSVP;

@Controller
@RequestMapping(path="/api")
public class RSVPController {
    
    //This is landing page. Once click on submit, request will be 
    //directed to appropriate method in Rest Controller
    @GetMapping(path="/rsvp")
    public String upsertRSVPViaForm(Model model){

        model.addAttribute("rsvp", new RSVP());

        return "index";
    }

}
