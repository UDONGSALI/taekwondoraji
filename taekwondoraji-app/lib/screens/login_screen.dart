import 'dart:math' as math;

import 'package:flutter/material.dart';

import 'gym_selection_screen.dart';

const _appName = '태권도라지';
const _tagline = '한 걸음씩 강해지는 수련';
const _kakaoLogin = '카카오 로그인';
const _signUp = '회원가입';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen>
    with SingleTickerProviderStateMixin {
  late final AnimationController _stampController;
  late final Animation<double> _stampScale;
  late final Animation<double> _stampOpacity;

  @override
  void initState() {
    super.initState();
    _stampController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 850),
    );
    _stampScale = TweenSequence<double>([
      TweenSequenceItem(tween: Tween(begin: 0.65, end: 1.08), weight: 45),
      TweenSequenceItem(tween: Tween(begin: 1.08, end: 1), weight: 20),
      TweenSequenceItem(tween: Tween(begin: 1, end: 1), weight: 35),
    ]).animate(
      CurvedAnimation(parent: _stampController, curve: Curves.easeOutCubic),
    );
    _stampOpacity = TweenSequence<double>([
      TweenSequenceItem(tween: Tween(begin: 0, end: 0.9), weight: 25),
      TweenSequenceItem(tween: Tween(begin: 0.9, end: 0.9), weight: 45),
      TweenSequenceItem(tween: Tween(begin: 0.9, end: 0), weight: 30),
    ]).animate(_stampController);
  }

  @override
  void dispose() {
    _stampController.dispose();
    super.dispose();
  }

  Future<void> _showFootprintStamp() async {
    await _stampController.forward(from: 0);

    if (!mounted) {
      return;
    }

    Navigator.of(context).push(
      MaterialPageRoute(builder: (context) => const GymSelectionScreen()),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF8F8F8),
      body: SafeArea(
        child: Stack(
          alignment: Alignment.center,
          children: [
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 28),
              child: Column(
                children: [
                  const Spacer(flex: 4),
                  const Text(
                    _appName,
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      color: Colors.black,
                      fontSize: 42,
                      fontWeight: FontWeight.w800,
                      letterSpacing: 0,
                    ),
                  ),
                  const SizedBox(height: 12),
                  const Text(
                    _tagline,
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      color: Color(0xFF555555),
                      fontSize: 17,
                      fontWeight: FontWeight.w500,
                      letterSpacing: 0,
                    ),
                  ),
                  const Spacer(flex: 5),
                  SizedBox(
                    width: double.infinity,
                    height: 54,
                    child: ElevatedButton(
                      onPressed: _showFootprintStamp,
                      style: ElevatedButton.styleFrom(
                        elevation: 0,
                        backgroundColor: const Color(0xFFFEE500),
                        foregroundColor: Colors.black,
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(10),
                        ),
                      ),
                      child: const Text(
                        _kakaoLogin,
                        style: TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.w700,
                          letterSpacing: 0,
                        ),
                      ),
                    ),
                  ),
                  const SizedBox(height: 12),
                  SizedBox(
                    width: double.infinity,
                    height: 54,
                    child: OutlinedButton(
                      onPressed: () {},
                      style: OutlinedButton.styleFrom(
                        foregroundColor: Colors.black,
                        side: const BorderSide(color: Colors.black, width: 1.2),
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(10),
                        ),
                      ),
                      child: const Text(
                        _signUp,
                        style: TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.w700,
                          letterSpacing: 0,
                        ),
                      ),
                    ),
                  ),
                  const SizedBox(height: 28),
                ],
              ),
            ),
            IgnorePointer(
              child: AnimatedBuilder(
                animation: _stampController,
                builder: (context, child) {
                  return Opacity(
                    opacity: _stampOpacity.value,
                    child: Transform.rotate(
                      angle: 35 * math.pi / 180,
                      child: Transform.scale(
                        scale: _stampScale.value,
                        child: child,
                      ),
                    ),
                  );
                },
                child: Image.asset(
                  'assets/images/footprints.png',
                  width: 220,
                  fit: BoxFit.contain,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
