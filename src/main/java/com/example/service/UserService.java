package com.example.service;

import com.example.dao.UserDao;
import com.example.entity.User;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User login(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean register(User user) {
        if (userDao.existsByUsername(user.getUsername())) {
            return false;
        }
        user.setRole("student");
        userDao.add(user);
        return true;
    }

    public User findById(Integer id) {
        return userDao.findById(id);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public void update(User user) {
        userDao.update(user);
    }

    public void updatePassword(Integer id, String password) {
        userDao.updatePassword(id, password);
    }

    public void delete(Integer id) {
        userDao.delete(id);
    }
}
