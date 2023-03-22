package com.guardjo.simpleblogfinder.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@AllArgsConstructor(staticName = "of")
@ToString
@Entity
@Table(name = "SEARCH_TERM",
indexes = {
        @Index(name = "searchTermValue", columnList = "searchTermValue"),
        @Index(name = "totalCount", columnList = "totalCount")
})
public class SearchTerm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEARCH_TERM_ID")
    private Long id;
    private String searchTermValue;
    private long totalCount = 1;

    protected SearchTerm() {

    }

    public void increaseCount() {
        this.totalCount++;
    }
}
