package com.careerdevs.UserRestApi.controllers;

import com.careerdevs.UserRestApi.models.CommentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private Environment env;

    @GetMapping("/firstPage")
    public CommentModel[] getFirstPage(RestTemplate restTemplate) {
        String url = "https://gorest.co.in/public/v2/comments" + "?access-token=" + env.getProperty("GO_REST_KEY");
        return restTemplate.getForObject(url, CommentModel[].class);
    }

    @GetMapping("/{id}")
    public ResponseEntity getOneComment(RestTemplate restTemplate, @PathVariable("id") int commentId) {

        try {

            String url = "https://gorest.co.in/public/v2/comments/" + commentId + "?access-token=" + env.getProperty("GO_REST_KEY");
            return new ResponseEntity(restTemplate.getForObject(url, CommentModel.class), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteOneComment(RestTemplate restTemplate, @PathVariable ("id") int commentId) {

        try {

            String url = "https://gorest.co.in/public/v2/comments/" + commentId + "?access-token=" + env.getProperty("GO_REST_KEY");
            CommentModel deleteComment = restTemplate.getForObject(url, CommentModel.class);

            restTemplate.delete(url);

            return new ResponseEntity<>(deleteComment, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping
    public ResponseEntity<Object> postComment (RestTemplate restTemplate, @RequestBody CommentModel newComment) {

        try {

            String url = "https://gorest.co.in/public/v2/comments/" + "?access-token=" + env.getProperty("GO_REST_KEY");

            HttpEntity<CommentModel> request = new HttpEntity<>(newComment);

            CommentModel createdComment = restTemplate.postForObject(url, request, CommentModel.class);

            return new ResponseEntity<>(createdComment, HttpStatus.CREATED);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping
    public ResponseEntity<Object> putComment (RestTemplate restTemplate, @RequestBody CommentModel updateComment) {

        try {

            String url = "https://gorest.co.in/public/v2/comments/" + updateComment.getId() + "?access-token=" + env.getProperty("GO_REST_KEY");

            HttpEntity<CommentModel> request = new HttpEntity<>(updateComment);

            ResponseEntity<CommentModel> response = restTemplate.exchange(url, HttpMethod.PUT, request, CommentModel.class);

            return new ResponseEntity<>(response.getBody(), HttpStatus.CREATED);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } // THIS WILL ONLY UPDATE AN ALREADY EXISTING USER! AND UNLIKE PATCH, ALL CATEGORIES MUST BE INCLUDED IN THE REQUEST BODY FOR IT TO WORK, EVEN IF YOU DON'T UPDATE THE INFORMATION IN ALL OF THEM.

    }


}
