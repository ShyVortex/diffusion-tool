package it.unimol.diffusiontool.validators;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BirthdateValidator {
    private final LocalDate currentDate = LocalDate.now();
    private static final BirthdateValidator BIRTHDATE_VALIDATOR = new BirthdateValidator();

    public BirthdateValidator() {
    }

    public static BirthdateValidator getInstance() {
        return BIRTHDATE_VALIDATOR;
    }

    public boolean isValid(LocalDate date) {
        if (date == null)
            return false;
        if (date.isEqual(this.currentDate))
            return false;
        if (date.isAfter(this.currentDate))
            return false;
        if (date.isBefore(LocalDate.of(1915, 12, 30)))
            return false;

        return ChronoUnit.DAYS.between(date, this.currentDate) > 3650L;
    }
}
