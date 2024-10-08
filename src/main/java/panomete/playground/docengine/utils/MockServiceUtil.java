package panomete.playground.docengine.utils;

import org.springframework.stereotype.Service;
import panomete.playground.docengine.entity.*;
import reactor.core.publisher.Mono;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class MockServiceUtil {
    public Mono<Person> mockPersonAsync() {
        Address address = new Address(
                "12 1, mock street ,mock city",
                "mock state",
                "mock country",
                "12345566"
        );
        List<Todo> todoList = new ArrayList<>();
        todoList.add(new Todo(
                "thing 1",
                "not done"
        ));
        todoList.add(new Todo(
                "thing 2",
                "done"
        ));
        todoList.add(new Todo(
                "thing 3",
                "not done"
        ));
        Person person = new Person(
                "1234",
                "John",
                "Doe",
                "123456789",
                Date.valueOf("1990-01-01"),
                address,
                todoList,
                Age.OLD,
                Gender.MALE
        );
        return Mono.just(person);
    }

//    public Person mockPersonSync() {
//        Address address = new Address(
//                "12 1, mock street ,mock city",
//                "mock state",
//                "mock country",
//                "12345566"
//        );
//        return new Person(
//                "1234",
//                "John",
//                "Doe",
//                "123456789",
//                Date.valueOf("1990-01-01"),
//                address
//        );
//    }
//
//
//    public Flux<Person> mockPersonListAsync() {
//        List<Person> person = new ArrayList<>();
//        for(int i=1;i<=10;i++) {
//            Address address = new Address(
//                    "12 1, mock street ,mock city",
//                    "mock state",
//                    "mock country",
//                    "12345566"
//            );
//            person.add(new Person(
//                    "1234",
//                    "John the " + i,
//                    "Doe the " + i,
//                    "123456789",
//                    Date.valueOf("1990-01-01"),
//                    address
//            ));
//        }
//        return Flux.fromIterable(person);
//    }
//
//    public List<Person> mockPersonListSync() {
//        List<Person> person = new ArrayList<>();
//        for(int i=1;i<=10;i++) {
//            Address address = new Address(
//                    "12 1, mock street ,mock city",
//                    "mock state",
//                    "mock country",
//                    "12345566"
//            );
//            person.add(new Person(
//                    "1234",
//                    "John the " + i,
//                    "Doe the " + i,
//                    "123456789",
//                    Date.valueOf("1990-01-01"),
//                    address
//            ));
//        }
//        return person;
//    }
}
