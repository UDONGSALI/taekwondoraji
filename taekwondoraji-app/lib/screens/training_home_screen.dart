import 'package:flutter/material.dart';

import '../services/member_api_service.dart';
import '../theme/app_colors.dart';
import '../theme/app_text_styles.dart';
import 'goal_screen.dart';

class TrainingHomeScreen extends StatelessWidget {
  const TrainingHomeScreen({
    required this.memberGymMapId,
    required this.memberName,
    required this.gymName,
    required this.beltName,
    required this.point,
    this.memberAge,
    this.memberPhoneNumber,
    this.joinedDate,
    super.key,
  });

  final int? memberGymMapId;
  final String memberName;
  final String gymName;
  final String beltName;
  final int point;
  final int? memberAge;
  final String? memberPhoneNumber;
  final String? joinedDate;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      body: SafeArea(
        child: Column(
          children: [
            _AppHeader(gymName: gymName),
            Expanded(
              child: SingleChildScrollView(
                padding: const EdgeInsets.fromLTRB(14, 8, 14, 24),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    _TrainingProfileCard(
                      memberName: memberName,
                      gymName: gymName,
                      beltName: beltName,
                      point: point,
                      memberAge: memberAge,
                      memberPhoneNumber: memberPhoneNumber,
                      joinedDate: joinedDate,
                    ),
                    const SizedBox(height: 16),
                    _GoalLevelSection(memberGymMapId: memberGymMapId),
                    const SizedBox(height: 16),
                    _AttendanceSection(memberGymMapId: memberGymMapId),
                  ],
                ),
              ),
            ),
            _BottomNavBar(
              memberGymMapId: memberGymMapId,
              gymName: gymName,
            ),
          ],
        ),
      ),
    );
  }
}

class _TrainingProfileCard extends StatelessWidget {
  const _TrainingProfileCard({
    required this.memberName,
    required this.gymName,
    required this.beltName,
    required this.point,
    required this.memberAge,
    required this.memberPhoneNumber,
    required this.joinedDate,
  });

  final String memberName;
  final String gymName;
  final String beltName;
  final int point;
  final int? memberAge;
  final String? memberPhoneNumber;
  final String? joinedDate;

  @override
  Widget build(BuildContext context) {
    final infoRows = [
      _ProfileInfo(label: '이름', value: memberName),
      _ProfileInfo(label: '나이', value: memberAge == null ? '미등록' : '${memberAge}세'),
      _ProfileInfo(label: '등록일자', value: _emptyToDefault(joinedDate)),
      const _ProfileInfo(label: '좌우명', value: '한 걸음씩 강해지는 수련'),
      _ProfileInfo(label: '띠', value: _beltLabel(beltName)),
    ];

    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: AppColors.surface,
        border: Border.all(color: AppColors.border),
        borderRadius: BorderRadius.circular(18),
        boxShadow: const [
          BoxShadow(
            color: Color(0x12000000),
            blurRadius: 18,
            offset: Offset(0, 8),
          ),
        ],
      ),
      child: LayoutBuilder(
        builder: (context, constraints) {
          final photoWidth =
              (constraints.maxWidth * 0.38).clamp(112.0, 162.0).toDouble();

          return Row(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _PhotoPlaceholder(width: photoWidth),
              const SizedBox(width: 10),
              Expanded(child: _InfoList(rows: infoRows)),
            ],
          );
        },
      ),
    );
  }

  String _emptyToDefault(String? value) {
    final text = value?.trim();
    if (text == null || text.isEmpty) {
      return '미등록';
    }

    return text;
  }

  String _beltLabel(String value) {
    return switch (value.toLowerCase()) {
      'white' => '흰띠',
      'yellow' => '노란띠',
      'blue' => '파란띠',
      'red' => '빨간띠',
      'black' => '검은띠',
      _ => value,
    };
  }
}

class _PhotoPlaceholder extends StatelessWidget {
  const _PhotoPlaceholder({required this.width});

