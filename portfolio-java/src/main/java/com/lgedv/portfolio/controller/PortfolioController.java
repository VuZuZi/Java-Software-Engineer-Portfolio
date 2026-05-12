package com.lgedv.portfolio.controller;

import com.lgedv.portfolio.model.Experience;
import com.lgedv.portfolio.model.Project;
import com.lgedv.portfolio.model.Skill;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Controller
public class PortfolioController {

    @GetMapping("/")
    public String home(Model model) {
        // Thông tin cá nhân
        model.addAttribute("name", "Nguyễn Văn An");
        model.addAttribute("title", "Java Software Engineer");
        model.addAttribute("target", "LG Electronics Development Vietnam");
        model.addAttribute("location", "Đà Nẵng, Vietnam");
        model.addAttribute("email", "nguyenvanan@example.com");
        model.addAttribute("phone", "+84 123 456 789");
        model.addAttribute("linkedin", "linkedin.com/in/nguyenvanan");
        model.addAttribute("github", "github.com/nguyenvanan");

        // Tóm tắt
        model.addAttribute("summary",
                "Java Developer với 3+ năm kinh nghiệm trong phát triển hệ thống Automotive, " +
                        "đặc biệt là Infotainment (AVN) và middleware protocols. Thành thạo OOP, Design Patterns, " +
                        "và quy trình phát triển Agile với Git/Gerrit/JIRA. Mong muốn đóng góp cho LGEDV trong lĩnh vực " +
                        "linh kiện ô tô thông minh và thân thiện với môi trường."
        );

        // Kỹ năng
        List<Skill> skills = Arrays.asList(
                new Skill("Core Java", "Java 17, Kotlin, OOP, Design Patterns, Multithreading", "fab fa-java"),
                new Skill("Automotive", "AVN, HMI Framework, Middleware Integration, CAN Protocol", "fas fa-car"),
                new Skill("Tools", "Git, Gerrit, JIRA, Confluence, Gradle, Maven", "fas fa-tools"),
                new Skill("Methodology", "Agile, Scrum, V-model, UML, Clean Code", "fas fa-project-diagram"),
                new Skill("Soft Skills", "English (TOEIC 850), Teamwork, Report to Management", "fas fa-language")
        );
        model.addAttribute("skills", skills);

        // Dự án
        List<Project> projects = Arrays.asList(
                new Project(
                        "1",
                        "Smart Cluster Simulator for AVN",
                        "Java 17, Kotlin, Git/Gerrit, JIRA, Gradle",
                        "Xây dựng module xử lý real-time cho In-Vehicle Infotainment System, mô phỏng giao tiếp CAN bus với middleware. Áp dụng các Design Patterns Observer, Factory, Singleton để tối ưu hiệu năng và khả năng mở rộng.",
                        "📈 Giảm 30% thời gian tích hợp module mới | ⚡ Xử lý 200+ events/giây | 📝 Code coverage 95%",
                        "/images/project1.jpg",
                        "https://github.com/nguyenvanan/avn-simulator",
                        "https://demo.avnsimulator.com"
                ),
                new Project(
                        "2",
                        "Android Framework Integration - Telematics",
                        "Android Framework, Java, AOSP, Binder IPC",
                        "Phát triển và tùy chỉnh Android Framework layer cho hệ thống Telematics automotive. Tích hợp các dịch vụ hệ thống với HMI applications thông qua Binder IPC.",
                        "📱 Giảm 40% thời gian khởi động hệ thống | 🔧 Tích hợp thành công 5+ dịch vụ mới",
                        "/images/project2.jpg",
                        "https://github.com/nguyenvanan/android-telematics",
                        null
                ),
                new Project(
                        "3",
                        "Middleware Protocol Bridge",
                        "Java, Socket Programming, Custom Protocol, MQTT",
                        "Thiết kế và implement bridge kết nối giữa HMI layer và middleware systems. Xây dựng protocol tùy chỉnh để đảm bảo độ trễ thấp và độ tin cậy cao cho dữ liệu xe.",
                        "🔌 Hỗ trợ 3+ loại protocols | ⏱️ Độ trễ trung bình <50ms | 🛡️ 99.99% uptime",
                        "/images/project3.jpg",
                        "https://github.com/nguyenvanan/protocol-bridge",
                        null
                )
        );
        model.addAttribute("projects", projects);

        // Kinh nghiệm làm việc
        List<Experience> experiences = Arrays.asList(
                new Experience(
                        "Công ty Công nghệ ABC",
                        "Java Developer (Automotive)",
                        LocalDate.of(2022, 6, 1),
                        LocalDate.now(),
                        Arrays.asList(
                                "Phát triển HMI modules cho dashboard ô tô thông minh",
                                "Làm việc với team quốc tế sử dụng JIRA và Gerrit",
                                "Báo cáo tiến độ 2 tuần/lần cho ban quản lý",
                                "Tham gia code review và viết unit tests với JUnit 5"
                        )
                ),
                new Experience(
                        "Công ty Giải pháp XYZ",
                        "Junior Java Developer",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2022, 5, 31),
                        Arrays.asList(
                                "Xây dựng REST APIs với Spring Boot",
                                "Tối ưu database queries với Hibernate và JPA",
                                "Viết tài liệu kỹ thuật và user guides bằng tiếng Anh"
                        )
                )
        );
        model.addAttribute("experiences", experiences);

        return "index";
    }
}