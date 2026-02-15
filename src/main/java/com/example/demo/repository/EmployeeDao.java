package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Request;

@Repository
public interface EmployeeDao extends JpaRepository<Request, String> {

	 @Query("SELECT r FROM Request r WHERE r.empName = :empName")
     public Request searchByEmpName(@Param("empName") String empName);
}