  final double width;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: width,
      height: 204,
      decoration: BoxDecoration(
        color: AppColors.white,
        border: Border.all(color: AppColors.border),
        borderRadius: BorderRadius.circular(14),
      ),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Icon(
            Icons.person_rounded,
            size: 56,
            color: AppColors.muted,
          ),
          const SizedBox(height: 12),
          const Text(
            '사진 영역',
            style: TextStyle(
              color: AppColors.muted,
              fontSize: 13,
              fontWeight: FontWeight.w700,
            ),
          ),
        ],
      ),
    );
  }
}

class _InfoList extends StatelessWidget {
  const _InfoList({required this.rows});

  final List<_ProfileInfo> rows;

  @override
  Widget build(BuildContext context) {
    return Column(
      children: List.generate(rows.length, (index) {
        final row = rows[index];
        return _InfoRow(
          label: row.label,
          value: row.value,
          showDivider: index != rows.length - 1,
        );
      }),
    );
  }
}

class _ProfileInfo {
  const _ProfileInfo({
    required this.label,
    required this.value,
  });

  final String label;
  final String value;
}

class _InfoRow extends StatelessWidget {
  const _InfoRow({
    required this.label,
    required this.value,
    required this.showDivider,
  });

  final String label;
  final String value;
  final bool showDivider;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(vertical: 9),
      decoration: BoxDecoration(
        border: showDivider
            ? const Border(bottom: BorderSide(color: AppColors.softBorder))
            : null,
      ),
      child: Row(
        children: [
          SizedBox(
            width: 62,
            child: Text(
              label,
              style: const TextStyle(
                color: AppColors.primary,
                fontSize: 14,
                fontWeight: FontWeight.w900,
              ),
            ),
          ),
          Container(
            width: 1,
            height: 16,
            color: AppColors.border,
          ),
          const SizedBox(width: 10),
          Expanded(
            child: Text(
              value,
              maxLines: 2,
              overflow: TextOverflow.ellipsis,
              style: const TextStyle(
                color: AppColors.text,
                fontSize: 14,
                fontWeight: FontWeight.w700,
                height: 1.2,
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class _AppHeader extends StatelessWidget {
  const _AppHeader({required this.gymName});

  final String gymName;

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 48,
      padding: const EdgeInsets.symmetric(horizontal: 12),
      color: AppColors.background,
      child: Row(
        children: [
          _HeaderIconButton(
            icon: Icons.menu_rounded,
            color: AppColors.primary,
            onPressed: () {
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(content: Text('설정 메뉴 준비 중입니다.')),
              );
            },
          ),
          const SizedBox(width: 4),
          Expanded(
            child: Text(
              gymName,
              maxLines: 1,
              overflow: TextOverflow.ellipsis,
              style: const TextStyle(
                color: AppColors.primary,
                fontSize: 14,
                fontWeight: FontWeight.w800,
              ),
            ),
          ),
          _HeaderIconButton(
            icon: Icons.notifications_rounded,
            color: Color(0xFFF4B63E),
            onPressed: () {
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(content: Text('알림 화면 준비 중입니다.')),
              );
            },
          ),
        ],
      ),
    );
  }
}

class _HeaderIconButton extends StatelessWidget {
  const _HeaderIconButton({
    required this.icon,
    required this.color,
    required this.onPressed,
  });

  final IconData icon;
  final Color color;
  final VoidCallback onPressed;

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: 36,
      height: 36,
      child: IconButton(
        onPressed: onPressed,
        padding: EdgeInsets.zero,
        icon: Icon(icon, size: 22, color: color),
      ),
    );
  }
}

class _GoalLevelSection extends StatelessWidget {
  const _GoalLevelSection({required this.memberGymMapId});

  final int? memberGymMapId;

