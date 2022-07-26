package com.minji.springbootjpacrud.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "member")
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

}
