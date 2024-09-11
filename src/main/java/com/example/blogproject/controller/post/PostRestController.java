package com.example.blogproject.controller.post;

import com.example.blogproject.dto.DisplayedImageDTO;

import com.example.blogproject.entity.post.Post;
import com.example.blogproject.repository.blog.BlogRepository;
import com.example.blogproject.repository.post.PostRepository;
import com.example.blogproject.service.image.TemporaryImageService;
import com.example.blogproject.service.post.PostService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostRestController {
    private final PostRepository postRepository;
    private final BlogRepository blogRepository;
    private final TemporaryImageService tempImageService;
    private final PostService postService;

    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/image")
    public ResponseEntity<DisplayedImageDTO> uploadTempImg(@RequestParam("img") MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        try {
            DisplayedImageDTO imgDTO = tempImageService.saveTemporaryImage(file);
            return new ResponseEntity<>(imgDTO, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post updatedPost) {
        return postRepository.findById(id).map(post -> {
            post.setTitle(updatedPost.getTitle());
            post.setContent(updatedPost.getContent());
            post.setStatus(updatedPost.getStatus());
            post.setLikes(updatedPost.getLikes());
            post.setViews(updatedPost.getViews());
            post.setImage(updatedPost.getImage());
            post.setBlog(updatedPost.getBlog());
            postRepository.save(post);
            return ResponseEntity.ok(post);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePost(@PathVariable Long id) {
        return postRepository.findById(id).map(post -> {
            postRepository.delete(post);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }


}