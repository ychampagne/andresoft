package org.andresoft.workinprogress;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class Person
{

    public enum Sex
    {
        MALE,
        FEMALE
    }

    String name;
    LocalDate birthday;
    Sex gender;
    String emailAddress;

    public int getAge()
    {
        return ThreadLocalRandom.current().nextInt(0, 100);
    }

    public void printPerson()
    {
        // ...
    }

    public Sex getGender()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public static List<Person> createRoster()
    {
        List<Person> persons = new ArrayList<Person>();
        Person p1 = new Person();
        p1.setGender(Sex.MALE);

        Person p2 = new Person();
        p2.setGender(Sex.FEMALE);

        persons.add(p1);
        persons.add(p2);

        return persons;
    }

    public String getEmailAddress()
    {
        // TODO Auto-generated method stub

        return null;

    }

    public String getName()
    {
        return name;
    }

    public void setName(
                        String name)
    {
        this.name = name;
    }

    public LocalDate getBirthday()
    {
        return birthday;
    }

    public void setBirthday(
                            LocalDate birthday)
    {
        this.birthday = birthday;
    }

    public void setGender(
                          Sex gender)
    {
        this.gender = gender;
    }

    public void setEmailAddress(
                                String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    public static <T, SOURCE extends Collection<T>, DEST extends Collection<T>>
            DEST
            transferElements(
                             SOURCE sourceCollection,
                             Supplier<DEST> collectionFactory)
    {

        DEST result = collectionFactory.get();
        for (T t : sourceCollection)
        {
            result.add(t);
        }
        return result;
    }

}
