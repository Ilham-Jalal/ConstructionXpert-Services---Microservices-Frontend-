package com.task_service.model;

import com.task_service.enums.Priority;
import com.task_service.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "Task.findAutocompleteSuggestions",
                query = "SELECT DISTINCT CASE " +
                        "WHEN LOWER(t.title) LIKE LOWER(CONCAT('%', :input, '%')) THEN t.title " +
                        "WHEN LOWER(t.description) LIKE LOWER(CONCAT('%', :input, '%')) THEN t.description " +
                        "END " +
                        "FROM Task t " +
                        "WHERE :input IS NOT NULL " +
                        "AND (LOWER(t.title) LIKE LOWER(CONCAT('%', :input, '%')) " +
                        "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :input, '%')))"
        )
})
@Table(name = "task")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "resource_ids")
    private String resourceIds;
}
