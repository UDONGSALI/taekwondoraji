import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import '../services/member_api_service.dart';
import '../theme/app_colors.dart';
import '../theme/app_text_styles.dart';
import '../widgets/address_search_panel.dart';

class SignupScreen extends StatefulWidget {
  const SignupScreen({super.key});

  @override
  State<SignupScreen> createState() => _SignupScreenState();
}

class _SignupScreenState extends State<SignupScreen> {
  final _apiService = MemberApiService();
  final _loginIdController = TextEditingController();
  final _passwordController = TextEditingController();
  final _nameController = TextEditingController();
  final _ageController = TextEditingController();
  final _phoneFirstController = TextEditingController();
  final _phoneSecondController = TextEditingController();
  final _phoneThirdController = TextEditingController();
  final _postalCodeController = TextEditingController();
  final _addressRoadController = TextEditingController();
  final _addressDetailController = TextEditingController();

  final _passwordFocusNode = FocusNode();
  final _nameFocusNode = FocusNode();
  final _ageFocusNode = FocusNode();
  final _phoneSecondFocusNode = FocusNode();
  final _phoneThirdFocusNode = FocusNode();
  final _addressDetailFocusNode = FocusNode();

  bool _isSubmitting = false;
  bool _isAddressSearchOpen = false;
  bool _lastShowPassword = false;
  bool _lastShowName = false;
  bool _lastShowOptional = false;

  bool get _showPassword => _loginIdController.text.trim().isNotEmpty;
  bool get _showName => _showPassword && _passwordController.text.isNotEmpty;
  bool get _showOptional => _showName && _nameController.text.trim().isNotEmpty;

  String? get _phoneNumber {
    final first = _phoneFirstController.text.trim();
    final second = _phoneSecondController.text.trim();
    final third = _phoneThirdController.text.trim();

    if (first.isEmpty && second.isEmpty && third.isEmpty) {
      return null;
    }

    if (first.length == 3 && second.length == 4 && third.length == 4) {
      return '$first-$second-$third';
    }

    return '';
  }

  @override
  void initState() {
    super.initState();
    _loginIdController.addListener(_refreshWhenVisibilityChanged);
    _passwordController.addListener(_refreshWhenVisibilityChanged);
    _nameController.addListener(_refreshWhenVisibilityChanged);
  }

  @override
  void dispose() {
    _loginIdController.dispose();
    _passwordController.dispose();
    _nameController.dispose();
    _ageController.dispose();
    _phoneFirstController.dispose();
    _phoneSecondController.dispose();
    _phoneThirdController.dispose();
    _postalCodeController.dispose();
    _addressRoadController.dispose();
    _addressDetailController.dispose();
    _passwordFocusNode.dispose();
    _nameFocusNode.dispose();
    _ageFocusNode.dispose();
    _phoneSecondFocusNode.dispose();
    _phoneThirdFocusNode.dispose();
    _addressDetailFocusNode.dispose();
    super.dispose();
  }

  void _refreshWhenVisibilityChanged() {
    final nextShowPassword = _showPassword;
    final nextShowName = _showName;
    final nextShowOptional = _showOptional;

    if (_lastShowPassword == nextShowPassword &&
        _lastShowName == nextShowName &&
        _lastShowOptional == nextShowOptional) {
      return;
    }

    _lastShowPassword = nextShowPassword;
    _lastShowName = nextShowName;
    _lastShowOptional = nextShowOptional;
    setState(() {});
  }

  Future<void> _submit() async {
    final phoneNumber = _phoneNumber;

    if (!_showOptional) {
      _showMessage('필수 정보를 입력해 주세요.');
      return;
    }

    if (phoneNumber == '') {
      _showMessage('전화번호는 3자리, 4자리, 4자리로 입력해 주세요.');
      return;
    }

    setState(() {
      _isSubmitting = true;
    });

    try {
      await _apiService.signup(
        loginId: _loginIdController.text.trim(),
        loginPassword: _passwordController.text,
        memberName: _nameController.text.trim(),
        age: _ageController.text,
        phoneNumber: phoneNumber,
        postalCode: _postalCodeController.text,
        addressRoad: _addressRoadController.text,
        addressDetail: _addressDetailController.text,
      );

      if (!mounted) {
        return;
      }

      _showMessage('회원가입이 완료되었습니다.');
      Navigator.of(context).pop();
    } catch (error) {
      if (!mounted) {
        return;
      }

      _showMessage(error.toString().replaceFirst('Exception: ', ''));
    } finally {
      if (mounted) {
        setState(() {
          _isSubmitting = false;
        });
      }
    }
  }

