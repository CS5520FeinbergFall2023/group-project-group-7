package com.NEU23FallGroup7.jobtrackpro.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GetCurTime {
    public static String getTime(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        return formattedDateTime;
    }
}
