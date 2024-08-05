package com.deyvidsalvatore.web.gestaomasterx.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class FuncionarioUtils {
    
    private static final LocalDateTime EPOCH = LocalDateTime.of(1970, 1, 1, 0, 0);

    public static int generateFuncionarioId() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmm");
        String formattedDateTime = now.format(formatter);
        
        LocalDateTime dateTime = LocalDateTime.parse(formattedDateTime, formatter);
        long millis = ChronoUnit.MILLIS.between(EPOCH, dateTime);

        int id = (int) (millis % Integer.MAX_VALUE);

        return id;
    }
    
    public static int generateDepartamentoId() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String formattedDateTime = now.format(formatter);
        long id = Long.parseLong(formattedDateTime);
        if (id > Integer.MAX_VALUE) {
            id = id % Integer.MAX_VALUE;
        }  
        return (int) id;
    }
}
