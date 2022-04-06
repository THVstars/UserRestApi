package com.careerdevs.UserRestApi.controllers;

import com.careerdevs.UserRestApi.models.ToDosModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

//THIS IS WORKING!!!
@RestController
@RequestMapping("/api/todo")
public class ToDoController {

    @Autowired
    private Environment env;

    @GetMapping("/firstPage")
    public ToDosModel[] getFirstPage(RestTemplate restTemplate) {
        String url = "https://gorest.co.in/public/v2/todos" + "?access-token=" + env.getProperty("GO_REST_KEY");
        return restTemplate.getForObject(url, ToDosModel[].class);
    }

    @GetMapping("/{id}")
    public ResponseEntity getOneToDo(RestTemplate restTemplate, @PathVariable("id") int toDoId) {

        try {

            String url = "https://gorest.co.in/public/v2/todos/" + toDoId + "?access-token=" + env.getProperty("GO_REST_KEY");
            return new ResponseEntity(restTemplate.getForObject(url, ToDosModel.class), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteOneToDo(RestTemplate restTemplate, @PathVariable ("id") int toDoId) {

        try {

            String url = "https://gorest.co.in/public/v2/todos/" + toDoId + "?access-token=" + env.getProperty("GO_REST_KEY");
            ToDosModel deleteToDo = restTemplate.getForObject(url, ToDosModel.class);

            restTemplate.delete(url);

            return new ResponseEntity<>(deleteToDo, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping
    public ResponseEntity<Object> postToDo (RestTemplate restTemplate, @RequestBody ToDosModel newToDo) {

        try {

            String url = "https://gorest.co.in/public/v2/todos/" + "?access-token=" + env.getProperty("GO_REST_KEY");

            HttpEntity<ToDosModel> request = new HttpEntity<>(newToDo);

            ToDosModel createdToDo = restTemplate.postForObject(url, request, ToDosModel.class);

            return new ResponseEntity<>(createdToDo, HttpStatus.CREATED);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } // TO CREATE A NEW TO.DO, THE USER YOU ASSIGN THE POST TO MUST ALREADY EXIST.

    }

    @PutMapping
    public ResponseEntity<Object> putToDo (RestTemplate restTemplate, @RequestBody ToDosModel updateToDo) {

        try {

            String url = "https://gorest.co.in/public/v2/todos/" + updateToDo.getId() + "?access-token=" + env.getProperty("GO_REST_KEY");

            HttpEntity<ToDosModel> request = new HttpEntity<>(updateToDo);

            ResponseEntity<ToDosModel> response = restTemplate.exchange(url, HttpMethod.PUT, request, ToDosModel.class);

            return new ResponseEntity<>(response.getBody(), HttpStatus.CREATED);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } // THIS WILL ONLY UPDATE AN ALREADY EXISTING USER! AND UNLIKE PATCH, ALL CATEGORIES MUST BE INCLUDED IN THE REQUEST BODY FOR IT TO WORK, EVEN IF YOU DON'T UPDATE THE INFORMATION IN ALL OF THEM.

    }

}
