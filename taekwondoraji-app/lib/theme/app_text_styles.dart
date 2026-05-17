import 'package:flutter/material.dart';

import 'app_colors.dart';

class AppTextStyles {
  const AppTextStyles._();

  static TextStyle get appTitle => TextStyle(
        color: AppColors.primary,
        fontSize: 44,
        fontWeight: FontWeight.w900,
        letterSpacing: 0,
      );

  static TextStyle get screenTitle => TextStyle(
        color: AppColors.primary,
        fontSize: 34,
        fontWeight: FontWeight.w900,
        letterSpacing: 0,
        height: 1.05,
      );

  static TextStyle get subtitle => TextStyle(
        color: AppColors.muted,
        fontSize: 15,
        fontWeight: FontWeight.w700,
        letterSpacing: 0,
      );

  static TextStyle get body => TextStyle(
        color: AppColors.muted,
        fontSize: 14,
        height: 1.35,
      );

  static TextStyle get cardTitle => TextStyle(
        color: AppColors.primary,
        fontSize: 17,
        fontWeight: FontWeight.w900,
        letterSpacing: 0,
      );

  static const button = TextStyle(
    fontSize: 16,
    fontWeight: FontWeight.w700,
    letterSpacing: 0,
  );
}
