package com.guardjo.simpleblogfinder.repository;

import com.guardjo.simpleblogfinder.domain.SearchTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SearchTermRepository extends JpaRepository<SearchTerm, Long>, QuerydslPredicateExecutor<SearchTerm> {
    Optional<SearchTerm> findSearchTermBySearchTermValueEqualsIgnoreCase(String searchTermValue);
}
