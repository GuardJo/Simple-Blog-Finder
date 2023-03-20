package com.guardjo.simpleblogfinder.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;

@Getter
@AllArgsConstructor(staticName = "of")
@Entity
@Table(name = "SEARCH_TERM",
indexes = {
        @Index(name = "totalCount", columnList = "totalCount")
})
public class SearchTerm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEARCH_TERM_ID")
    private Long id;
    private String searchTermValue;
    private long totalCount;

    protected SearchTerm() {

    }
}
