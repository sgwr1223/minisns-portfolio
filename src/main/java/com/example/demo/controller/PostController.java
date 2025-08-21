package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;

@Controller
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    // 投稿一覧表示（検索 & ページネーション対応）
    @GetMapping("/posts")
    public String showPosts(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(value = "keyword", required = false) String keyword) {
        Pageable pageable = PageRequest.of(page, 5); // 1ページ5件
        Page<Post> postPage;

        if (keyword != null && !keyword.isBlank()) {
            postPage = postRepository.findByContentContainingOrderByCreatedAtDesc(keyword, pageable);
            model.addAttribute("keyword", keyword);
        } else {
            postPage = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        }

        model.addAttribute("postPage", postPage);
        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        return "posts";
    }

    // 投稿処理
    @PostMapping("/posts")
    public String createPost(@RequestParam String content, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        Post post = new Post();
        post.setContent(content);
        post.setUser(loginUser);
        postRepository.save(post);
        return "redirect:/posts";
    }

    // 投稿削除処理
    @PostMapping("/posts/delete")
    public String deletePost(@RequestParam Long id, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        Post post = postRepository.findById(id).orElse(null);
        if (post != null && post.getUser().getId().equals(loginUser.getId())) {
            postRepository.delete(post);
        }
        return "redirect:/posts";
    }

    // 編集画面表示
    @GetMapping("/posts/edit/{id}")
    public String showEditPost(@PathVariable Long id, HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        Post post = postRepository.findById(id).orElse(null);
        if (post == null || !post.getUser().getId().equals(loginUser.getId())) {
            return "redirect:/posts";
        }
        model.addAttribute("post", post);
        return "editPost";
    }

    // 更新処理
    @PostMapping("/posts/edit")
    public String updatePost(@RequestParam Long id,
                             @RequestParam String content,
                             HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        Post post = postRepository.findById(id).orElse(null);
        if (post != null && post.getUser().getId().equals(loginUser.getId())) {
            post.setContent(content);
            postRepository.save(post);
        }
        return "redirect:/posts";
    }
}
