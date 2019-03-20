package com.howtodoinjava.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;


import com.howtodoinjava.entity.EmployeeEntity;
import com.howtodoinjava.entity.Login;

@Repository
public class EmployeeDaoImpl implements EmployeeDAO,UserDetailsService  {

	 @Autowired
	    private SessionFactory sessionFactory;
	 
	
	    public void addEmployee(EmployeeEntity employee) {
	        this.sessionFactory.getCurrentSession().save(employee);
	    }
	 
	   
	    public List<EmployeeEntity> getAllEmployees() {
	        return this.sessionFactory.getCurrentSession().createQuery("from Employee").list();
	    }
	 

	    public void deleteEmployee(Integer employeeId) {
	        EmployeeEntity employee = (EmployeeEntity) sessionFactory.getCurrentSession().load(
	                EmployeeEntity.class, employeeId);
	        if (null != employee) {
	            this.sessionFactory.getCurrentSession().delete(employee);
	        }
	    }
	 
	  
	    public UserDetails loadUserByUsername(String username)
	            throws UsernameNotFoundException, DataAccessException
	    {
	        System.out.println("Getting access details from employee dao !!");
	        System.out.println(username);
	       Session s=this.sessionFactory.openSession();
	       Transaction t=s.beginTransaction();
	       Query q=s.createQuery("from Login l where l.username=:user");
	       q.setParameter("user", username);
		       Login l1=(Login)q.uniqueResult();
		       UserDetails user=null;
		       if(l1!=null) {
		       
		       user= new User(l1.getUsername(),l1.getPassword(), true, true, true, true, new GrantedAuthority[]{ new GrantedAuthorityImpl(l1.getRole()) });
		       }
		       t.commit();
	  	        return user;
	    }
}
