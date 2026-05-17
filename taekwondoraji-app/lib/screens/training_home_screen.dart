import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';

import '../services/member_api_service.dart';
import '../theme/app_colors.dart';
import '../theme/app_text_styles.dart';
import 'daily_quest_screen.dart';
import 'goal_screen.dart';
import 'settings_screen.dart';
import 'store_screen.dart';

class TrainingHomeScreen extends StatefulWidget {
  const TrainingHomeScreen({
    required this.memberId,
    required this.memberGymMapId,
    required this.memberName,
    required this.gymName,
    required this.beltLabel,
    this.memberAge,
    this.memberPhoneNumber,
    this.memberMotto,
    this.memberProfileImageUrl,
    this.joinedDate,
    super.key,
  });

  final int memberId;
  final int? memberGymMapId;
  final String memberName;
  final String gymName;
  final String beltLabel;
  final int? memberAge;
  final String? memberPhoneNumber;
  final String? memberMotto;
  final String? memberProfileImageUrl;
  final String? joinedDate;

  @override
  State<TrainingHomeScreen> createState() => _TrainingHomeScreenState();
}

class _TrainingHomeScreenState extends State<TrainingHomeScreen> {
  late String _memberName;
  late int? _memberAge;
  late String? _memberPhoneNumber;
  late String? _memberMotto;
  late String? _memberProfileImageUrl;

  @override
  void initState() {
    super.initState();
    _memberName = widget.memberName;
    _memberAge = widget.memberAge;
    _memberPhoneNumber = widget.memberPhoneNumber;
    _memberMotto = widget.memberMotto;
    _memberProfileImageUrl = widget.memberProfileImageUrl;
  }

