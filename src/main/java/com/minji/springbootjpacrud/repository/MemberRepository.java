package com.minji.springbootjpacrud.repository;

import com.minji.springbootjpacrud.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByIdBetweenOrderByIdDesc(Long from, Long to);

    Page<Member> findByIdBetween(Long from, Long to, Pageable pageable);
}
