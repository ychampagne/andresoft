package org.andresoft.tutorial;

import java.util.Date;
import java.util.List;

import org.andresoft.tutorial.domain.Event;
import org.andresoft.tutorial.domain.Person;
import org.andresoft.tutorial.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.Test;

public class EventCrudTest
{

    @Test
    public void testCreateEvents()
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try
        {
            session.beginTransaction();

            Event theEvent = new Event();
            theEvent.setTitle("Event 1");
            theEvent.setDate(new Date());
            session.save(theEvent);
            session.getTransaction().commit();
        }
        catch (Exception e)
        {
            session.getTransaction().rollback();

        }
        finally
        {
            // session.close();

        }
    }

    @Test
    public void testListEvents()
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Event> events = session.createQuery("from Event").list();
        session.getTransaction().commit();
        for (Event event : (List<Event>) events)
        {
            System.out.println(event.toString());
        }

    }

    @Test
    public void testCreatePersons()
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try
        {
            session.beginTransaction();

            Person p1 = new Person();
            p1.setFirstname("Person1F");
            p1.setLastname("Person1L");
            p1.setAge(31);

            Person p2 = new Person();
            p2.setFirstname("Person2F");
            p2.setLastname("Person2L");
            p2.setAge(32);

            session.save(p1);
            session.save(p2);

            List<Event> events = session.createQuery("from Event").list();
            for (Event event : (List<Event>) events)
            {
                p1.addEvent(event);;
            }
            p2.addEvent(events.get(0));

            session.getTransaction().commit();
        }
        catch (Exception e)
        {
            session.getTransaction().rollback();

        }
        finally
        {
            // session.close();

        }
    }

}
