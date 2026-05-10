import 'package:flutter/material.dart';

import '../services/member_api_service.dart';
import '../theme/app_colors.dart';
import '../theme/app_text_styles.dart';
import 'gym_selection_screen.dart';
import 'training_home_screen.dart';

class GymMenuScreen extends StatelessWidget {
  const GymMenuScreen({
    required this.memberId,
    required this.memberName,
    super.key,
  });

  final int memberId;
  final String memberName;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.fromLTRB(22, 8, 22, 28),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text('$memberName님', style: AppTextStyles.screenTitle),
              const SizedBox(height: 8),
              const Text('오늘의 수련을 시작해볼까요?', style: AppTextStyles.subtitle),
              const SizedBox(height: 28),
              _TodayGymCard(onPressed: () => _showMyGymModal(context)),
              const SizedBox(height: 18),
              _PrimaryMenuButton(
                label: '도장 들어가기',
                onPressed: () => _showMyGymModal(context),
              ),
              const SizedBox(height: 12),
              _SecondaryMenuButton(
                label: '도장에 참여하기',
                onPressed: () => _showGymJoinModal(context),
              ),
              const SizedBox(height: 8),
              Center(
                child: TextButton(
                  onPressed: () {
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text('준비 중입니다.')),
                    );
                  },
                  child: const Text('나의 도장 만들기'),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  void _showMyGymModal(BuildContext context) {
    showDialog<void>(
      context: context,
      barrierColor: AppColors.primary.withValues(alpha: 0.38),
      builder: (context) => _MyGymDialog(
        memberId: memberId,
        memberName: memberName,
      ),
    );
  }

  void _showGymJoinModal(BuildContext context) {
    showDialog<void>(
      context: context,
      barrierColor: AppColors.primary.withValues(alpha: 0.38),
      builder: (context) => _GymJoinDialog(memberId: memberId),
    );
  }
}

class _TodayGymCard extends StatelessWidget {
  const _TodayGymCard({required this.onPressed});

  final VoidCallback onPressed;