  @override
  Widget build(BuildContext context) {
    final id = memberGymMapId;

    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        color: AppColors.surface,
        border: Border.all(color: AppColors.border),
        borderRadius: BorderRadius.circular(18),
        boxShadow: const [
          BoxShadow(
            color: Color(0x10000000),
            blurRadius: 12,
            offset: Offset(0, 5),
          ),
        ],
      ),
      child: id == null
          ? _GoalLevelGrid(levels: _defaultGoalLevels)
          : FutureBuilder<List<GoalLevel>>(
        future: MemberApiService().fetchGoalLevels(id),
        builder: (context, snapshot) {
          if (snapshot.connectionState != ConnectionState.done) {
            return const SizedBox(
              height: 190,
              child: Center(child: CircularProgressIndicator()),
            );
          }

          if (snapshot.hasError) {
            return const SizedBox(
              height: 190,
              child: Center(child: Text('성장 정보를 불러오지 못했습니다.')),
            );
          }

          final levels = snapshot.data ?? [];
          return _GoalLevelGrid(levels: levels);
        },
      ),
    );
  }
}

const _defaultGoalLevels = [
  GoalLevel(category: '기초체력', point: 0, level: 0),
  GoalLevel(category: '품새', point: 0, level: 0),
  GoalLevel(category: '외부활동', point: 0, level: 0),
  GoalLevel(category: '격파', point: 0, level: 0),
  GoalLevel(category: '발차기', point: 0, level: 0),
  GoalLevel(category: '겨루기', point: 0, level: 0),
];

class _GoalLevelGrid extends StatelessWidget {
  const _GoalLevelGrid({required this.levels});

  final List<GoalLevel> levels;

  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(
      builder: (context, constraints) {
        final columnCount = constraints.maxWidth < 360 ? 2 : 3;
        final itemWidth =
            (constraints.maxWidth - (10 * (columnCount - 1))) / columnCount;

      return Wrap(
          spacing: 10,
          runSpacing: 10,
          children: levels
              .map(
                (level) => SizedBox(
                  width: itemWidth,
                  height: 45,
                  child: _GoalLevelCard(level: level),
                ),
              )
              .toList(),
        );
      },
    );
  }
}

class _GoalLevelCard extends StatelessWidget {
  const _GoalLevelCard({required this.level});

  final GoalLevel level;

