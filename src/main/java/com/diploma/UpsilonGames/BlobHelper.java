package com.diploma.UpsilonGames;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Random;

import com.diploma.UpsilonGames.games.Game;
import com.diploma.UpsilonGames.pictures.Picture;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlobHelper {
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Autowired
    public BlobHelper(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;

    }
    public Blob createBlob(InputStream content, long size) {
        Session session;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        return session.getLobHelper().createBlob(content, size);
    }
    public Blob createBlob(byte[] bytes) {
        Session session;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        return session.getLobHelper().createBlob(bytes);
    }
}