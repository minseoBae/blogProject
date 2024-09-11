package com.example.blogproject.service.post;

import com.example.blogproject.dto.DisplayedImageDTO;
import com.example.blogproject.dto.PostSaveRequestDTO;
import com.example.blogproject.entity.blog.Blog;
import com.example.blogproject.entity.post.Image;
import com.example.blogproject.entity.post.Post;
import com.example.blogproject.repository.blog.BlogRepository;
import com.example.blogproject.repository.image.ImageRepository;
import com.example.blogproject.repository.post.PostRepository;
import com.example.blogproject.service.image.FileService;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final FileService fileService;
    private final BlogRepository blogRepository;
    private final ImageRepository imageRepository;

    public void savePost(PostSaveRequestDTO postSaveRequestDTO, Blog blog) throws IOException {
        String uploadDir = "C://Temp/upload/post/";
        List<DisplayedImageDTO> tempImages = extractImageElement(postSaveRequestDTO.getContent());
        for (DisplayedImageDTO tempImage : tempImages) {
            moveTempFileToSavedFolder(tempImage, "content");
        }
        String updatedContent = changeImageSource(postSaveRequestDTO.getContent());

        String uploadedFileUrl = "/upload/post/"+ fileService.storeFile(postSaveRequestDTO.getImage(), uploadDir);
        Post post = Post.builder()
                .title(postSaveRequestDTO.getTitle())
                .content(postSaveRequestDTO.getContent())
                .status(postSaveRequestDTO.getStatus())
                .imageUrl(uploadedFileUrl)
                .createdAt(LocalDateTime.now())
                .blog(blog)
                .build();

        post = postRepository.save(post);

        for (DisplayedImageDTO tempImage : tempImages) {
            Image image = new Image();
            image.setOriginalName(tempImage.getOriginalName());
            image.setFilePath(tempImage.getSavedPath().replaceFirst("temporary", "content"));
            image.setPost(post);
            imageRepository.save(image);
        }
    }

    public Page<Post> findAll(Pageable pageable, boolean status, Long blogId) {
        Pageable sortedByCreatedAt = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt"));

        return postRepository.findAllByBlogIdAndStatus(blogId, status, sortedByCreatedAt);
    }

    @Transactional
    public Post findById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Transactional
    public void update(Post post) {
        Post existingBoard = postRepository.findById(post.getId()).orElse(null);
        if (existingBoard != null) {
            post.setUpdatedAt(LocalDateTime.now());
            postRepository.save(post);
        }
    }

    @Transactional
    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    public boolean checkUser(Long userId, Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        Blog postedBlog = blogRepository.findById(post.getBlog().getId()).orElse(null);
        Long blogUserId = postedBlog.getUser().getId();
        return Objects.equals(blogUserId, userId);
    }

    private List<DisplayedImageDTO> extractImageElement(String contentBody) {
        Document doc = Jsoup.parse(contentBody);
        Elements elements = doc.getElementsByTag("img");

        List<DisplayedImageDTO> tempImgs = new ArrayList<>();
        for (Element element : elements) {
            tempImgs.add(DisplayedImageDTO.builder()
                    .savedPath(element.attr("src"))
                    .originalName(element.attr("data-filename"))
                    .build());
        }

        return tempImgs;
    }

    private void moveTempFileToSavedFolder(DisplayedImageDTO tempImg, String movedFolderName) throws IOException {
        String tempPath = FileService.resourcePathToSavedPath(tempImg.getSavedPath());
        String targetPath = tempPath.replaceFirst("temporary", movedFolderName);
        fileService.moveFile(tempPath, targetPath);
    }

    private String changeImageSource(String contentBody) {
        String movedFolderName = "content";
        Element doc = Jsoup.parseBodyFragment(contentBody).body();
        Elements elements = doc.getElementsByTag("img");

        for (Element element : elements) {
            String tempSrc = element.attr("src");
            String savedSrc = tempSrc.replaceFirst("temporary", movedFolderName);
            element.attr("src", savedSrc);
        }

        return doc.select("body").html();
    }
}