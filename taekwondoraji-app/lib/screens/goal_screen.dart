import 'package:flutter/material.dart';

import '../services/member_api_service.dart';
import '../theme/app_colors.dart';
import '../theme/app_text_styles.dart';
import 'daily_quest_screen.dart';
import 'store_screen.dart';

class GoalScreen extends StatefulWidget {
  const GoalScreen({
    required this.memberGymMapId,
    required this.gymName,
    super.key,
  });

  final int? memberGymMapId;
  final String gymName;

  @override
  State<GoalScreen> createState() => _GoalScreenState();
}

class _GoalScreenState extends State<GoalScreen> {
  String _selectedCategory = '기초체력';
  String _selectedStatus = 'waiting';
  final Set<String> _hiddenGoalKeys = {};
  Future<List<GoalLevel>>? _goalLevelsFuture;
  Future<List<GoalItem>>? _goalsFuture;

  @override
  void initState() {
    super.initState();
    _goalLevelsFuture = _createGoalLevelsFuture();
    _goalsFuture = _createGoalsFuture();
  }

  Future<List<GoalLevel>> _createGoalLevelsFuture() {
    final memberGymMapId = widget.memberGymMapId;
    return memberGymMapId == null
        ? Future.value(_defaultGoalLevels)
        : MemberApiService().fetchGoalLevels(memberGymMapId);
  }

  Future<List<GoalItem>> _createGoalsFuture() {
    final memberGymMapId = widget.memberGymMapId;
    return memberGymMapId == null
        ? MemberApiService().fetchGoals()
        : MemberApiService().fetchMemberGoals(memberGymMapId);
  }

  void _hideGoal(GoalItem goal) {
    if (!mounted) {
      return;
    }

    setState(() {
      _hiddenGoalKeys.add(_goalVisibilityKey(goal));
    });
  }

  void _showGoal(GoalItem goal) {
    if (!mounted) {
      return;
    }

    setState(() {
      _hiddenGoalKeys.remove(_goalVisibilityKey(goal));
    });
  }

  void _changeStatus(String status) {
    setState(() {
      _selectedStatus = status;
      _hiddenGoalKeys.clear();
      _goalsFuture = _createGoalsFuture();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      body: SafeArea(
        child: Column(
          children: [
            const _GoalHeader(),
            Expanded(
              child: Padding(
                padding: const EdgeInsets.fromLTRB(24, 8, 24, 14),
                child: Column(
                  children: [
                    _GoalCategorySection(
                      goalLevelsFuture:
                          _goalLevelsFuture ??= _createGoalLevelsFuture(),
                      selectedCategory: _selectedCategory,
                      onSelected: (category) {
                        setState(() {
                          _selectedCategory = category;
                        });
                      },
                    ),
                    const SizedBox(height: 16),
                    _GoalStatusTabs(
                      selectedStatus: _selectedStatus,
                      onSelected: _changeStatus,
                    ),
                    const SizedBox(height: 12),
                    Expanded(
                      child: _GoalListSection(
                        memberGymMapId: widget.memberGymMapId,
                        goalsFuture: _goalsFuture ??= _createGoalsFuture(),
                        category: _selectedCategory,
                        status: _selectedStatus,
                        hiddenGoalKeys: _hiddenGoalKeys,
                        onDismissed: _hideGoal,
                        onDismissFailed: _showGoal,
                      ),
                    ),
                  ],
                ),
              ),
            ),
            _GoalBottomNav(
              memberGymMapId: widget.memberGymMapId,
              gymName: widget.gymName,
            ),
          ],
        ),
      ),
    );
  }
}

class _GoalHeader extends StatelessWidget {
  const _GoalHeader();

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.fromLTRB(24, 22, 24, 10),
      color: AppColors.background,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            '목표',
            style: AppTextStyles.screenTitle.copyWith(fontSize: 42),
          ),
        ],
      ),
    );
  }
}

