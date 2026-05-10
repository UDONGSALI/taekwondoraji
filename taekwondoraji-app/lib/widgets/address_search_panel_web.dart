// ignore: avoid_web_libraries_in_flutter
import 'dart:html' as html;
import 'dart:ui_web' as ui_web;

import 'package:flutter/material.dart';

class AddressSearchResult {
  const AddressSearchResult({
    required this.postalCode,
    required this.addressRoad,
  });

  final String postalCode;
  final String addressRoad;
}

class AddressSearchPanel extends StatefulWidget {
  const AddressSearchPanel({required this.onSelected, super.key});

  final ValueChanged<AddressSearchResult> onSelected;

  @override
  State<AddressSearchPanel> createState() => _AddressSearchPanelState();
}

class _AddressSearchPanelState extends State<AddressSearchPanel> {
  late final String _viewType;

  @override
  void initState() {
    super.initState();
    _viewType = 'daum-postcode-${DateTime.now().microsecondsSinceEpoch}';

    html.window.onMessage.listen((event) {
      final data = event.data;
      if (data is! Map || data['type'] != 'daum-postcode') {
        return;
      }

      widget.onSelected(
        AddressSearchResult(
          postalCode: data['zonecode'] as String? ?? '',
          addressRoad: data['roadAddress'] as String? ?? '',
        ),
      );
    });

    ui_web.platformViewRegistry.registerViewFactory(_viewType, (viewId) {
      return html.IFrameElement()
        ..src = 'daum_postcode.html'
        ..style.border = '0'
        ..style.width = '100%'
        ..style.height = '100%';
    });
  }

  @override
  Widget build(BuildContext context) {
    return HtmlElementView(viewType: _viewType);
  }
}
