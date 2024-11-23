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
                "28 ม.0 ต.ท่าขุนธนบุรี",
                "อ.เมืองกำแพงทองคำ จ.กำแพงทองคำ",
                "รัฐสยาม",
                "62000"
        );
        List<Todo> todoList = new ArrayList<>();
        todoList.add(new Todo(
                "สิ่งที่ 1",
                "ยังไม่เสร็จ"
        ));
        todoList.add(new Todo(
                "สิ่งที่ 2",
                "เสร็จแล้ว"
        ));
        todoList.add(new Todo(
                "สิ่งที่ 3",
                "ยังไม่เสร็จ"
        ));
        Person person = new Person(
                "b3c60307-d35b-47c6-bb7c-25ecd4355c07",
                "ฌาณหส",
                "ทิพย์พิมพ์วงศ์",
                "1629900563999",
                Date.valueOf("2000-11-09"),
                address,
                todoList,
                Age.YOUNG,
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
