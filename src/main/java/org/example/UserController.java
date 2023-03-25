package org.example;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.accounts.AccountService;
import org.example.accounts.UserProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final AccountService accountService;

    @RequestMapping("/")
    public String showUsers(Model model) {
        log.info("Вызвана страница отображения списка пользователей");
        model.addAttribute("usersList", accountService.index());

        return "index";
    }

    @RequestMapping("/addUserForm")
    public String showAddUserForm(Model model) {
        log.info("Вызвана страница добавления пользователя");

        model.addAttribute("userProfile", new UserProfile());

        return "add-user";
    }

    @RequestMapping("/addUser")
    public RedirectView addUsersToMap(@ModelAttribute("userProfile") UserProfile profile) {
        log.info("Добавление пользователя в хранилище. Имя: " + profile.getName() + " Email: " + profile.getEmail());

        if (profile.getName().equals("")) {
            return new RedirectView("/");
        }
        accountService.addUser(profile);

        return new RedirectView("/");
    }

    @RequestMapping("/deleteUser")
    public RedirectView deleteUserFromMap(@RequestParam("userIdToDelete") int id) {
        log.info("Удаление пользователя, ID: " + id);

        accountService.deleteUserById(id);

        return new RedirectView("/");
    }

    @RequestMapping("/updateUserForm")
    public String showUpdateUserForm(@RequestParam("userIdToUpdate") int id, Model model) {
        log.info("Вызвана страница обновления данных пользователя ID: " + id);

        UserProfile userProfile = accountService.getUserById(id);
        model.addAttribute("userProfile", userProfile);

        return "update-user";
    }

    @RequestMapping("/updateUser")
    public RedirectView updateUser(UserProfile profile) {
        log.info("Обновление данных пользователя. Name: " + profile.getName() + " Email: " + profile.getEmail());

        accountService.updateUser(profile.getId(), profile);

        return new RedirectView("/");
    }
}