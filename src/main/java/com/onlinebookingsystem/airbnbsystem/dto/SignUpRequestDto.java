package com.onlinebookingsystem.airbnbsystem.dto;


import lombok.Data;

//todo :- need to add validator in this dto
@Data
public class SignUpRequestDto {

    private String name;
    private  String email;
    private String password;
}
