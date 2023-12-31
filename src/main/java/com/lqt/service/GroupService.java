package com.lqt.service;

import com.lqt.dto.UserDto;
import com.lqt.pojo.Group;

import java.util.List;

public interface GroupService {
    Group create(Group group, Long userId);

    Group update(Group groupUpdate, Long id, Long userId);

    Boolean delete(Long groupId, Long userId);

    Group findById(Long groupId);

    List<Group> getAllGroups(String name);

    List<Group> getAllGroupsByUserId(Long userId);

    List<Group> getAllGroupsOfMeParticipated(Long userId);

    Boolean addMemberToGroup(Long userId, Long groupId, Long adminId);

    Boolean deleteMemberFromGroup(Long userId, Long groupId, Long adminId);

    List<String> getAllMailOfGroup(Long groupId);

    List<String> getAllEmailOfAlumni();

    List<Group> getGroupByName(String name);

    List<UserDto> getAllUsersOfGroup(Long groupId);
}
