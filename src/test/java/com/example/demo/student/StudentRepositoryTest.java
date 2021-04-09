package com.example.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

                                        //UNIT TESTING OUR REPOSITORY

//when unit testing a repository, use DataJpaTest
//Annotation for a JPA test that focuses only on JPA components.
//Using this annotation will disable full auto-configuration
// and instead apply only configuration relevant to JPA tests.
@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;


    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    //testing our repository because we have a self made method inside of it
    //Boolean selectExistsEmail(String email); , testing if our method indeed does work and execute like it should

    @Test
    void itShouldCheckIfStudentEmailExists() {
        //given
        String email = "jake@gmail.com";

        Student student = new Student(
                "Jake", email, Gender.MALE
        );
        underTest.save(student);

        //when
        boolean exists = underTest.selectExistsEmail(email);

        //then
        assertThat(exists).isTrue();
    }

    @Test
    void itShouldCheckIfStudentEmailDoesNotExist() {
        //given
        String email = "jake@gmail.com";

        //when
        boolean exists = underTest.selectExistsEmail(email);

        //then
        assertThat(exists).isFalse();
    }

}