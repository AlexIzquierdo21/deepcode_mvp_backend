package com.deepcode.deepcode_backend.repository;

import com.deepcode.deepcode_backend.entity.ChallengesModel;
import com.deepcode.deepcode_backend.entity.LanguageChallenge;
import com.deepcode.deepcode_backend.entity.LevelChallenge;
import com.deepcode.deepcode_backend.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/// Repository para gestionar retos de programación
/// Extiende JpaRepository para obtener métodos CRUD automáticos (save, findAll, findById, delete, etc.)
public interface ChallengesRepository extends JpaRepository<ChallengesModel, Long> {

    /// Busca todos los retos de un lenguaje específico (PYTHON, JAVA, KOTLIN, HTML_CSS_JS)
    List<ChallengesModel> findByLanguage(LanguageChallenge language);

    /// Busca todos los retos de un nivel específico (BEGINNER, INTERMEDIATE)
    List<ChallengesModel> findByLevel(LevelChallenge level);

    /// Busca todos los retos creados por un usuario específico
    List<ChallengesModel> findByCreatedBy(UserModel createdBy);

    /// Busca los retos por lenguaje y por nivel
    List<ChallengesModel> findByLanguageAndLevel(LanguageChallenge language, LevelChallenge level);

    /// Verifica si existe un reto con el mismo título, lenguaje y nivel (ignora mayúsculas en título)
    /// Usado para prevenir duplicados al crear retos
    /// Un reto es considerado duplicado si coinciden: título (case-insensitive) + lenguaje + nivel + está activo
    @Query("SELECT COUNT(c) > 0 FROM ChallengesModel c WHERE LOWER(c.title) = LOWER(:title) AND c.language = :language AND c.level = :level")
    boolean existsByTitleAndLanguageAndLevel(
            @Param("title") String title,
            @Param("language") LanguageChallenge language,
            @Param("level") LevelChallenge level
    );
}
