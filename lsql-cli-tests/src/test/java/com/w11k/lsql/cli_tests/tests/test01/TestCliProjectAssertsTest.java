package com.w11k.lsql.cli_tests.tests.test01;

import com.w11k.lsql.tests.cli.schema_public.Person1;
import com.w11k.lsql.tests.cli.schema_public.Person2;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public final class TestCliProjectAssertsTest {

    @Test
    public void with() {
        Person1 p1 = new Person1();
        p1 = p1
                .withId(1)
                .withFirstName("a");

        assertEquals(p1.getId(), new Integer(1));
        assertEquals(p1.getFirstName(), "a");

        Person2 p2 = new Person2()
                .withId(2)
                .withFirstName("b")
                .withAge(50);

        assertEquals(p2.getId(), new Integer(2));
        assertEquals(p2.getFirstName(), "b");
        assertEquals(p2.getAge(), new Integer(50));
    }

    @Test
    public void assignIntoNew() {
        Person1 p1 = new Person1().withId(1).withFirstName("a");
        Person2 p2 = new Person2().withId(2).withFirstName("b").withAge(50);

        p2 = p1.assignIntoNew(p2);
        assertEquals(p2.getId(), new Integer(1));
        assertEquals(p2.getFirstName(), "a");
        assertEquals(p2.getAge(), new Integer(50));
    }

    @Test
    public void updatedWith() {
        Person1 p1 = new Person1().withId(1).withFirstName("a");
        Person2 p2 = new Person2().withId(2).withFirstName("b").withAge(50);

        p1 = p1.updatedWith(p2);
        assertEquals(p1.getId(), new Integer(2));
        assertEquals(p1.getFirstName(), "b");
    }

}