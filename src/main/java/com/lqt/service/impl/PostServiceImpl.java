package com.lqt.service.impl;

import com.lqt.dto.PostDto;
import com.lqt.dto.UserDto;
import com.lqt.exception.ResourceNotFoundException;
import com.lqt.pojo.Post;
import com.lqt.pojo.Role;
import com.lqt.pojo.User;
import com.lqt.repository.PostRepository;
import com.lqt.service.PostService;
import com.lqt.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserService userService;

    @Override
    public PostDto post(PostDto postDto, Long userId) {
        Post post = Post.builder()
                .isLocked(postDto == null ? false : true)
                .userId(userId)
                .content(postDto.getContent())
                .isSurvey(postDto.getIsSurvey())
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        return mapper.map(postRepository.post(post), PostDto.class);
    }

    @Override
    public PostDto update(PostDto postDto, Long postId, Long userId) {
        Post post = postRepository.findPostById(postId);
        if (post == null){
            throw new ResourceNotFoundException("Post", "id", postId);
        }
        post.setIsLocked(postDto.getIsLocked());
        post.setContent(postDto.getContent());
        post.setIsSurvey(postDto.getIsSurvey());
        if (post.getUserId() == userId){
            return mapper.map(postRepository.update(post), PostDto.class);
        }else{
            return null;
        }
    }

    @Override
    public Boolean delete(Long id, User u) {
        Post post = postRepository.findPostById(id);
        if (post == null){
            throw new ResourceNotFoundException("Post", "id", id);
        }
        List<Role> roles = userService.getAllRoleOfUser(u.getId());
        Boolean hasAdminRole = roles.stream().anyMatch(r -> r.getName().equals("SYS_ADMIN"));
        if (hasAdminRole || post.getUserId() == u.getId()){
            postRepository.delete(post);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public PostDto lockPost(Long id, Long userId) {
        Post post = postRepository.findPostById(id);
        if (post == null){
            throw new ResourceNotFoundException("Post", "id", id);
        }
        post.setIsLocked(true);
        List<Role> roles = userService.getAllRoleOfUser(userId);
        Boolean hasAdminRole = roles.stream().anyMatch(r -> r.getName().equals("SYS_ADMIN"));
        if (hasAdminRole || post.getUserId() == userId){
            return mapper.map(postRepository.lockPost(post), PostDto.class);
        }else{
            return null;
        }
    }

    @Override
    public PostDto findPostById(Long id) {
        Post post = postRepository.findPostById(id);
        return mapper.map(post, PostDto.class);
    }

    @Override
    public List<PostDto> findAllPosts() {
        List<Post> posts = postRepository.findAllPosts();
        return posts.stream()
                .map(p -> mapper.map(p, PostDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> findPostsByUserId(Long userId, String orderDir) {
        String direction  = orderDir != null ? orderDir.toLowerCase() : "asc";
        List<Post> posts = postRepository.findPostsByUserId(userId, direction);
        return posts.stream()
                .map(p -> mapper.map(p, PostDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAllPosts();
        List<PostDto> postDtos = new ArrayList<>();
        posts.forEach(p -> {
            PostDto postDto = PostDto.builder()
                    .isLocked(p.getIsLocked())
                    .isSurvey(p.getIsSurvey())
                    .content(p.getContent())
                    .id(p.getId())
                    .userId(p.getUserId())
                    .timestamp(p.getTimestamp())
                    .build();
            postDtos.add(postDto);
        });
        return postDtos;
    }
}
