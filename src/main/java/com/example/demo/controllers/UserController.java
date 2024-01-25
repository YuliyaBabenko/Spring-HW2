package com.example.demo.controllers;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** Перенаправление c "/" на "/html"
     * @return
     */
    @GetMapping("/")
    public String homepage() {
        return "redirect:/home.html";
    }

    /** Страница вывода списка пользователей (GET)
     * @param model
     * @return
     */
    @GetMapping("/users")
    public String findAll(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "user-list";
        //return "home.html";
    }

    /** Форма ввода данных нового пользователя (GET)
     * @param user
     * @return
     */
    @GetMapping("/user-create")
    public String createUserForm(User user) {
        return "user-create";
    }

    /** Обработчик POST-запроса создания нового пользователя
     * @param user
     * @return
     */
    @PostMapping("/user-create")
    public String createUser(User user) {
        userService.saveUser(user);
        return "redirect:/users";
    }

    /** Обработчик GET-запроса на удаление пользователя по id
     * @param id
     * @return
     */
    @GetMapping("/user-delete/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.deleteUserById(id);
        return "redirect:/users";
    }

    /** Форма ввода данных для редактирования пользователя по id (GET)
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/user-update/{id}")
    public String editUserForm(Model model, @PathVariable int id) {
        User user = userService.getUserByID(id);
        if (user == null) return "redirect:/users";
        System.out.println(user);
        model.addAttribute("user", user);
        return "user-update";
    }

    /** Обработчик POST запроса на изменение пользователя по id
     * @param user
     * @return
     */
    @PostMapping("/user-update/{id}")
    public String updateUser(User user) {

        userService.updateUser(user);
        return "redirect:/users";
    }
}