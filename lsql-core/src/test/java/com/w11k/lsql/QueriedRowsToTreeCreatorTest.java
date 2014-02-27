package com.w11k.lsql;

import com.google.common.collect.Sets;
import com.w11k.lsql.tests.AbstractLSqlTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

import static org.testng.Assert.*;

public class QueriedRowsToTreeCreatorTest extends AbstractLSqlTest {

    public static class Person extends RowPojo {
        int id;

        String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Address extends RowPojo {
        private int id;

        private int person_id;

        private String city;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPerson_id() {
            return person_id;
        }

        public void setPerson_id(int person_id) {
            this.person_id = person_id;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }

    @BeforeMethod
    public void setupTablesAndData() {
        createTable("CREATE TABLE person (id INT PRIMARY KEY, name TEXT)");
        lSql.executeRawSql("INSERT INTO person (id, name) VALUES (1, 'person1')");
        lSql.executeRawSql("INSERT INTO person (id, name) VALUES (2, 'person2')");

        createTable("CREATE TABLE address (id INT PRIMARY KEY, person_id INT, city TEXT)");
        lSql.executeRawSql("INSERT INTO address (id, person_id, city) VALUES (1, 1, 'city1')");
        lSql.executeRawSql("INSERT INTO address (id, person_id, city) VALUES (2, 1, 'city2')");
        lSql.executeRawSql("INSERT INTO address (id, person_id, city) VALUES (3, 2, 'city3')");
        lSql.executeRawSql("INSERT INTO address (id, person_id, city) VALUES (4, 2, 'city4')");
    }

    @Test
    public void onlyKeepsColumnOfTheIdColumnTable() {
        List<RowPojo> persons = createQuery().asRowTree("id", "address_id");
        assertEquals(persons.size(), 2);
        for (Row person : persons) {
            assertTrue(person.containsKey("id"));
            assertTrue(person.containsKey("name"));
            assertFalse(person.containsKey("address_id"));
            assertFalse(person.containsKey("person_id"));
            assertFalse(person.containsKey("city"));
            List<Row> addresses = person.getJoinedRows("address_ids");
            assertEquals(addresses.size(), 2);
            for (Row address : addresses) {
                assertTrue(address.containsKey("address_id"));
                assertTrue(address.containsKey("person_id"));
                assertTrue(address.containsKey("city"));
                assertFalse(address.containsKey("id"));
                assertFalse(address.containsKey("name"));
            }
        }
    }

    @Test
    public void autoPluralNames() {
        Set<Integer> collectedAddressIds = Sets.newLinkedHashSet();
        List<RowPojo> persons = createQuery().asRowTree("id", "address_id");
        assertEquals(persons.size(), 2);
        for (Row person : persons) {
            List<Row> addresses = person.getJoinedRows("address_ids");
            assertEquals(addresses.size(), 2);
            for (Row address : addresses) {
                collectedAddressIds.add(address.getInt("address_id"));
            }
        }
        assertTrue(collectedAddressIds.contains(1));
        assertTrue(collectedAddressIds.contains(2));
        assertTrue(collectedAddressIds.contains(3));
        assertTrue(collectedAddressIds.contains(4));
    }

    @Test
    public void userDefinedName() {
        Set<Integer> collectedAddressIds = Sets.newLinkedHashSet();
        List<RowPojo> persons = createQuery().asRowTree("id", "address_id as addresses");
        assertEquals(persons.size(), 2);
        for (Row person : persons) {
            List<Row> addresses = person.getJoinedRows("addresses");
            assertEquals(addresses.size(), 2);
            for (Row address : addresses) {
                collectedAddressIds.add(address.getInt("address_id"));
            }
        }
        assertTrue(collectedAddressIds.contains(1));
        assertTrue(collectedAddressIds.contains(2));
        assertTrue(collectedAddressIds.contains(3));
        assertTrue(collectedAddressIds.contains(4));
    }

    @Test
    public void useRowPojoClass() {
        lSql.table("person", Person.class);

        List<Person> persons = createQuery().asTree("id", "address_id as addresses");
        assertEquals(persons.size(), 2);

        Set<Object> collected = Sets.newLinkedHashSet();
        for (Person person : persons) {
            collected.add(person.getId());
            collected.add(person.getName());
            List<Row> addresses = person.getJoinedRows("addresses");
            assertEquals(addresses.size(), 2);
            for (Row address : addresses) {
                collected.add(address.getString("city"));
            }
        }
        assertTrue(collected.contains(1));
        assertTrue(collected.contains("person1"));
        assertTrue(collected.contains(2));
        assertTrue(collected.contains("person2"));
        assertTrue(collected.contains("city1"));
        assertTrue(collected.contains("city2"));
        assertTrue(collected.contains("city3"));
        assertTrue(collected.contains("city4"));
    }

    @Test
    public void useRowPojoClassInJoinedCollections() {
        lSql.table("person", Person.class);
        lSql.table("address", Address.class);

        List<Person> persons = createQuery().asTree("id", "address_id as addresses");
        assertEquals(persons.size(), 2);

        Set<Object> collected = Sets.newLinkedHashSet();
        for (Person person : persons) {
            assertEquals(person.size(), 3);
            List<Address> addresses = person.getJoined("addresses");
            assertEquals(addresses.size(), 2);
            for (Address address : addresses) {
                collected.add(address.getId());
                collected.add(address.getCity());
            }
        }
        assertTrue(collected.contains(1));
        assertTrue(collected.contains(2));
        assertTrue(collected.contains("city1"));
        assertTrue(collected.contains("city2"));
        assertTrue(collected.contains("city3"));
        assertTrue(collected.contains("city4"));
    }

    @Test
    public void asTreeResolvesAliases() {
        lSql.table("person", Person.class);
        lSql.table("address", Address.class);

        Query q = createQueryWithAliases();

        List<Person> persons = q.asTree("pid", "aid as addresses");
        assertEquals(persons.size(), 2);

        Set<Object> collected = Sets.newLinkedHashSet();
        for (Person person : persons) {
            assertEquals(person.size(), 3);
            List<Address> addresses = person.getJoined("addresses");
            assertEquals(addresses.size(), 2);
            for (Address address : addresses) {
                collected.add(address.getId());
                collected.add(address.getCity());
            }
        }
        assertTrue(collected.contains(1));
        assertTrue(collected.contains(2));
        assertTrue(collected.contains("city1"));
        assertTrue(collected.contains("city2"));
        assertTrue(collected.contains("city3"));
        assertTrue(collected.contains("city4"));
    }

    @Test
    public void asRowTreeKeepsAliases() {
        lSql.table("person", Person.class);
        lSql.table("address", Address.class);

        Query q = createQueryWithAliases();

        List<RowPojo> persons = q.asRowTree("pid", "aid as addresses");
        assertEquals(persons.size(), 2);

        Set<Object> collected = Sets.newLinkedHashSet();
        for (RowPojo row : persons) {
            assertEquals(row.size(), 3);
            collected.add("p#" + row.get("pid"));
            collected.add("p#" + row.get("pname"));
            List<RowPojo> addresses = row.getJoined("addresses");
            assertEquals(addresses.size(), 2);
            for (RowPojo address : addresses) {
                collected.add("a#" + address.get("aid"));
                collected.add("a#p#" + address.get("aperson_id"));
                collected.add("a#" + address.get("acity"));
            }
        }
        assertTrue(collected.contains("p#1"));
        assertTrue(collected.contains("p#2"));
        assertTrue(collected.contains("p#person1"));
        assertTrue(collected.contains("p#person2"));

        assertTrue(collected.contains("a#1"));
        assertTrue(collected.contains("a#2"));
        assertTrue(collected.contains("a#p#1"));
        assertTrue(collected.contains("a#p#2"));

        assertTrue(collected.contains("a#city1"));
        assertTrue(collected.contains("a#city2"));
    }

    /**
     * https://github.com/w11k/lsql/issues/2
     */
    @Test
    public void soeOnToString() {
        List<RowPojo> persons = createQuery().asRowTree("id", "address_id as addresses");
        for (Row person : persons) {
            String s = person.toString();
            assertNotNull(s);
        }
    }

    /**
     * https://github.com/w11k/lsql/issues/3
     */
    @Test
    public void npeWhenJoinedRowIsEmpty() {
        lSql.executeRawSql("INSERT INTO person (id, name) VALUES (3, 'person3')");
        List<RowPojo> persons = createQuery().asRowTree("id", "address_id as addresses");
        assertEquals(persons.size(), 3);

        Row person3 = null;
        for (Row person : persons) {
            if (person.getInt("id") == 3) {
                person3 = person;
            }
        }

        assertNotNull(person3);
        assertTrue(person3.getJoinedRows("addresses").isEmpty());
    }

    private Query createQuery() {
        return lSql.executeRawQuery("SELECT p.*, " +
                "a.id AS address_id, a.person_id, a.city FROM person p LEFT OUTER JOIN address a ON a.person_id = p.id");
    }

    private Query createQueryWithAliases() {
        return lSql.executeRawQuery("SELECT p.id AS pid, p.name AS pname, " +
                "a.id AS aid, a.person_id AS aperson_id, a.city AS acity " +
                "FROM person p LEFT OUTER JOIN address a ON a.person_id = p.id");
    }

}
