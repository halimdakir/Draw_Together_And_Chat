package Labb3.String;

import java.util.ArrayList;
import java.util.List;

public class StringHandle {


    //-------------- Check coming string if a message or a shape
    public static String checkTypeOfShape(String message){
        List<String> stringList = new ArrayList<>();
        String shapeType = "";
        String[] words=message.split(" ");
        int l = words.length;

        for (int i= 0; i<l; i++){
            stringList.add(words[i]);
        }

        //Type of the Shape
        if (stringList.size()>1) {
            String shapeTypeTemp = stringList.get(1);
            char[] chars = new char[shapeTypeTemp.length()-1];
            shapeTypeTemp.getChars(1, shapeTypeTemp.length(), chars, 0);
            for (int i = 0; i < chars.length; i++) {
                shapeType += chars[i];
            }
            if (shapeType.equals("rect") ||shapeType.equals("circle"))
            return shapeType;
        }
        return "message";
    }

}


