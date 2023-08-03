package com.lqt.repository;

import com.lqt.pojo.Share;

import java.util.List;

public interface ShareRepository {
    Share sharePost(Share share);
    List<Share> getAllShareOfPost(Long postId);
}
