package net.Marketplace.part2.controller;

import jakarta.validation.Valid;
import net.Marketplace.part2.dto.ArticleDTO;
import net.Marketplace.part2.entity.Articles;
import net.Marketplace.part2.entity.Users;
import net.Marketplace.part2.repository.ArticlesRepository;
import net.Marketplace.part2.repository.UsersRepository;
import net.Marketplace.part2.utils.ValidationUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;
/**
 * Controller handling administrative functionalities.
 */
@Controller
public class AdminController {
    private final UsersRepository usersRepository;
    private final ArticlesRepository articlesRepository;

    public AdminController(UsersRepository usersRepository, ArticlesRepository articlesRepository){
        this.usersRepository = usersRepository;
        this.articlesRepository = articlesRepository;
    }
    /**
     * Method to load users for the admin view.
     * @param pageable Pagination information.
     * @param model Model object.
     * @return redirect to users page.
     */
    @GetMapping("/admin/user")
    String loadUsers(Pageable pageable, Model model){
        Page<Users> users = usersRepository.findAll(pageable);
        model.addAttribute("users", users.getContent());
        return "users";
    }
    /**
     * Method to load articles for the admin view.
     * @param pageable Pagination information.
     * @param model Model object.
     * @return redirect to /admin/ page.
     */
    @GetMapping("/admin/")
    String loadArticles(Pageable pageable, Model model){
        Page<Articles> articles = articlesRepository.findAll(pageable);
        model.addAttribute("articles", articles.getContent());
        return "admin";
    }
    /**
     * Method to show the form for adding a new product.
     * @param model Model object.
     * @return redirect to newProduct page.
     */
    @GetMapping("/admin/newProduct")
    public String showNewProductForm(Model model){
        ArticleDTO dto = new ArticleDTO();

        model.addAttribute("article", dto);
        return "newProduct";
    }
    /**
     * Method to add a new product.
     * @param article New article details.
     * @param result Binding result for validation.
     * @param model Model object.
     * @return redirect to /admin/ page.
     */
    @PostMapping("/admin/newProduct")
    public String addNewProduct(@Valid @ModelAttribute ArticleDTO article, BindingResult result, Model model){
        if(result.hasErrors()){
            return "newProduct";
        }


        // Check if stock is an integer
        if (!ValidationUtils.isInteger(String.valueOf(article.getStock()))) {
            result.addError(new FieldError("article", "stock", "Stock must be an integer"));
            return "newProduct";
        }

        // Check if price is a float
        if (!ValidationUtils.isFloat(String.valueOf(article.getPrice()))) {
            result.addError(new FieldError("article", "price", "Price must be a float"));
            return "newProduct";
        }

        // Check if article already exists by email
        Optional<Articles> articleWithName = articlesRepository.findByName(article.getName());
        if (articleWithName.isPresent()) {
            result.addError(new FieldError("article", "name", "Name already exist"));
            return "newProduct";
        }

        // Create a new Articles entity and save it in the database
        Articles newArticle = new Articles();
        newArticle.setName(article.getName());
        newArticle.setPrice(article.getPrice());
        newArticle.setStock(article.getStock());
        newArticle.setImage(article.getImage());
        articlesRepository.save(newArticle);

        return "redirect:/admin/";
    }

    /**
     * Method to delete an article.
     * @param articleId ID of the article to delete.
     * @return redirect to /admin/ page.
     */
    @PostMapping("/admin/deleteArticle")
    public String deleteArticle(@RequestParam("articleId") UUID articleId) {
        try {
            articlesRepository.deleteById(articleId);

        } catch (EmptyResultDataAccessException e) {
            // L'élément n'a pas été trouvé pour cet ID
            System.out.println("could not find Article with this ID");

        }
        return "redirect:/admin/";

    }
    /**
     * Method to add loyalty points to a user.
     * @param userId ID of the user to add points.
     * @param loyaltyPoints Points to add.
     * @return redirect to /admin/user page.
     */
    @PostMapping("/admin/user/addPoints")
    public String addPoints(@RequestParam("userId") UUID userId, @RequestParam("loyaltyPoints") int loyaltyPoints) {
        Users user = usersRepository.getById(userId);
        if (user != null) {
            user.setLoyaltyPoints(loyaltyPoints);
            usersRepository.save(user);
        }
        return "redirect:/admin/user";
    }

}
