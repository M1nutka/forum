package com.noname.forum.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@AllArgsConstructor 
public class RefreshJwtRequest {
    public  String refreshToken;
}
