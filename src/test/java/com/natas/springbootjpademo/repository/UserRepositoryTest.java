package com.natas.springbootjpademo.repository;

import com.natas.springbootjpademo.entity.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private static final String MOCK_ID = "mock_id";

    @Before
    public void init() {
        User user1 = new User();
        user1.setName("user1");
        user1.setAge(18);
        userRepository.save(user1);
    }

    @Test
    public void should_return_all_users() {
        List<User> userList = userRepository.findAll();
        Assert.assertTrue(userList.size() != 0);
    }

    @Test
    public void should_return_user_when_find_by_existed_id() {
        Optional<User> optionalUser = userRepository.findById(getId());
        Assert.assertTrue(optionalUser.isPresent());
    }

    @Test
    public void should_return_null_when_find_by_not_existed_id() {
        Optional<User> optionalUser = userRepository.findById(MOCK_ID);
        Assert.assertFalse(optionalUser.isPresent());
    }

    @Test
    public void should_return_saved_user_when_insert_user() {
        User user = new User();
        user.setName("user2");
        user.setAge(19);
        user = userRepository.save(user);
        Assert.assertNotNull(user.getId());
    }

    @Test
    public void should_return_saved_user_when_update_user() {
        User user = userRepository.findById(getId()).get();
        String name = "mock_name";
        user.setName(name);
        User savedUser = userRepository.save(user);
        Assert.assertEquals(name, savedUser.getName());
    }

    @Test
    public void should_success_when_delete_by_existed_id() {
        userRepository.deleteById(getId());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void should_throw_empty_result_data_access_exception_when_delete_by_existed_id() {
        userRepository.deleteById(MOCK_ID);
    }


    private String getId() {
        List<User> userList = userRepository.findAll();
        return userList.get(0).getId();
    }


}
