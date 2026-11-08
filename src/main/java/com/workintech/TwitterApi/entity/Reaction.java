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
@Table(name="reactions")
public class Reaction {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="reaction")
    private ReactionType reactionType;

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