  Future<void> _openProfileEditModal() async {
    final result = await showDialog<MemberProfile>(
      context: context,
      barrierColor: AppColors.primary.withValues(alpha: 0.38),
      builder: (context) => _MemberProfileEditDialog(
        memberId: widget.memberId,
        memberName: _memberName,
        memberAge: _memberAge,
        memberPhoneNumber: _memberPhoneNumber,
        memberMotto: _memberMotto,
        memberProfileImageUrl: _memberProfileImageUrl,
      ),
    );

    if (result == null || !mounted) {
      return;
    }

    setState(() {
      _memberName = result.memberName;
      _memberAge = result.age;
      _memberPhoneNumber = result.phoneNumber;
      _memberMotto = result.motto;
      _memberProfileImageUrl = result.profileImageUrl;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      body: SafeArea(
        child: Column(
          children: [
            _AppHeader(
              gymName: widget.gymName,
              onEditProfile: _openProfileEditModal,
            ),
            Expanded(
              child: SingleChildScrollView(
                padding: const EdgeInsets.fromLTRB(24, 8, 24, 28),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    _TrainingProfileCard(
                      memberName: _memberName,
                      gymName: widget.gymName,
                      beltLabel: widget.beltLabel,
                      memberAge: _memberAge,
                      memberPhoneNumber: _memberPhoneNumber,
                      memberMotto: _memberMotto,
                      memberProfileImageUrl: _memberProfileImageUrl,
                      joinedDate: widget.joinedDate,
                    ),
                    const SizedBox(height: 16),
                    _GoalLevelSection(memberGymMapId: widget.memberGymMapId),
                    const SizedBox(height: 16),
                    _AttendanceSection(memberGymMapId: widget.memberGymMapId),
                  ],
                ),
              ),
            ),
            _BottomNavBar(
              memberGymMapId: widget.memberGymMapId,
              gymName: widget.gymName,
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
    required this.beltLabel,
    required this.memberAge,
    required this.memberPhoneNumber,
    required this.memberMotto,
    required this.memberProfileImageUrl,
    required this.joinedDate,
  });

  final String memberName;
  final String gymName;
  final String beltLabel;
  final int? memberAge;
  final String? memberPhoneNumber;
  final String? memberMotto;
  final String? memberProfileImageUrl;
  final String? joinedDate;

  @override
  Widget build(BuildContext context) {
    final infoRows = [
      _ProfileInfo(label: '이름', value: memberName),
      _ProfileInfo(label: '나이', value: memberAge == null ? '미등록' : '${memberAge}세'),
      _ProfileInfo(label: '등록일자', value: _emptyToDefault(joinedDate)),
      _ProfileInfo(label: '좌우명', value: _emptyToDefault(memberMotto)),
      _ProfileInfo(label: '띠', value: beltLabel),
    ];

    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(18),
      decoration: BoxDecoration(
        color: AppColors.softAccent,
        borderRadius: BorderRadius.circular(16),
      ),
      child: LayoutBuilder(
        builder: (context, constraints) {
          final photoWidth =
              (constraints.maxWidth * 0.38).clamp(112.0, 162.0).toDouble();

          return Row(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _PhotoPlaceholder(
                width: photoWidth,
                imageUrl: memberProfileImageUrl,
              ),
              const SizedBox(width: 10),
              Expanded(
                child: _InfoList(
                  rows: infoRows,
                ),
              ),
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

}

class _PhotoPlaceholder extends StatelessWidget {
  const _PhotoPlaceholder({
    required this.width,
    required this.imageUrl,
  });

  final double width;
  final String? imageUrl;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: width,
      height: 204,
      decoration: BoxDecoration(
        color: AppColors.white,
        borderRadius: BorderRadius.circular(14),
      ),
      clipBehavior: Clip.antiAlias,
      child: imageUrl == null
          ? Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Icon(
                  Icons.person_rounded,
                  size: 56,
                  color: AppColors.primary,
                ),
                const SizedBox(height: 12),
                Text(
                  '사진 영역',
                  style: TextStyle(
                    color: AppColors.muted,
                    fontSize: 13,
                    fontWeight: FontWeight.w700,
                  ),
                ),
              ],
            )
          : Image.network(
              imageUrl!,
              fit: BoxFit.cover,
              errorBuilder: (context, error, stackTrace) {
                return Icon(
                  Icons.person_rounded,
                  size: 56,
                  color: AppColors.primary,
                );
              },
            ),
    );
  }
}

class _InfoList extends StatelessWidget {
  const _InfoList({
    required this.rows,
  });

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
      padding: const EdgeInsets.symmetric(vertical: 10),
      decoration: BoxDecoration(
        border: showDivider
            ? Border(bottom: BorderSide(color: AppColors.softBorder))
            : null,
      ),
      child: Row(
        children: [
          SizedBox(
            width: 62,
            child: Text(
              label,
              style: TextStyle(
                color: AppColors.muted,
                fontSize: 14,
                fontWeight: FontWeight.w900,
              ),
            ),
          ),
          const SizedBox(width: 12),
          Expanded(
            child: Text(
              value,
              maxLines: 2,
              overflow: TextOverflow.ellipsis,
              style: TextStyle(
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

class _MemberProfileEditDialog extends StatefulWidget {
  const _MemberProfileEditDialog({
    required this.memberId,
    required this.memberName,
    required this.memberAge,
    required this.memberPhoneNumber,
    required this.memberMotto,
    required this.memberProfileImageUrl,
  });

  final int memberId;
  final String memberName;
  final int? memberAge;
  final String? memberPhoneNumber;
  final String? memberMotto;
  final String? memberProfileImageUrl;

  @override
  State<_MemberProfileEditDialog> createState() =>
      _MemberProfileEditDialogState();
}

class _MemberProfileEditDialogState extends State<_MemberProfileEditDialog> {
  final _formKey = GlobalKey<FormState>();
  late final TextEditingController _nameController;
  late final TextEditingController _ageController;
  late final TextEditingController _phoneController;
  late final TextEditingController _mottoController;
  Uint8List? _selectedImageBytes;
  String? _selectedImageName;
  bool _saving = false;

  @override
  void initState() {
    super.initState();
    _nameController = TextEditingController(text: widget.memberName);
    _ageController = TextEditingController(
      text: widget.memberAge == null ? '' : '${widget.memberAge}',
    );
    _phoneController = TextEditingController(
      text: widget.memberPhoneNumber ?? '',
    );
    _mottoController = TextEditingController(text: widget.memberMotto ?? '');
  }

  @override
  void dispose() {
    _nameController.dispose();
    _ageController.dispose();
    _phoneController.dispose();
    _mottoController.dispose();
    super.dispose();
  }

  Future<void> _pickImage() async {
    final image = await ImagePicker().pickImage(
      source: ImageSource.gallery,
      imageQuality: 86,
      maxWidth: 1200,
    );
    if (image == null) {
      return;
    }

    final bytes = await image.readAsBytes();
    if (!mounted) {
      return;
    }

    setState(() {
      _selectedImageBytes = bytes;
      _selectedImageName = image.name;
    });
  }

  Future<void> _save() async {
    if (_saving || !(_formKey.currentState?.validate() ?? false)) {
      return;
    }

    setState(() {
      _saving = true;
    });

    try {
      final api = MemberApiService();
      var profile = await api.updateMemberProfile(
        memberId: widget.memberId,
        memberName: _nameController.text,
        age: _ageController.text,
        phoneNumber: _phoneController.text,
        motto: _mottoController.text,
      );

      final imageBytes = _selectedImageBytes;
      if (imageBytes != null) {
        profile = await api.uploadMemberProfileImage(
          memberId: widget.memberId,
          filename: _selectedImageName ?? '${widget.memberId}.jpg',
          bytes: imageBytes,
        );
      }

      if (!mounted) {
        return;
      }

      Navigator.of(context).pop(profile);
    } catch (error) {
      if (!mounted) {
        return;
      }

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(error.toString().replaceFirst('Exception: ', ''))),
      );
      setState(() {
        _saving = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Dialog(
      backgroundColor: AppColors.surface,
      insetPadding: const EdgeInsets.symmetric(horizontal: 22),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(18)),
      child: SingleChildScrollView(
        padding: const EdgeInsets.fromLTRB(18, 18, 18, 16),
        child: Form(
          key: _formKey,
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  Expanded(
                    child: Text('내 정보 수정', style: AppTextStyles.cardTitle),
                  ),
                  IconButton(
                    onPressed: _saving ? null : () => Navigator.of(context).pop(),
                    icon: Icon(Icons.close_rounded, color: AppColors.primary),
                  ),
                ],
              ),
              const SizedBox(height: 12),
              Center(
                child: _ProfileImagePickerPreview(
                  imageUrl: widget.memberProfileImageUrl,
                  selectedImageBytes: _selectedImageBytes,
                  onPickImage: _saving ? null : _pickImage,
                ),
              ),
              const SizedBox(height: 18),
              _ProfileTextField(
                controller: _nameController,
                label: '이름',
                validator: (value) {
                  final text = value?.trim() ?? '';
                  if (text.isEmpty) {
                    return '이름을 입력해 주세요.';
                  }
                  if (text.length > 50) {
                    return '50자 이하로 입력해 주세요.';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 10),
              _ProfileTextField(
                controller: _ageController,
                label: '나이',
                keyboardType: TextInputType.number,
              ),
              const SizedBox(height: 10),
              _ProfileTextField(
                controller: _phoneController,
                label: '전화번호',
                keyboardType: TextInputType.phone,
              ),
              const SizedBox(height: 10),
              _ProfileTextField(
                controller: _mottoController,
                label: '좌우명',
                maxLength: 100,
              ),
              const SizedBox(height: 16),
              SizedBox(
                width: double.infinity,
                height: 48,
                child: ElevatedButton(
                  onPressed: _saving ? null : _save,
                  child: _saving
                      ? const SizedBox(
                          width: 20,
                          height: 20,
                          child: CircularProgressIndicator(strokeWidth: 2),
                        )
                      : const Text('저장'),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _ProfileImagePickerPreview extends StatelessWidget {
  const _ProfileImagePickerPreview({
    required this.imageUrl,
    required this.selectedImageBytes,
    required this.onPickImage,
  });

  final String? imageUrl;
  final Uint8List? selectedImageBytes;
  final VoidCallback? onPickImage;

  @override
  Widget build(BuildContext context) {
    final selectedBytes = selectedImageBytes;

    return InkWell(
      onTap: onPickImage,
      borderRadius: BorderRadius.circular(18),
      child: Container(
        width: 128,
        height: 128,
        decoration: BoxDecoration(
          color: AppColors.softAccent,
          borderRadius: BorderRadius.circular(18),
        ),
        clipBehavior: Clip.antiAlias,
        child: Stack(
          fit: StackFit.expand,
          children: [
            if (selectedBytes != null)
              Image.memory(selectedBytes, fit: BoxFit.cover)
            else if (imageUrl != null)
              Image.network(imageUrl!, fit: BoxFit.cover)
            else
              Icon(Icons.person_rounded, size: 54, color: AppColors.primary),
            Align(
              alignment: Alignment.bottomCenter,
              child: Container(
                width: double.infinity,
                padding: const EdgeInsets.symmetric(vertical: 7),
                color: AppColors.primary.withValues(alpha: 0.82),
                child: Text(
                  '사진 선택',
                  textAlign: TextAlign.center,
                  style: TextStyle(
                    color: AppColors.white,
                    fontSize: 12,
                    fontWeight: FontWeight.w900,
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _ProfileTextField extends StatelessWidget {
  const _ProfileTextField({
    required this.controller,
    required this.label,
    this.keyboardType,
    this.maxLength,
    this.validator,
  });

  final TextEditingController controller;
  final String label;
  final TextInputType? keyboardType;
  final int? maxLength;
  final FormFieldValidator<String>? validator;

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      controller: controller,
      keyboardType: keyboardType,
      maxLength: maxLength,
      validator: validator,
      decoration: InputDecoration(
        labelText: label,
        counterText: '',
        filled: true,
        fillColor: AppColors.softAccent,
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(12),
          borderSide: BorderSide.none,
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(12),
          borderSide: BorderSide(color: AppColors.primary, width: 1.2),
        ),
      ),
    );
  }
}

class _AppHeader extends StatelessWidget {
  const _AppHeader({
    required this.gymName,
    required this.onEditProfile,
  });

  final String gymName;
  final VoidCallback onEditProfile;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.fromLTRB(24, 12, 24, 4),
      color: AppColors.background,
      child: Row(
        children: [
          Expanded(
            child: Text(
              gymName,
              maxLines: 1,
              overflow: TextOverflow.ellipsis,
              style: TextStyle(
                color: AppColors.muted,
                fontSize: 13,
                fontWeight: FontWeight.w800,
              ),
            ),
          ),
          _HeaderTextButton(
            label: '내 정보 수정',
            onPressed: onEditProfile,
          ),
          const SizedBox(width: 6),
          _HeaderIconButton(
            icon: Icons.settings_outlined,
            color: AppColors.primary,
            onPressed: () {
              Navigator.of(context).push(
                MaterialPageRoute(
                  builder: (context) => const SettingsScreen(),
                ),
              );
            },
          ),
          _HeaderIconButton(
            icon: Icons.notifications_none_rounded,
            color: AppColors.primary,
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

class _HeaderTextButton extends StatelessWidget {
  const _HeaderTextButton({
    required this.label,
    required this.onPressed,
  });

  final String label;
  final VoidCallback onPressed;

  @override
  Widget build(BuildContext context) {
    return TextButton(
      onPressed: onPressed,
      style: TextButton.styleFrom(
        padding: const EdgeInsets.symmetric(horizontal: 8),
        minimumSize: const Size(0, 36),
        tapTargetSize: MaterialTapTargetSize.shrinkWrap,
        foregroundColor: AppColors.primary,
      ),
      child: Text(
        label,
        style: TextStyle(
          fontSize: 12,
          fontWeight: FontWeight.w900,
          color: AppColors.primary,
        ),
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

    return id == null
        ? _GoalLevelGrid(levels: _defaultGoalLevels)
        : FutureBuilder<List<GoalLevel>>(
        future: MemberApiService().fetchGoalLevels(id),
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

          final levels = snapshot.data ?? [];
          return _GoalLevelGrid(levels: levels);
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
      padding: const EdgeInsets.symmetric(horizontal: 8),
      decoration: BoxDecoration(
        color: AppColors.softAccent,
        borderRadius: BorderRadius.circular(12),
      ),
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
                    color: AppColors.primary,
                    fontSize: 13,
                    fontWeight: FontWeight.w900,
                  ),
                ),
              ),
            ),
          ),
          const SizedBox(width: 5),
          SizedBox(
            width: 34,
            child: Text(
              'Lv. ${level.level}',
              maxLines: 1,
              textAlign: TextAlign.right,
              style: TextStyle(
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

    return SizedBox(
      width: double.infinity,
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
                              color: AppColors.softAccent,
                              borderRadius: BorderRadius.circular(12),
                            ),
                            child: Text(
                              '${_today.year}년\n${_today.month}월',
                              textAlign: TextAlign.center,
                              style: TextStyle(
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
                                    borderRadius: BorderRadius.circular(12),
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
                                        style: TextStyle(
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
        color: AppColors.softAccent,
        borderRadius: BorderRadius.circular(12),
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
                        style: TextStyle(
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
      padding: const EdgeInsets.fromLTRB(18, 8, 18, 22),
      decoration: BoxDecoration(
        color: AppColors.white,
        border: Border(top: BorderSide(color: AppColors.softBorder)),
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
          _NavItem(
            icon: Icons.list_alt_rounded,
            label: '일퀘',
            onTap: () {
              Navigator.of(context).push(
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
              Navigator.of(context).push(
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
