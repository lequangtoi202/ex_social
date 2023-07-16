package com.lqt.service;

import com.lqt.pojo.Share;

public interface ShareService {
    Share sharePost(Long postId, Long userId);
}
