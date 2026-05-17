import 'package:flutter/material.dart';

import '../services/app_settings_store.dart';
import '../theme/app_colors.dart';
import '../theme/app_text_styles.dart';

class SettingsScreen extends StatefulWidget {
  const SettingsScreen({super.key});

  @override
  State<SettingsScreen> createState() => _SettingsScreenState();
}

class _SettingsScreenState extends State<SettingsScreen> {
  late AppSettingsData _settings;

  @override
  void initState() {
    super.initState();
    _settings = AppSettingsStore.data;
  }

  void _save(AppSettingsData settings) {
    AppSettingsStore.save(settings);
    setState(() {
      _settings = settings;
    });
  }

  void _reset() {
    AppSettingsStore.reset();
    setState(() {
      _settings = AppSettingsStore.data;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      body: SafeArea(
        child: Column(
          children: [
            _SettingsHeader(onBack: () => Navigator.of(context).pop()),
            Expanded(
              child: ListView(
                padding: const EdgeInsets.fromLTRB(24, 8, 24, 28),
                children: [
                  _SettingsSection(
                    title: '알림',
                    children: [
                      _SwitchSettingTile(
                        icon: Icons.notifications_rounded,
                        title: '푸시 알림',
                        value: _settings.pushNotifications,
                        onChanged: (value) => _save(
                          _settings.copyWith(pushNotifications: value),
                        ),
                      ),
                      _SwitchSettingTile(
                        icon: Icons.event_available_rounded,
                        title: '출석 알림',
                        value: _settings.attendanceReminder,
                        enabled: _settings.pushNotifications,
                        onChanged: (value) => _save(
                          _settings.copyWith(attendanceReminder: value),
                        ),
                      ),
                      _SwitchSettingTile(
                        icon: Icons.flag_rounded,
                        title: '목표와 일퀘 알림',
                        value: _settings.questReminder,
                        enabled: _settings.pushNotifications,
                        onChanged: (value) => _save(
                          _settings.copyWith(questReminder: value),
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 14),
                  _SettingsSection(
                    title: '사용감',
                    children: [
                      _SwitchSettingTile(
                        icon: Icons.vibration_rounded,
                        title: '진동',
                        value: _settings.vibration,
                        onChanged: (value) => _save(
                          _settings.copyWith(vibration: value),
                        ),
                      ),
                      _SwitchSettingTile(
                        icon: Icons.volume_up_rounded,
                        title: '효과음',
                        value: _settings.soundEffects,
                        onChanged: (value) => _save(
                          _settings.copyWith(soundEffects: value),
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 14),
                  _SettingsSection(
                    title: '화면',
                    children: [
                      _SegmentSettingTile<AppThemeModeSetting>(
                        icon: Icons.contrast_rounded,
                        title: '화면 모드',
                        selected: _settings.themeMode,
                        segments: const [
                          ButtonSegment(
                            value: AppThemeModeSetting.system,
                            label: Text('자동'),
                          ),
                          ButtonSegment(
                            value: AppThemeModeSetting.light,
                            label: Text('밝게'),
                          ),
                          ButtonSegment(
                            value: AppThemeModeSetting.dark,
                            label: Text('어둡게'),
                          ),
                        ],
                        onChanged: (value) => _save(
                          _settings.copyWith(themeMode: value),
                        ),
                      ),
                      _SegmentSettingTile<AppColorBaseSetting>(
                        icon: Icons.palette_rounded,
                        title: '색상',
                        selected: _settings.colorBase,
                        segments: const [
                          ButtonSegment(
                            value: AppColorBaseSetting.gray,
                            label: Text('회색'),
                          ),
                          ButtonSegment(
                            value: AppColorBaseSetting.brown,
                            label: Text('연갈색'),
                          ),
                        ],
                        onChanged: (value) => _save(
                          _settings.copyWith(colorBase: value),
                        ),
                      ),
                      _SegmentSettingTile<AppStartScreenSetting>(
                        icon: Icons.home_rounded,
                        title: '시작 화면',
                        selected: _settings.startScreen,
                        segments: const [
                          ButtonSegment(
                            value: AppStartScreenSetting.home,
                            label: Text('홈'),
                          ),
                          ButtonSegment(
                            value: AppStartScreenSetting.goal,
                            label: Text('목표'),
                          ),
                          ButtonSegment(
                            value: AppStartScreenSetting.dailyQuest,
                            label: Text('일퀘'),
                          ),
                        ],
                        onChanged: (value) => _save(
                          _settings.copyWith(startScreen: value),
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 14),
                  _SettingsSection(
                    title: '앱',
                    children: [
                      _ActionSettingTile(
                        icon: Icons.restart_alt_rounded,
                        title: '설정 초기화',
                        onTap: _reset,
                      ),
                      const _InfoSettingTile(
                        icon: Icons.info_rounded,
                        title: '버전',
                        value: '1.0.0',
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _SettingsHeader extends StatelessWidget {
  const _SettingsHeader({required this.onBack});

  final VoidCallback onBack;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.fromLTRB(24, 22, 24, 10),
      color: AppColors.background,
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Expanded(
            child: Text(
              '설정',
              style: AppTextStyles.screenTitle.copyWith(fontSize: 42),
            ),
          ),
          SizedBox(
            width: 40,
            height: 40,
            child: IconButton(
              onPressed: onBack,
              padding: EdgeInsets.zero,
              icon: Icon(
                Icons.arrow_forward_ios_rounded,
                size: 20,
                color: AppColors.primary,
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class _SettingsSection extends StatelessWidget {
  const _SettingsSection({
    required this.title,
    required this.children,
  });

  final String title;
  final List<Widget> children;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: EdgeInsets.zero,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            title,
            style: TextStyle(
              color: AppColors.primary,
              fontSize: 22,
              fontWeight: FontWeight.w900,
            ),
          ),
          const SizedBox(height: 4),
          ...children,
        ],
      ),
    );
  }
}

class _SwitchSettingTile extends StatelessWidget {
  const _SwitchSettingTile({
    required this.icon,
    required this.title,
    required this.value,
    required this.onChanged,
    this.enabled = true,
  });

  final IconData icon;
  final String title;
  final bool value;
  final bool enabled;
  final ValueChanged<bool> onChanged;

  @override
  Widget build(BuildContext context) {
    return _SettingTileShell(
      icon: icon,
      title: title,
      enabled: enabled,
      trailing: Switch.adaptive(
        value: enabled && value,
        onChanged: enabled ? onChanged : null,
        activeThumbColor: AppColors.primary,
      ),
    );
  }
}

class _SegmentSettingTile<T> extends StatelessWidget {
  const _SegmentSettingTile({
    required this.icon,
    required this.title,
    required this.selected,
    required this.segments,
    required this.onChanged,
  });

  final IconData icon;
  final String title;
  final T selected;
  final List<ButtonSegment<T>> segments;
  final ValueChanged<T> onChanged;

  @override
  Widget build(BuildContext context) {
    return _SettingTileShell(
      icon: icon,
      title: title,
      trailing: SegmentedButton<T>(
        segments: segments,
        selected: {selected},
        showSelectedIcon: false,
        onSelectionChanged: (values) => onChanged(values.first),
        style: ButtonStyle(
          visualDensity: VisualDensity.compact,
          textStyle: WidgetStateProperty.all(
            TextStyle(fontSize: 12, fontWeight: FontWeight.w800),
          ),
        ),
      ),
    );
  }
}

class _ActionSettingTile extends StatelessWidget {
  const _ActionSettingTile({
    required this.icon,
    required this.title,
    required this.onTap,
  });

  final IconData icon;
  final String title;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return _SettingTileShell(
      icon: icon,
      title: title,
      trailing: TextButton(
        onPressed: onTap,
        child: const Text('초기화'),
      ),
    );
  }
}

class _InfoSettingTile extends StatelessWidget {
  const _InfoSettingTile({
    required this.icon,
    required this.title,
    required this.value,
  });

  final IconData icon;
  final String title;
  final String value;

  @override
  Widget build(BuildContext context) {
    return _SettingTileShell(
      icon: icon,
      title: title,
      trailing: Text(
        value,
        style: TextStyle(
          color: AppColors.muted,
          fontSize: 13,
          fontWeight: FontWeight.w800,
        ),
      ),
    );
  }
}

class _SettingTileShell extends StatelessWidget {
  const _SettingTileShell({
    required this.icon,
    required this.title,
    required this.trailing,
    this.enabled = true,
  });

  final IconData icon;
  final String title;
  final Widget trailing;
  final bool enabled;

  @override
  Widget build(BuildContext context) {
    final foreground = enabled ? AppColors.primary : AppColors.muted;

    return Container(
      constraints: const BoxConstraints(minHeight: 66),
      padding: const EdgeInsets.symmetric(vertical: 8),
      decoration: BoxDecoration(
        border: Border(bottom: BorderSide(color: AppColors.softBorder)),
      ),
      child: Row(
        children: [
          Icon(icon, size: 22, color: foreground),
          const SizedBox(width: 14),
          Expanded(
            child: Text(
              title,
              style: TextStyle(
                color: foreground,
                fontSize: 18,
                fontWeight: FontWeight.w900,
              ),
            ),
          ),
          const SizedBox(width: 14),
          trailing,
        ],
      ),
    );
  }
}
