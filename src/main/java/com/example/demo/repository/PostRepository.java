package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    // ページネーション付き：すべての投稿を新しい順で取得
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // ページネーション付き：キーワード検索（投稿内容）＋新しい順
    Page<Post> findByContentContainingOrderByCreatedAtDesc(String keyword, Pageable pageable);
}
