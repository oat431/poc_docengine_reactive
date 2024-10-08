package panomete.playground.docengine.entity;

import java.sql.Date;
import java.util.List;

public record Person(
        String uuid,
        String firstName,
        String lastName,
        String nationalId,
        Date dateOfBirth,
        Address address,
        List<Todo> todos,
        Age age,
        Gender gender

) {
    public boolean isMale() {
        return gender == Gender.MALE;
    }

    public boolean isFemale() {
        return gender == Gender.FEMALE;
    }

    public boolean isOther() {
        return gender == Gender.OTHER;
    }

    public boolean isOld() {
        return age == Age.OLD;
    }

    public boolean isYoung() {
        return age == Age.YOUNG;
    }

    public boolean isAdult() {
        return age == Age.ADULT;
    }
}
