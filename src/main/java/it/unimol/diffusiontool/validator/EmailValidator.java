package it.unimol.diffusiontool.validator;

import java.util.ArrayList;
import java.util.Collection;

public class EmailValidator {
    private Collection<String> specialChars;
    private Collection<String> domains;
    private Collection<String> providers;
    private static final EmailValidator EMAIL_VALIDATOR = new EmailValidator();

    public static EmailValidator getInstance() {
        return EMAIL_VALIDATOR;
    }

    protected EmailValidator() {
        initSpecialChars();
        initDomains();
        initProviders();
    }

    // todo complete
    protected void initSpecialChars() {
        this.specialChars = new ArrayList<>();
        specialChars.add("#");
        specialChars.add("*");
        specialChars.add(";");
        specialChars.add("'");
        specialChars.add(":");
        specialChars.add("(");
        specialChars.add(")");
        specialChars.add("{");
        specialChars.add("}");
        specialChars.add("$");
        specialChars.add("%");
        specialChars.add("&");
        specialChars.add("/");
        specialChars.add("=");
        specialChars.add("!");
        specialChars.add("|");
        specialChars.add("^");
        specialChars.add("?");
    }

    // todo complete
    protected void initDomains() {
        this.domains = new ArrayList<>();
        domains.add(".com");
        domains.add(".org");
        domains.add(".net");
        domains.add(".int");
        domains.add(".edu");
        domains.add(".gov");
        domains.add(".me");
        domains.add(".us");
        domains.add(".ca");
        domains.add(".eu");
        domains.add(".co.uk");
        domains.add(".au");
        domains.add(".it");
        domains.add(".fr");
        domains.add(".es");
        domains.add(".be");
        domains.add(".cr");
        domains.add(".ck");
        domains.add(".ru");
        domains.add(".cn");
        domains.add(".io");
        domains.add(".dev");
    }

    // todo complete
    protected void initProviders() {
        this.providers = new ArrayList<>();
        providers.add("gmail");
        providers.add("outlook");
        providers.add("hotmail");
        providers.add("libero");
        providers.add("ik");
        providers.add("infomaniak");
        providers.add("protonmail");
        providers.add("yahoo");
        providers.add("msn");
        providers.add("live");
        providers.add("yandex");
    }

    private boolean containsAny(String characterSequence, Collection<String> searchList) {
        for (String str : searchList)
            if (characterSequence.contains(str))
                return true;

        return false;
    }

    public boolean isValid(String email) {
        if (email.startsWith(" "))
            return false;
        if (email.startsWith("@"))
            return false;
        if (email.startsWith("."))
            return false;
        if (email.startsWith("+"))
            return false;
        if (email.startsWith("-"))
            return false;
        if (!email.contains("@"))
            return false;
        if (!email.contains("."))
            return false;
        if (email.endsWith(" "))
            return false;
        if (email.endsWith("@"))
            return false;
        if (email.endsWith("."))
            return false;
        if (email.endsWith("+"))
            return false;
        if (email.endsWith("-"))
            return false;
        if (EMAIL_VALIDATOR.containsAny(email, specialChars))
            return false;
        if (!EMAIL_VALIDATOR.containsAny(email, domains))
            return false;
        if (!EMAIL_VALIDATOR.containsAny(email, providers))
            return false;

        return true;
    }
}
