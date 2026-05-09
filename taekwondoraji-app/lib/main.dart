import 'package:flutter/material.dart';

import 'screens/login_screen.dart';

const appTitle = '태권도라지';

void main() {
  runApp(const TaekwondorajiApp());
}

class TaekwondorajiApp extends StatelessWidget {
  const TaekwondorajiApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: appTitle,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.black),
        scaffoldBackgroundColor: const Color(0xFFF8F8F8),
        useMaterial3: true,
      ),
      home: const LoginScreen(),
    );
  }
}
