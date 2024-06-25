package com.manager.control.finance.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateUtil {
    public static String formatterLocalDateTime(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return date.format(formatter);
    }



}