class _GoalCategorySection extends StatelessWidget {
  const _GoalCategorySection({
    required this.goalLevelsFuture,
    required this.selectedCategory,
    required this.onSelected,
  });

  final Future<List<GoalLevel>> goalLevelsFuture;
  final String selectedCategory;
  final ValueChanged<String> onSelected;

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<List<GoalLevel>>(
      future: goalLevelsFuture,
      builder: (context, snapshot) {
        if (snapshot.connectionState != ConnectionState.done) {
          return const SizedBox(
            height: 70,
            child: Center(child: CircularProgressIndicator()),
          );
        }

        if (snapshot.hasError) {
          return const SizedBox(
            height: 70,
            child: Center(child: Text('성장 정보를 불러오지 못했습니다.')),
          );
        }

        return _GoalCategoryGrid(
          levels: snapshot.data ?? _defaultGoalLevels,
          selectedCategory: selectedCategory,
          onSelected: onSelected,
        );
      },
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

class _GoalCategoryGrid extends StatelessWidget {
  const _GoalCategoryGrid({
    required this.levels,
    required this.selectedCategory,
    required this.onSelected,
  });

  final List<GoalLevel> levels;
  final String selectedCategory;
  final ValueChanged<String> onSelected;

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
                  height: 48,
                  child: _GoalCategoryCard(
                    level: level,
                    selected: selectedCategory == level.category,
                    onTap: () => onSelected(level.category),
                  ),
                ),
              )
              .toList(),
        );
      },
    );
  }
}

class _GoalCategoryCard extends StatelessWidget {
  const _GoalCategoryCard({
    required this.level,
    required this.selected,
    required this.onTap,
  });

  final GoalLevel level;
  final bool selected;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    final compactCategory =
        level.category == '기초체력' ? '기초 체력' : level.category;

