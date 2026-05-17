package com.controller;

import com.model.Skill;
import com.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class SkillController {
@Autowired
private SkillRepository skillRepository;

    @GetMapping("/skills")
    public String getSkillsGrouped(Model model) {
        List<Skill> allSkills = skillRepository.findAll();

        Map<String, List<Skill>> skillsByCategory = allSkills.stream()
                .collect(Collectors.groupingBy(skill ->
                        skill.getCategory() != null ? skill.getCategory() : "Khác"
                ));

        model.addAttribute("skillsByCategory", skillsByCategory);
        model.addAttribute("totalSkills", allSkills.size());
        return "skills";
    }
}
