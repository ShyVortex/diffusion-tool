package it.unimol.diffusiontool.validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class UsernameValidator {
    private Collection<String> specialChars;
    private static final UsernameValidator USERNAME_VALIDATOR = new UsernameValidator();

    public static UsernameValidator getInstance() {
        return USERNAME_VALIDATOR;
    }

    protected UsernameValidator() {
        this.initSpecialChars();
    }

    protected void initSpecialChars() {
        this.specialChars = new ArrayList();
        this.specialChars.add("#");
        this.specialChars.add("*");
        this.specialChars.add(";");
        this.specialChars.add("'");
        this.specialChars.add(":");
        this.specialChars.add("(");
        this.specialChars.add(")");
        this.specialChars.add("{");
        this.specialChars.add("}");
        this.specialChars.add("$");
        this.specialChars.add("%");
        this.specialChars.add("&");
        this.specialChars.add("/");
        this.specialChars.add("=");
        this.specialChars.add("!");
        this.specialChars.add("|");
        this.specialChars.add("^");
        this.specialChars.add("?");
        this.specialChars.add("+");
        this.specialChars.add("-");
        this.specialChars.add("@");
        this.specialChars.add(".");
    }

    private boolean containsAny(String characterSequence, Collection<String> searchList) {
        Iterator var3 = searchList.iterator();

        String str;
        do {
            if (!var3.hasNext()) {
                return false;
            }

            str = (String)var3.next();
        } while(!characterSequence.contains(str));

        return true;
    }

    public boolean isValid(String username) {
        return !USERNAME_VALIDATOR.containsAny(username, this.specialChars);
    }
}