import 'package:flutter/material.dart';

import '../services/member_api_service.dart';
import '../theme/app_colors.dart';
import '../theme/app_text_styles.dart';

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

  void _refreshGoals() {
    setState(() {
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
            _GoalHeader(gymName: widget.gymName),
            Expanded(
              child: Padding(
                padding: const EdgeInsets.fromLTRB(14, 8, 14, 14),
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
                      onSelected: (status) {
                        setState(() {
                          _selectedStatus = status;
                        });
                      },
                    ),
                    const SizedBox(height: 12),
                    Expanded(
                      child: _GoalListSection(
                        memberGymMapId: widget.memberGymMapId,
                        goalsFuture: _goalsFuture ??= _createGoalsFuture(),
                        category: _selectedCategory,
                        status: _selectedStatus,
                        onApplied: _refreshGoals,
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
  const _GoalHeader({required this.gymName});

  final String gymName;

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 48,
      padding: const EdgeInsets.symmetric(horizontal: 12),
      color: AppColors.background,
      child: Row(
        children: [
          SizedBox(
            width: 36,
            height: 36,
            child: IconButton(
              onPressed: () => Navigator.of(context).pop(),
              padding: EdgeInsets.zero,
              icon: const Icon(
                Icons.arrow_back_rounded,
                size: 22,
                color: AppColors.primary,
              ),
            ),
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
          const Icon(
            Icons.notifications_rounded,
            color: Color(0xFFF4B63E),
            size: 22,
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
      child: FutureBuilder<List<GoalLevel>>(
        future: goalLevelsFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState != ConnectionState.done) {
            return const SizedBox(
              height: 110,
              child: Center(child: CircularProgressIndicator()),
            );
          }

          if (snapshot.hasError) {
            return const SizedBox(
              height: 110,
              child: Center(child: Text('성장 정보를 불러오지 못했습니다.')),
            );
          }

          return _GoalCategoryGrid(
            levels: snapshot.data ?? _defaultGoalLevels,
            selectedCategory: selectedCategory,
            onSelected: onSelected,
          );
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
                  height: 45,
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
      color: selected ? AppColors.primary : AppColors.white,
      borderRadius: BorderRadius.circular(12),
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(12),
        child: Container(
          padding: const EdgeInsets.symmetric(horizontal: 9),
          decoration: BoxDecoration(
            border: Border.all(
              color: selected ? AppColors.primary : AppColors.border,
            ),
            borderRadius: BorderRadius.circular(12),
          ),
          child: Row(
            children: [
              Expanded(
                child: Text(
                  compactCategory,
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                  style: TextStyle(
                    color: selected ? AppColors.white : AppColors.primary,
                    fontSize: 13,
                    fontWeight: FontWeight.w900,
                  ),
                ),
              ),
              Container(
                width: 1,
                height: 20,
                color: selected ? AppColors.white : AppColors.border,
              ),
              const SizedBox(width: 8),
              SizedBox(
                width: 40,
                child: Text(
                  'Lv. ${level.level}',
                  maxLines: 1,
                  textAlign: TextAlign.right,
                  style: TextStyle(
                    color: selected ? AppColors.white : AppColors.text,
                    fontSize: 13,
                    fontWeight: FontWeight.w900,
                  ),
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
    required this.onApplied,
  });

  final int? memberGymMapId;
  final Future<List<GoalItem>> goalsFuture;
  final String category;
  final String status;
  final VoidCallback onApplied;

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
            .toList();
        if (goals.isEmpty) {
          return Container(
            width: double.infinity,
            height: double.infinity,
            decoration: BoxDecoration(
              color: AppColors.surface,
              border: Border.all(color: AppColors.border),
              borderRadius: BorderRadius.circular(18),
            ),
            child: const Center(child: Text('등록된 목표가 없습니다.')),
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
                    goal: goals[index],
                    memberGymMapId: memberGymMapId,
                    onApplied: onApplied,
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
        color: AppColors.surface,
        border: Border.all(color: AppColors.border),
        borderRadius: BorderRadius.circular(14),
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
                    borderRadius: BorderRadius.circular(11),
                  ),
                ),
                child: Text(
                  item.label,
                  style: const TextStyle(
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

class _GoalListTile extends StatelessWidget {
  const _GoalListTile({
    required this.goal,
    required this.memberGymMapId,
    required this.onApplied,
  });

  final GoalItem goal;
  final int? memberGymMapId;
  final VoidCallback onApplied;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      margin: const EdgeInsets.only(bottom: 10),
      padding: const EdgeInsets.all(15),
      decoration: BoxDecoration(
        color: AppColors.surface,
        border: Border.all(color: AppColors.border),
        borderRadius: BorderRadius.circular(16),
        boxShadow: const [
          BoxShadow(
            color: Color(0x0D000000),
            blurRadius: 10,
            offset: Offset(0, 4),
          ),
        ],
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
                padding:
                    const EdgeInsets.symmetric(horizontal: 10, vertical: 8),
                decoration: BoxDecoration(
                  color: AppColors.softAccent,
                  borderRadius: BorderRadius.circular(999),
                ),
                child: Text(
                  '${goal.point}P',
                  style: const TextStyle(
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
                    onPressed: memberGymMapId == null
                        ? null
                        : () => _applyGoal(context),
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
                      style: const TextStyle(
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
    );
  }

  String _formatDate(DateTime date) {
    return '${date.year}.${_twoDigits(date.month)}.${_twoDigits(date.day)}';
  }

  String _twoDigits(int value) {
    return value.toString().padLeft(2, '0');
  }

  Future<void> _applyGoal(BuildContext context) async {
    final id = memberGymMapId;
    if (id == null) {
      return;
    }

    try {
      await MemberApiService().applyGoal(
        memberGymMapId: id,
        goalId: goal.goalId,
      );
      onApplied();
      if (!context.mounted) {
        return;
      }

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('${goal.name} 목표를 신청했습니다.')),
      );
    } catch (error) {
      if (!context.mounted) {
        return;
      }

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(error.toString().replaceFirst('Exception: ', ''))),
      );
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
      padding: const EdgeInsets.fromLTRB(10, 10, 10, 18),
      decoration: const BoxDecoration(
        color: AppColors.surface,
        border: Border(top: BorderSide(color: AppColors.border)),
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
