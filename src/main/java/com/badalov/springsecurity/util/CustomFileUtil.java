package com.badalov.springsecurity.util;

import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CustomFileUtil {

    public static String generateUserPhotoFileName(String dateFormat, String fileNamePrefix) {
        Date date = new Date();
        SimpleDateFormat formater = new SimpleDateFormat(dateFormat);
        String dateCreation = formater.format(date);
        return String.join("-", fileNamePrefix, dateCreation);
    }

    private static boolean deleteOldUserPhoto(String imageSource) {
        File oldFile = new File(imageSource);
        boolean deleteResult = false;
        if(oldFile.exists()) {
            deleteResult = oldFile.delete();
        }
        return deleteResult;
    }
}
