package com.controller;

import com.model.Message;
import com.model.MessageStatus;
import com.model.User;
import com.repository.UserRepository;
import com.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Message Controller handles all contact and messaging operations.
 *
 * @author Portfolio Platform Team
 * @version 1.0
 */
@Controller
public class MessageController {

    private static final String ADMIN_EMAIL = "markdoan38@gmail.com";
    private static final String REDIRECT_CONTACT = "redirect:/contact";
    private static final String REDIRECT_LOGIN = "redirect:/login";
    private static final String REDIRECT_DASHBOARD = "redirect:/dashboard";

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Displays the contact page with contact form and sent messages history.
     *
     * @param oAuth2User the currently authenticated OAuth2 user
     * @param model the Spring MVC model to hold attributes for the view
     * @return the contact page view name or redirect to login if not authenticated
     */
    @GetMapping("/contact")
    public String showContactForm(@AuthenticationPrincipal OAuth2User oAuth2User, Model model) {
        if (oAuth2User == null) {
            return REDIRECT_LOGIN + "?redirect=contact";
        }

        String userEmail = oAuth2User.getAttribute("email");
        String userName = oAuth2User.getAttribute("name");
        String userPicture = oAuth2User.getAttribute("picture");

        buildUserModel(model, userName, userEmail, userPicture);

        boolean isAdmin = ADMIN_EMAIL.equals(userEmail);
        List<Message> displayMessages;

        if (isAdmin) {
            displayMessages = messageService.getMessagesByReceiverEmail(ADMIN_EMAIL);
        } else {
            displayMessages = messageService.getMessagesByEmail(userEmail);
        }

        model.addAttribute("displayMessages", displayMessages);

        // Group messages by subject - TRUYEN DU 3 THAM SO
        List<Map<String, Object>> groupedMessages = groupMessagesBySubject(displayMessages, isAdmin, userEmail);
        model.addAttribute("groupedMessages", groupedMessages);
        model.addAttribute("isAdmin", isAdmin);

        User adminUser = userRepository.findByEmail(ADMIN_EMAIL).orElse(null);
        model.addAttribute("adminUser", adminUser);

        Message message = new Message();
        message.setName(userName);
        message.setEmail(userEmail);
        message.setReceiverEmail(ADMIN_EMAIL);
        model.addAttribute("message", message);

        return "contact";
    }

    /**
     * Groups messages by subject for display in conversation list.
     *
     * @param messages list of messages to group
     * @param isAdmin flag indicating if current user is admin
     * @param currentUserEmail email of currently logged in user
     * @return list of message groups
     */
    private List<Map<String, Object>> groupMessagesBySubject(List<Message> messages, boolean isAdmin, String currentUserEmail) {
        Map<String, Map<String, Object>> groups = new LinkedHashMap<>();

        for (Message msg : messages) {
            String subject = msg.getSubject();
            if (subject == null || subject.trim().isEmpty()) {
                continue;
            }

            if (!groups.containsKey(subject)) {
                // Xac dinh nguoi khac trong cuoc tro chuyen
                String otherPartyEmail = msg.getEmail().equals(currentUserEmail) ? msg.getReceiverEmail() : msg.getEmail();

                Map<String, Object> group = new HashMap<>();
                group.put("subject", subject);
                group.put("messages", new ArrayList<Message>());
                group.put("messageCount", 0);
                group.put("unreadCount", 0);
                group.put("otherPartyEmail", otherPartyEmail);
                groups.put(subject, group);
            }

            Map<String, Object> group = groups.get(subject);
            @SuppressWarnings("unchecked")
            List<Message> groupMessages = (List<Message>) group.get("messages");
            groupMessages.add(msg);
            group.put("messageCount", (int) group.get("messageCount") + 1);
            group.put("lastMessage", msg);

            if (isAdmin && msg.getStatus() != MessageStatus.READ && !ADMIN_EMAIL.equals(msg.getEmail())) {
                group.put("unreadCount", (int) group.get("unreadCount") + 1);
            }
        }

        List<Map<String, Object>> result = new ArrayList<>(groups.values());
        result.sort((a, b) -> {
            Message msgA = (Message) a.get("lastMessage");
            Message msgB = (Message) b.get("lastMessage");
            if (msgA.getSentAt() == null || msgB.getSentAt() == null) {
                return 0;
            }
            return msgB.getSentAt().compareTo(msgA.getSentAt());
        });

        return result;
    }

