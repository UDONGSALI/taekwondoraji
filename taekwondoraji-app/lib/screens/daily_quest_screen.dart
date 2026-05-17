import 'package:flutter/material.dart';

import '../services/member_api_service.dart';
import '../theme/app_colors.dart';
import '../theme/app_text_styles.dart';
import 'goal_screen.dart';
import 'store_screen.dart';

class DailyQuestScreen extends StatefulWidget {
  const DailyQuestScreen({
    required this.memberGymMapId,
    required this.gymName,
    super.key,
  });

  final int? memberGymMapId;
  final String gymName;

  @override
  State<DailyQuestScreen> createState() => _DailyQuestScreenState();
}

class _DailyQuestScreenState extends State<DailyQuestScreen> {
  String _selectedStatus = 'waiting';
  final Set<String> _hiddenQuestKeys = {};
  Future<List<DailyQuestItem>>? _questsFuture;

  @override
  void initState() {
    super.initState();
    _questsFuture = _createQuestsFuture();
  }

  Future<List<DailyQuestItem>> _createQuestsFuture() {
    final memberGymMapId = widget.memberGymMapId;
    return memberGymMapId == null
        ? Future.value([])
        : MemberApiService().fetchDailyQuests(memberGymMapId);
  }

  void _hideQuest(DailyQuestItem quest) {
    if (!mounted) {
      return;
    }

    setState(() {
      _hiddenQuestKeys.add(_dailyQuestVisibilityKey(quest));
    });
  }

  void _showQuest(DailyQuestItem quest) {
    if (!mounted) {
      return;
    }

    setState(() {
      _hiddenQuestKeys.remove(_dailyQuestVisibilityKey(quest));
    });
  }

  void _changeStatus(String status) {
    setState(() {
      _selectedStatus = status;
      _hiddenQuestKeys.clear();
      _questsFuture = _createQuestsFuture();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      body: SafeArea(
        child: Column(
          children: [
            const _DailyQuestHeader(),
            Expanded(
              child: Padding(
                padding: const EdgeInsets.fromLTRB(24, 8, 24, 14),
                child: Column(
                  children: [
                    _DailyQuestStatusTabs(
                      selectedStatus: _selectedStatus,
                      onSelected: _changeStatus,
                    ),
                    const SizedBox(height: 12),
                    Expanded(
                      child: _DailyQuestListSection(
                        memberGymMapId: widget.memberGymMapId,
                        questsFuture: _questsFuture ??= _createQuestsFuture(),
                        status: _selectedStatus,
                        hiddenQuestKeys: _hiddenQuestKeys,
                        onDismissed: _hideQuest,
                        onDismissFailed: _showQuest,
                      ),
                    ),
                  ],
                ),
              ),
            ),
            _DailyQuestBottomNav(
              memberGymMapId: widget.memberGymMapId,
              gymName: widget.gymName,
            ),
          ],
        ),
      ),
    );
  }
}

class _DailyQuestHeader extends StatelessWidget {
  const _DailyQuestHeader();

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
            '일퀘',
            style: AppTextStyles.screenTitle.copyWith(fontSize: 42),
          ),
        ],
      ),
    );
  }
}

class _DailyQuestStatusTabs extends StatelessWidget {
  const _DailyQuestStatusTabs({
    required this.selectedStatus,
    required this.onSelected,
  });

  final String selectedStatus;
  final ValueChanged<String> onSelected;

  static const _statuses = [
    _DailyQuestStatusTab(status: 'waiting', label: '대기'),
    _DailyQuestStatusTab(status: 'progress', label: '신청'),
    _DailyQuestStatusTab(status: 'complete', label: '완료'),
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

class _DailyQuestStatusTab {
  const _DailyQuestStatusTab({
    required this.status,
    required this.label,
  });

  final String status;
  final String label;
}

class _DailyQuestListSection extends StatelessWidget {
  const _DailyQuestListSection({
    required this.memberGymMapId,
    required this.questsFuture,
    required this.status,
    required this.hiddenQuestKeys,
    required this.onDismissed,
    required this.onDismissFailed,
  });

  final int? memberGymMapId;
  final Future<List<DailyQuestItem>> questsFuture;
  final String status;
  final Set<String> hiddenQuestKeys;
  final ValueChanged<DailyQuestItem> onDismissed;
  final ValueChanged<DailyQuestItem> onDismissFailed;

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<List<DailyQuestItem>>(
      future: questsFuture,
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
            child: Center(child: Text('일퀘 목록을 불러오지 못했습니다.')),
          );
        }

        final quests = (snapshot.data ?? [])
            .where((quest) => quest.questStatus == status)
            .where(
              (quest) => !hiddenQuestKeys.contains(
                _dailyQuestVisibilityKey(quest),
              ),
            )
            .toList();
        if (quests.isEmpty) {
          return Container(
            width: double.infinity,
            height: double.infinity,
            decoration: BoxDecoration(
              color: AppColors.softAccent,
              borderRadius: BorderRadius.circular(16),
            ),
            child: Center(child: Text('등록된 일퀘가 없습니다.')),
          );
        }

        return ListView.builder(
          itemCount: quests.length,
          itemBuilder: (context, index) {
            return _DailyQuestListTile(
              key: ValueKey(
                '${quests[index].questStatus}-${quests[index].memberDailyQuestId ?? quests[index].dailyQuestId}',
              ),
              quest: quests[index],
              memberGymMapId: memberGymMapId,
              onDismissed: onDismissed,
              onDismissFailed: onDismissFailed,
            );
          },
        );
      },
    );
  }
}

