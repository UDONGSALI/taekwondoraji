import 'dart:math' as math;

import 'package:flutter/material.dart';

import '../services/member_api_service.dart';
import '../theme/app_colors.dart';
import '../theme/app_text_styles.dart';
import 'gym_menu_screen.dart';
import 'signup_screen.dart';

const _appName = '태권도라지';
const _tagline = '한 걸음씩 강해지는 수련';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen>
    with SingleTickerProviderStateMixin {
  final _apiService = MemberApiService();
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

  Future<void> _goToGymMenuWithStamp(LoginResult loginResult) async {
    await _stampController.forward(from: 0);

    if (!mounted) {
      return;
    }

    Navigator.of(context).push(
      MaterialPageRoute(
        builder: (context) => GymMenuScreen(
          memberId: loginResult.memberId,
          memberName: loginResult.memberName,
        ),
      ),
    );
  }

  Future<void> _openLoginDialog() async {
    final result = await showGeneralDialog<LoginResult>(
      context: context,
      barrierDismissible: true,
      barrierLabel: '로그인 닫기',
      barrierColor: AppColors.primary.withValues(alpha: 0.38),
      transitionDuration: const Duration(milliseconds: 260),
      pageBuilder: (context, animation, secondaryAnimation) {
        return _LoginDialog(apiService: _apiService);
      },
      transitionBuilder: (context, animation, secondaryAnimation, child) {
        final curvedAnimation = CurvedAnimation(
          parent: animation,
          curve: Curves.easeOutCubic,
          reverseCurve: Curves.easeInCubic,
        );

        return SlideTransition(
          position: Tween<Offset>(
            begin: const Offset(-1.15, 0),
            end: Offset.zero,
          ).animate(curvedAnimation),
          child: FadeTransition(opacity: curvedAnimation, child: child),
        );
      },
    );

    if (result != null) {
      await _goToGymMenuWithStamp(result);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: Stack(
          alignment: Alignment.center,
          children: [
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 28),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Spacer(flex: 3),
                  Center(
                    child: Container(
                      width: 92,
                      height: 92,
                      decoration: BoxDecoration(
                        color: AppColors.surface,
                        border: Border.all(color: AppColors.border),
                        borderRadius: BorderRadius.circular(28),
                      ),
                      child: Padding(
                        padding: const EdgeInsets.all(18),
                        child: Image.asset('assets/images/footprints.png'),
                      ),
                    ),
                  ),
                  const SizedBox(height: 28),
                  Center(
                    child: Text(
                      _appName,
                      textAlign: TextAlign.center,
                      style: AppTextStyles.appTitle,
                    ),
                  ),
                  const SizedBox(height: 10),
                  Center(
                    child: Text(
                      _tagline,
                      textAlign: TextAlign.center,
                      style: AppTextStyles.subtitle,
                    ),
                  ),
                  const Spacer(flex: 4),
                  SizedBox(
                    width: double.infinity,
                    height: 60,
                    child: ElevatedButton(
                      onPressed: _openLoginDialog,
                      style: ElevatedButton.styleFrom(
                        elevation: 0,
                        backgroundColor: AppColors.primary,
                        foregroundColor: AppColors.white,
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(14),
                        ),
                      ),
                      child: const Text('로그인', style: AppTextStyles.button),
                    ),
                  ),
                  const SizedBox(height: 12),
                  SizedBox(
                    width: double.infinity,
                    height: 56,
                    child: OutlinedButton(
                      onPressed: () {
                        Navigator.of(context).push(
                          MaterialPageRoute(
                            builder: (context) => const SignupScreen(),
                          ),
                        );
                      },
                      style: OutlinedButton.styleFrom(
                        foregroundColor: AppColors.primary,
                        side: BorderSide(color: AppColors.border),
                        backgroundColor: AppColors.surface,
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(14),
                        ),
                      ),
                      child: const Text('회원가입', style: AppTextStyles.button),
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

class _LoginDialog extends StatefulWidget {
  const _LoginDialog({required this.apiService});

  final MemberApiService apiService;

  @override
  State<_LoginDialog> createState() => _LoginDialogState();
}

class _LoginDialogState extends State<_LoginDialog> {
  final _loginIdController = TextEditingController();
  final _passwordController = TextEditingController();
  final _loginIdFocusNode = FocusNode();
  final _passwordFocusNode = FocusNode();

  bool _isSubmitting = false;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (mounted) {
        _loginIdFocusNode.requestFocus();
      }
    });
  }

  @override
  void dispose() {
    _loginIdController.dispose();
    _passwordController.dispose();
    _loginIdFocusNode.dispose();
    _passwordFocusNode.dispose();
    super.dispose();
  }

  Future<void> _submit() async {
    final loginId = _loginIdController.text.trim();
    final loginPassword = _passwordController.text;

    if (loginId.isEmpty || loginPassword.isEmpty) {
      _showLoginFailMessage();
      return;
    }

    setState(() {
      _isSubmitting = true;
    });

    try {
      final result = await widget.apiService.login(
        loginId: loginId,
        loginPassword: loginPassword,
      );

      if (!mounted) {
        return;
      }

      Navigator.of(context).pop(result);
    } catch (_) {
      if (!mounted) {
        return;
      }

      _showLoginFailMessage();
    } finally {
      if (mounted) {
        setState(() {
          _isSubmitting = false;
        });
      }
    }
  }

  void _showLoginFailMessage() {
    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(content: Text('로그인 정보를 확인해 주세요.')),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Dialog(
      backgroundColor: AppColors.surface,
      insetPadding: EdgeInsets.symmetric(
        horizontal: MediaQuery.sizeOf(context).width * 0.10,
      ),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(18)),
      child: Padding(
        padding: const EdgeInsets.fromLTRB(22, 24, 22, 20),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('로그인', style: AppTextStyles.screenTitle),
            const SizedBox(height: 6),
            Text('오늘의 수련을 시작해요', style: AppTextStyles.subtitle),
            const SizedBox(height: 20),
            TextField(
              controller: _loginIdController,
              focusNode: _loginIdFocusNode,
              textInputAction: TextInputAction.next,
              onSubmitted: (_) => _passwordFocusNode.requestFocus(),
              decoration: _loginInputDecoration('로그인 아이디'),
            ),
            const SizedBox(height: 12),
            TextField(
              controller: _passwordController,
              focusNode: _passwordFocusNode,
              obscureText: true,
              textInputAction: TextInputAction.done,
              onSubmitted: (_) => _submit(),
              decoration: _loginInputDecoration('비밀번호'),
            ),
            const SizedBox(height: 20),
            SizedBox(
              width: double.infinity,
              height: 60,
              child: ElevatedButton(
                onPressed: _isSubmitting ? null : _submit,
                style: ElevatedButton.styleFrom(
                  elevation: 0,
                  backgroundColor: AppColors.primary,
                  foregroundColor: AppColors.white,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(14),
                  ),
                ),
                child: Text(
                  _isSubmitting ? '확인 중...' : '로그인',
                  style: TextStyle(
                    fontSize: 17,
                    fontWeight: FontWeight.w800,
                    letterSpacing: 0,
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

InputDecoration _loginInputDecoration(String label) {
  return InputDecoration(
    labelText: label,
    filled: true,
    fillColor: AppColors.white,
    contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 15),
    border: OutlineInputBorder(
      borderRadius: BorderRadius.circular(12),
      borderSide: BorderSide(color: AppColors.border),
    ),
    enabledBorder: OutlineInputBorder(
      borderRadius: BorderRadius.circular(12),
      borderSide: BorderSide(color: AppColors.border),
    ),
    focusedBorder: OutlineInputBorder(
      borderRadius: BorderRadius.circular(12),
      borderSide: BorderSide(color: AppColors.primary),
    ),
  );
}
