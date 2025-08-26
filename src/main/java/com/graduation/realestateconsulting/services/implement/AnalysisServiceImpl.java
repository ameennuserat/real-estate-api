package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.response.AnalysisResponse;
import com.graduation.realestateconsulting.model.enums.BookingStatus;
import com.graduation.realestateconsulting.repository.*;
import com.graduation.realestateconsulting.services.AnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
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
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startOfCurrentMonth = now.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endOfCurrentMonth = now.plusDays(1).truncatedTo(ChronoUnit.DAYS).minusNanos(1);

        LocalDateTime endOfPreviousMonth = startOfCurrentMonth.minusDays(1);
        LocalDateTime startOfPreviousMonth = endOfPreviousMonth.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);

        long expertCurrentMonth =  expertRepository.countBetweenDates(startOfCurrentMonth, endOfCurrentMonth);
        long expertPreviousMonth =  expertRepository.countBetweenDates(startOfPreviousMonth, endOfPreviousMonth);
        double expertIncreasePercentage = 0.0;
        if (expertPreviousMonth > 0) {
            expertIncreasePercentage = ((double) (expertCurrentMonth - expertPreviousMonth) / expertPreviousMonth) * 100;
        }
        log.info("endOfCurrentMonth: {}, startOfCurrentMonth: {},endOfPreviousMonth: {},startOfPreviousMonth: {}",endOfCurrentMonth,startOfCurrentMonth,endOfPreviousMonth,startOfPreviousMonth);
        log.info("expertCurrentMonth: {}, expertPreviousMonth: {},expertIncreasePercentage: {}", expertCurrentMonth, expertPreviousMonth, expertIncreasePercentage);

        long officeCurrentMonth =  officeRepository.countBetweenDates(startOfCurrentMonth, endOfCurrentMonth);
        long officePreviousMonth =  officeRepository.countBetweenDates(startOfPreviousMonth, endOfPreviousMonth);
        double officeIncreasePercentage = 0.0;
        if (officePreviousMonth > 0) {
            officeIncreasePercentage = ((double) (officeCurrentMonth - officePreviousMonth) / officePreviousMonth) * 100;
        }

        long clientCurrentMonth =  clientRepository.countBetweenDates(startOfCurrentMonth, endOfCurrentMonth);
        long clientPreviousMonth =  clientRepository.countBetweenDates(startOfPreviousMonth, endOfPreviousMonth);
        double clientIncreasePercentage = 0.0;
        if (clientPreviousMonth > 0) {
            clientIncreasePercentage = ((double) (clientCurrentMonth - clientPreviousMonth) / clientPreviousMonth) * 100;
        }

        long ticketCurrentMonth =  ticketRepository.countBetweenDates(startOfCurrentMonth, endOfCurrentMonth);
        long ticketPreviousMonth =  ticketRepository.countBetweenDates(startOfPreviousMonth, endOfPreviousMonth);
        double ticketIncreasePercentage = 0.0;
        if (ticketPreviousMonth > 0) {
            ticketIncreasePercentage = ((double) (ticketCurrentMonth - ticketPreviousMonth) / ticketPreviousMonth) * 100;
        }

        long postsCurrentMonth =  postsRepository.countBetweenDates(startOfCurrentMonth, endOfCurrentMonth);
        long postsPreviousMonth =  postsRepository.countBetweenDates(startOfPreviousMonth, endOfPreviousMonth);
        double postsIncreasePercentage = 0.0;
        if (postsPreviousMonth > 0) {
            postsIncreasePercentage = ((double) (postsCurrentMonth - postsPreviousMonth) / postsPreviousMonth) * 100;
        }

        long propertyCurrentMonth =  propertyRepository.countBetweenDates(startOfCurrentMonth, endOfCurrentMonth);
        long propertyPreviousMonth =  propertyRepository.countBetweenDates(startOfPreviousMonth, endOfPreviousMonth);
        double propertyIncreasePercentage = 0.0;
        if (propertyPreviousMonth > 0) {
            propertyIncreasePercentage = ((double) (propertyCurrentMonth - propertyPreviousMonth) / propertyPreviousMonth) * 100;
        }

        long propertyImageCurrentMonth =  propertyImageRepository.countBetweenDates(startOfCurrentMonth, endOfCurrentMonth);
        long propertyImagePreviousMonth =  propertyImageRepository.countBetweenDates(startOfPreviousMonth, endOfPreviousMonth);
        double propertyImageIncreasePercentage = 0.0;
        if (propertyImagePreviousMonth > 0) {
            propertyImageIncreasePercentage = ((double) (propertyImageCurrentMonth - propertyImagePreviousMonth) / propertyImagePreviousMonth) * 100;
        }

        long reportCurrentMonth =  reportRepository.countBetweenDates(startOfCurrentMonth, endOfCurrentMonth);
        long reportPreviousMonth =  reportRepository.countBetweenDates(startOfPreviousMonth, endOfPreviousMonth);
        double reportIncreasePercentage = 0.0;
        if (reportPreviousMonth > 0) {
            reportIncreasePercentage = ((double) (reportCurrentMonth - reportPreviousMonth) / reportPreviousMonth) * 100;
        }

        long bookingCurrentMonth =  bookingRepository.countBetweenDates(startOfCurrentMonth, endOfCurrentMonth);
        long bookingPreviousMonth =  bookingRepository.countBetweenDates(startOfPreviousMonth, endOfPreviousMonth);
        double bookingIncreasePercentage = 0.0;
        if (bookingPreviousMonth > 0) {
            bookingIncreasePercentage = ((double) (bookingCurrentMonth - bookingPreviousMonth) / bookingPreviousMonth) * 100;
        }

        double revenuesCurrentMonth =  bookingRepository.getAllRevenues(startOfCurrentMonth, endOfCurrentMonth, BookingStatus.COMPLETED);
        double revenuesPreviousMonth =  bookingRepository.getAllRevenues(startOfPreviousMonth, endOfPreviousMonth, BookingStatus.COMPLETED);
        double revenuesIncreasePercentage = 0.0;
        if (revenuesPreviousMonth > 0) {
            revenuesIncreasePercentage = ((revenuesCurrentMonth - revenuesPreviousMonth) / revenuesPreviousMonth) * 100;
        }

        return AnalysisResponse.builder()
                .expertCount(expertRepository.count())
                .expertCurrentMonthCount(expertCurrentMonth)
                .expertIncreasePercentage(expertIncreasePercentage)
                .officeCount(officeRepository.count())
                .officeCurrentMonthCount(officeCurrentMonth)
                .officeIncreasePercentage(officeIncreasePercentage)
                .clientCount(clientRepository.count())
                .clientCurrentMonthCount(clientCurrentMonth)
                .clientIncreasePercentage(clientIncreasePercentage)
                .ticketCount(ticketRepository.count())
                .ticketCurrentMonthCount(ticketCurrentMonth)
                .ticketIncreasePercentage(ticketIncreasePercentage)
                .propertyCount(propertyRepository.count())
                .propertyCurrentMonthCount(propertyCurrentMonth)
                .propertyIncreasePercentage(propertyIncreasePercentage)
                .propertyImageCount(propertyImageRepository.count())
                .propertyImageCurrentMonthCount(propertyImageCurrentMonth)
                .propertyImageIncreasePercentage(propertyImageIncreasePercentage)
                .postsCount(postsRepository.count())
                .postsCurrentMonthCount(postsCurrentMonth)
                .postsIncreasePercentage(postsIncreasePercentage)
                .bookingCount(bookingRepository.count())
                .bookingCurrentMonthCount(bookingCurrentMonth)
                .bookingIncreasePercentage(bookingIncreasePercentage)
                .reportCount(reportRepository.count())
                .reportCurrentMonthCount(reportCurrentMonth)
                .reportIncreasePercentage(reportIncreasePercentage)

                .revenuesCurrentMonthCount(revenuesCurrentMonth)
                .revenuesIncreasePercentage(revenuesIncreasePercentage)

                .build();
    }
}