package com.diploma.UpsilonGames;

import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories
@SpringBootApplication
@EnableTransactionManagement
public class UpsilonGamesApplication {

	public static void main(String[] args) {
		SpringApplication.run(UpsilonGamesApplication.class, args);
	}
//	@Bean(name="entityManagerFactory")// Need to expose SessionFactory to be able to work with BLOBs
//	public SessionFactory entityManagerFactory(HibernateEntityManagerFactory hemf) {
//		return hemf.getSessionFactory();
//	}

	public LocalSessionFactoryBean entityManagerFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		return sessionFactory;
	}

}
