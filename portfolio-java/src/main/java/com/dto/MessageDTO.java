package com.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageDTO {
    private Long id;
    private String content;
    private boolean fromMe;
    private String name;
    private String time;
}