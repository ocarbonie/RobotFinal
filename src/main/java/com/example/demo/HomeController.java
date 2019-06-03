package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class HomeController {

  @Autowired
  MessageRepository messageRepository;

  @Autowired
  UserService userService;

  @GetMapping("/register")
  public String showRegistrationPage(Model model){
    model.addAttribute("user", new User());
    return "registration";
  }

  @PostMapping("/register")
  public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){

    if(result.hasErrors()){
      return "registration";
    }

    else {
      userService.saveUser(user);
      model.addAttribute("message", "User Account Created");
    }
    return "redirect:/";
  }

  @RequestMapping("/login")
  public String login(){
    return "login";
  }

  @RequestMapping("/")
  public String listCourses(Model model){
    model.addAttribute("messages", messageRepository.findAll());
    if(userService.getUser() != null) {
      model.addAttribute("user_id", userService.getUser().getId());
    }
    return "list";
  }

  @GetMapping("/add")
  public String messageForm(Model model) {
    model.addAttribute("message", new Message());
    return "messageform";
  }

  @PostMapping("/process")
  public String processForm(@Valid Message message, BindingResult result){
    if(result.hasErrors()){
      return "messageform";
    }

    message.setUser(userService.getUser());
    messageRepository.save(message);
    return "redirect:/";
  }

  @RequestMapping("/detail/{id}")
  public String showMessage(@PathVariable("id") long id, Model model){
    model.addAttribute("message", messageRepository.findById(id).get());
    if(userService.getUser() != null) {
      model.addAttribute("user_id", userService.getUser().getId());
    }
    return "show";
  }

  @RequestMapping("/update/{id}")
  public String updateMessage(@PathVariable("id") long id, Model model){
    model.addAttribute("message", messageRepository.findById(id).get());
    return "messageform";
  }

  @RequestMapping("/delete/{id}")
  public String delMessage(@PathVariable("id") long id){
    messageRepository.deleteById(id);
    return "redirect:/";
  }

}