    /**
     * Processes and saves a new message sent from the contact form.
     *
     * @param oAuth2User the currently authenticated OAuth2 user
     * @param message the message object populated from form data
     * @param redirectAttributes attributes for redirect scenario
     * @return redirect to contact page or login if not authenticated
     */
    @PostMapping("/contact/send")
    @ResponseBody
    public Map<String, Object> sendMessage(@AuthenticationPrincipal OAuth2User oAuth2User,
                                           @ModelAttribute Message message) {
        Map<String, Object> response = new HashMap<>();

        if (oAuth2User == null) {
            response.put("success", false);
            response.put("message", "Vui long dang nhap!");
            return response;
        }

        String userEmail = oAuth2User.getAttribute("email");
        String userName = oAuth2User.getAttribute("name");

        message.setEmail(userEmail);
        message.setName(userName);
        message.setReceiverEmail(ADMIN_EMAIL);
        message.setSentAt(LocalDateTime.now());
        message.setStatus(MessageStatus.UNREAD);
        message.setEmailRe(ADMIN_EMAIL);

        messageService.saveMessage(message);

        response.put("success", true);
        response.put("message", "Tin nhan da duoc gui thanh cong!");
        return response;
    }

    /**
     * Provides REST API endpoint to fetch messages for AJAX refresh.
     *
     * @param oAuth2User the currently authenticated OAuth2 user
     * @return list of messages for current user
     */
    @GetMapping("/contact/messages")
    @ResponseBody
    public List<Message> getMessages(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User == null) {
            return List.of();
        }
        String userEmail = oAuth2User.getAttribute("email");
        if (ADMIN_EMAIL.equals(userEmail)) {
            return messageService.getMessagesByReceiverEmail(ADMIN_EMAIL);
        } else {
            return messageService.getMessagesByEmail(userEmail);
        }
    }

    /**
     * Displays the admin message management page.
     *
     * @param oAuth2User the currently authenticated OAuth2 user
     * @param model the Spring MVC model to hold attributes for the view
     * @return admin messages page or redirect to dashboard if not admin
     */
    @GetMapping("/admin/messages")
    public String manageMessages(@AuthenticationPrincipal OAuth2User oAuth2User, Model model) {
        if (oAuth2User == null) {
            return REDIRECT_LOGIN;
        }

        String email = oAuth2User.getAttribute("email");
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null || user.getRole() == null || !user.getRole().name().equals("ADMIN")) {
            return REDIRECT_DASHBOARD;
        }

        model.addAttribute("messages", messageService.getAllMessages());
        model.addAttribute("unreadCount", messageService.getUnreadCount());

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", oAuth2User.getAttribute("name"));
        userInfo.put("email", email);
        userInfo.put("picture", oAuth2User.getAttribute("picture"));
        model.addAttribute("user", userInfo);

        return "admin/messages";
    }

    /**
     * Marks a specific message as read by admin.
     *
     * @param id the unique identifier of the message to mark as read
     * @return redirect back to admin messages page
     */
    @PostMapping("/admin/messages/{id}/read")
    public String markAsRead(@PathVariable Long id) {
        messageService.markAsRead(id);
        return "redirect:/admin/messages";
    }

    /**
     * Deletes a specific message from the system.
     *
     * @param id the unique identifier of the message to delete
     * @return redirect back to admin messages page
     */
    @PostMapping("/admin/messages/{id}/delete")
    public String deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return "redirect:/admin/messages";
    }

    /**
     * Builds the user information model for the contact page.
     *
     * @param model the Spring MVC model to populate
     * @param name the user's display name
     * @param email the user's email address
     * @param picture the user's profile picture URL
     */
    private void buildUserModel(Model model, String name, String email, String picture) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", name);
        userInfo.put("email", email);
        userInfo.put("picture", picture);
        model.addAttribute("user", userInfo);
    }
}