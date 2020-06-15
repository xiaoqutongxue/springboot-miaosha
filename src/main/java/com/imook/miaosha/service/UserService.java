package com.imook.miaosha.service;

import com.imook.miaosha.dao.UserDao;
import com.imook.miaosha.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User getById(int id){
        return userDao.getUserById(id);
    }

    /**
     * 事务的相关测试
     * 添加注解：@Transactional  让事务起作用
     * 加上了注解就能让事务添加进去  当一个事务中有一个操作异常时，整个事务都会回滚  反之不加注解 整个事务不会回滚
     * @return
     */
    @Transactional
    public Boolean Tx() {
        User user01 = new User();
        user01.setId(2);
        user01.setName("zhangsan");
        userDao.insert(user01);

//        User user02 = new User();
//        user02.setId(1);
//        user02.setName("xiaoqu02");
//        userDao.insert(user02);

        return true;
    }
}
