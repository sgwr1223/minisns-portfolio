package com.example.demo.usercontroller;

import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // =====================
    // 登録
    // =====================
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        // 重複チェック（email）
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            model.addAttribute("message", "そのメールアドレスはすでに登録されています。");
            return "register";
        }

        userRepository.save(user);
        model.addAttribute("message", "登録が完了しました！");
        return "success";
    }

    // =====================
    // ログイン / ログアウト
    // =====================
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {

        Optional<User> userOpt = userRepository.findByEmailAndPassword(email, password);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            session.setAttribute("loginUser", user);
            return "redirect:/mypage";
        } else {
            model.addAttribute("message", "メールアドレスまたはパスワードが間違っています。");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, Model model) {
        session.invalidate();
        model.addAttribute("message", "ログアウトしました。");
        return "login";
    }

    // =====================
    // マイページ
    // =====================
    @GetMapping("/mypage")
    public String mypage(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", loginUser);
        return "mypage";
    }

    // =====================
    // 退会
    // =====================
    @GetMapping("/delete")
    public String showDeleteForm() {
        return "delete";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam String email,
                             @RequestParam String password,
                             HttpSession session,
                             Model model) {

        Optional<User> userOpt = userRepository.findByEmailAndPassword(email, password);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            userRepository.delete(user);
            session.invalidate();
            model.addAttribute("message", "削除しました。");
            return "index";
        } else {
            model.addAttribute("message", "メールアドレスかパスワードが間違っています。");
            return "delete";
        }
    }

    // =====================
    // プロフィール閲覧
    // =====================
    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", loginUser);
        return "profile"; // profile.html（閲覧専用）
    }

    // =====================
    // プロフィール編集
    // =====================
    @GetMapping("/profile/edit")
    public String showEditProfile(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", loginUser);
        return "editProfile"; // editProfile.html
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@RequestParam String email,
                                @RequestParam String password,
                                @RequestParam String name,
                                @RequestParam(required = false) String profile,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        User user = userRepository.findById(loginUser.getId()).orElse(null);
        if (user != null) {
            user.setEmail(email);
            user.setPassword(password);
            user.setName(name);
            user.setProfile(profile);

            userRepository.save(user);
            session.setAttribute("loginUser", user);

            redirectAttributes.addFlashAttribute("message", "プロフィールを更新しました！");
            return "redirect:/mypage";
        } else {
            redirectAttributes.addFlashAttribute("message", "ユーザーが存在しません。");
            return "redirect:/profile/edit";
        }
    }
}
