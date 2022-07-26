package com.minji.springbootjpa.test;

import com.minji.springbootjpa.nplus1.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JpaTest {

    @Autowired
    private AcademyRepository academyRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private AcademyService academyService;

    @AfterEach
    public void cleanAll() {
        System.out.println("=========================================삭제 수행=========================================");
        academyRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    @BeforeEach
    public void setup() {
        List<Academy> academies = new ArrayList<>();
        Teacher teacher = teacherRepository.save(new Teacher("선생님"));

        for (int i = 0; i < 10; i++) {
            Academy academy = Academy.builder()
                    .name("강남스쿨" + i)
                    .build();

            academy.addSubject(Subject.builder().name("자바웹개발" + i).teacher(teacher).build());
            academy.addSubject(Subject.builder().name("파이썬자동화" + i).teacher(teacher).build()); // Subject를 추가
            academies.add(academy);
        }

        academyRepository.saveAll(academies);
    }

    @Test
    public void Academy여러개를_조회시_Subject가_N1_쿼리가발생한다() throws Exception {
        System.out.println("=========================================테스트 전=========================================");

        //given
        List<String> subjectNames = academyService.findAllSubjectNames();

        //then
        assertThat(subjectNames.size()).isEqualTo(10);
        System.out.println("=========================================테스트 후=========================================");


    }

    @Test
    public void AcademyRepo여러개를_조회시_Subject가_N1_쿼리가발생한다() throws Exception {
        System.out.println("=========================================테스트 전=========================================");

        //given
        List<Academy> academyList = academyRepository.findAll();

        //then
        assertThat(academyList.size()).isEqualTo(10);
        System.out.println("=========================================테스트 후=========================================");

    }

    @Test
    public void Academy여러개를_joinFetch로_가져온다() throws Exception {
        //given
        List<Academy> academies = academyRepository.findAllJoinFetch();

        //then
        assertThat(academies.size()).isEqualTo(20); // 20개가 조회!?
    }

    @Test
    public void Academy여러개를_EntityGraph로_가져온다() throws Exception {
        //given
        List<Academy> academies = academyRepository.findAllEntityGraph();
        List<String> subjectNames = academyService.findAllSubjectNamesByEntityGraph();

        //then
        assertThat(academies.size()).isEqualTo(20);
        assertThat(subjectNames.size()).isEqualTo(20);
    }

    @Test
    public void Academy여러개를_distinct해서_가져온다() throws Exception {
        //given
        System.out.println("조회 시작");
        List<Academy> academies = academyRepository.findAllJoinFetchDistinct();

        //then
        System.out.println("조회 끝");
        assertThat(academies.size()).isEqualTo(10);
    }

    @Test
    public void Academy_Subject_Teacher를_한번에_가져온다() throws Exception {
        //given
        System.out.println("조회 시작");
        List<Teacher> teachers = academyRepository.findAllWithTeacher().stream()
                .map(a -> a.getSubjects().get(0).getTeacher())
                .collect(Collectors.toList());

        //then
        System.out.println("조회 끝");
        assertThat(teachers.size()).isEqualTo(10);
    }

    @Test
    public void Academy_Subject_Teacher를_EntityGraph한번에_가져온다() throws Exception {
        //given
        System.out.println("조회 시작");
        List<Teacher> teachers = academyRepository.findAllEntityGraphWithTeacher().stream()
                .map(a -> a.getSubjects().get(0).getTeacher())
                .collect(Collectors.toList());

        //then
        System.out.println("조회 끝");
        assertThat(teachers.size()).isEqualTo(10);
        assertThat(teachers.get(0).getName()).isEqualTo("선생님");

    }
}