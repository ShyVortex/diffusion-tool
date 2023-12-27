package it.unimol.diffusiontool.validator;

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
        if (date.isEqual(this.currentDate)) {
            return false;
        } else if (date.isAfter(this.currentDate)) {
            return false;
        } else {
            return ChronoUnit.DAYS.between(date, this.currentDate) > 365L;
        }
    }
}
