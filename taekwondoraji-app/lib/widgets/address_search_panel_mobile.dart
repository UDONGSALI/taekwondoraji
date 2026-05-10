import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:webview_flutter/webview_flutter.dart';

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
  late final WebViewController _controller;

  @override
  void initState() {
    super.initState();
    _controller = WebViewController()
      ..setJavaScriptMode(JavaScriptMode.unrestricted)
      ..addJavaScriptChannel(
        'AddressChannel',
        onMessageReceived: (message) {
          final data = jsonDecode(message.message) as Map<String, dynamic>;
          widget.onSelected(
            AddressSearchResult(
              postalCode: data['zonecode'] as String? ?? '',
              addressRoad: data['roadAddress'] as String? ?? '',
            ),
          );
        },
      )
      ..loadHtmlString(_postcodeHtml);
  }

  @override
  Widget build(BuildContext context) {
    return WebViewWidget(controller: _controller);
  }
}

const _postcodeHtml = '''
<!DOCTYPE html>
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <style>
    html, body, #postcode {
      width: 100%;
      height: 100%;
      margin: 0;
      padding: 0;
    }
  </style>
</head>
<body>
  <div id="postcode"></div>
  <script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
  <script>
    new daum.Postcode({
      oncomplete: function(data) {
        AddressChannel.postMessage(JSON.stringify({
          zonecode: data.zonecode,
          roadAddress: data.roadAddress
        }));
      },
      width: '100%',
      height: '100%'
    }).embed(document.getElementById('postcode'));
  </script>
</body>
</html>
''';
