package com.softdesign.devintensive.utils.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by savos on 03.07.2016.
 */

public class TextValueValidator {
    public enum Type {Phone, Email, Http, HttpPrefix};

    private static Pattern patternEmail = Pattern.compile("^[\\p{L}\\p{N}\\._%+-]+@[\\p{L}\\p{N}\\.\\-]+\\.[\\p{L}]{2,}$");
    private static Pattern patternPhone = Pattern.compile("[+7|8]+[0-9]{10}");

    public static boolean validate(Type type, String text){
        return validate(type, text, "");
    }

    public static boolean validate(Type type, String text, String prefixForHttpPrefix){
        boolean isValid = false;
        switch (type){
            case Phone:
                    isValid = validatePhone(text);
                break;
            case Email:
                    isValid = validateEmail(text);
                break;
            case Http:
                break;
            case HttpPrefix:
                break;
        }
        return isValid;
    }

    private static boolean validatePhone(String text){
        text = text.replace(" ", "");
        text = text.replace("(", "");
        text = text.replace(")", "");
        text = text.replace("-", "");

        return patternPhone.matcher(text).matches();
    }

    private static boolean validateEmail(String text){

        return patternEmail.matcher(text).matches();
    }
}