  @override
  Widget build(BuildContext context) {
    return Material(
      color: AppColors.surface,
      borderRadius: BorderRadius.circular(18),
      child: InkWell(
        onTap: onPressed,
        borderRadius: BorderRadius.circular(18),
        child: Container(
          width: double.infinity,
          padding: const EdgeInsets.all(20),
          decoration: BoxDecoration(
            border: Border.all(color: AppColors.border),
            borderRadius: BorderRadius.circular(18),
          ),
          child: Row(
            children: [
              Container(
                width: 58,
                height: 58,
                decoration: BoxDecoration(
                  color: AppColors.softAccent,
                  borderRadius: BorderRadius.circular(18),
                ),
                child: Padding(
                  padding: const EdgeInsets.all(12),
                  child: Image.asset('assets/images/footprints.png'),
                ),
              ),
              const SizedBox(width: 16),
              const Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text('오늘의 도장', style: AppTextStyles.cardTitle),
                    SizedBox(height: 6),
                    Text(
                      '소속 도장을 선택하고 수련을 시작하세요.',
                      style: AppTextStyles.body,
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

class _PrimaryMenuButton extends StatelessWidget {
  const _PrimaryMenuButton({
    required this.label,
    required this.onPressed,
  });

  final String label;
  final VoidCallback onPressed;

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: double.infinity,
      height: 62,
      child: ElevatedButton(
        onPressed: onPressed,
        style: ElevatedButton.styleFrom(
          elevation: 0,
          backgroundColor: AppColors.primary,
          foregroundColor: AppColors.white,
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
        ),
        child: Text(label, style: AppTextStyles.button),
      ),
    );
  }
}

class _SecondaryMenuButton extends StatelessWidget {
  const _SecondaryMenuButton({
    required this.label,
    required this.onPressed,
  });

  final String label;
  final VoidCallback onPressed;

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: double.infinity,
      height: 58,
      child: OutlinedButton(
        onPressed: onPressed,
        style: OutlinedButton.styleFrom(
          foregroundColor: AppColors.primary,
          backgroundColor: AppColors.surface,
          side: const BorderSide(color: AppColors.border),
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
        ),
        child: Text(label, style: AppTextStyles.button),
      ),
    );
  }
}

class _MyGymDialog extends StatelessWidget {
  const _MyGymDialog({
    required this.memberId,
    required this.memberName,
  });

  final int memberId;
  final String memberName;

  @override
  Widget build(BuildContext context) {
    return Dialog(
      backgroundColor: AppColors.surface,
      insetPadding: const EdgeInsets.symmetric(horizontal: 22),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(18)),
      child: SizedBox(
        height: 520,
        child: Column(
          children: [
            const _DialogHeader(title: '내 도장'),
            Expanded(
              child: FutureBuilder<List<MyGym>>(
                future: MemberApiService().fetchMyGyms(memberId),
                builder: (context, snapshot) {
                  if (snapshot.connectionState != ConnectionState.done) {
                    return const Center(child: CircularProgressIndicator());
                  }

                  if (snapshot.hasError) {
                    return const Center(child: Text('도장 목록을 불러오지 못했습니다.'));
                  }

                  final gyms = snapshot.data ?? [];
                  if (gyms.isEmpty) {
                    return const Center(child: Text('소속된 도장이 없습니다.'));
                  }

                  return ListView.separated(
                    padding: const EdgeInsets.all(16),
                    itemCount: gyms.length,
                    separatorBuilder: (context, index) =>
                        const SizedBox(height: 10),
                    itemBuilder: (context, index) {
                      final gym = gyms[index];
                      return _MyGymTile(
                        gym: gym,
                        onEnter: () {
                          Navigator.of(context).pop();
                          Navigator.of(context).push(
                            MaterialPageRoute(
                              builder: (context) => TrainingHomeScreen(
                                memberGymMapId: gym.memberGymMapId,
                                memberName: memberName,
                                gymName: gym.gymName,
                                beltName: gym.beltName,
                                point: gym.point,
                                memberAge: gym.memberAge,
                                memberPhoneNumber: gym.memberPhoneNumber,
                                joinedDate: gym.joinedDate,
                              ),
                            ),
                          );
                        },
                      );
                    },
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _MyGymTile extends StatelessWidget {
  const _MyGymTile({
    required this.gym,
    required this.onEnter,
  });

  final MyGym gym;
  final VoidCallback onEnter;

  @override
  Widget build(BuildContext context) {
    final memberRoleLabel = switch (gym.memberRole.toLowerCase()) {
      'master' => '관장',
      'teacher' => '사범',
      _ => '관원',
    };
    final memberStatusLabel = switch (gym.memberStatus.toLowerCase()) {
      'active' => '서비스 중',
      'stop' => '중지',
      _ => '대기',
    };
    final beltLabel = _beltLabel(gym.beltName);
    final address = [
      gym.addressRoad,
      gym.addressDetail,
    ].whereType<String>().where((text) => text.isNotEmpty).join(' ');

    return Container(
      padding: const EdgeInsets.all(15),
      decoration: BoxDecoration(
        color: AppColors.white,
        border: Border.all(color: AppColors.border),
        borderRadius: BorderRadius.circular(12),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(gym.gymName, style: AppTextStyles.cardTitle),
          if (address.isNotEmpty) ...[
            const SizedBox(height: 7),
            Text(address, style: AppTextStyles.body),
          ],
          const SizedBox(height: 10),
          Text(
            '권한 $memberRoleLabel · 상태 $memberStatusLabel',
            style: const TextStyle(color: AppColors.muted, fontSize: 13),
          ),
          const SizedBox(height: 8),
          Row(
            children: [
              Expanded(
                child: Text(
                  '$beltLabel · ${gym.point}P',
                  style: const TextStyle(
                    color: AppColors.primary,
                    fontSize: 14,
                    fontWeight: FontWeight.w800,
                  ),
                ),
              ),
              TextButton(onPressed: onEnter, child: const Text('들어가기')),
            ],
          ),
        ],
      ),
    );
  }

  String _beltLabel(String beltName) {
    return switch (beltName.toLowerCase()) {
      'white' => '흰띠',
      'yellow' => '노란띠',
      'blue' => '파란띠',
      'red' => '빨간띠',
      'black' => '검은띠',
      _ => beltName,
    };
  }
}

class _GymJoinDialog extends StatelessWidget {
  const _GymJoinDialog({required this.memberId});

  final int memberId;

  Future<void> _joinGym(BuildContext context, GymListItem gym) async {
    try {
      await MemberApiService().joinGym(
        memberId: memberId,
        gymId: gym.gymId,
      );

      if (!context.mounted) {
        return;
      }

      Navigator.of(context).pop();
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('${gym.gymName} 참여 신청이 완료되었습니다.')),
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

  @override
  Widget build(BuildContext context) {
    return Dialog(
      backgroundColor: AppColors.surface,
      insetPadding: const EdgeInsets.symmetric(horizontal: 18),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(18)),
      child: SizedBox(
        height: 620,
        child: Column(
          children: [
            const _DialogHeader(title: '도장에 참여하기'),
            Expanded(
              child: GymSelectionView(
                onSelectGym: (gym) => _joinGym(context, gym),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _DialogHeader extends StatelessWidget {
  const _DialogHeader({required this.title});

  final String title;

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 58,
      padding: const EdgeInsets.symmetric(horizontal: 16),
      decoration: const BoxDecoration(
        border: Border(bottom: BorderSide(color: AppColors.border)),
      ),
      child: Row(
        children: [
          Expanded(child: Text(title, style: AppTextStyles.cardTitle)),
          IconButton(
            onPressed: () => Navigator.of(context).pop(),
            icon: const Icon(Icons.close_rounded),
            color: AppColors.primary,
          ),
        ],
      ),
    );
  }
}
