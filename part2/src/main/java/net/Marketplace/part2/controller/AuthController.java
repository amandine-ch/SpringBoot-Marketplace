package net.Marketplace.part2.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import net.Marketplace.part2.auth.CustomUserDetailsService;
import net.Marketplace.part2.repository.UsersRepository;
import net.Marketplace.part2.dto.UserDTO;
import net.Marketplace.part2.entity.Users;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;
    private SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    public AuthController(UsersRepository usersRepository, PasswordEncoder passwordEncoder, CustomUserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }
    /**
     * Displays the registration form.
     *
     * @param model The model to be populated with data for rendering the view.
     * @return The name of the registration form view.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        UserDTO dto = new UserDTO();
//        Users dto = new Users();

        model.addAttribute("user", dto);
        return "register";
    }
    /**
     * Registers a new user.
     *
     * @param user   The UserDTO object containing user data.
     * @param result The BindingResult object for validation results.
     * @param model  The model to be populated with data for rendering the view.
     * @return The redirection path after registration.
     */
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute UserDTO user, BindingResult result, Model model){
        if(result.hasErrors()){
            return "register";
        }

        // Check if user already exists by email
        Optional<Users> usersWithEmail = usersRepository.findByEmail(user.getEmail());
        if (usersWithEmail.isPresent()) {
            System.out.println("check user ");
            result.addError(new FieldError("user", "email", "Email already exists"));
            return "register";
        }
        System.out.println("hashcode");
        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        System.out.println("create user");
        // Create a new Users entity and save it in the database
        Users newUser = new Users();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        usersRepository.save(newUser);

        return "redirect:/login";
    }
    /**
     * Displays the login form.
     *
     * @param model The model to be populated with data for rendering the view.
     * @return The name of the login form view.
     */
    @GetMapping("/login")
    public String showLoginForm(Model model){
        UserDTO dto = new UserDTO();
        model.addAttribute("user", dto);
        return "login";
    }
    /**
     * Logs in the user.
     *
     * @param userDTO The UserDTO object containing user data.
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @param session The HTTP session.
     * @return The redirection path after login.
     */
    @PostMapping("/login")
    public String login(@ModelAttribute("user") UserDTO userDTO, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userDTO.getEmail());
            // Créer un token d'authentification avec les informations de l'utilisateur
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, userDTO.getPassword(), userDetails.getAuthorities());


            // Authentifier l'utilisateur en utilisant l'AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            // Stocker l'authentification dans le contexte de sécurité
            SecurityContext context =  SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context, request, response);

            // Rediriger en fonction du rôle après une connexion réussie
            Users user = (Users) userDetails;
            if (session.getAttribute("fromCart") != null) {
                // S'il vient du panier, retirez la variable de session et redirigez-le vers order-form
                session.removeAttribute("fromCart");
                return "redirect:/order/";
            } else {
                if (user.getAdmin()) {
                    return "redirect:/admin/";
                }
                return "redirect:/";
            }

        } catch (Exception e) {
            // Gérer les erreurs d'authentification (par exemple, mot de passe incorrect)
            return "redirect:/login";
        }
    }
}
