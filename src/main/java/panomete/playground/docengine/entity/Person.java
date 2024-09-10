package panomete.playground.docengine.entity;

import java.sql.Date;

public record Person(
        String uuid,
        String firstName,
        String lastName,
        String nationalId,
        Date dateOfBirth,
        Address address

) {
}
