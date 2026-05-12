package com.lgedv.portfolio.model;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Skill {
    private String category;
    private String items;
    private String iconClass;

    public Skill(String category, String items, String iconClass) {
        this.category = category;
        this.items = items;
        this.iconClass = iconClass;
    }

}