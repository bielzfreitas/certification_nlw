package com.rocketseat.certification_nlw.modules.students.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "answers_certification_students")
@Builder
public class AnswersCertificationsEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    //colunas FK
    @Column(name = "certification_id")
    private UUID certificationID;
    //funcionando como ligação entre uma entidade e uma coluna, sem manipulação
    @ManyToOne()
    @JoinColumn(name = "certification_id", insertable = false, updatable = false)
    @JsonBackReference
    private CertificationStudentEntity certificationStudentEntity;

    //FK
    @Column(name = "student_id")
    private UUID studentID;
    //funcionando como ligação entre uma entidade e uma coluna, sem manipulação
    @ManyToOne
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private StudentEntity studentEntity;
    
    //FK
    @Column(name = "question_id")
    private UUID questionID;
    //funcionando como ligação entre uma entidade e uma coluna, sem manipulação
    

    //FK
    @Column(name = "answer_id")
    private UUID answerID;
    //funcionando como ligação entre uma entidade e uma coluna, sem manipulação
    
    @Column(name = "is_correct")
    private boolean isCorrect;

    //data criação
    @CreationTimestamp
    private LocalDateTime createdAt;
}
