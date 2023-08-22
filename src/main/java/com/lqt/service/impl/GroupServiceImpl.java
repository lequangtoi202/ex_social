package com.lqt.service.impl;

import com.lqt.dto.UserDto;
import com.lqt.exception.ResourceNotFoundException;
import com.lqt.pojo.Group;
import com.lqt.pojo.GroupsMembers;
import com.lqt.pojo.Role;
import com.lqt.pojo.User;
import com.lqt.repository.GroupRepository;
import com.lqt.repository.UserRepository;
import com.lqt.service.GroupService;
import com.lqt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;


    @Override
    public Group create(Group group, Long userId) {
        User user = userRepository.findById(userId);
        if (user == null){
            throw new ResourceNotFoundException("User", "id", userId);
        }
        group.setCreatorId(userId);
        List<Role> roles = userService.getAllRoleOfUser(userId);
        Boolean hasAdminRole = roles.stream().anyMatch(r -> r.getName().equals("SYS_ADMIN"));
        if (hasAdminRole){
            return groupRepository.create(group);
        }else{
            return null;
        }
    }

    @Override
    public Group update(Group groupUpdate, Long id, Long userId) {
        Group group = groupRepository.findById(id);
        if (group == null){
            throw new ResourceNotFoundException("Group", "id", id);
        }
        group.setGroupName(groupUpdate.getGroupName());
        List<Role> roles = userService.getAllRoleOfUser(userId);
        Boolean hasAdminRole = roles.stream().anyMatch(r -> r.getName().equals("SYS_ADMIN"));
        if (hasAdminRole){
            return groupRepository.update(group);
        }else{
            return null;
        }

    }

    @Override
    public Boolean delete(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId);
        if (group == null){
            throw new ResourceNotFoundException("Group", "id", groupId);
        }
        List<Role> roles = userService.getAllRoleOfUser(userId);
        Boolean hasAdminRole = roles.stream().anyMatch(r -> r.getName().equals("SYS_ADMIN"));
        if (hasAdminRole){
            return groupRepository.delete(group);
        }else{
            return false;
        }
    }

    @Override
    public Group findById(Long groupId) {
        return groupRepository.findById(groupId);
    }

    @Override
    public List<Group> getAllGroups(String name) {
        return groupRepository.getAllGroups(name);
    }

    @Override
    public List<Group> getAllGroupsByUserId(Long userId) {
        return groupRepository.getAllGroupsByUserId(userId);
    }

    @Override
    public List<Group> getAllGroupsOfMeParticipated(Long userId) {
        return groupRepository.getAllGroupsOfMeParticipated(userId);
    }

    @Override
    public Boolean addMemberToGroup(Long userId, Long groupId, Long adminId) {
        Group group = groupRepository.findById(groupId);
        if (group == null){
            throw new ResourceNotFoundException("Group", "id", groupId);
        }
        User user = userRepository.findById(userId);
        if (user == null){
            throw new ResourceNotFoundException("User", "id", userId);
        }
        GroupsMembers groupsMembers = GroupsMembers.builder()
                .group(group)
                .userId(user.getId())
                .build();
        List<Role> roles = userService.getAllRoleOfUser(adminId);
        Boolean hasAdminRole = roles.stream().anyMatch(r -> r.getName().equals("SYS_ADMIN"));
        if (hasAdminRole){
            return groupRepository.addMemberToGroup(groupsMembers);
        }else{
            return false;
        }
    }

    @Override
    public Boolean deleteMemberFromGroup(Long userId, Long groupId, Long adminId) {
        GroupsMembers groupsMembers = groupRepository.findMemberInGroupByUserIdAndGroupId(userId, groupId);
        if (groupsMembers == null){
            throw new RuntimeException("Can not find any GroupsMembers with user_id and group_id "+ userId + " " + groupId);
        }
        List<Role> roles = userService.getAllRoleOfUser(adminId);
        Boolean hasAdminRole = roles.stream().anyMatch(r -> r.getName().equals("SYS_ADMIN"));
        if (hasAdminRole){
            return groupRepository.deleteMemberFromGroup(groupsMembers);
        }else{
            return false;
        }
    }

    @Override
    public List<String> getAllMailOfGroup(Long groupId) {
        return groupRepository.getAllMailOfGroup(groupId);
    }

    @Override
    public List<String> getAllEmailOfAlumni() {
        return groupRepository.getAllEmailOfAlumni();
    }

    @Override
    public List<Group> getGroupByName(String name) {
        return groupRepository.getGroupByName(name);
    }

    @Override
    public List<UserDto> getAllUsersOfGroup(Long groupId) {
        return groupRepository.getAllUsersOfGroup(groupId)
                .stream()
                .map(u -> UserDto.builder()
                    .username(u.getUsername())
                    .backgroundImage(u.getBackgroundImage())
                    .id(u.getId())
                    .phone(u.getPhone())
                    .password(u.getPassword())
                    .fullName(u.getFullName())
                    .email(u.getEmail())
                    .resetPasswordToken(u.getPasswordResetToken())
                    .avatarLink(u.getAvatar())
                    .build())
                .collect(Collectors.toList());
    }
}
