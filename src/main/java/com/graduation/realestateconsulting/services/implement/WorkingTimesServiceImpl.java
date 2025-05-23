package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.config.JwtService;
import com.graduation.realestateconsulting.model.dto.request.WorkingTimesRequest;
import com.graduation.realestateconsulting.model.dto.response.WorkingTimesResponse;
import com.graduation.realestateconsulting.model.entity.Expert;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.entity.WorkingTimes;
import com.graduation.realestateconsulting.model.mapper.WorkingTimesMapper;
import com.graduation.realestateconsulting.repository.ExpertRepository;
import com.graduation.realestateconsulting.repository.UserRepository;
import com.graduation.realestateconsulting.repository.WorkingTimesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.graduation.realestateconsulting.services.WorkingTimesService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkingTimesServiceImpl implements WorkingTimesService {
    private final WorkingTimesRepository workingTimesRepository;
    private final ExpertRepository expertRepository;
    private final WorkingTimesMapper workingTimesMapper;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(WorkingTimesServiceImpl.class);

    @Transactional
    @Override
    public List<WorkingTimesResponse> createWorkingTimes(Long id, List<WorkingTimesRequest> workingTimesRequest) {
        Expert expert = expertRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("expert not found"));
        List<WorkingTimes> workingTimesLis = workingTimesMapper.toEntities(workingTimesRequest, expert);
        workingTimesLis.forEach(wt -> wt.setExpert(expert));
        workingTimesRepository.saveAll(workingTimesLis);
        return workingTimesMapper.toDto(workingTimesLis);
    }

    @Override
    public WorkingTimesResponse getWorkingTimes(Long id) {
        WorkingTimes workingTimes = workingTimesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("working times not found"));
        return workingTimesMapper.toDto(workingTimes);
    }

    @Override
    public List<WorkingTimesResponse> getWorkingTimes() {
        User user = userRepository.findByEmail(jwtService.getCurrentUserName()).orElseThrow(() -> new IllegalArgumentException("user not found"));
        List<WorkingTimes> workingTimes = workingTimesRepository.findByExpert_Id(user.getId());
        return workingTimesMapper.toDto(workingTimes);
    }

    @Override
    public WorkingTimesResponse updateWorkingTime(Long id, WorkingTimesRequest workingTimesRequest) {
        WorkingTimes workingTimes = workingTimesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("working times not found"));
        workingTimesMapper.toEntity(workingTimesRequest, workingTimes);
        return workingTimesMapper.toDto(workingTimesRepository.save(workingTimes));
    }

    @Transactional
    @Override
    public void deleteWorkingTime(Long id) {
        WorkingTimes workingTimes = workingTimesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("working times not found"));
        workingTimesRepository.delete(workingTimes);
    }
}