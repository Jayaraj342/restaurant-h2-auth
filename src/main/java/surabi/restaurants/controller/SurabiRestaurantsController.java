package surabi.restaurants.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import surabi.restaurants.entity.Item;
import surabi.restaurants.repository.ItemRepository;

import java.util.List;

@Controller
public class SurabiRestaurantsController {

    @Autowired
    ItemRepository itemRepository;

    @RequestMapping("/")
    public String viewHomePage(Model model) {
        List<Item> listItems = itemRepository.findAll();
        model.addAttribute("listItems", listItems);

        return "index";
    }

    @RequestMapping("/new")
    public String showNewItemForm(Model model) {
        Item item = new Item();
        model.addAttribute("item", item);

        return "new_item";
    }

    @PostMapping(value = "/save")
    public String saveItem(@ModelAttribute("item") Item item) {
        itemRepository.save(item);

        return "redirect:/";
    }

    @RequestMapping("/edit/{id}")
    public ModelAndView showEditItemForm(@PathVariable(name = "id") Integer id) {
        ModelAndView mav = new ModelAndView("edit_item");

        Item item = itemRepository.findById(id).orElse(null);
        mav.addObject("item", item);

        return mav;
    }

    @RequestMapping("/delete/{id}")
    public String deleteItem(@PathVariable(name = "id") Integer id) {
        itemRepository.deleteById(id);

        return "redirect:/";
    }
}
