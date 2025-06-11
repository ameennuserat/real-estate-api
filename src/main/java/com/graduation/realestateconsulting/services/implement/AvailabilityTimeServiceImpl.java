package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.request.AvailabilityExceptionRequest;
import com.graduation.realestateconsulting.model.dto.request.AvailableTimeToBookingRequest;
import com.graduation.realestateconsulting.model.entity.AvailabilityExceptions;
import com.graduation.realestateconsulting.model.entity.Booking;
import com.graduation.realestateconsulting.model.entity.WorkingTimes;
import com.graduation.realestateconsulting.model.enums.BookingStatus;
import com.graduation.realestateconsulting.repository.AvailabilityExceptionRepository;
import com.graduation.realestateconsulting.repository.BookingRepository;
import com.graduation.realestateconsulting.repository.WorkingTimesRepository;
import com.graduation.realestateconsulting.services.AvailabilityTimeService;
import com.graduation.realestateconsulting.trait.TimeBlock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvailabilityTimeServiceImpl implements AvailabilityTimeService {
    private final WorkingTimesRepository workingTimesRepository;
    private final AvailabilityExceptionRepository availabilityExceptionRepository;
    private final BookingRepository bookingRepository;

    @Override
    public List<LocalTime> findAvailablePeriods(AvailableTimeToBookingRequest request) {
        // 1. حساب فترات الإتاحة الفعلية للخبير في اليوم المحدد.
        // هذه تأخذ في الاعتبار ساعات العمل القياسية والاستثناءات (إجازات أو ساعات إضافية).
        List<TimeBlock> effectiveAvailabilityBlocks = calculateEffectiveAvailability(request.getExpertId(), request.getDate());
        // 2. جلب الحجوزات القائمة (العوائق) لهذا الخبير في هذا اليوم.
        // يتم استبعاد الحجوزات الملغاة.
        List<TimeBlock> bookedSlots = getBookedObstacles(request.getExpertId(), request.getDate());

        // 3. إعداد قائمة لتخزين أوقات البدء المتاحة.
        List<LocalTime> availableStartTimes = new ArrayList<>();

        // 4. تحديد أصغر فاصل زمني للتحقق من الفتحات (مثلاً، كل 15 دقيقة).
        // هذا يسمح بإيجاد فتحات تبدأ في أوقات مختلفة حتى لو كانت مدة الجلسة أطول.
        int slotIncrementInMinutes = request.getDuration(); // يمكن جعله قابلاً للتكوين أو مرتبطاً بأصغر مدة حجز

        // 5. المرور على كل فترة إتاحة فعلية للبحث عن فتحات.
        for (TimeBlock effectiveBlock : effectiveAvailabilityBlocks) {
            LocalDateTime currentPotentialStartTime = effectiveBlock.start();

            // استمر في التحقق طالما أن نهاية الفتحة المحتملة (البداية + المدة)
            // لا تتجاوز نهاية فترة الإتاحة الفعلية.
            while (!currentPotentialStartTime.plusMinutes(request.getDuration()).isAfter(effectiveBlock.end())) {
                LocalDateTime potentialSlotEnd = currentPotentialStartTime.plusMinutes(request.getDuration());

                // بناء فترة زمنية للفتحة المقترحة.
                TimeBlock potentialSlot = new TimeBlock(currentPotentialStartTime, potentialSlotEnd);

                // التحقق مما إذا كانت هذه الفتحة المقترحة تتداخل مع أي حجز قائم.
                boolean overlapsWithBooking = false;
                for (TimeBlock bookedSlot : bookedSlots) {
                    if (potentialSlot.overlaps(bookedSlot)) {
                        overlapsWithBooking = true;
                        break; // لا داعي للتحقق من حجوزات أخرى لهذه الفتحة.
                    }
                }

                // إذا لم يكن هناك تداخل، فهذه الفتحة متاحة.
                if (!overlapsWithBooking) {
                    availableStartTimes.add(currentPotentialStartTime.toLocalTime());
                }

                // انتقل إلى وقت البدء المحتمل التالي.
                currentPotentialStartTime = currentPotentialStartTime.plusMinutes(slotIncrementInMinutes);
            }
        }

        // 6. الحصول على الوقت والتاريخ الحاليين.
        LocalDateTime currentSystemDateTime = LocalDateTime.now(); // مهم: انتبه للمناطق الزمنية

        // 7. استدعاء التابع المساعد لتصفية الأوقات وفرزها.
        return filterAndSortFuturePeriods(availableStartTimes, request.getDate(), currentSystemDateTime);
    }


    @Override
    public List<TimeBlock> calculateEffectiveAvailability(Long expertId, LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        System.out.println(dayOfWeek);
        List<TimeBlock> baseWorkingBlocks = new ArrayList<>();

        // أ. جلب ساعات العمل القياسية (WorkingHours) لهذا اليوم.
        // بناءً على توضيحك، نتوقع سطراً واحداً على الأكثر من هذا الاستعلام.
        WorkingTimes workingHoursOpt = workingTimesRepository
                .findByExpertIdAndDayOfWeek(expertId, dayOfWeek).orElseThrow(() -> new IllegalArgumentException("this day not available")); // افترض أن هذا التابع يُرجع List
//        if (workingHoursOpt.isPresent()) {
        // WorkingHoursEntity wh = workingHoursOpt.get();
        // التحقق من أن وقت البدء قبل وقت النهاية في WorkingHoursEntity
        if (workingHoursOpt.getStartTime().isBefore(workingHoursOpt.getEndTime())) {
            baseWorkingBlocks.add(new TimeBlock(
                    LocalDateTime.of(date, workingHoursOpt.getStartTime()),
                    LocalDateTime.of(date, workingHoursOpt.getEndTime())
            ));
        }
        // }

        // ب. جلب جميع استثناءات الإتاحة (AvailabilityExceptions) لهذا الخبير في اليوم المحدد.
        LocalDateTime dayStartBoundary = date.atStartOfDay();
        LocalDateTime dayEndBoundary = date.plusDays(1).atStartOfDay();
        List<AvailabilityExceptions> exceptions = availabilityExceptionRepository
                .findAllByExpertIdAndEndTimeAfterAndStartTimeBefore(expertId, dayStartBoundary, dayEndBoundary);
        // استعلام يجلب الاستثناءات التي تتقاطع مع اليوم المحدد.

        List<TimeBlock> currentEffectiveBlocks = new ArrayList<>(baseWorkingBlocks);

        // ج. معالجة الاستثناءات التي هي "عدم إتاحة" (isAvailable = false).
        // هذه الاستثناءات "تقتطع" أو "تزيل" أجزاء من فترات العمل الأساسية.
        List<AvailabilityExceptions> blockerExceptions = exceptions.stream()
                .filter(ex -> !ex.isAvailable()) // افترض وجود تابع isAvailable() أو getAvailable()
                .toList();

        for (AvailabilityExceptions blockerEx : blockerExceptions) {
            List<TimeBlock> nextIterationBlocks = getTimeBlocks(blockerEx, currentEffectiveBlocks);
            currentEffectiveBlocks = nextIterationBlocks; // قم بتحديث قائمة الفترات الفعالة.
        }

        // د. إضافة الاستثناءات التي هي "إتاحة إضافية" (isAvailable = true).
        List<TimeBlock> extraAvailableBlocks = exceptions.stream()
                .filter(AvailabilityExceptions::isAvailable) // افترض وجود تابع isAvailable() أو getAvailable()
                .map(ex -> new TimeBlock(ex.getStartTime(), ex.getEndTime()))
                .filter(TimeBlock::isValid)
                .toList();
        currentEffectiveBlocks.addAll(extraAvailableBlocks);

        // هـ. فرز ودمج جميع الفترات الناتجة (الأساسية، والمعدلة، والإضافية)
        // للتخلص من التداخلات والحصول على قائمة نهائية نظيفة.
        return mergeAndSortTimeBlocks(currentEffectiveBlocks);
    }

    private static List<TimeBlock> getTimeBlocks(AvailabilityExceptions blockerEx, List<TimeBlock> currentEffectiveBlocks) {
        TimeBlock blockerPeriod = new TimeBlock(blockerEx.getStartTime(), blockerEx.getEndTime());
        List<TimeBlock> nextIterationBlocks = new ArrayList<>();
        for (TimeBlock effectiveBlock : currentEffectiveBlocks) {
            // إذا لم يكن هناك تداخل بين فترة الإتاحة الحالية وفترة الحظر، احتفظ بفترة الإتاحة.
            if (!effectiveBlock.overlaps(blockerPeriod)) {
                nextIterationBlocks.add(effectiveBlock);
                continue;
            }

            // هناك تداخل. قم بتقسيم فترة الإتاحة الحالية إلى أجزاء لا تتداخل مع فترة الحظر.
            // الجزء الأول: قبل فترة الحظر.
            if (effectiveBlock.start().isBefore(blockerPeriod.start())) {
                TimeBlock part1 = new TimeBlock(effectiveBlock.start(), blockerPeriod.start());
                if (part1.isValid()) nextIterationBlocks.add(part1);
            }
            // الجزء الثاني: بعد فترة الحظر.
            if (effectiveBlock.end().isAfter(blockerPeriod.end())) {
                TimeBlock part2 = new TimeBlock(blockerPeriod.end(), effectiveBlock.end());
                if (part2.isValid()) nextIterationBlocks.add(part2);
            }
        }
        return nextIterationBlocks;
    }

    @Override
    public List<TimeBlock> getBookedObstacles(Long expertId, LocalDate date) {
        LocalDateTime dayStartBoundary = date.atStartOfDay();
        LocalDateTime dayEndBoundary = date.plusDays(1).atStartOfDay();

        List<Booking> bookings = bookingRepository
                .findAllByExpertIdAndStartTimeBeforeAndEndTimeAfterAndBookingStatusNot(
                        expertId,
                        dayEndBoundary,     // الحجوزات التي تبدأ قبل نهاية اليوم
                        dayStartBoundary,   // وتنتهي بعد بداية اليوم (تتقاطع مع اليوم)
                        BookingStatus.CANCELED // استبعاد الملغاة (افترض وجود Enum BookingStatus)
                );

        return bookings.stream()
                .map(booking -> new TimeBlock(booking.getStartTime(), booking.getEndTime()))
                .sorted(Comparator.comparing(TimeBlock::start)) // الفرز حسب وقت البدء
                .collect(Collectors.toList());
    }

    @Override
    public List<TimeBlock> mergeAndSortTimeBlocks(List<TimeBlock> blocks) {
        if (blocks == null || blocks.isEmpty()) {
            return new ArrayList<>();
        }

        // 1. إزالة أي فترات غير صالحة (إذا وجدت) وفرز الفترات حسب وقت البدء.
        List<TimeBlock> sortedBlocks = blocks.stream()
                .filter(TimeBlock::isValid)
                .sorted(Comparator.comparing(TimeBlock::start))
                .toList();

        if (sortedBlocks.isEmpty()) {
            return new ArrayList<>();
        }

        List<TimeBlock> mergedBlocks = new ArrayList<>();
        TimeBlock currentMergeCandidate = sortedBlocks.get(0);

        for (int i = 1; i < sortedBlocks.size(); i++) {
            TimeBlock nextBlock = sortedBlocks.get(i);
            // إذا كان currentMergeCandidate ينتهي عند أو بعد بداية nextBlock (تداخل أو تجاور)
            if (!currentMergeCandidate.end().isBefore(nextBlock.start())) {
                // قم بتوسيع فترة الدمج الحالية لتشمل nextBlock
                currentMergeCandidate = new TimeBlock(
                        currentMergeCandidate.start(),
                        // اختر النهاية الأبعد بينهما
                        currentMergeCandidate.end().isAfter(nextBlock.end()) ? currentMergeCandidate.end() : nextBlock.end()
                );
            } else {
                // لا يوجد تداخل/تجاور، أضف فترة الدمج الحالية وابدأ فترة دمج جديدة.
                mergedBlocks.add(currentMergeCandidate);
                currentMergeCandidate = nextBlock;
            }
        }
        mergedBlocks.add(currentMergeCandidate); // أضف آخر فترة دمج مرشحة.

        return mergedBlocks;
    }

    @Override
    public List<LocalTime> filterAndSortFuturePeriods(List<LocalTime> generatedSlots, LocalDate queryDate, LocalDateTime currentDateTime) {
        if (generatedSlots == null || generatedSlots.isEmpty()) {
            return new ArrayList<>();
        }
        System.out.println(currentDateTime);
        return generatedSlots.stream()
                .distinct() // إزالة التكرارات أولاً
                .map(slotLocalTime -> LocalDateTime.of(queryDate, slotLocalTime)) // تحويل إلى LocalDateTime للمقارنة
                .filter(slotFullDateTime -> slotFullDateTime.isAfter(currentDateTime)) // الاحتفاظ بالفتحات المستقبلية فقط
                .map(LocalDateTime::toLocalTime) // إعادة التحويل إلى LocalTime
                .sorted() // فرز الأوقات المتبقية
                .collect(Collectors.toList());
    }
}
