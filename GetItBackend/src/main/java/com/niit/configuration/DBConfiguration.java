package com.niit.configuration;

import java.util.Properties;

import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.niit.model.BlogComments;
import com.niit.model.BlogLikes;
import com.niit.model.Blogs;
import com.niit.model.Feedback;
import com.niit.model.Friend;
import com.niit.model.Job;
import com.niit.model.Notification;
import com.niit.model.ProfilePicture;
import com.niit.model.User;

@Configuration
@EnableTransactionManagement
public class DBConfiguration 
{
	@Bean
	public SessionFactory sessionFactory()
	{
		LocalSessionFactoryBuilder lsfb = new LocalSessionFactoryBuilder(getDataSource());
		Properties hp = new Properties();
		hp.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
		hp.setProperty("hibernate.hbm2ddl.auto", "update");
		hp.setProperty("hibernate.show_sql", "true");
		lsfb.addProperties(hp);
		Class classes[] = new Class[] {User.class, Job.class, Blogs.class, Notification.class, BlogLikes.class, BlogComments.class, ProfilePicture.class, Friend.class, Feedback.class};
		return lsfb.addAnnotatedClasses(classes).buildSessionFactory();		
	}
	
	@Bean
	public DataSource getDataSource()
	{
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
		dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:XE");
		dataSource.setUsername("GetIt_DB");
		dataSource.setPassword("123456");
		return dataSource;
	}
	
	@Bean 
	public HibernateTransactionManager hibernateTransactionManager()
	{
		return new HibernateTransactionManager(sessionFactory());
	}
}
