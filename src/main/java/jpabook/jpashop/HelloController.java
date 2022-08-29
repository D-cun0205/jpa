package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping(name = "hello")
    public String hello(Model model) {
        model.addAttribute("data", "hlo!");
        return "hello";
    }
}
