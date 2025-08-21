package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;

@Controller
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    // コメント投稿処理
    @PostMapping("/comments")
    public String createComment(@RequestParam Long postId,
                                 @RequestParam String content,
                                 HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            return "redirect:/posts";
        }

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(loginUser);
        comment.setPost(post);

        commentRepository.save(comment);

        return "redirect:/posts";
    }
}
