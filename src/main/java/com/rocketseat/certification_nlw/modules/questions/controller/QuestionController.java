package com.rocketseat.certification_nlw.modules.questions.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rocketseat.certification_nlw.modules.questions.dto.AlternativesResultDTO;
import com.rocketseat.certification_nlw.modules.questions.dto.QuestionResultDTO;
import com.rocketseat.certification_nlw.modules.questions.entities.AlternativesEntity;
import com.rocketseat.certification_nlw.modules.questions.entities.QuestionEntity;
import com.rocketseat.certification_nlw.modules.questions.repositories.QuestionRepository;


@RestController
@RequestMapping("/questions")
public class QuestionController {
    
    @Autowired
    private QuestionRepository questionRepository;

    //precisa informar o atributo
    @GetMapping("/technology/{technology}")
    public List<QuestionResultDTO> findByTechnology(@PathVariable String technology) {
        System.out.println("TECH === " + technology);
        var result = this.questionRepository.findByTechnology(technology);
        
        //acesso a funcionalidades necessárias percorrendo cada questão
        var toMap = result.stream().map(question -> mapQuestionToDTO(question))
        //convertendo uma collection
            .collect(Collectors.toList());
        return toMap;
    }

    static QuestionResultDTO mapQuestionToDTO(QuestionEntity question) {
        //com o @Builder em "AlertnativeResultDTO" ajuda a não precisar utilizar "new.."
        var questionResultDTO = QuestionResultDTO.builder()
            .id(question.getId())
            .technology(question.getTechnology())
            .description(question.getDescription()).build();

        //mapeando a list
        List<AlternativesResultDTO> alternativeResultDTOs = question.getAlternatives()
        //para cada alternativa
            .stream().map(alternative -> mapAlternativeDTO(alternative))
            .collect(Collectors.toList());
        //retornando a conversão
        questionResultDTO.setAlternatives(alternativeResultDTOs);
        return questionResultDTO;
    }

    static AlternativesResultDTO mapAlternativeDTO(AlternativesEntity alternativesResultDTO) {
       return AlternativesResultDTO.builder()
            .id(alternativesResultDTO.getId())
            .description(alternativesResultDTO.getDescription()).build();
    }

}
