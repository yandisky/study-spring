package com.example.springsecurity.system.service;

import com.example.springsecurity.system.entity.Role;
import com.example.springsecurity.system.entity.User;
import com.example.springsecurity.system.entity.UserRole;
import com.example.springsecurity.system.enums.RoleType;
import com.example.springsecurity.system.exception.RoleNotFoundException;
import com.example.springsecurity.system.exception.UserNameAlreadyExistException;
import com.example.springsecurity.system.exception.UserNameNotFoundException;
import com.example.springsecurity.system.repository.RoleRepository;
import com.example.springsecurity.system.repository.UserRepository;
import com.example.springsecurity.system.repository.UserRoleRepository;
import com.example.springsecurity.system.web.representation.UserRepresentation;
import com.example.springsecurity.system.web.request.UserRegisterRequest;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional(rollbackFor = Exception.class)
    public void save(UserRegisterRequest userRegisterRequest) {
        ensureUserNameNotExist(userRegisterRequest.getUsername());
        User user = userRegisterRequest.toUser();
        user.setPassword(bCryptPasswordEncoder.encode(userRegisterRequest.getPassword()));
        userRepository.save(user);
        //给用户绑定角色
        if (!roleRepository.findByName(RoleType.USER.getName()).isPresent()) {
            roleRepository.save(new Role(RoleType.USER.getName()));
        }
        Role role = roleRepository.findByName(RoleType.USER.getName()).orElseThrow(() -> new RoleNotFoundException(ImmutableMap.of("roleName", RoleType.USER.getName())));
        userRoleRepository.save(new UserRole(user, role));
    }

    public User find(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNameNotFoundException(ImmutableMap.of("username", username)));
    }

    public Page<UserRepresentation> getAll(int pageNum, int pageSize) {
        return userRepository.findAll(PageRequest.of(pageNum, pageSize)).map(User::toUserRepresentation);
    }

    private void ensureUserNameNotExist(String userName) {
        boolean exist = userRepository.findByUsername(userName).isPresent();
        if (exist) {
            throw new UserNameAlreadyExistException(ImmutableMap.of("username", userName));
        }
    }
}
