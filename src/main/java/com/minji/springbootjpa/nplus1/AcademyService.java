package com.minji.springbootjpa.nplus1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AcademyService {

    private AcademyRepository academyRepository;

    public AcademyService(AcademyRepository academyRepository) {
        this.academyRepository = academyRepository;
    }

    @Transactional
    public List<String> findAllSubjectNames() {
        return extractSubjectNames(academyRepository.findAll());
    }

    @Transactional
    public List<String> findAllSubjectNamesByJoinFetch() {
        return extractSubjectNames(academyRepository.findAllJoinFetch());
    }

    @Transactional
    public List<String> findAllSubjectNamesByEntityGraph() {
        return extractSubjectNames(academyRepository.findAllEntityGraph());
    }

    @Transactional
    public List<String> findAllSubjectNamesByJoinFetchDistinct() {
        return extractSubjectNames(academyRepository.findAllJoinFetchDistinct());
    }

    @Transactional
    public List<String> findAllSubjectNamesByEntityGraphDistinct() {
        return extractSubjectNames(academyRepository.findAllEntityGraphDistinct());
    }

    /**
     * Lazy Load를 수행하기 위해 메소드를 별도로 생성
     */
    private List<String> extractSubjectNames(List<Academy> academies) {
        log.info(">>>>>>>>[모든 과목을 추출한다]<<<<<<<<<");
        log.info("Academy Size : {}", academies.size());

        return academies.stream()
                .map(a -> a.getSubjects().get(0).getName())
                .collect(Collectors.toList());
    }
}