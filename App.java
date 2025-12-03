package com.codegnan;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import com.codegnan.entity.Employee;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args)
    {
        StandardServiceRegistry registry = null;
        SessionFactory sessionFactory = null;

        try {
            //1. Create StandardServiceRegistry
            registry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")
                    .build();
            //2. Create Metadata
            Metadata metadata = new MetadataSources(registry)
                    .getMetadataBuilder()
                    .build();
            //3. create SessionFactory
            sessionFactory = metadata.getSessionFactoryBuilder().build();

            //try-with-resources for session auto-close
            try (Session session = sessionFactory.openSession()) {
                Transaction tx = null;
                try {
                    //5. begin transaction
                    tx = session.beginTransaction();
                    //6. create and persist employee Object.
                    Employee emp = new Employee(101, "mani", "HR", 35000);
                    session.save(emp);
                    //7. commit transaction
                    tx.commit();
                    System.out.println("Record inserted sucessfully");
                    System.out.println(emp);
                } catch (Exception ex) {
                    if (tx != null) {
                        tx.rollback();
                    }
                    System.out.println("Transaction failed:" + ex.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Hibernate setup failed:" + e.getMessage());
        } finally {
            //8. Always close in final block
            if (sessionFactory != null) {
                sessionFactory.close();
            }
            if (registry != null) {
                StandardServiceRegistryBuilder.destroy(registry);
            }
        }
    }
}
