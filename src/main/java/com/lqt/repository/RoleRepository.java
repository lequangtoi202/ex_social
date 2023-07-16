package com.lqt.repository;

import com.lqt.pojo.Role;

public interface RoleRepository {
    Role save(Role role);
    Role update(Role role, Long id);
    boolean delete(Long id);
    Role getRoleByName(String name);
    Role getRoleById(Long id);
}
