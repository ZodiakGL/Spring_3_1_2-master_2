package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping ("/admin")
public class AdminController {

	private final UserService userService;
	private final RoleService roleService;

	@Autowired
	public AdminController(UserService userService, RoleService roleService) {
		this.userService = userService;
		this.roleService = roleService;
	}

	@GetMapping
	public String getAllUsers (Model model) {
		List<User> users = userService.getAllUsers();
		model.addAttribute("users", users);
		return "users";
	}
	@GetMapping("/new")
	public String addUser(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "newUser";
	}

	@PostMapping()
	public String create(@ModelAttribute("user") @Valid User user, @RequestParam("listRoles") ArrayList<Long> roles) {
		user.setRoles(roleService.findByIdRoles(roles));
		userService.updateUser(user);
		return "redirect:/admin";
	}

	@PostMapping("/edit/user")
	public String editUser(@ModelAttribute("user") @Valid User user, @RequestParam(value = "listRoles", required = false) ArrayList<Long> roles) {
		if(roles != null) {
			user.setRoles(roleService.findByIdRoles(roles));
		} else {
			user.setRoles(roleService.getRolesByUserId(user.getId()));
		}
		userService.updateUser(user);
		return "redirect:/admin";
	}

	@PostMapping("/edit")
	public String edit (@RequestParam("userId") Long id, Model model) {
		model.addAttribute("user", userService.getUserById(id));
		return  "edit";
	}

	@PostMapping("/delete")
	public String delete (@RequestParam("userId") Long id) {
		userService.delete(id);
		return "redirect:/admin";
	}
}