  void _showMessage(String message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(message)),
    );
  }

  void _selectAddress(AddressSearchResult result) {
    _postalCodeController.text = result.postalCode;
    _addressRoadController.text = result.addressRoad;
    setState(() {
      _isAddressSearchOpen = false;
    });
    _addressDetailFocusNode.requestFocus();
  }

  void _moveNextWhenFilled(String value, int length, FocusNode nextFocusNode) {
    if (value.length == length) {
      nextFocusNode.requestFocus();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: SafeArea(
        child: Stack(
          children: [
            SingleChildScrollView(
              padding: const EdgeInsets.fromLTRB(22, 8, 22, 28),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text('회원가입', style: AppTextStyles.screenTitle),
                  const SizedBox(height: 8),
                  const Text(
                    '필수 정보부터 차례대로 입력해 주세요',
                    style: AppTextStyles.subtitle,
                  ),
                  const SizedBox(height: 28),
                  _SignupTextField(
                    controller: _loginIdController,
                    label: '로그인 아이디',
                    textInputAction: TextInputAction.next,
                    onSubmitted: (_) => _passwordFocusNode.requestFocus(),
                  ),
                  _AnimatedField(
                    visible: _showPassword,
                    fieldKey: 'password',
                    child: _SignupTextField(
                      key: const ValueKey('password-field'),
                      controller: _passwordController,
                      focusNode: _passwordFocusNode,
                      label: '비밀번호',
                      obscureText: true,
                      textInputAction: TextInputAction.next,
                      onSubmitted: (_) => _nameFocusNode.requestFocus(),
                    ),
                  ),
                  _AnimatedField(
                    visible: _showName,
                    fieldKey: 'name',
                    child: _SignupTextField(
                      key: const ValueKey('name-field'),
                      controller: _nameController,
                      focusNode: _nameFocusNode,
                      label: '이름',
                      textInputAction: TextInputAction.next,
                      onSubmitted: (_) => _ageFocusNode.requestFocus(),
                    ),
                  ),
                  _AnimatedField(
                    visible: _showOptional,
                    fieldKey: 'optional',
                    child: _OptionalSignupFields(
                      ageController: _ageController,
                      ageFocusNode: _ageFocusNode,
                      phoneFirstController: _phoneFirstController,
                      phoneSecondController: _phoneSecondController,
                      phoneThirdController: _phoneThirdController,
                      phoneSecondFocusNode: _phoneSecondFocusNode,
                      phoneThirdFocusNode: _phoneThirdFocusNode,
                      postalCodeController: _postalCodeController,
                      addressRoadController: _addressRoadController,
                      addressDetailController: _addressDetailController,
                      addressDetailFocusNode: _addressDetailFocusNode,
                      isSubmitting: _isSubmitting,
                      onOpenAddressSearch: () {
                        setState(() {
                          _isAddressSearchOpen = true;
                        });
                      },
                      onSubmit: _submit,
                      onPhoneFilled: _moveNextWhenFilled,
                    ),
                  ),
                ],
              ),
            ),
            if (_isAddressSearchOpen)
              _AddressSearchLayer(
                onClose: () {
                  setState(() {
                    _isAddressSearchOpen = false;
                  });
                },
                onSelected: _selectAddress,
              ),
          ],
        ),
      ),
    );
  }
}

class _OptionalSignupFields extends StatelessWidget {
  const _OptionalSignupFields({
    required this.ageController,
    required this.ageFocusNode,
    required this.phoneFirstController,
    required this.phoneSecondController,
    required this.phoneThirdController,
    required this.phoneSecondFocusNode,
    required this.phoneThirdFocusNode,
    required this.postalCodeController,
    required this.addressRoadController,
    required this.addressDetailController,
    required this.addressDetailFocusNode,
    required this.isSubmitting,
    required this.onOpenAddressSearch,
    required this.onSubmit,
    required this.onPhoneFilled,
  });

