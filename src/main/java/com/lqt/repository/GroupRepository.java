package com.lqt.repository;

import com.lqt.dto.UserDto;
import com.lqt.pojo.Group;
import com.lqt.pojo.GroupsMembers;
import com.lqt.pojo.User;

import java.util.List;

public interface GroupRepository {
    Group create(Group group);

    Group update(Group groupUpdate);

    Boolean delete(Group group);

    Group findById(Long groupId);

    List<Group> getAllGroups(String name);

    List<Group> getAllGroupsByUserId(Long userId);

    List<Group> getAllGroupsOfMeParticipated(Long userId);

    Boolean addMemberToGroup(GroupsMembers groupsMembers);

    GroupsMembers findMemberInGroupByUserIdAndGroupId(Long userId, Long groupId);

    Boolean deleteMemberFromGroup(GroupsMembers groupsMembers);

    List<String> getAllMailOfGroup(Long groupId);

    List<String> getAllEmailOfAlumni();

    List<Group> getGroupByName(String name);

    List<User> getAllUsersOfGroup(Long groupId);
}