  @override
  Widget build(BuildContext context) {
    final compactCategory =
        level.category == '기초체력' ? '기초 체력' : level.category;

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 9),
      decoration: BoxDecoration(
        color: AppColors.white,
        border: Border.all(color: AppColors.border),
        borderRadius: BorderRadius.circular(12),
      ),
      child: Row(
        children: [
          Expanded(
            child: Text(
              compactCategory,
              maxLines: 1,
              overflow: TextOverflow.ellipsis,
              style: const TextStyle(
                color: AppColors.primary,
                fontSize: 13,
                fontWeight: FontWeight.w900,
              ),
            ),
          ),
          Container(
            width: 1,
            height: 20,
            color: AppColors.border,
          ),
          const SizedBox(width: 8),
          SizedBox(
            width: 40,
            child: Text(
              'Lv. ${level.level}',
              maxLines: 1,
              textAlign: TextAlign.right,
              style: const TextStyle(
                color: AppColors.text,
                fontSize: 13,
                fontWeight: FontWeight.w900,
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class _AttendanceSection extends StatefulWidget {
  const _AttendanceSection({required this.memberGymMapId});

  final int? memberGymMapId;

  @override
  State<_AttendanceSection> createState() => _AttendanceSectionState();
}

class _AttendanceSectionState extends State<_AttendanceSection> {
  final DateTime _today = _koreaToday();
  late Future<List<AttendanceDay>> _attendanceFuture;

  @override
  void initState() {
    super.initState();
    _attendanceFuture = _fetchAttendances();
  }

  Future<List<AttendanceDay>> _fetchAttendances() async {
    final memberGymMapId = widget.memberGymMapId;
    if (memberGymMapId == null) {
      return [];
    }

    return MemberApiService().fetchAttendances(
      memberGymMapId: memberGymMapId,
      year: _today.year,
      month: _today.month,
    );
  }

  Future<void> _attendToday() async {
    final memberGymMapId = widget.memberGymMapId;
    if (memberGymMapId == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('도장 정보를 다시 불러와 주세요.')),
      );
      return;
    }

    try {
      await MemberApiService().attendToday(memberGymMapId);
      if (!mounted) {
        return;
      }

      setState(() {
        _attendanceFuture = _fetchAttendances();
      });
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('오늘 출석이 완료되었습니다.')),
      );
    } catch (error) {
      if (!mounted) {
        return;
      }

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(error.toString().replaceFirst('Exception: ', ''))),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    final visibleDayCount = _monthVisibleDayCount(_today);
    final rowCount = (visibleDayCount / 7).ceil();
    const sideWidth = 95.0;
    const gap = 12.0;

    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: AppColors.surface,
        border: Border.all(color: AppColors.border),
        borderRadius: BorderRadius.circular(18),
        boxShadow: const [
          BoxShadow(
            color: Color(0x10000000),
            blurRadius: 12,
            offset: Offset(0, 5),
          ),
        ],
      ),
      child: LayoutBuilder(
        builder: (context, constraints) {
          const maxCalendarWidth = 376.0;
          const maxCellSize = 38.0;
          final availableCalendarWidth = constraints.maxWidth - sideWidth - gap;
          final calendarWidth = availableCalendarWidth > maxCalendarWidth
              ? maxCalendarWidth
              : availableCalendarWidth;
          final calendarInnerWidth = calendarWidth - 16;
          final availableCellSize = (calendarInnerWidth - (4 * 6)) / 7;
          final cellSize = availableCellSize > maxCellSize
              ? maxCellSize
              : availableCellSize;
          final gridHeight = (cellSize * rowCount) + (4 * (rowCount - 1));
          final calendarHeight = 9 + 14 + 6 + 1 + 6 + gridHeight + 9 + 6;

          return SizedBox(
            height: calendarHeight,
            child: FutureBuilder<List<AttendanceDay>>(
              future: _attendanceFuture,
              builder: (context, snapshot) {
                final isLoading =
                    snapshot.connectionState != ConnectionState.done;
                final attendances = snapshot.data ?? [];
                final attendedDates = attendances
                    .map((attendance) => attendance.attendanceDate)
                    .toList();
                final checked = attendedDates.any(
                  (date) => _isSameDate(date, _today),
                );

                return Row(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    SizedBox(
                      width: calendarWidth,
                      child: _MiniAttendanceCalendar(
                        today: _today,
                        attendedDates: attendedDates,
                      ),
                    ),
                    const SizedBox(width: 12),
                    SizedBox(
                      width: sideWidth,
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.stretch,
                        children: [
                          Container(
                            width: double.infinity,
                            padding: const EdgeInsets.symmetric(vertical: 16),
                            decoration: BoxDecoration(
                              color: AppColors.white,
                              border: Border.all(color: AppColors.border),
                              borderRadius: BorderRadius.circular(14),
                            ),
                            child: Text(
                              '${_today.year}년\n${_today.month}월',
                              textAlign: TextAlign.center,
                              style: const TextStyle(
                                color: AppColors.primary,
                                fontSize: 18,
                                fontWeight: FontWeight.w900,
                                height: 1.25,
                              ),
                            ),
                          ),
                          const SizedBox(height: 10),
                          Expanded(
                            child: SizedBox.expand(
                              child: ElevatedButton(
                                onPressed:
                                    isLoading || checked ? null : _attendToday,
                                style: ElevatedButton.styleFrom(
                                  elevation: 0,
                                  padding: EdgeInsets.zero,
                                  minimumSize: Size.zero,
                                  tapTargetSize:
                                      MaterialTapTargetSize.shrinkWrap,
                                  backgroundColor: AppColors.primary,
                                  disabledBackgroundColor: AppColors.softAccent,
                                  foregroundColor: AppColors.white,
                                  disabledForegroundColor: AppColors.primary,
                                  shape: RoundedRectangleBorder(
                                    borderRadius: BorderRadius.circular(14),
                                  ),
                                ),
                                child: isLoading
                                    ? const SizedBox(
                                        width: 22,
                                        height: 22,
                                        child: CircularProgressIndicator(
                                          strokeWidth: 2,
                                        ),
                                      )
                                    : Text(
                                        checked ? '출석\n완료' : '출석\n하기',
                                        textAlign: TextAlign.center,
                                        style: const TextStyle(
                                          fontSize: 18,
                                          fontWeight: FontWeight.w900,
                                          height: 1.28,
                                        ),
                                      ),
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                );
              },
            ),
          );
        },
      ),
    );
  }

  bool _isSameDate(DateTime a, DateTime b) {
    return a.year == b.year && a.month == b.month && a.day == b.day;
  }

  int _monthVisibleDayCount(DateTime base) {
    final firstDay = DateTime(base.year, base.month, 1);
    final lastDay = DateTime(base.year, base.month + 1, 0);
    final firstWeekdayIndex = firstDay.weekday % 7;
    final startDay = firstDay.subtract(Duration(days: firstWeekdayIndex));
    final lastWeekdayIndex = lastDay.weekday % 7;
    final endDay = lastDay.add(Duration(days: 6 - lastWeekdayIndex));

    return endDay.difference(startDay).inDays + 1;
  }
}

DateTime _koreaToday() {
  final koreaNow = DateTime.now().toUtc().add(const Duration(hours: 9));
  return DateTime(koreaNow.year, koreaNow.month, koreaNow.day);
}

class _MiniAttendanceCalendar extends StatelessWidget {
  const _MiniAttendanceCalendar({
    required this.today,
    required this.attendedDates,
  });

  final DateTime today;
  final List<DateTime> attendedDates;

  @override
  Widget build(BuildContext context) {
    const weekLabels = ['일', '월', '화', '수', '목', '금', '토'];
    final days = _monthDays(today);

    return Container(
      padding: const EdgeInsets.fromLTRB(9, 9, 9, 9),
      decoration: BoxDecoration(
        color: AppColors.white,
        border: Border.all(color: AppColors.border),
        borderRadius: BorderRadius.circular(14),
      ),
      child: Column(
        children: [
          Row(
            children: weekLabels
                .map(
                  (label) => Expanded(
                    child: Center(
                      child: Text(
                        label,
                        style: const TextStyle(
                          color: AppColors.muted,
                          fontSize: 12,
                          fontWeight: FontWeight.w800,
                        ),
                      ),
                    ),
                  ),
                )
                .toList(),
          ),
          const SizedBox(height: 6),
          Container(height: 1, color: AppColors.softBorder),
          const SizedBox(height: 6),
          Expanded(
            child: LayoutBuilder(
              builder: (context, constraints) {
                final rowCount = (days.length / 7).ceil();
                const maxCellSize = 38.0;
                final availableCellSize = (constraints.maxWidth - (4 * 6)) / 7;
                final cellSize = availableCellSize > maxCellSize
                    ? maxCellSize
                    : availableCellSize;
                final gridHeight =
                    (cellSize * rowCount) + (4 * (rowCount - 1));

                return SizedBox(
                  height: gridHeight,
                  child: Column(
                    children: List.generate(rowCount, (rowIndex) {
                      return Padding(
                        padding: EdgeInsets.only(
                          bottom: rowIndex == rowCount - 1 ? 0 : 4,
                        ),
                        child: Row(
                          children: List.generate(7, (columnIndex) {
                            final index = (rowIndex * 7) + columnIndex;
                            final day = days[index];
                            final isCurrentMonth = day.month == today.month;
                            final isToday = _isSameDate(day, today);
                            final hasFire = attendedDates.any(
                              (attendanceDate) =>
                                  _isSameDate(attendanceDate, day),
                            );

                            return Expanded(
                              child: Center(
                                child: SizedBox(
                                  width: cellSize,
                                  height: cellSize,
                                  child: _CalendarDayCell(
                                    day: day.day,
                                    isCurrentMonth: isCurrentMonth,
                                    isToday: isToday,
                                    hasFire: hasFire,
                                  ),
                                ),
                              ),
                            );
                          }),
                        ),
                      );
                    }),
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }

  List<DateTime> _monthDays(DateTime base) {
    final firstDay = DateTime(base.year, base.month, 1);
    final lastDay = DateTime(base.year, base.month + 1, 0);
    final firstWeekdayIndex = firstDay.weekday % 7;
    final startDay = firstDay.subtract(Duration(days: firstWeekdayIndex));
    final lastWeekdayIndex = lastDay.weekday % 7;
    final endDay = lastDay.add(Duration(days: 6 - lastWeekdayIndex));
    final dayCount = endDay.difference(startDay).inDays + 1;

    return List.generate(
      dayCount,
      (index) => startDay.add(Duration(days: index)),
    );
  }

  bool _isSameDate(DateTime a, DateTime b) {
    return a.year == b.year && a.month == b.month && a.day == b.day;
  }
}

class _CalendarDayCell extends StatelessWidget {
  const _CalendarDayCell({
    required this.day,
    required this.isCurrentMonth,
    required this.isToday,
    required this.hasFire,
  });

  final int day;
  final bool isCurrentMonth;
  final bool isToday;
  final bool hasFire;

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: hasFire ? const Color(0xFFFFF1C2) : Colors.transparent,
        border:
            isToday ? Border.all(color: AppColors.secondary, width: 1.5) : null,
        borderRadius: BorderRadius.circular(10),
      ),
      child: Center(
        child: hasFire
            ? SizedBox(
                width: double.infinity,
                height: double.infinity,
                child: Center(
                  child: SizedBox.square(
                    dimension: 30,
                    child: FittedBox(
                      fit: BoxFit.contain,
                      child: Icon(
                        Icons.local_fire_department_rounded,
                        color: Color(0xFFFF8A00),
                        size: 24,
                      ),
                    ),
                  ),
                ),
              )
            : Text(
                '$day',
                style: TextStyle(
                  color: isCurrentMonth
                      ? (isToday ? AppColors.primary : AppColors.muted)
                      : AppColors.softBorder,
                  fontSize: 11,
                  fontWeight: isToday ? FontWeight.w900 : FontWeight.w700,
                ),
              ),
      ),
    );
  }
}

class _BottomNavBar extends StatelessWidget {
  const _BottomNavBar({
    required this.memberGymMapId,
    required this.gymName,
  });

  final int? memberGymMapId;
  final String gymName;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.fromLTRB(10, 10, 10, 18),
      decoration: const BoxDecoration(
        color: AppColors.surface,
        border: Border(top: BorderSide(color: AppColors.border)),
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceAround,
        children: [
          const _NavItem(icon: Icons.home_rounded, label: '홈', active: true),
          _NavItem(
            icon: Icons.track_changes_rounded,
            label: '목표',
            onTap: () {
              Navigator.of(context).push(
                MaterialPageRoute(
                  builder: (context) => GoalScreen(
                    memberGymMapId: memberGymMapId,
                    gymName: gymName,
                  ),
                ),
              );
            },
          ),
          const _NavItem(icon: Icons.list_alt_rounded, label: '일퀘'),
          const _NavItem(icon: Icons.shopping_bag_outlined, label: '상점'),
          const _NavItem(icon: Icons.emoji_events_outlined, label: '랭킹'),
        ],
      ),
    );
  }
}

class _NavItem extends StatelessWidget {
  const _NavItem({
    required this.icon,
    required this.label,
    this.active = false,
    this.onTap,
  });

  final IconData icon;
  final String label;
  final bool active;
  final VoidCallback? onTap;

  @override
  Widget build(BuildContext context) {
    final color = active ? AppColors.primary : AppColors.muted;

    return InkWell(
      onTap: onTap,
      borderRadius: BorderRadius.circular(10),
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(icon, color: color, size: 25),
            const SizedBox(height: 3),
            Text(
              label,
              style: TextStyle(
                color: color,
                fontSize: 12,
                fontWeight: active ? FontWeight.w800 : FontWeight.w600,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
