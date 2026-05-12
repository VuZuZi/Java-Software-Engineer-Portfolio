package com.lgedv.portfolio.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
@Setter
@Getter
public class Experience {
    private String company;
    private String position;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> responsibilities;

    public Experience(String company, String position,
                      LocalDate startDate, LocalDate endDate,
                      List<String> responsibilities) {
        this.company = company;
        this.position = position;
        this.startDate = startDate;
        this.endDate = endDate;
        this.responsibilities = responsibilities;
    }
}