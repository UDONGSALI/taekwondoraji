import 'package:flutter/material.dart';

import 'screens/login_screen.dart';
import 'services/app_settings_store.dart';
import 'theme/app_colors.dart';
import 'theme/app_theme.dart';

const appTitle = '태권도라지';

void main() {
  runApp(const TaekwondorajiApp());
}

class TaekwondorajiApp extends StatelessWidget {
  const TaekwondorajiApp({super.key});

  @override
  Widget build(BuildContext context) {
    return ValueListenableBuilder<AppSettingsData>(
      valueListenable: AppSettingsStore.listenable,
      builder: (context, settings, _) {
        AppColors.use(settings.colorBase);

        return MaterialApp(
          debugShowCheckedModeBanner: false,
          title: appTitle,
          theme: AppTheme.light(),
          home: const LoginScreen(),
        );
      },
    );
  }
}
