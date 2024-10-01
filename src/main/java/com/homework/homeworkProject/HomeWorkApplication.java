package com.homework.homeworkProject;

import com.homework.homeworkProject.dto.HorseRiders;
import com.homework.homeworkProject.dto.Jockey;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.homework.homeworkProject.UtilityMethods.*;

@SpringBootApplication
@RestController
public class HomeWorkApplication {
	HorseRiders listOfRiders = new HorseRiders();

	public static void main(String[] args) {
		SpringApplication.run(HomeWorkApplication.class, args);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/hellospring")
	public String helloSPring() {
		return "It's working, if you dont see this then id does not";
	}

	@RequestMapping(method = RequestMethod.GET, path = "/hellospring/getJockey/{firstname}/{lastname}")
	public ResponseEntity<Jockey> helloSPringGetname(@PathVariable String firstname, @PathVariable String lastname) {
		for (Jockey jockey : listOfRiders.getHorseRiders()) {
			if (jockey.getFirstName().equals(firstname) && jockey.getLastName().equals(lastname)) {
				return ResponseEntity.ok(jockey);
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/hellospring/createJockey")
	public ResponseEntity<Jockey> registerRider(@RequestBody Jockey weWantToCreateThisJockey) {
		if(weWantToCreateThisJockey.getFirstName() == null ||
				weWantToCreateThisJockey.getLastName() == null ||
				weWantToCreateThisJockey.getHorseName() == null ||
				weWantToCreateThisJockey.getCountry() == null ||
				weWantToCreateThisJockey.getFirstName().isEmpty() ||
				weWantToCreateThisJockey.getLastName().isEmpty() ||
				weWantToCreateThisJockey.getHorseName().isEmpty() ||
				weWantToCreateThisJockey.getCountry().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		String firstName = weWantToCreateThisJockey.getFirstName();
		String lastName = weWantToCreateThisJockey.getLastName();
		String horseName = weWantToCreateThisJockey.getHorseName();
		String country = weWantToCreateThisJockey.getCountry();
		Jockey jockey = createPerson(firstName, lastName, horseName, country);
		listOfRiders.getHorseRiders().add(jockey);
		return ResponseEntity.status(HttpStatus.CREATED).body(jockey);
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/hellospring/updatePerson/{id}")
	public ResponseEntity<Jockey> updateRider(@PathVariable long id, @RequestBody Jockey weWantToUpdateThisJockey) {
		if(weWantToUpdateThisJockey.getFirstName() == null ||
				weWantToUpdateThisJockey.getLastName() == null ||
				weWantToUpdateThisJockey.getHorseName() == null ||
				weWantToUpdateThisJockey.getCountry() == null ||
				weWantToUpdateThisJockey.getFirstName().isEmpty() ||
				weWantToUpdateThisJockey.getLastName().isEmpty() ||
				weWantToUpdateThisJockey.getHorseName().isEmpty() ||
				weWantToUpdateThisJockey.getCountry().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		int position=-1;
		Jockey existingJockey = new Jockey();
		String firstName = weWantToUpdateThisJockey.getFirstName();
		String lastName = weWantToUpdateThisJockey.getLastName();
		String horseName = weWantToUpdateThisJockey.getHorseName();
		String country = weWantToUpdateThisJockey.getCountry();
		for (Jockey jockey : listOfRiders.getHorseRiders()) {
			if (jockey.getId() == id) {
				existingJockey = jockey;
				position = listOfRiders.getHorseRiders().indexOf(existingJockey);
				break;
			}
		}
		if(position==-1){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		existingJockey = updatePerson(existingJockey, firstName, lastName, horseName, country);
		listOfRiders.getHorseRiders().set(position, existingJockey);
		return ResponseEntity.status(HttpStatus.OK).body(existingJockey);
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/hellospring/removePerson/{id}")
	public ResponseEntity<Jockey> removeRider(@PathVariable long id) {
		int position=-1;
		Jockey existingJockey = new Jockey();
		for (Jockey jockey : listOfRiders.getHorseRiders()) {
			if (jockey.getId() == id) {
				existingJockey = jockey;
				position = listOfRiders.getHorseRiders().indexOf(existingJockey);
				break;
			}
		}
		if(position==-1){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		listOfRiders.getHorseRiders().remove(position);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
}
