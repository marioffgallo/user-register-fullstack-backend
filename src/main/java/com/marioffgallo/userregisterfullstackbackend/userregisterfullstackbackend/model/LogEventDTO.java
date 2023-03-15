package com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogEventDTO {
    private int id;
    private String action;
    private Date date;
    private String payload;

}

