# Gahaha - Simple ORM of Apache Groovy for sample codes.

During the experiment now.

# usage
Gahaha is not yet provided on Maven repository etc.  
So, please clone this repository and execute `gradle build`.  
Then you can find `build/libs/gahaha-{VERSION}.jar`.  
So, you have to include this jar file into your classpath.  
e.x.) `groovy -cp gahaha-0.1.jar:. test.groovy`  

`test.groovy` is like following.

```
import gahaha.*

class Person {
    String name
    Integer age
}

Gahaha.gahanize(Person)
Person a = new Person(name:"Gahaha!", age:31).save()
assert a.id == 1
assert a.name == "Gahaha!"
assert a.age == 31
```