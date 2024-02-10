package com.rocketseat.certification_nlw.modules.questions.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rocketseat.certification_nlw.modules.questions.entities.QuestionEntity;

//Listando todas as questões somente da tecnologia que o usuário irá fazer a prova
public interface QuestionRepository extends JpaRepository<QuestionEntity, UUID> {

    //automaticamente, por baixo dos panos, faz um mapeamento e se for um atributo.. faz um select e busca o parâmetro technology
    List<QuestionEntity> findByTechnology(String technology);
    
}
