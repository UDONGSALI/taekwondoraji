import 'package:flutter/material.dart';

import 'app_colors.dart';

class AppTextStyles {
  const AppTextStyles._();

  static const appTitle = TextStyle(
    color: AppColors.primary,
    fontSize: 42,
    fontWeight: FontWeight.w800,
    letterSpacing: 0,
  );

  static const screenTitle = TextStyle(
    color: AppColors.primary,
    fontSize: 31,
    fontWeight: FontWeight.w800,
    letterSpacing: 0,
  );

  static const subtitle = TextStyle(
    color: AppColors.muted,
    fontSize: 15,
    fontWeight: FontWeight.w500,
    letterSpacing: 0,
  );

  static const body = TextStyle(
    color: AppColors.muted,
    fontSize: 14,
    height: 1.35,
  );

  static const cardTitle = TextStyle(
    color: AppColors.primary,
    fontSize: 18,
    fontWeight: FontWeight.w800,
    letterSpacing: 0,
  );

  static const button = TextStyle(
    fontSize: 16,
    fontWeight: FontWeight.w700,
    letterSpacing: 0,
  );
}
