package com.manager.control.finance.dtos;

import com.manager.control.finance.utils.LocalDateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessageDTO {

    private String message;
    private String timestamp;

    public ResponseMessageDTO(String message) {
        this.message = message;
        this.timestamp = LocalDateUtil.formatterLocalDateTime(LocalDateTime.now());
    }
}
