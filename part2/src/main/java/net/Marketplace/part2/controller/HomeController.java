package net.Marketplace.part2.controller;

import net.Marketplace.part2.entity.Articles;
import net.Marketplace.part2.repository.ArticlesRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
/**
 * Controller class managing home page.
 */
@Controller
public class HomeController {
    private final ArticlesRepository articlesRepository;
    /**
     * Constructor for HomeController.
     *
     * @param articlesRepository Repository for managing articles data.
     */
    public HomeController(ArticlesRepository articlesRepository) {
        this.articlesRepository = articlesRepository;
    }
    /**
     * Displays the home page.
     *
     * @param model The model to be populated with data for rendering the view.
     * @return redirect to index.
     */
    @GetMapping("/")
    public String home(Model model) {
        List<Articles> allArticlesList = articlesRepository.findAll();
        List<Articles> articlesList = allArticlesList.subList(0,5);
        Optional<Articles> bestSellerOptional = articlesRepository.findByName("Bouteille 1.5L Eau du Robinet de Wa'er");
        Articles bestSeller = bestSellerOptional.isPresent() ? bestSellerOptional.get() : null;

        model.addAttribute("articles", articlesList);
        model.addAttribute("bestSeller", bestSeller);

        return "index";
    }
}
