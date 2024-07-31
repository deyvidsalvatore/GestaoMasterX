package com.deyvidsalvatore.web.gestaomasterx.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UsuarioUtils {

    public static int generateUsuarioId() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMss");
        String formattedDateTime = now.format(formatter);
        int id = Integer.parseInt(formattedDateTime);
        if (id > Integer.MAX_VALUE) {
            id = id % Integer.MAX_VALUE;
        }  
        return id;
    }
}
