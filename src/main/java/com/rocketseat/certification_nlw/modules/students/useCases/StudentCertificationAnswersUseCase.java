package com.rocketseat.certification_nlw.modules.students.useCases;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rocketseat.certification_nlw.modules.questions.entities.QuestionEntity;
import com.rocketseat.certification_nlw.modules.questions.repositories.QuestionRepository;
import com.rocketseat.certification_nlw.modules.students.dto.StudentCertificationAnswerDTO;
import com.rocketseat.certification_nlw.modules.students.dto.VerifyHasCertificationDTO;
import com.rocketseat.certification_nlw.modules.students.entities.AnswersCertificationsEntity;
import com.rocketseat.certification_nlw.modules.students.entities.CertificationStudentEntity;
import com.rocketseat.certification_nlw.modules.students.entities.StudentEntity;
import com.rocketseat.certification_nlw.modules.students.repositories.CertificationStudentRepository;
import com.rocketseat.certification_nlw.modules.students.repositories.StudentRepository;

@Service
public class StudentCertificationAnswersUseCase {

    //chamando o repository (question)
    @Autowired
    private QuestionRepository questionRepository;

    //chamando o repository (student)
    @Autowired
    private StudentRepository studentRepository;

    //chamando o repository (certification)
    @Autowired
    private CertificationStudentRepository certificationStudentRepository;

    //chamando o UseCase para verificação (verify)
    @Autowired
    private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;




    //não tem retorno
    public CertificationStudentEntity execute(StudentCertificationAnswerDTO dto) throws Exception {

        var hasCertification = this.verifyIfHasCertificationUseCase
                .execute(new VerifyHasCertificationDTO(dto.getEmail(), dto.getTechnology()));

        if(hasCertification) {
            throw new Exception("Você já tirou a sua certificação!");
        }

        //Buscar as alternativas das perguntas (Correct ou Incorrect)
        //percorrer item a item no questions
        List<QuestionEntity> questionsEntity = questionRepository.findByTechnology(dto.getTechnology());
        List<AnswersCertificationsEntity> answersCertifications = new ArrayList<>();

        //para conseguir manipular os dados e ter a "grade"
        //consegue modificar a variável dentro o lambda
        AtomicInteger correctAnswers = new AtomicInteger(0);

        dto.getQuestionAnswers()
            .stream().forEach(questionAnswer -> {
                //criando variável question fora do filter
                //por conta do conflito, de "quation" para "q"
                var question = questionsEntity.stream()
                    .filter(q -> q.getId().equals(questionAnswer.getQuestionID())).findFirst().get();

                var findCorrectAlternative = question.getAlternatives().stream()
                    .filter(alternative -> alternative.isCorrect()).findFirst().get();
                
                if(findCorrectAlternative.getId().equals(questionAnswer.getAlternativeID())) {
                    questionAnswer.setCorrect(true);
                    correctAnswers.incrementAndGet();
                } else {
                    questionAnswer.setCorrect(false);
                }

                var answersCertificationsEntity = AnswersCertificationsEntity.builder()
                    .answerID(questionAnswer.getAlternativeID())
                    .questionID(questionAnswer.getQuestionID())
                    .isCorrect(questionAnswer.isCorrect()).build();
                
                answersCertifications.add(answersCertificationsEntity);
            });
        
        //Verificar se existe studant pelo email
        var student = studentRepository.findByEmail(dto.getEmail());
        UUID studentID;
        if(student.isEmpty()) {
            var studentCreated = StudentEntity.builder().email(dto.getEmail()).build();
            studentCreated = studentRepository.save(studentCreated);
            studentID = studentCreated.getId();
        } else {
            studentID = student.get().getId();
        }

        
        CertificationStudentEntity certificationStudentEntity = 
            CertificationStudentEntity.builder()
                .technology(dto.getTechnology())
                .studentID(studentID)
                .grade(correctAnswers.get())
                .build();

        var certificationStudentCreated = certificationStudentRepository.save(certificationStudentEntity);
        
        //add em cada item o certificationID
        answersCertifications.stream().forEach(answersCertification -> {
            answersCertification.setCertificationID(certificationStudentEntity.getId());
            answersCertification.setCertificationStudentEntity(certificationStudentEntity);
        });

        certificationStudentEntity.setAnswersCertificationsEntities(answersCertifications);
        certificationStudentRepository.save(certificationStudentEntity);



        return certificationStudentCreated;
        //Salvar as informações da certificação
    }
}
