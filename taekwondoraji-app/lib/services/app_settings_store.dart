import 'package:flutter/foundation.dart';

enum AppThemeModeSetting {
  system,
  light,
  dark,
}

enum AppColorBaseSetting {
  gray,
  brown,
}

enum AppStartScreenSetting {
  home,
  goal,
  dailyQuest,
}

class AppSettingsData {
  const AppSettingsData({
    required this.pushNotifications,
    required this.attendanceReminder,
    required this.questReminder,
    required this.vibration,
    required this.soundEffects,
    required this.themeMode,
    required this.colorBase,
    required this.startScreen,
  });

  factory AppSettingsData.defaults() {
    return const AppSettingsData(
      pushNotifications: true,
      attendanceReminder: true,
      questReminder: true,
      vibration: true,
      soundEffects: false,
      themeMode: AppThemeModeSetting.system,
      colorBase: AppColorBaseSetting.gray,
      startScreen: AppStartScreenSetting.home,
    );
  }

  final bool pushNotifications;
  final bool attendanceReminder;
  final bool questReminder;
  final bool vibration;
  final bool soundEffects;
  final AppThemeModeSetting themeMode;
  final AppColorBaseSetting colorBase;
  final AppStartScreenSetting startScreen;

  AppSettingsData copyWith({
    bool? pushNotifications,
    bool? attendanceReminder,
    bool? questReminder,
    bool? vibration,
    bool? soundEffects,
    AppThemeModeSetting? themeMode,
    AppColorBaseSetting? colorBase,
    AppStartScreenSetting? startScreen,
  }) {
    return AppSettingsData(
      pushNotifications: pushNotifications ?? this.pushNotifications,
      attendanceReminder: attendanceReminder ?? this.attendanceReminder,
      questReminder: questReminder ?? this.questReminder,
      vibration: vibration ?? this.vibration,
      soundEffects: soundEffects ?? this.soundEffects,
      themeMode: themeMode ?? this.themeMode,
      colorBase: colorBase ?? this.colorBase,
      startScreen: startScreen ?? this.startScreen,
    );
  }
}

class AppSettingsStore {
  AppSettingsStore._();

  static AppSettingsData _data = AppSettingsData.defaults();
  static final ValueNotifier<AppSettingsData> _notifier =
      ValueNotifier<AppSettingsData>(_data);

  static AppSettingsData get data => _data;
  static ValueListenable<AppSettingsData> get listenable => _notifier;

  static void save(AppSettingsData data) {
    _data = data;
    _notifier.value = data;
  }

  static void reset() {
    _data = AppSettingsData.defaults();
    _notifier.value = _data;
  }
}
