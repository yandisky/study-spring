package com.example.springboot.service;

import com.example.springboot.entity.User;
import com.example.springboot.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public User selectUserByName(String name) {
        return userDao.findUserByName(name);
    }

    public List<User> selectAllUser() {
        return userDao.findAllUser();
    }

    public void insertService() {
        userDao.insertUser("xiao 1", 1, 1.23);
        userDao.insertUser("xiao 2", 2, 2.34);
    }

    public void deleteService(Integer id) {
        userDao.deleteUser(id);
    }

    @Transactional
    public void updateMoney() {
        userDao.updateUser("xiao 1", 1, 1.11, 1);
    }
}
