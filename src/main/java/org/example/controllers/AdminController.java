package org.example.controllers;

import lombok.extern.slf4j.Slf4j;
import org.example.accounts.models.UserProfile;
import org.example.dao.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@Slf4j
public class AdminController {
    private final AccountService accountService;

    @Autowired
    public AdminController(@Qualifier("hibernateUserDAO") AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping("/admin/showUsers")
    public String showUsers(Model model) {
        log.info("Вызвана страница отображения списка пользователей");
        model.addAttribute("usersList", accountService.index());

        return "admin/index";
    }

    @RequestMapping("/admin/addUserForm")
    public String showAddUserForm(Model model) {
        log.info("Вызвана страница добавления пользователя");

        model.addAttribute("userProfile", new UserProfile());

        return "admin/add-user";
    }

    @RequestMapping("/admin/addUser")
    public ModelAndView addUsersToMap(@ModelAttribute("userProfile") UserProfile profile) {
        log.info("Добавление пользователя в хранилище. Login: "
                + profile.getUsername()
                + " Role: " + profile.getRole()
                + " Имя: " + profile.getName()
                + " Email: " + profile.getEmail());

        if (profile.getName().equals("")) {
            return new ModelAndView("redirect:/");
        } else if (accountService.checkIfLoginExists(profile.getUsername())) {
            return new ModelAndView("redirect:/admin/userExists");
        }

        accountService.addUser(profile);

        return new ModelAndView("redirect:/admin/showUsers");
    }

    @RequestMapping("/admin/userExists")
    public String showUserExistsMessage() {
        log.info("Вызвана страница сообщения о существующем логине");

        return "admin/login-exists";
    }

    @RequestMapping("/admin/deleteUser")
    public RedirectView deleteUserFromMap(@RequestParam("userIdToDelete") int id) {
        log.info("Удаление пользователя, ID: " + id);

        accountService.deleteUserById(id);

        return new RedirectView("/admin/showUsers");
    }

    @RequestMapping("/admin/updateUserForm")
    public String showUpdateUserForm(@RequestParam("userIdToUpdate") int id, Model model) {
        log.info("Вызвана страница обновления данных пользователя ID: " + id);

        UserProfile userProfile = accountService.getUserById(id);
        model.addAttribute("userProfile", userProfile);

        return "admin/update-user";
    }

    @RequestMapping("/admin/updateUser")
    public RedirectView updateUser(UserProfile profile) {
        log.info("Обновление данных пользователя. Name: " + profile.getName() + " Email: " + profile.getEmail());

        accountService.updateUser(profile.getId(), profile);

        return new RedirectView("/admin/showUsers");
    }
}
