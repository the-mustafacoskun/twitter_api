package com.workintech.TwitterApi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="likes")
public class Like {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH, CascadeType.DETACH,CascadeType.PERSIST})
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(cascade={CascadeType.DETACH,CascadeType.REFRESH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name="tweet_id")
    private Tweet tweet;

}
