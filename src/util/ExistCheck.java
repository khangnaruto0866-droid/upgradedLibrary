package util;

public class ExistCheck {
    public static <T> T noNull(T object){
        if(object==null){
            throw new IllegalArgumentException("❌ Error: Target record not found or required data is missing!");
        }
        return object;
    }
}
