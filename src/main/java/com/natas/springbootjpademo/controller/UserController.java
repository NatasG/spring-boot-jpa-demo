package com.natas.springbootjpademo.controller;

import com.natas.springbootjpademo.entity.User;
import com.natas.springbootjpademo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping()
    public String list(@RequestParam(name = "page", defaultValue = "0") Integer page,
                       @RequestParam(name = "size", defaultValue = "10") Integer size,
                       String name,
                       Integer minAge,
                       Integer maxAge,
                       Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
        Page<User> userList = userRepository.findAll(
                (Specification<User>) (root, query, criteriaBuilder) -> {
                    List<Predicate> list = new ArrayList<>();

                    if (!StringUtils.isEmpty(name)) {
                        list.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + name + "%"));
                    }

                    if (!StringUtils.isEmpty(minAge)) {
                        list.add(criteriaBuilder.ge(root.get("age").as(Integer.class), minAge));
                    }

                    if (!StringUtils.isEmpty(maxAge)) {
                        list.add(criteriaBuilder.le(root.get("age").as(Integer.class), maxAge));
                    }

                    Predicate[] p = new Predicate[list.size()];
                    query.where(criteriaBuilder.and(list.toArray(p)));
                    return query.getRestriction();
                }
                , pageable);
        model.addAttribute("userList", userList);
        return "user/list";
    }

    @GetMapping("add")
    public String toAdd() {
        return "user/add";
    }

    @PostMapping("add")
    public String add(User user) {
        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping("{id}/delete")
    public String delete(@PathVariable String id) {
        userRepository.deleteById(id);
        return "redirect:/users";
    }

    @GetMapping("{id}/edit")
    public String toEdit(@PathVariable String id, Model model) {
        User user = userRepository.findById(id).get();
        model.addAttribute("user", user);
        return "user/edit";
    }

    @PostMapping("edit")
    public String edit(User user) {
        userRepository.save(user);
        return "redirect:/users";
    }


}