  final TextEditingController ageController;
  final FocusNode ageFocusNode;
  final TextEditingController phoneFirstController;
  final TextEditingController phoneSecondController;
  final TextEditingController phoneThirdController;
  final FocusNode phoneSecondFocusNode;
  final FocusNode phoneThirdFocusNode;
  final TextEditingController postalCodeController;
  final TextEditingController addressRoadController;
  final TextEditingController addressDetailController;
  final FocusNode addressDetailFocusNode;
  final bool isSubmitting;
  final VoidCallback onOpenAddressSearch;
  final VoidCallback onSubmit;
  final void Function(String value, int length, FocusNode nextFocusNode)
      onPhoneFilled;

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        _SignupTextField(
          controller: ageController,
          focusNode: ageFocusNode,
          label: '나이',
          keyboardType: TextInputType.number,
          inputFormatters: [FilteringTextInputFormatter.digitsOnly],
        ),
        const SizedBox(height: 14),
        const Text(
          '전화번호',
          style: TextStyle(
            color: AppColors.muted,
            fontSize: 13,
            fontWeight: FontWeight.w700,
          ),
        ),
        const SizedBox(height: 8),
        Row(
          children: [
            Expanded(
              child: _PhoneTextField(
                controller: phoneFirstController,
                maxLength: 3,
                onChanged: (value) => onPhoneFilled(
                  value,
                  3,
                  phoneSecondFocusNode,
                ),
              ),
            ),
            const Padding(
              padding: EdgeInsets.symmetric(horizontal: 8),
              child: Text('-'),
            ),
            Expanded(
              child: _PhoneTextField(
                controller: phoneSecondController,
                focusNode: phoneSecondFocusNode,
                maxLength: 4,
                onChanged: (value) => onPhoneFilled(
                  value,
                  4,
                  phoneThirdFocusNode,
                ),
              ),
            ),
            const Padding(
              padding: EdgeInsets.symmetric(horizontal: 8),
              child: Text('-'),
            ),
            Expanded(
              child: _PhoneTextField(
                controller: phoneThirdController,
                focusNode: phoneThirdFocusNode,
                maxLength: 4,
              ),
            ),
          ],
        ),
        const SizedBox(height: 24),
        Row(
          children: [
            Expanded(
              child: _SignupTextField(
                controller: postalCodeController,
                label: '우편번호',
                readOnly: true,
              ),
            ),
            const SizedBox(width: 10),
            SizedBox(
              height: 54,
              child: OutlinedButton(
                onPressed: onOpenAddressSearch,
                style: OutlinedButton.styleFrom(
                  foregroundColor: AppColors.primary,
                  side: const BorderSide(color: AppColors.primary),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(10),
                  ),
                ),
                child: const Text('주소 검색'),
              ),
            ),
          ],
        ),
        const SizedBox(height: 14),
        _SignupTextField(
          controller: addressRoadController,
          label: '도로명 주소',
          readOnly: true,
        ),
        const SizedBox(height: 14),
        _SignupTextField(
          controller: addressDetailController,
          focusNode: addressDetailFocusNode,
          label: '상세 주소',
        ),
        const SizedBox(height: 28),
        SizedBox(
          width: double.infinity,
          height: 54,
          child: ElevatedButton(
            onPressed: isSubmitting ? null : onSubmit,
            style: ElevatedButton.styleFrom(
              elevation: 0,
              backgroundColor: AppColors.primary,
              foregroundColor: AppColors.white,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(10),
              ),
            ),
            child: Text(
              isSubmitting ? '가입 중...' : '가입하기',
              style: AppTextStyles.button,
            ),
          ),
        ),
      ],
    );
  }
}

class _AnimatedField extends StatelessWidget {
  const _AnimatedField({
    required this.visible,
    required this.fieldKey,
    required this.child,
  });

  final bool visible;
  final String fieldKey;
  final Widget child;

  @override
  Widget build(BuildContext context) {
    return AnimatedSwitcher(
      duration: const Duration(milliseconds: 220),
      child: visible
          ? Padding(
              key: ValueKey(fieldKey),
              padding: const EdgeInsets.only(top: 14),
              child: child,
            )
          : const SizedBox.shrink(),
    );
  }
}

class _AddressSearchLayer extends StatelessWidget {
  const _AddressSearchLayer({
    required this.onClose,
    required this.onSelected,
  });

