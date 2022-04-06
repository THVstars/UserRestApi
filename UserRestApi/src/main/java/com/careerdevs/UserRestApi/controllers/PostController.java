package com.careerdevs.UserRestApi.controllers;

import com.careerdevs.UserRestApi.models.PostModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private Environment env;

    @GetMapping("/firstPage")
    public PostModel[] getFirstPage(RestTemplate restTemplate) {
        String url = "https://gorest.co.in/public/v2/posts" + "?access-token=" + env.getProperty("GO_REST_KEY");
        return restTemplate.getForObject(url, PostModel[].class);
    }

    @GetMapping("/{id}")
    public ResponseEntity getOnePost(RestTemplate restTemplate, @PathVariable("id") int postId) {

        //REMEMBER TO ADD SLASHES IN THE URL WHERE NECESSARY OR ELSE THINGS WON'T WORK CORRECTLY!

        try {

            String url = "https://gorest.co.in/public/v2/posts/" + postId + "?access-token=" + env.getProperty("GO_REST_KEY");
            return new ResponseEntity(restTemplate.getForObject(url, PostModel.class), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteOnePost(RestTemplate restTemplate, @PathVariable("id") int postId) {
        try {
            String url = "https://gorest.co.in/public/v2/posts/" + postId + "?access-token=" + env.getProperty("GO_REST_KEY");
            PostModel deletePost = restTemplate.getForObject(url, PostModel.class);

            restTemplate.delete(url);

            return new ResponseEntity<>(deletePost, HttpStatus.OK); // this doesn't work without "new" being included... interesting.
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Object> postPost (RestTemplate restTemplate, @RequestBody PostModel newPost) {

        try {
            String url = "https://gorest.co.in/public/v2/posts" + "?access-token=" + env.getProperty("GO_REST_KEY");

            HttpEntity<PostModel> request = new HttpEntity<>(newPost);

            PostModel createdPost = restTemplate.postForObject(url, request, PostModel.class);

            return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } // TO CREATE A NEW POST, THE USER YOU ASSIGN THE POST TO MUST ALREADY EXIST.
    }

    @PutMapping
    public ResponseEntity<Object> putPost (RestTemplate restTemplate, @RequestBody PostModel updatePost) {

        try {
            String url = "https://gorest.co.in/public/v2/posts/" + updatePost.getId() + "?access-token=" + env.getProperty("GO_REST_KEY");

            HttpEntity<PostModel> request = new HttpEntity<>(updatePost);

            ResponseEntity<PostModel> response = restTemplate.exchange(url, HttpMethod.PUT, request, PostModel.class);

            return new ResponseEntity<>(response.getBody(), HttpStatus.CREATED);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } // THIS WILL ONLY UPDATE AN ALREADY EXISTING USER! AND UNLIKE PATCH, ALL CATEGORIES MUST BE INCLUDED IN THE REQUEST BODY FOR IT TO WORK, EVEN IF YOU DON'T UPDATE THE INFORMATION IN ALL OF THEM.

}
