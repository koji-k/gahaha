package gahaha

import spock.lang.Specification

class GahahaSpec extends Specification {

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
}

// Test classes
class Person {
    String name
    Integer age
}
