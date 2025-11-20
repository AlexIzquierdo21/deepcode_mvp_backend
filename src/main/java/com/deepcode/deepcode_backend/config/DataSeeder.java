package com.deepcode.deepcode_backend.config;

import com.deepcode.deepcode_backend.entity.ChallengesModel;
import com.deepcode.deepcode_backend.entity.LanguageChallenge;
import com.deepcode.deepcode_backend.entity.LevelChallenge;
import com.deepcode.deepcode_backend.entity.UserModel;
import com.deepcode.deepcode_backend.repository.ChallengesRepository;
import com.deepcode.deepcode_backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class DataSeeder {

    /// Seeders se ejecutan solo en profiles "dev" o "docker"
    /// Esto evita que se ejecuten en producción
    @Bean
    @Profile({"dev", "docker"})
    CommandLineRunner initDatabase(
            UserRepository userRepository,
            ChallengesRepository challengesRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            /// Solo ejecutar seeders si la BD está vacía
            if (userRepository.count() > 0) {
                System.out.println("BD ya contiene datos, saltando seeders...");
                return;
            }

            System.out.println("Iniciando seeders de datos de prueba...");

            /// ===================================
            /// CREAR USUARIOS
            /// ===================================

            /// Usuario 1: Admin (para crear challenges)
            UserModel admin = new UserModel();
            admin.setUsername("Admin DeepCode");
            admin.setEmail("admin@deepcode.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setCreatedAt(LocalDateTime.now());
            userRepository.save(admin);

            /// Usuario 2: Alex (usuario de prueba)
            UserModel alex = new UserModel();
            alex.setUsername("Alex");
            alex.setEmail("alex@test.com");
            alex.setPassword(passwordEncoder.encode("test123"));
            alex.setCreatedAt(LocalDateTime.now());
            userRepository.save(alex);

            /// Usuario 3: Maria (usuario de prueba)
            UserModel maria = new UserModel();
            maria.setUsername("Maria");
            maria.setEmail("maria@test.com");
            maria.setPassword(passwordEncoder.encode("test123"));
            maria.setCreatedAt(LocalDateTime.now());
            userRepository.save(maria);

            /// Usuario 4: Carlos (usuario de prueba)
            UserModel carlos = new UserModel();
            carlos.setUsername("Carlos");
            carlos.setEmail("carlos@test.com");
            carlos.setPassword(passwordEncoder.encode("test123"));
            carlos.setCreatedAt(LocalDateTime.now());
            userRepository.save(carlos);

            System.out.println("Usuarios creados: " + userRepository.count());

            /// ===================================
            /// CHALLENGES - PYTHON BEGINNER
            /// ===================================

            ChallengesModel py1 = new ChallengesModel();
            py1.setTitle("Hello World en Python");
            py1.setDescription("Crea un programa que imprima 'Hello World' en la consola.");
            py1.setLanguage(LanguageChallenge.PYTHON);
            py1.setLevel(LevelChallenge.BEGINNER);
            py1.setCreatedBy(admin);
            py1.setCreatedAt(LocalDateTime.now());
            challengesRepository.save(py1);

            ChallengesModel py2 = new ChallengesModel();
            py2.setTitle("Suma de dos números");
            py2.setDescription("Crea una función que reciba dos números y devuelva su suma.");
            py2.setLanguage(LanguageChallenge.PYTHON);
            py2.setLevel(LevelChallenge.BEGINNER);
            py2.setCreatedBy(admin);
            py2.setCreatedAt(LocalDateTime.now());
            challengesRepository.save(py2);

            ChallengesModel py3 = new ChallengesModel();
            py3.setTitle("Número par o impar");
            py3.setDescription("Crea una función que determine si un número es par o impar.");
            py3.setLanguage(LanguageChallenge.PYTHON);
            py3.setLevel(LevelChallenge.BEGINNER);
            py3.setCreatedBy(admin);
            py3.setCreatedAt(LocalDateTime.now());
            challengesRepository.save(py3);

            /// ===================================
            /// CHALLENGES - PYTHON INTERMEDIATE
            /// ===================================

            ChallengesModel py4 = new ChallengesModel();
            py4.setTitle("FizzBuzz");
            py4.setDescription("Implementa el clásico problema FizzBuzz del 1 al 100.");
            py4.setLanguage(LanguageChallenge.PYTHON);
            py4.setLevel(LevelChallenge.INTERMEDIATE);
            py4.setCreatedBy(admin);
            py4.setCreatedAt(LocalDateTime.now());
            challengesRepository.save(py4);

            ChallengesModel py5 = new ChallengesModel();
            py5.setTitle("Invertir una lista");
            py5.setDescription("Crea una función que invierta los elementos de una lista sin usar reverse().");
            py5.setLanguage(LanguageChallenge.PYTHON);
            py5.setLevel(LevelChallenge.INTERMEDIATE);
            py5.setCreatedBy(admin);
            py5.setCreatedAt(LocalDateTime.now());
            challengesRepository.save(py5);

            ChallengesModel py6 = new ChallengesModel();
            py6.setTitle("Palíndromo");
            py6.setDescription("Crea una función que verifique si una palabra es un palíndromo.");
            py6.setLanguage(LanguageChallenge.PYTHON);
            py6.setLevel(LevelChallenge.INTERMEDIATE);
            py6.setCreatedBy(admin);
            py6.setCreatedAt(LocalDateTime.now());
            challengesRepository.save(py6);

            /// ===================================
            /// CHALLENGES - JAVA BEGINNER
            /// ===================================

            ChallengesModel java1 = new ChallengesModel();
            java1.setTitle("Hello World en Java");
            java1.setDescription("Crea un programa que imprima 'Hello World' usando System.out.println().");
            java1.setLanguage(LanguageChallenge.JAVA);
            java1.setLevel(LevelChallenge.BEGINNER);
            java1.setCreatedBy(admin);
            java1.setCreatedAt(LocalDateTime.now());
            challengesRepository.save(java1);

            ChallengesModel java2 = new ChallengesModel();
            java2.setTitle("Calculadora simple");
            java2.setDescription("Crea una clase Calculator con métodos para sumar, restar, multiplicar y dividir.");
            java2.setLanguage(LanguageChallenge.JAVA);
            java2.setLevel(LevelChallenge.BEGINNER);
            java2.setCreatedBy(admin);
            java2.setCreatedAt(LocalDateTime.now());
            challengesRepository.save(java2);

            /// ===================================
            /// CHALLENGES - JAVA INTERMEDIATE
            /// ===================================

            ChallengesModel java3 = new ChallengesModel();
            java3.setTitle("Sistema de gestión de estudiantes");
            java3.setDescription("Crea una clase Student con atributos y métodos para gestionar información de estudiantes.");
            java3.setLanguage(LanguageChallenge.JAVA);
            java3.setLevel(LevelChallenge.INTERMEDIATE);
            java3.setCreatedBy(admin);
            java3.setCreatedAt(LocalDateTime.now());
            challengesRepository.save(java3);

            ChallengesModel java4 = new ChallengesModel();
            java4.setTitle("ArrayList de nombres");
            java4.setDescription("Crea un programa que gestione una lista de nombres usando ArrayList.");
            java4.setLanguage(LanguageChallenge.JAVA);
            java4.setLevel(LevelChallenge.INTERMEDIATE);
            java4.setCreatedBy(admin);
            java4.setCreatedAt(LocalDateTime.now());
            challengesRepository.save(java4);

            /// ===================================
            /// CHALLENGES - KOTLIN BEGINNER
            /// ===================================

            ChallengesModel kt1 = new ChallengesModel();
            kt1.setTitle("Hello World en Kotlin");
            kt1.setDescription("Crea un programa que imprima 'Hello World' usando println().");
            kt1.setLanguage(LanguageChallenge.KOTLIN);
            kt1.setLevel(LevelChallenge.BEGINNER);
            kt1.setCreatedBy(admin);
            kt1.setCreatedAt(LocalDateTime.now());
            challengesRepository.save(kt1);

            ChallengesModel kt2 = new ChallengesModel();
            kt2.setTitle("Variables y tipos de datos");
            kt2.setDescription("Crea un programa que demuestre el uso de var, val y diferentes tipos de datos.");
            kt2.setLanguage(LanguageChallenge.KOTLIN);
            kt2.setLevel(LevelChallenge.BEGINNER);
            kt2.setCreatedBy(admin);
            kt2.setCreatedAt(LocalDateTime.now());
            challengesRepository.save(kt2);

            /// ===================================
            /// CHALLENGES - KOTLIN INTERMEDIATE
            /// ===================================

            ChallengesModel kt3 = new ChallengesModel();
            kt3.setTitle("Data class Person");
            kt3.setDescription("Crea una data class Person con propiedades y métodos.");
            kt3.setLanguage(LanguageChallenge.KOTLIN);
            kt3.setLevel(LevelChallenge.INTERMEDIATE);
            kt3.setCreatedBy(admin);
            kt3.setCreatedAt(LocalDateTime.now());
            challengesRepository.save(kt3);

            ChallengesModel kt4 = new ChallengesModel();
            kt4.setTitle("Lista de tareas con Kotlin");
            kt4.setDescription("Crea una app de consola para gestionar una lista de tareas usando listas mutables.");
            kt4.setLanguage(LanguageChallenge.KOTLIN);
            kt4.setLevel(LevelChallenge.INTERMEDIATE);
            kt4.setCreatedBy(admin);
            kt4.setCreatedAt(LocalDateTime.now());
            challengesRepository.save(kt4);

            /// ===================================
            /// CHALLENGES - HTML/CSS/JS BEGINNER
            /// ===================================

            ChallengesModel web1 = new ChallengesModel();
            web1.setTitle("Página HTML básica");
            web1.setDescription("Crea una página HTML con título, párrafos y una imagen.");
            web1.setLanguage(LanguageChallenge.HTML_CSS_JS);
            web1.setLevel(LevelChallenge.BEGINNER);
            web1.setCreatedBy(admin);
            web1.setCreatedAt(LocalDateTime.now());
            challengesRepository.save(web1);

            ChallengesModel web2 = new ChallengesModel();
            web2.setTitle("Formulario de contacto");
            web2.setDescription("Crea un formulario HTML con inputs para nombre, email y mensaje.");
            web2.setLanguage(LanguageChallenge.HTML_CSS_JS);
            web2.setLevel(LevelChallenge.BEGINNER);
            web2.setCreatedBy(admin);
            web2.setCreatedAt(LocalDateTime.now());
            challengesRepository.save(web2);

            /// ===================================
            /// CHALLENGES - HTML/CSS/JS INTERMEDIATE
            /// ===================================

            ChallengesModel web3 = new ChallengesModel();
            web3.setTitle("Landing page responsive");
            web3.setDescription("Crea una landing page moderna y responsive con HTML, CSS y JavaScript.");
            web3.setLanguage(LanguageChallenge.HTML_CSS_JS);
            web3.setLevel(LevelChallenge.INTERMEDIATE);
            web3.setCreatedBy(admin);
            web3.setCreatedAt(LocalDateTime.now());
            challengesRepository.save(web3);

            ChallengesModel web4 = new ChallengesModel();
            web4.setTitle("Calculadora con JavaScript");
            web4.setDescription("Crea una calculadora funcional usando HTML, CSS y JavaScript.");
            web4.setLanguage(LanguageChallenge.HTML_CSS_JS);
            web4.setLevel(LevelChallenge.INTERMEDIATE);
            web4.setCreatedBy(admin);
            web4.setCreatedAt(LocalDateTime.now());
            challengesRepository.save(web4);

            ChallengesModel web5 = new ChallengesModel();
            web5.setTitle("To-Do List interactiva");
            web5.setDescription("Crea una lista de tareas donde puedas agregar, completar y eliminar tareas.");
            web5.setLanguage(LanguageChallenge.HTML_CSS_JS);
            web5.setLevel(LevelChallenge.INTERMEDIATE);
            web5.setCreatedBy(admin);
            web5.setCreatedAt(LocalDateTime.now());
            challengesRepository.save(web5);

            System.out.println("Challenges creados: " + challengesRepository.count());

            /// ===================================
            /// RESUMEN FINAL
            /// ===================================

            System.out.println("SEEDERS COMPLETADOS");
            System.out.println("\nDATOS CREADOS:");
            System.out.println("Usuarios: " + userRepository.count());
            System.out.println("Challenges: " + challengesRepository.count());

            System.out.println("\nCREDENCIALES DE PRUEBA:");
            System.out.println("ADMIN: ");
            System.out.println("Email: admin@deepcode.com");
            System.out.println("Pass:  admin123");
            System.out.println("USUARIOS DE PRUEBA: ");
            System.out.println("alex@test.com / test123");
            System.out.println("maria@test.com / test123");
            System.out.println("carlos@test.com / test123");

            System.out.println("\nDISTRIBUCIÓN DE CHALLENGES:");
            System.out.println("Python: 6 challenges (3 Beginner, 3 Intermediate)");
            System.out.println("Java:  4 challenges (2 Beginner, 2 Intermediate)");
            System.out.println("Kotlin:4 challenges (2 Beginner, 2 Intermediate)");
            System.out.println("HTML/CSS/JS: 5 challenges (2 Beginner, 3 Intermediate)");
            System.out.println("\nAPI lista en: http://localhost:8080");
        };
    }
}