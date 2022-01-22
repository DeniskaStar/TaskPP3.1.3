package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping()
    public String getAllUsers(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("allUsers", userService.getAllUsers());
        model.addAttribute("user", user);
        model.addAttribute("newUser", new User());
        return "admin";
    }

    @PostMapping()
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam(name = "role") String [] roles) {

        Set <Role> roleSet = new HashSet<>();

        if (roles != null){
            for (String role: roles){
                roleSet.add(roleService.getRoleByName(role));
            }
        }
        user.setRoles(roleSet);
        userService.save(user);

        return "redirect:/admin";
    }

    @PatchMapping ("/{id}")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam(name = "role") String [] roles) {

        Set <Role> roleSet = new HashSet<>();

        if (roles != null){
            for (String role: roles){
                roleSet.add(roleService.getRoleByName(role));
            }
        }
        user.setRoles(roleSet);

        userService.update(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
