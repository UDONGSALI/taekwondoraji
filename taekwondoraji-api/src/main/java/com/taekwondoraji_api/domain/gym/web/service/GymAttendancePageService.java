package com.taekwondoraji_api.domain.gym.web.service;

import com.taekwondoraji_api.common.code.BeltCode;
import com.taekwondoraji_api.domain.attendance.repository.MemberAttendanceRepository;
import com.taekwondoraji_api.domain.gym.web.dto.MemberAttendanceCountRow;
import com.taekwondoraji_api.domain.gym.web.dto.MemberAttendanceInfoPage;
import com.taekwondoraji_api.domain.gym.web.dto.MemberAttendanceRankRow;
import com.taekwondoraji_api.domain.gym.web.dto.MemberAttendanceRow;
import com.taekwondoraji_api.domain.gym.web.dto.MemberAttendanceSummary;
import com.taekwondoraji_api.domain.member.entity.MemberGymMap;
import com.taekwondoraji_api.domain.member.repository.MemberGymMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GymAttendancePageService {

    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    private final MemberGymMapRepository memberGymMapRepository;
    private final MemberAttendanceRepository memberAttendanceRepository;

    public MemberAttendanceInfoPage getAttendanceInfoPage(Integer gymId, String month) {
        YearMonth currentMonth = YearMonth.now();
        YearMonth selectedMonth = parseMonth(month, currentMonth);
        LocalDate startDate = selectedMonth.atDay(1);
        LocalDate endDate = selectedMonth.atEndOfMonth();
        int attendanceDays = getAttendanceDays(selectedMonth);

        Map<Integer, Long> attendanceCountMap = memberAttendanceRepository
                .countMonthlyAttendancesByGym(gymId, startDate, endDate)
                .stream()
                .collect(Collectors.toMap(
                        MemberAttendanceCountRow::memberGymMapId,
                        MemberAttendanceCountRow::attendanceCount
                ));

        List<MemberGymMap> memberGymMaps = memberGymMapRepository.findAllByGym_GymIdOrderByMemberGymMapIdDesc(gymId);
        List<MemberAttendanceRow> rows = IntStream.range(0, memberGymMaps.size())
                .mapToObj(index -> toRow(
                        index + 1,
                        memberGymMaps.get(index),
                        attendanceCountMap.getOrDefault(memberGymMaps.get(index).getMemberGymMapId(), 0L),
                        attendanceDays
                ))
                .toList();

        int totalAttendanceCount = rows.stream()
                .mapToInt(MemberAttendanceRow::attendanceCount)
                .sum();

        MemberAttendanceSummary summary = new MemberAttendanceSummary(
                rows.size(),
                totalAttendanceCount,
                formatDecimalAverage(totalAttendanceCount, rows.size()) + "\uD68C",
                formatRate(totalAttendanceCount, rows.size() * attendanceDays),
                attendanceDays
        );

        return new MemberAttendanceInfoPage(
                selectedMonth.format(MONTH_FORMATTER),
                currentMonth.format(MONTH_FORMATTER),
                selectedMonth.getYear() + "\uB144 " + selectedMonth.getMonthValue() + "\uC6D4",
                rows,
                summary,
                toRankRows(rows, Comparator.comparingInt(MemberAttendanceRow::attendanceCount).reversed()),
                toRankRows(rows, Comparator.comparingInt(MemberAttendanceRow::attendanceCount)
                        .thenComparing(MemberAttendanceRow::memberName))
        );
    }

    private YearMonth parseMonth(String month, YearMonth currentMonth) {
        if (month == null || month.isBlank()) {
            return currentMonth;
        }

        try {
            YearMonth parsedMonth = YearMonth.parse(month.trim(), MONTH_FORMATTER);
            return parsedMonth.isAfter(currentMonth) ? currentMonth : parsedMonth;
        } catch (DateTimeParseException exception) {
            return currentMonth;
        }
    }

    private int getAttendanceDays(YearMonth selectedMonth) {
        YearMonth currentMonth = YearMonth.now();
        if (selectedMonth.equals(currentMonth)) {
            return LocalDate.now().getDayOfMonth();
        }

        return selectedMonth.lengthOfMonth();
    }

    private MemberAttendanceRow toRow(
            int no,
            MemberGymMap memberGymMap,
            long attendanceCount,
            int attendanceDays
    ) {
        int count = Math.toIntExact(attendanceCount);
        return new MemberAttendanceRow(
                no,
                memberGymMap.getMemberGymMapId(),
                memberGymMap.getMemberInfo().getMemberName(),
                memberGymMap.getMemberRole().getLabel(),
                memberGymMap.getMemberStatus().getLabel(),
                BeltCode.label(memberGymMap.getBeltName()),
                count,
                formatRate(count, attendanceDays)
        );
    }

    private List<MemberAttendanceRankRow> toRankRows(
            List<MemberAttendanceRow> rows,
            Comparator<MemberAttendanceRow> comparator
    ) {
        List<MemberAttendanceRow> rankedRows = rows.stream()
                .sorted(comparator.thenComparing(MemberAttendanceRow::memberName))
                .limit(3)
                .toList();

        return IntStream.range(0, rankedRows.size())
                .mapToObj(index -> new MemberAttendanceRankRow(
                        index + 1,
                        rankedRows.get(index).memberName(),
                        rankedRows.get(index).memberRole(),
                        rankedRows.get(index).attendanceCount(),
                        rankedRows.get(index).attendanceRateLabel()
                ))
                .toList();
    }

    private String formatDecimalAverage(int numerator, int denominator) {
        if (denominator <= 0) {
            return "0";
        }

        return BigDecimal.valueOf(numerator)
                .divide(BigDecimal.valueOf(denominator), 1, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString();
    }

    private String formatRate(int numerator, int denominator) {
        if (denominator <= 0) {
            return "0%";
        }

        return BigDecimal.valueOf(numerator)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(denominator), 1, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString() + "%";
    }
}
