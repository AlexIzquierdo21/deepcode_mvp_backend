package com.deepcode.deepcode_backend.repository;

import com.deepcode.deepcode_backend.entity.ChallengesModel;
import com.deepcode.deepcode_backend.entity.LanguageChallenge;
import com.deepcode.deepcode_backend.entity.LevelChallenge;
import com.deepcode.deepcode_backend.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository para gestionar retos de programación
// Extiende JpaRepository para obtener métodos CRUD automáticos (save, findAll, findById, delete, etc.)
public interface ChallengesRepository extends JpaRepository<ChallengesModel, Long> {

    // Busca todos los retos de un lenguaje específico (PYTHON, JAVA, KOTLIN, HTML_CSS_JS)
    List<ChallengesModel> findByLanguage(LanguageChallenge language);

    // Busca todos los retos de un nivel específico (BEGINNER, INTERMEDIATE)
    List<ChallengesModel> findByLevel(LevelChallenge level);

    // Busca todos los retos creados por un usuario específico
    List<ChallengesModel> findByCreatedBy(UserModel createdBy);
}
