import 'package:flutter/material.dart';

import '../services/app_settings_store.dart';

class AppColorPalette {
  const AppColorPalette({
    required this.background,
    required this.surface,
    required this.primary,
    required this.secondary,
    required this.text,
    required this.muted,
    required this.border,
    required this.softBorder,
    required this.softAccent,
  });

  final Color background;
  final Color surface;
  final Color primary;
  final Color secondary;
  final Color text;
  final Color muted;
  final Color border;
  final Color softBorder;
  final Color softAccent;
}

class AppColors {
  const AppColors._();

  static const gray = AppColorPalette(
    background: Color(0xFFFFFFFF),
    surface: Color(0xFFFFFFFF),
    primary: Color(0xFF111111),
    secondary: Color(0xFF19D47B),
    text: Color(0xFF1E1E22),
    muted: Color(0xFF9C9CA3),
    border: Color(0xFFE8E8EA),
    softBorder: Color(0xFFF4F4F5),
    softAccent: Color(0xFFF7F7F8),
  );

  static const brown = AppColorPalette(
    background: Color(0xFFF6EFE5),
    surface: Color(0xFFFFFCF7),
    primary: Color(0xFF2F2318),
    secondary: Color(0xFFB8915D),
    text: Color(0xFF21170F),
    muted: Color(0xFF7C6A59),
    border: Color(0xFFE7D9C8),
    softBorder: Color(0xFFF1E7DA),
    softAccent: Color(0xFFEAD7BD),
  );

  static AppColorPalette _current = gray;

  static void use(AppColorBaseSetting colorBase) {
    _current = switch (colorBase) {
      AppColorBaseSetting.gray => gray,
      AppColorBaseSetting.brown => brown,
    };
  }

  static Color get background => _current.background;
  static Color get surface => _current.surface;
  static Color get primary => _current.primary;
  static Color get secondary => _current.secondary;
  static Color get text => _current.text;
  static Color get muted => _current.muted;
  static Color get border => _current.border;
  static Color get softBorder => _current.softBorder;
  static Color get softAccent => _current.softAccent;
  static const kakao = Color(0xFFFEE500);
  static const white = Colors.white;
}
