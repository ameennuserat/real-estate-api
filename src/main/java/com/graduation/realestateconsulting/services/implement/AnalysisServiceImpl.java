package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.response.AnalysisResponse;
import com.graduation.realestateconsulting.repository.*;
import com.graduation.realestateconsulting.services.AnalysisService;
import com.graduation.realestateconsulting.services.OfficeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {

    private final ExpertRepository expertRepository;
    private final OfficeRepository officeRepository;
    private final ClientRepository clientRepository;
    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;
    private final PostsRepository postsRepository;
    private final PropertyRepository propertyRepository;
    private final PropertyImageRepository propertyImageRepository;
    private final ReportRepository reportRepository;


    @Override
    public AnalysisResponse getAnalysis() {
        return AnalysisResponse.builder()
                .expertCount(expertRepository.count())
                .officeCount(officeRepository.count())
                .bookingCount(bookingRepository.count())
                .ticketCount(ticketRepository.count())
                .postsCount(postsRepository.count())
                .propertyCount(propertyRepository.count())
                .propertyImageCount(propertyImageRepository.count())
                .ClientCount(clientRepository.count())
                .reportCount(reportRepository.count())
                .build();
    }
}