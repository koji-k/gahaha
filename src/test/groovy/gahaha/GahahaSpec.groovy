package gahaha

import spock.lang.Specification

class GahahaSpec extends Specification {

    def setup() {
        Gahaha.gahanize(Person)
        Person.deleteAll()
    }

    def "basic"() {
        setup:
        Gahaha.gahanize(Person)

        when:
        new Person(name: "AAA", age: 31).save()
        new Person(name: "BBB", age: 32).save()
        then:
        Person.count() == 2
        Person.get(1).name == "AAA"
        Person.get(1).age == 31
        Person.get(2).name == "BBB"
        Person.get(2).age == 32

        expect:
        Person.list().size() == 2

        when:
        Person.get(1).delete()
        then:
        Person.count() == 1
        Person.list().head().name == "BBB"

        when:
        new Person(name:"C", age:100).save()
        then:
        Person.count() == 2

        when:
        Person.deleteAll()
        then:
        Person.count() == 0
    }

    def "findAllWhere"() {
        setup:
        Gahaha.gahanize(Person)
        new Person(name: "AAA", age: 31).save()
        new Person(name: "BBB", age: 32).save()
        new Person(name: "CCC", age: 33).save()
        new Person(name: "DDD", age: 31).save()
        new Person(name: "AAA", age: 31).save()

        when:
        List<Person> persons = Person.findAllWhere(name: "AAA", age: 31)
        then:
        persons.size() == 2
        persons[0].name == "AAA"
        persons[0].age == 31
        persons[1].name == "AAA"
        persons[1].age == 31

        when:
        persons = Person.findAllWhere(age:31)
        then:
        persons.size() == 3
        persons[0].name == "AAA"
        persons[0].age == 31
        persons[1].name == "DDD"
        persons[1].age == 31
        persons[2].name == "AAA"
        persons[2].age == 31

        when:
        persons = Person.findAllWhere(age:99)
        then:
        persons == []
    }

    def "findWhere"() {
        setup:
        Gahaha.gahanize(Person)
        new Person(name: "DDD", age: 31).save()
        new Person(name: "AAA", age: 31).save()
        Person person

        when:
        person = Person.findWhere(age: 31)
        then:
        Person.count() == 2
        person.name == "DDD"
        person.age == 31

        when: "With sort option"
        person = Person.findWhere(age: 31, ['sort':'id', 'order':'desc'])
        then:
        Person.count() == 2
        person.name == "AAA"
        person.age == 31

        when: "Not found"
        person = Person.findWhere(age:99)
        then:
        person == null
    }

    def "Property Check"() {
        setup:
        Person person = new Person(name:"AAA", age:31, date: Date.parse("yyyy/MM/dd", "2016/06/23")).save()
        expect:
        person.name.class == String
        person.age.class == Integer
        person.date.class == java.sql.Timestamp
        person.date.format("yyyy/MM/dd") == "2016/06/23"
    }
}

// Test classes
class Person {
    String name
    Integer age
    Date date
}
