package com.careerdevs.UserRestApi.controllers;

import com.careerdevs.UserRestApi.models.CommentModel;
import com.careerdevs.UserRestApi.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@RestController
@RequestMapping("/api/user") // all route handlers added to this controller/class will begin with this.

public class UserController {

    @Autowired
    private Environment env;

    @GetMapping("/{getUser}")
    public Object getUser(@PathVariable String getUser, RestTemplate restTemplate) {

        return restTemplate.getForObject("https://gorest.co.in/public/v2/users/" + getUser + "?access-token=" + env.getProperty("GO_REST_KEY"), UserModel.class);
    }

    @GetMapping("/page/{pageNum}")
    public Object getPage(RestTemplate restTemplate, @PathVariable("pageNum") int pageNumber) {

        try {

           String url = "https://gorest.co.in/public/v2/users?page=" + pageNumber;

            /*ResponseEntity<UserModel[]> firstPage = restTemplate.getForEntity(url, UserModel[].class);

            UserModel[] firstPageUsers = firstPage.getBody();

            HttpHeaders responseHeaders = firstPage.getHeaders();

            String totalPages = Objects.requireNonNull(responseHeaders.get("X-Pagination-Pages")).get(0);

            System.out.println("Total Pages: " + totalPages);

            for (int i = 0; i < firstPageUsers.length; i++) {
                UserModel tempUser = firstPageUsers[i];

                System.out.println(tempUser.toString());

            }

            return new ResponseEntity<>(firstPageUsers, HttpStatus.OK);*/

            return restTemplate.getForObject("https://gorest.co.in/public/v2/users" + "?access-token=" + env.getProperty("GO_REST_KEY") + "&page=" + pageNumber, UserModel[].class);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity getAll(RestTemplate restTemplate) {

        try {

            ArrayList<UserModel> allUsers = new ArrayList<>();
            String url = "https://gorest.co.in/public/v2/users";

            ResponseEntity<UserModel[]> response = restTemplate.getForEntity(url, UserModel[].class);
            allUsers.addAll(Arrays.asList(Objects.requireNonNull(response.getBody())));

            int totalPageNumber = Integer.parseInt(response.getHeaders().get("X-Pagination-Pages").get(0));

            for (int i = 2; i <= totalPageNumber; i++) {
                String tempUrl = url + "?page=" + i;
                UserModel[] pageData = restTemplate.getForObject(tempUrl, UserModel[].class);
                allUsers.addAll(Arrays.asList(Objects.requireNonNull(pageData)));
            }

            return new ResponseEntity<>(allUsers, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value = "users/", consumes = "application/json")
    public UserModel createUser(@RequestBody UserModel user, RestTemplate restTemplate) {

        return restTemplate.postForObject("https://gorest.co.in/public/v2/users", user, UserModel.class);
    }

    @PostMapping("/qp") // Using RequestParam (Mid. Can also be done with PathVariable, but that's the worst option.)
    public Object postUserQueryParam(@RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("gender") String gender, @RequestParam("status") String status, RestTemplate restTemplate) {

        try {

            String url = "https://gorest.co.in/public/v2/users/" + "?access-token=" + env.getProperty("GO_REST_KEY");

            UserModel newUser = new UserModel(name, email, gender, status);

            HttpEntity<UserModel> request = new HttpEntity<>(newUser);

            return restTemplate.postForEntity(url, request, UserModel.class);

        } catch (Exception exception) {

            System.out.println(exception.getClass());
            return exception.getMessage();
        }
    }

    @PostMapping("/") // Using RequestBody (best)
    public ResponseEntity postUser(RestTemplate restTemplate, @RequestBody UserModel newUser) {

        try {

            String url = "https://gorest.co.in/public/v2/users/" + "?access-token=" + env.getProperty("GO_REST_KEY");

            HttpEntity<UserModel> request = new HttpEntity<>(newUser);

            return restTemplate.postForEntity(url, request, UserModel.class);

        } catch (Exception e) {

            System.out.println(e.getClass() + "\n" + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity putUser(RestTemplate restTemplate, @RequestBody UserModel updateUser) {

        try {

            String url = "https://gorest.co.in/public/v2/users/" + updateUser.getId() + "?access-token=" + env.getProperty("GO_REST_KEY");

            HttpEntity<UserModel> request = new HttpEntity<>(updateUser);

            ResponseEntity<UserModel> response = restTemplate.exchange(url, HttpMethod.PUT, request, UserModel.class);

            return new ResponseEntity<>(response.getBody(), HttpStatus.CREATED);

        } catch (Exception e) {

            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @DeleteMapping("/{deleteUser}")
    public String deleteUser(@PathVariable String deleteUser, RestTemplate restTemplate) {

        try {

            restTemplate.delete("https://gorest.co.in/public/v2/users/" + deleteUser + "?access-token=" + env.getProperty("GO_REST_KEY"));

            return "Deleted the user: #" + deleteUser;

        } catch (Exception e) {

            return "Error. User does not exist.";

        }
    }

    // put request must include the id of the user that needs to be updated
    // i also need to add a delete request

    // java object converted to binary data and then converted to json data = serialized
    // json data converted to binary data and then converted to java object = deserialized

}
