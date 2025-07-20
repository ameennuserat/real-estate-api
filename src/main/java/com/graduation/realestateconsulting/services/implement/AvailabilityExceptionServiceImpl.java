package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.config.JwtService;
import com.graduation.realestateconsulting.model.dto.request.AvailabilityExceptionRequest;
import com.graduation.realestateconsulting.model.dto.request.UpdateAvailabilityExceptionRequest;
import com.graduation.realestateconsulting.model.dto.response.AvailabilityExceptionResponse;
import com.graduation.realestateconsulting.model.entity.AvailabilityExceptions;
import com.graduation.realestateconsulting.model.entity.Expert;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.mapper.AvailabilityExceptionMapper;
import com.graduation.realestateconsulting.repository.AvailabilityExceptionRepository;
import com.graduation.realestateconsulting.repository.ExpertRepository;
import com.graduation.realestateconsulting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.graduation.realestateconsulting.services.AvailabilityExceptionService;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityExceptionServiceImpl implements AvailabilityExceptionService {
    private final AvailabilityExceptionRepository repository;
    private final ExpertRepository expertRepository;
    private final UserRepository userRepository;
    private final AvailabilityExceptionMapper mapper;
    private final JwtService jwtService;

    @Override
    public AvailabilityExceptionResponse create(AvailabilityExceptionRequest availabilityExceptions) {
        User user = userRepository.findByEmail(jwtService.getCurrentUserName()).orElseThrow(() -> new RuntimeException("User not found"));
        Expert expert = user.getExpert();
        AvailabilityExceptions availabilityExceptions1 = mapper.toEntity(availabilityExceptions);
        availabilityExceptions1.setExpert(expert);
         return mapper.toDto(repository.save(availabilityExceptions1));
    }

    @Override
    public AvailabilityExceptionResponse update(UpdateAvailabilityExceptionRequest availabilityExceptions, Long id) {
        AvailabilityExceptions availabilityExceptions1 = repository.findById(id).orElseThrow(()->new RuntimeException("Exception not fount"));
        mapper.updateFromDto(availabilityExceptions, availabilityExceptions1);
        return mapper.toDto(repository.save(availabilityExceptions1));
    }

    @Override
    public List<AvailabilityExceptionResponse> getAll() {
        List<AvailabilityExceptions> availabilityExceptions = repository.findAll();
        return mapper.toDtos(availabilityExceptions);
    }

    @Override
    public void delete(Long id) {
        AvailabilityExceptions availabilityExceptions = repository.findById(id).orElseThrow(()->new RuntimeException("Exception not fount"));
        repository.delete(availabilityExceptions);
    }
}