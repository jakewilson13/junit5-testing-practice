package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

//does what we have commeted out on 23, 32, 37-40
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    //creating a mock of the student repository because we already tested it and ensured it's functionality
    //unit testing are isolated tests, only really want to focus on our service and not the repo
    @Mock
    private StudentRepository studentRepository;

//    private AutoCloseable autoCloseable;

    private StudentService underTest;

    @BeforeEach
    void setUp() {
        //if we have more than one mock it will initialize all of the mocks inside of this test class
        //this returns an autocloseable, which is
        //an object that may hold resources (such as file or socket handles) until it is closed.
//        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new StudentService(studentRepository);
    }

    //allows us to close the resource after the test
//    @AfterEach
//    void tearDown() throws Exception {
//        autoCloseable.close();
//    }

    @Test
    void canGetAllStudents() {
        //when
        underTest.getAllStudents();

        //then
        verify(studentRepository).findAll();
    }

    @Test
    void canAddStudent() {
        //given
        Student student = new Student(
                "Jake", "jake@gmail.com", Gender.MALE
        );

        //when

        //adding the student
        underTest.addStudent(student);

        //then

        //argument capture is used to capture ARGUMENT values for further assertions, in our service the argument given is student
        //provided by mockito
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);

        //verifying that the repository was invoked with the save method, and capture the values
        verify(studentRepository)
                .save(studentArgumentCaptor.capture());

        //we captured the value so we can make sure that it is the exact same one which was invoked by the underTest
        Student captured = studentArgumentCaptor.getValue();

        assertThat(captured).isEqualTo(student);
    }

    @Test
    void willThrowWhenEmailIsTaken() {
        //given
        Student student = new Student(
                "Jake", "jake@gmail.com", Gender.MALE
        );

        //when
        given(studentRepository.selectExistsEmail(student.getEmail()))
                .willReturn(true);


        //then
        assertThatThrownBy(() -> underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + student.getEmail() + " taken");

        verify(studentRepository, never()).save(any());
    }

    @Test
    @Disabled
    void deleteStudent() {
    }
}