String _dailyQuestVisibilityKey(DailyQuestItem quest) {
  return '${quest.questStatus}-${quest.dailyQuestId}';
}

class _DailyQuestListTile extends StatefulWidget {
  const _DailyQuestListTile({
    required this.quest,
    required this.memberGymMapId,
    required this.onDismissed,
    required this.onDismissFailed,
    super.key,
  });

  final DailyQuestItem quest;
  final int? memberGymMapId;
  final ValueChanged<DailyQuestItem> onDismissed;
  final ValueChanged<DailyQuestItem> onDismissFailed;

  @override
  State<_DailyQuestListTile> createState() => _DailyQuestListTileState();
}

class _DailyQuestListTileState extends State<_DailyQuestListTile> {
  static const _leaveDuration = Duration(milliseconds: 280);

  bool _isLeaving = false;
  Offset _leaveOffset = const Offset(1.15, 0);

  DailyQuestItem get quest => widget.quest;
  int? get memberGymMapId => widget.memberGymMapId;
  ValueChanged<DailyQuestItem> get onDismissed => widget.onDismissed;
  ValueChanged<DailyQuestItem> get onDismissFailed => widget.onDismissFailed;

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
                      Text(quest.name, style: AppTextStyles.cardTitle),
                      const SizedBox(height: 6),
                      Text(
                        _formatDate(quest.questDate),
                        style: AppTextStyles.body.copyWith(
                          color: AppColors.muted,
                          fontWeight: FontWeight.w700,
                        ),
                      ),
                      if (quest.description != null &&
                          quest.description!.trim().isNotEmpty) ...[
                        const SizedBox(height: 6),
                        Text(quest.description!, style: AppTextStyles.body),
                      ],
                      if (quest.questStatus == 'complete' &&
                          quest.completedAt != null) ...[
                        const SizedBox(height: 6),
                        Text(
                          '완료일 ${_formatDate(quest.completedAt!)}',
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
                        '${quest.point}P',
                        style: TextStyle(
                          color: AppColors.primary,
                          fontSize: 13,
                          fontWeight: FontWeight.w900,
                        ),
                      ),
                    ),
                    if (quest.questStatus == 'waiting') ...[
                      const SizedBox(height: 8),
                      SizedBox(
                        height: 34,
                        child: ElevatedButton(
                          onPressed:
                              memberGymMapId == null || _isLeaving
                              ? null
                              : _applyQuest,
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
                            '신청',
                            style: TextStyle(
                              fontSize: 13,
                              fontWeight: FontWeight.w900,
                            ),
                          ),
                        ),
                      ),
                    ],
                    if (quest.questStatus == 'progress') ...[
                      const SizedBox(height: 8),
                      SizedBox(
                        height: 34,
                        child: OutlinedButton(
                          onPressed:
                              memberGymMapId == null ||
                                  quest.memberDailyQuestId == null ||
                                  _isLeaving
                              ? null
                              : _cancelQuest,
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

  Future<void> _applyQuest() async {
    final id = memberGymMapId;
    if (id == null || _isLeaving) {
      return;
    }

    await _dismissWithRequest(
      offset: const Offset(1.15, 0),
      request: MemberApiService()
          .applyDailyQuest(
            memberGymMapId: id,
            dailyQuestId: quest.dailyQuestId,
          )
          .then<void>((_) {}),
    );
  }

  Future<void> _cancelQuest() async {
    final id = memberGymMapId;
    final memberDailyQuestId = quest.memberDailyQuestId;
    if (id == null || memberDailyQuestId == null || _isLeaving) {
      return;
    }

    await _dismissWithRequest(
      offset: const Offset(-1.15, 0),
      request: MemberApiService().deleteDailyQuestApplication(
        memberGymMapId: id,
        memberDailyQuestId: memberDailyQuestId,
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
      onDismissed(quest);
    }

    try {
      await request;
    } catch (_) {
      onDismissFailed(quest);
    }
  }
}

class _DailyQuestBottomNav extends StatelessWidget {
  const _DailyQuestBottomNav({
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
          _NavItem(
            icon: Icons.track_changes_rounded,
            label: '목표',
            onTap: () {
              Navigator.of(context).pushReplacement(
                MaterialPageRoute(
                  builder: (context) => GoalScreen(
                    memberGymMapId: memberGymMapId,
                    gymName: gymName,
                  ),
                ),
              );
            },
          ),
          const _NavItem(
            icon: Icons.list_alt_rounded,
            label: '일퀘',
            active: true,
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