  final VoidCallback onClose;
  final ValueChanged<AddressSearchResult> onSelected;

  @override
  Widget build(BuildContext context) {
    return Positioned.fill(
      child: Material(
        color: AppColors.primary.withValues(alpha: 0.42),
        child: Center(
          child: Container(
            width: double.infinity,
            height: 520,
            margin: const EdgeInsets.symmetric(horizontal: 18),
            decoration: BoxDecoration(
              color: AppColors.surface,
              border: Border.all(color: AppColors.border),
              borderRadius: BorderRadius.circular(12),
              boxShadow: [
                BoxShadow(
                  color: AppColors.primary.withValues(alpha: 0.18),
                  blurRadius: 28,
                  offset: const Offset(0, 14),
                ),
              ],
            ),
            clipBehavior: Clip.antiAlias,
            child: Column(
              children: [
                Container(
                  height: 54,
                  padding: const EdgeInsets.symmetric(horizontal: 16),
                  decoration: const BoxDecoration(
                    color: AppColors.surface,
                    border: Border(
                      bottom: BorderSide(color: AppColors.border),
                    ),
                  ),
                  child: Row(
                    children: [
                      const Expanded(
                        child: Text(
                          '주소 검색',
                          style: TextStyle(
                            color: AppColors.primary,
                            fontSize: 17,
                            fontWeight: FontWeight.w800,
                            letterSpacing: 0,
                          ),
                        ),
                      ),
                      IconButton(
                        onPressed: onClose,
                        icon: const Icon(Icons.close_rounded),
                        color: AppColors.primary,
                        tooltip: '닫기',
                      ),
                    ],
                  ),
                ),
                Expanded(child: AddressSearchPanel(onSelected: onSelected)),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class _SignupTextField extends StatelessWidget {
  const _SignupTextField({
    super.key,
    required this.controller,
    required this.label,
    this.focusNode,
    this.obscureText = false,
    this.keyboardType,
    this.inputFormatters,
    this.textInputAction,
    this.onSubmitted,
    this.readOnly = false,
  });

  final TextEditingController controller;
  final String label;
  final FocusNode? focusNode;
  final bool obscureText;
  final TextInputType? keyboardType;
  final List<TextInputFormatter>? inputFormatters;
  final TextInputAction? textInputAction;
  final ValueChanged<String>? onSubmitted;
  final bool readOnly;

  @override
  Widget build(BuildContext context) {
    return TextField(
      controller: controller,
      focusNode: focusNode,
      obscureText: obscureText,
      keyboardType: keyboardType,
      inputFormatters: inputFormatters,
      textInputAction: textInputAction,
      onSubmitted: onSubmitted,
      readOnly: readOnly,
      decoration: _inputDecoration(label),
    );
  }
}

class _PhoneTextField extends StatelessWidget {
  const _PhoneTextField({
    required this.controller,
    required this.maxLength,
    this.focusNode,
    this.onChanged,
  });

  final TextEditingController controller;
  final int maxLength;
  final FocusNode? focusNode;
  final ValueChanged<String>? onChanged;

  @override
  Widget build(BuildContext context) {
    return TextField(
      controller: controller,
      focusNode: focusNode,
      maxLength: maxLength,
      keyboardType: TextInputType.number,
      textAlign: TextAlign.center,
      inputFormatters: [
        FilteringTextInputFormatter.digitsOnly,
        LengthLimitingTextInputFormatter(maxLength),
      ],
      onChanged: onChanged,
      decoration: _inputDecoration('').copyWith(counterText: ''),
    );
  }
}

InputDecoration _inputDecoration(String label) {
  return InputDecoration(
    labelText: label.isEmpty ? null : label,
    filled: true,
    fillColor: AppColors.surface,
    contentPadding: const EdgeInsets.symmetric(
      horizontal: 16,
      vertical: 14,
    ),
    border: OutlineInputBorder(
      borderRadius: BorderRadius.circular(10),
      borderSide: const BorderSide(color: AppColors.border),
    ),
    enabledBorder: OutlineInputBorder(
      borderRadius: BorderRadius.circular(10),
      borderSide: const BorderSide(color: AppColors.border),
    ),
    focusedBorder: OutlineInputBorder(
      borderRadius: BorderRadius.circular(10),
      borderSide: const BorderSide(color: AppColors.primary),
    ),
  );
}
