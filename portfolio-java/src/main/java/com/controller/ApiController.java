package com.controller;

import com.model.Message;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiController {

    private static final String ADMIN_EMAIL = "markdoan38@gmail.com";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

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
        if (oAuth2User != null) {
            response.put("user", buildUserInfo(oAuth2User));
            response.put("admin", ADMIN_EMAIL.equals(oAuth2User.getAttribute("email")));
        }
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

    @GetMapping("/messages/conversations")
    public ResponseEntity<List<Map<String, Object>>> getConversations(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = oAuth2User.getAttribute("email");
        boolean isAdmin = ADMIN_EMAIL.equals(email);
        List<Message> userMessages = messageService.getMessagesForParticipant(email);

        Map<String, List<Message>> conversationMap = userMessages.stream()
                .collect(Collectors.groupingBy(Message::getConversationId));

        List<Map<String, Object>> conversations = conversationMap.entrySet().stream()
                .map(entry -> buildConversationResponse(entry.getKey(), entry.getValue(), email, isAdmin))
                .sorted((a, b) -> b.get("lastAt").toString().compareTo(a.get("lastAt").toString()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/messages/conversation/{conversationId}")
    public ResponseEntity<Map<String, Object>> getConversationById(@AuthenticationPrincipal OAuth2User oAuth2User,
                                                                   @PathVariable String conversationId) {
        if (oAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = oAuth2User.getAttribute("email");
        List<Message> messages = messageService.getMessagesByConversationId(conversationId);

        if (messages.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        messageService.markMessagesAsRead(conversationId, email);
        return ResponseEntity.ok(buildConversationDetailResponse(messages, email));
    }

    @PostMapping("/messages/send")
    public ResponseEntity<Map<String, Object>> sendMessage(@AuthenticationPrincipal OAuth2User oAuth2User,
                                                           @RequestBody SendMessageRequest request) {
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
        String receiverEmail = (isAdmin && request.receiverEmail() != null && !request.receiverEmail().isBlank())
                ? request.receiverEmail().trim()
                : ADMIN_EMAIL;

        String conversationId = generateConversationId(subject, senderEmail, receiverEmail);

        Message message = new Message();
        message.setConversationId(conversationId);
        message.setSender(senderName);
        message.setSenderEmail(senderEmail);
        message.setReceiverEmail(receiverEmail);
        message.setSubject(subject);
        message.setContent(content);
        message.setSentAt(LocalDateTime.now());

        Message saved = messageService.saveMessage(message);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Tin nhan da duoc gui thanh cong!",
                "data", formatMessage(saved, senderEmail)
        ));
    }

    @PostMapping("/messages/conversation/{conversationId}/read")
    public ResponseEntity<Map<String, Boolean>> markAsRead(@AuthenticationPrincipal OAuth2User oAuth2User,
                                                           @PathVariable String conversationId) {
        if (oAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        messageService.markMessagesAsRead(conversationId, oAuth2User.getAttribute("email"));
        return ResponseEntity.ok(Map.of("success", true));
    }

    // Helper methods
    private Map<String, Object> buildConversationResponse(String conversationId, List<Message> messages,
                                                          String currentEmail, boolean isAdmin) {
        messages.sort(Comparator.comparing(Message::getSentAt));
        Message firstMessage = messages.get(0);
        Message lastMessage = messages.get(messages.size() - 1);

        String otherParticipant = getOtherParticipant(firstMessage, currentEmail);

        Map<String, Object> conversation = new HashMap<>();
        conversation.put("id", conversationId);
        conversation.put("subject", firstMessage.getSubject());
        conversation.put("sender", isAdmin ? getParticipantName(otherParticipant) : firstMessage.getSender());
        conversation.put("lastMessage", lastMessage.getContent());
        conversation.put("lastAt", formatTime(lastMessage.getSentAt()));
        conversation.put("unreadCount", messages.stream()
                .filter(m -> m.getReceiverEmail().equals(currentEmail) && !m.isRead())
                .count());
        conversation.put("messages", messages.stream()
                .map(m -> formatMessage(m, currentEmail))
                .collect(Collectors.toList()));
        return conversation;
    }

    private Map<String, Object> buildConversationDetailResponse(List<Message> messages, String currentEmail) {
        Message firstMessage = messages.get(0);
        Map<String, Object> conversation = new HashMap<>();
        conversation.put("id", firstMessage.getConversationId());
        conversation.put("subject", firstMessage.getSubject());
        conversation.put("sender", getOtherParticipant(firstMessage, currentEmail));
        conversation.put("messages", messages.stream()
                .map(m -> formatMessage(m, currentEmail))
                .collect(Collectors.toList()));
        conversation.put("unreadCount", messages.stream()
                .filter(m -> m.getReceiverEmail().equals(currentEmail) && !m.isRead())
                .count());
        return conversation;
    }

    private String getOtherParticipant(Message message, String currentEmail) {
        if (message.getSenderEmail().equals(currentEmail)) {
            return message.getReceiverEmail();
        }
        return message.getSenderEmail();
    }

    private String getParticipantName(String email) {
        if (ADMIN_EMAIL.equals(email)) {
            return "Admin";
        }
        return userRepository.findByEmail(email)
                .map(User::getDisplayName)
                .orElse(email.split("@")[0]);
    }

    private String generateConversationId(String subject, String email1, String email2) {
        List<String> emails = Arrays.asList(email1, email2);
        Collections.sort(emails);
        return subject + "_" + emails.get(0) + "_" + emails.get(1);
    }

    private String formatTime(LocalDateTime time) {
        if (time == null) return "";
        if (time.toLocalDate().equals(LocalDateTime.now().toLocalDate())) {
            return time.format(TIME_FORMATTER);
        }
        return time.format(DATE_FORMATTER);
    }

    private Map<String, Object> formatMessage(Message message, String currentEmail) {
        Map<String, Object> formatted = new HashMap<>();
        formatted.put("id", message.getId());
        formatted.put("content", message.getContent());
        formatted.put("fromMe", message.getSenderEmail().equals(currentEmail));
        formatted.put("name", message.getSender());
        formatted.put("time", formatTime(message.getSentAt()));
        return formatted;
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

    // Request DTOs
    public record SendMessageRequest(String subject, String content, String receiverEmail, String conversationId) {}
}