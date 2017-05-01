package com.niit.customer.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.niit.customer.model.Customer;

@Configuration
@ComponentScan("com.niit")
@EnableTransactionManagement
public class ApplicationContextConfig {

	   @Bean(name="dataSource")
		public DataSource getDataSource(){
		DriverManagerDataSource dataSource=new DriverManagerDataSource();
		    dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			dataSource.setUrl("jdbc:sqlserver://localhost:1433;databaseName=DBRANCE");
			dataSource.setUsername("sa");
			dataSource.setPassword("SYSTEM");
			return dataSource;
		}
	   private Properties getHibernateProperties(){
			Properties properties=new Properties();
			properties.put("hibernate.hbm2ddl.auto","create");
			//properties.put("hibernate.hbm2ddl.import_files=","initial_data.sql");
			properties.put("hibernate.show_sql", "true");
			properties.put("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
			return properties;
		}
	   @Autowired
		@Bean(name="sessionFactory")
		public SessionFactory getSessionFactory(DataSource dataSource) 
		{
			LocalSessionFactoryBuilder sessionBuilder=new LocalSessionFactoryBuilder(dataSource);
			sessionBuilder.addProperties(getHibernateProperties());
			sessionBuilder.addAnnotatedClass(Customer.class);
			
		
			
		
			return sessionBuilder.buildSessionFactory();
		}
		
		@Autowired
		@Bean(name="transactionManager")
		public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory)
		{
			HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);

			return transactionManager;
		}

}