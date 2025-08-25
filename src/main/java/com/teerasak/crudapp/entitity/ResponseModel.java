package com.teerasak.crudapp.entitity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseModel {
    private String status;
    private String message;
    private Object data = null;
}
