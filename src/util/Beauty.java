package util;

public class Beauty {
    public static String beauty(String og){
        Validator.validString(og);
        String clean = og.trim();
        clean = clean.replaceAll("\\s+"," ");
        clean = clean.toLowerCase();

        char[] temp = clean.toCharArray();
        temp[0] = Character.toUpperCase(temp[0]);

        for(int i=0;i< temp.length;i++){
            if(Character.isWhitespace(temp[i])){
                temp[i+1]=Character.toUpperCase(temp[i+1]);
            }
        }
        return new String(temp);
    }

    public static String formatStringLength(String text, int maxLength) {
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }

    public static String formatMemNameLength(String name,int maxLength){
        if(name.length()<=maxLength){
            return name;
        }

        String[] part = name.split(" ");
        if(part.length<=2){
            return name.substring(0,maxLength-3) + "...";
        }

        StringBuilder shortName = new StringBuilder(part[0] + " ");
        for (int i = 1; i < part.length - 1; i++) {
            shortName.append(part[i].charAt(0)).append(". ");
        }
        shortName.append(part[part.length-1]);

        if(shortName.length()>=maxLength){
            return shortName.substring(0,maxLength) + "...";
        }

        return shortName.toString();
    }
}
