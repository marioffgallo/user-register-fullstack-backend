package com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogEvent {
    private String action;
    private Date date;
    private String payload;

}

