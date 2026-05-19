package com.controller;

import com.model.Message;
import com.model.MessageStatus;
import com.model.Project;
import com.model.Skill;
import com.model.User;
import com.repository.ProjectRepository;
import com.repository.SkillRepository;
import com.repository.UserRepository;
import com.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private static final String ADMIN_EMAIL = "markdoan38@gmail.com";

    private final ProjectRepository projectRepository;
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    private final MessageService messageService;

    public ApiController(ProjectRepository projectRepository,
                         SkillRepository skillRepository,
                         UserRepository userRepository,
                         MessageService messageService) {
        this.projectRepository = projectRepository;
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
        this.messageService = messageService;
    }

    @GetMapping("/auth/me")
    public Map<String, Object> currentUser(@AuthenticationPrincipal OAuth2User oAuth2User) {
        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", oAuth2User != null);

        if (oAuth2User == null) {
            return response;
        }

        String email = oAuth2User.getAttribute("email");
        response.put("user", buildUserInfo(oAuth2User));
        response.put("admin", ADMIN_EMAIL.equals(email));
        return response;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        saveUserIfNotExists(oAuth2User);

        Map<String, Object> response = new HashMap<>();
        response.put("user", buildUserInfo(oAuth2User));
        response.put("stats", Map.of(
                "projectsCount", projectRepository.count(),
                "skillsCount", skillRepository.count(),
                "avgMatch", 86
        ));
        response.put("projects", projectRepository.findAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/projects")
    public List<Project> projects() {
        return projectRepository.findAll();
    }

    @GetMapping("/skills")
    public List<Skill> skills() {
        return skillRepository.findAll();
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> messages(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = oAuth2User.getAttribute("email");
        return ResponseEntity.ok(messageService.getMessagesForParticipant(email));
    }

    @PostMapping("/messages")
    public ResponseEntity<Map<String, Object>> sendMessage(@AuthenticationPrincipal OAuth2User oAuth2User,
                                                           @RequestBody MessageRequest request) {
        if (oAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Vui long dang nhap!"));
        }

        String subject = request.subject() == null ? "" : request.subject().trim();
        String content = request.content() == null ? "" : request.content().trim();

        if (subject.isEmpty() || content.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Vui long nhap tieu de va noi dung!"));
        }

        String senderEmail = oAuth2User.getAttribute("email");
        String senderName = oAuth2User.getAttribute("name");
        boolean isAdmin = ADMIN_EMAIL.equals(senderEmail);
        String receiverEmail = isAdmin && request.receiverEmail() != null && !request.receiverEmail().isBlank()
                ? request.receiverEmail().trim()
                : ADMIN_EMAIL;

        Message message = new Message();
        message.setName(senderName);
        message.setEmail(senderEmail);
        message.setReceiverEmail(receiverEmail);
        message.setSubject(subject);
        message.setContent(content);
        message.setStatus(MessageStatus.UNREAD);
        message.setSentAt(LocalDateTime.now());
        message.setEmailRe(receiverEmail);

        Message saved = messageService.saveMessage(message);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Tin nhan da duoc gui thanh cong!");
        response.put("data", saved);
        return ResponseEntity.ok(response);
    }

    private Map<String, Object> buildUserInfo(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        User user = userRepository.findByEmail(email).orElse(null);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("email", email);
        userInfo.put("name", oAuth2User.getAttribute("name"));
        userInfo.put("picture", oAuth2User.getAttribute("picture"));
        userInfo.put("role", user != null && user.getRole() != null ? user.getRole().name() : "USER");
        return userInfo;
    }

    private void saveUserIfNotExists(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        if (email == null || userRepository.existsByEmail(email)) {
            return;
        }

        User user = new User();
        user.setEmail(email);
        user.setDisplayName(oAuth2User.getAttribute("name"));
        user.setAvatarUrl(oAuth2User.getAttribute("picture"));
        userRepository.save(user);
    }

    public record MessageRequest(String subject, String content, String receiverEmail) {
    }
}
