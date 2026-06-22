package util;

import java.io.File;

public class ExistCheck {
    public static <T> T noNull(T object){
        if(object==null){
            throw new IllegalArgumentException("❌ Error: Target record not found or required data is missing!");
        }
        return object;
    }

    public static void fileMkdir(File file){
        if(!file.exists()){
            file.mkdir();
        }
    }
}