    return Material(
      color: selected ? AppColors.primary : AppColors.softAccent,
      borderRadius: BorderRadius.circular(12),
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(12),
        child: Container(
          padding: const EdgeInsets.symmetric(horizontal: 8),
          decoration: BoxDecoration(borderRadius: BorderRadius.circular(12)),
          child: Row(
            children: [
              Expanded(
                child: Align(
                  alignment: Alignment.centerLeft,
                  child: FittedBox(
                    fit: BoxFit.scaleDown,
                    alignment: Alignment.centerLeft,
                    child: Text(
                      compactCategory,
                      maxLines: 1,
                      style: TextStyle(
                        color: selected ? AppColors.white : AppColors.primary,
                        fontSize: 13,
                        fontWeight: FontWeight.w900,
                      ),
                    ),
                  ),
                ),
              ),
              const SizedBox(width: 5),
              SizedBox(
                width: 42,
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.end,
                  children: [
                    Text(
                      'Lv. ${level.level}',
                      maxLines: 1,
                      textAlign: TextAlign.right,
                      style: TextStyle(
                        color: selected ? AppColors.white : AppColors.text,
                        fontSize: 13,
                        fontWeight: FontWeight.w900,
                      ),
                    ),
                    const SizedBox(height: 2),
                    Text(
                      '${level.point}P',
                      maxLines: 1,
                      textAlign: TextAlign.right,
                      style: TextStyle(
                        color: selected ? AppColors.white : AppColors.muted,
                        fontSize: 11,
                        fontWeight: FontWeight.w800,
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _GoalListSection extends StatelessWidget {
  const _GoalListSection({
    required this.memberGymMapId,
    required this.goalsFuture,
    required this.category,
    required this.status,
    required this.hiddenGoalKeys,
    required this.onDismissed,
    required this.onDismissFailed,
  });

  final int? memberGymMapId;
  final Future<List<GoalItem>> goalsFuture;
  final String category;
  final String status;
  final Set<String> hiddenGoalKeys;
  final ValueChanged<GoalItem> onDismissed;
  final ValueChanged<GoalItem> onDismissFailed;

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<List<GoalItem>>(
      future: goalsFuture,
      builder: (context, snapshot) {
        if (snapshot.connectionState != ConnectionState.done) {
          return const SizedBox(
            height: 220,
            child: Center(child: CircularProgressIndicator()),
          );
        }

        if (snapshot.hasError) {
          return const SizedBox(
            height: 220,
            child: Center(child: Text('목표 목록을 불러오지 못했습니다.')),
          );
        }

        final goals = (snapshot.data ?? [])
            .where((goal) => goal.category == category)
            .where((goal) => goal.goalStatus == status)
            .where((goal) => !hiddenGoalKeys.contains(_goalVisibilityKey(goal)))
            .toList();
        if (goals.isEmpty) {
          return Container(
            width: double.infinity,
            height: double.infinity,
            decoration: BoxDecoration(
              color: AppColors.softAccent,
              borderRadius: BorderRadius.circular(16),
            ),
            child: Center(child: Text('등록된 목표가 없습니다.')),
          );
        }

        return Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(category, style: AppTextStyles.cardTitle),
            const SizedBox(height: 10),
            Expanded(
              child: ListView.builder(
                itemCount: goals.length,
                itemBuilder: (context, index) {
                  return _GoalListTile(
                    key: ValueKey(
                      '${goals[index].goalStatus}-${goals[index].memberGoalId ?? goals[index].goalId}',
                    ),
                    goal: goals[index],
                    memberGymMapId: memberGymMapId,
                    onDismissed: onDismissed,
                    onDismissFailed: onDismissFailed,
                  );
                },
              ),
            ),
          ],
        );
      },
    );
  }
}

String _goalVisibilityKey(GoalItem goal) {
  return '${goal.goalStatus}-${goal.goalId}';
}

class _GoalStatusTabs extends StatelessWidget {
  const _GoalStatusTabs({
    required this.selectedStatus,
    required this.onSelected,
  });

  final String selectedStatus;
  final ValueChanged<String> onSelected;

  static const _statuses = [
    _GoalStatusTab(status: 'waiting', label: '대기'),
    _GoalStatusTab(status: 'progress', label: '신청'),
    _GoalStatusTab(status: 'complete', label: '완료'),
  ];

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(4),
      decoration: BoxDecoration(
        color: AppColors.softAccent,
        borderRadius: BorderRadius.circular(999),
      ),
      child: Row(
        children: _statuses.map((item) {
          final selected = selectedStatus == item.status;
          return Expanded(
            child: SizedBox(
              height: 38,
              child: TextButton(
                onPressed: () => onSelected(item.status),
                style: TextButton.styleFrom(
                  foregroundColor:
                      selected ? AppColors.white : AppColors.primary,
                  backgroundColor:
                      selected ? AppColors.primary : Colors.transparent,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(999),
                  ),
                ),
                child: Text(
                  item.label,
                  style: TextStyle(
                    fontSize: 14,
                    fontWeight: FontWeight.w900,
                  ),
                ),
              ),
            ),
          );
        }).toList(),
      ),
    );
  }
}

class _GoalStatusTab {
  const _GoalStatusTab({
    required this.status,
    required this.label,
  });

  final String status;
  final String label;
}

class _GoalListTile extends StatefulWidget {
  const _GoalListTile({
    required this.goal,
    required this.memberGymMapId,
    required this.onDismissed,
    required this.onDismissFailed,
    super.key,
  });

  final GoalItem goal;
  final int? memberGymMapId;
  final ValueChanged<GoalItem> onDismissed;
  final ValueChanged<GoalItem> onDismissFailed;

  @override
  State<_GoalListTile> createState() => _GoalListTileState();
}

class _GoalListTileState extends State<_GoalListTile> {
  static const _leaveDuration = Duration(milliseconds: 280);

  bool _isLeaving = false;
  Offset _leaveOffset = const Offset(1.15, 0);

  GoalItem get goal => widget.goal;
  int? get memberGymMapId => widget.memberGymMapId;
  ValueChanged<GoalItem> get onDismissed => widget.onDismissed;
  ValueChanged<GoalItem> get onDismissFailed => widget.onDismissFailed;

  @override
  Widget build(BuildContext context) {
    return TweenAnimationBuilder<double>(
      tween: Tween<double>(begin: 1, end: _isLeaving ? 0 : 1),
      duration: _leaveDuration,
      curve: Curves.easeInOutCubic,
      builder: (context, heightFactor, child) {
        return ClipRect(
          child: Align(
            heightFactor: heightFactor,
            alignment: Alignment.topCenter,
            child: child,
          ),
        );
      },
      child: AnimatedSlide(
        offset: _isLeaving ? _leaveOffset : Offset.zero,
        duration: _leaveDuration,
        curve: Curves.easeInCubic,
        child: AnimatedOpacity(
          opacity: _isLeaving ? 0 : 1,
          duration: _leaveDuration,
          curve: Curves.easeOut,
          child: Container(
            width: double.infinity,
            margin: const EdgeInsets.only(bottom: 12),
            padding: const EdgeInsets.all(15),
            decoration: BoxDecoration(
              color: AppColors.softAccent,
              borderRadius: BorderRadius.circular(14),
            ),
            child: Row(
              children: [
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(goal.name, style: AppTextStyles.cardTitle),
                      if (goal.description != null &&
                          goal.description!.trim().isNotEmpty) ...[
                        const SizedBox(height: 6),
                        Text(goal.description!, style: AppTextStyles.body),
                      ],
                      if (goal.goalStatus == 'complete' &&
                          goal.completedAt != null) ...[
                        const SizedBox(height: 6),
                        Text(
                          '완료일 ${_formatDate(goal.completedAt!)}',
                          style: AppTextStyles.body.copyWith(
                            color: AppColors.muted,
                            fontWeight: FontWeight.w700,
                          ),
                        ),
                      ],
                    ],
                  ),
                ),
                const SizedBox(width: 10),
                Column(
                  crossAxisAlignment: CrossAxisAlignment.end,
                  children: [
                    Container(
                      padding: const EdgeInsets.symmetric(
                        horizontal: 10,
                        vertical: 8,
                      ),
                      decoration: BoxDecoration(
                        color: AppColors.softAccent,
                        borderRadius: BorderRadius.circular(999),
                      ),
                      child: Text(
                        '${goal.point}P',
                        style: TextStyle(
                          color: AppColors.primary,
                          fontSize: 13,
                          fontWeight: FontWeight.w900,
                        ),
                      ),
                    ),
                    if (goal.goalStatus == 'waiting') ...[
                      const SizedBox(height: 8),
                      SizedBox(
                        height: 34,
                        child: ElevatedButton(
                          onPressed:
                              memberGymMapId == null || _isLeaving
                              ? null
                              : _applyGoal,
                          style: ElevatedButton.styleFrom(
                            elevation: 0,
                            backgroundColor: AppColors.primary,
                            foregroundColor: AppColors.white,
                            padding: const EdgeInsets.symmetric(horizontal: 13),
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(11),
                            ),
                          ),
                          child: Text(
                            goal.reapplyAvailable ? '재신청' : '신청',
                            style: TextStyle(
                              fontSize: 13,
                              fontWeight: FontWeight.w900,
                            ),
                          ),
                        ),
                      ),
                    ],
                    if (goal.goalStatus == 'progress') ...[
                      const SizedBox(height: 8),
                      SizedBox(
                        height: 34,
                        child: OutlinedButton(
                          onPressed:
                              memberGymMapId == null ||
                                  goal.memberGoalId == null ||
                                  _isLeaving
                              ? null
                              : _cancelGoal,
                          style: OutlinedButton.styleFrom(
                            foregroundColor: AppColors.primary,
                            side: BorderSide(color: AppColors.border),
                            padding: const EdgeInsets.symmetric(horizontal: 12),
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(11),
                            ),
                          ),
                          child: Text(
                            '신청 취소',
                            style: TextStyle(
                              fontSize: 13,
                              fontWeight: FontWeight.w900,
                            ),
                          ),
                        ),
                      ),
                    ],
                  ],
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  String _formatDate(DateTime date) {
    return '${date.year}.${_twoDigits(date.month)}.${_twoDigits(date.day)}';
  }

  String _twoDigits(int value) {
    return value.toString().padLeft(2, '0');
  }

  Future<void> _applyGoal() async {
    final id = memberGymMapId;
    if (id == null || _isLeaving) {
      return;
    }

    await _dismissWithRequest(
      offset: const Offset(1.15, 0),
      request: MemberApiService()
          .applyGoal(memberGymMapId: id, goalId: goal.goalId)
          .then<void>((_) {}),
    );
  }

  Future<void> _cancelGoal() async {
    final id = memberGymMapId;
    final memberGoalId = goal.memberGoalId;
    if (id == null || memberGoalId == null || _isLeaving) {
      return;
    }

    await _dismissWithRequest(
      offset: const Offset(-1.15, 0),
      request: MemberApiService().deleteGoalApplication(
        memberGymMapId: id,
        memberGoalId: memberGoalId,
      ),
    );
  }

  Future<void> _dismissWithRequest({
    required Offset offset,
    required Future<void> request,
  }) async {
    setState(() {
      _leaveOffset = offset;
      _isLeaving = true;
    });

    await Future<void>.delayed(_leaveDuration);
    if (mounted) {
      onDismissed(goal);
    }

    try {
      await request;
    } catch (_) {
      onDismissFailed(goal);
    }
  }
}

class _GoalBottomNav extends StatelessWidget {
  const _GoalBottomNav({
    required this.memberGymMapId,
    required this.gymName,
  });

  final int? memberGymMapId;
  final String gymName;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.fromLTRB(18, 8, 18, 22),
      decoration: BoxDecoration(
        color: AppColors.white,
        border: Border(top: BorderSide(color: AppColors.softBorder)),
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceAround,
        children: [
          _NavItem(
            icon: Icons.home_rounded,
            label: '홈',
            onTap: () => Navigator.of(context).pop(),
          ),
          const _NavItem(
            icon: Icons.track_changes_rounded,
            label: '목표',
            active: true,
          ),
          _NavItem(
            icon: Icons.list_alt_rounded,
            label: '일퀘',
            onTap: () {
              Navigator.of(context).pushReplacement(
                MaterialPageRoute(
                  builder: (context) => DailyQuestScreen(
                    memberGymMapId: memberGymMapId,
                    gymName: gymName,
                  ),
                ),
              );
            },
          ),
          _NavItem(
            icon: Icons.shopping_bag_outlined,
            label: '상점',
            onTap: () {
              Navigator.of(context).pushReplacement(
                MaterialPageRoute(
                  builder: (context) => StoreScreen(
                    memberGymMapId: memberGymMapId,
                    gymName: gymName,
                  ),
                ),
              );
            },
          ),
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
    return Semantics(
      label: label,
      selected: active,
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(12),
        child: SizedBox(
          width: 54,
          height: 48,
          child: Center(
            child: AnimatedContainer(
              duration: const Duration(milliseconds: 160),
              width: 42,
              height: 36,
              decoration: BoxDecoration(
                color: active ? AppColors.primary : Colors.transparent,
                borderRadius: BorderRadius.circular(12),
              ),
              child: Icon(
                icon,
                color: active ? AppColors.white : AppColors.primary,
                size: active ? 25 : 28,
              ),
            ),
          ),
        ),
      ),
    );
  }
}
