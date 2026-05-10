import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:http/http.dart' as http;

class LoginResult {
  const LoginResult({
    required this.memberId,
    required this.loginId,
    required this.memberName,
  });

  final int memberId;
  final String loginId;
  final String memberName;

  factory LoginResult.fromJson(Map<String, dynamic> json) {
    return LoginResult(
      memberId: json['memberId'] as int,
      loginId: json['loginId'] as String,
      memberName: json['memberName'] as String,
    );
  }
}

class MyGym {
  const MyGym({
    required this.memberGymMapId,
    required this.gymId,
    required this.gymName,
    required this.memberRole,
    required this.memberStatus,
    required this.beltName,
    required this.point,
    this.memberAge,
    this.memberPhoneNumber,
    this.joinedDate,
    this.addressRoad,
    this.addressDetail,
    this.phoneNumber,
  });

  final int? memberGymMapId;
  final int gymId;
  final String gymName;
  final String memberRole;
  final String memberStatus;
  final String beltName;
  final int point;
  final int? memberAge;
  final String? memberPhoneNumber;
  final String? joinedDate;
  final String? addressRoad;
  final String? addressDetail;
  final String? phoneNumber;

  factory MyGym.fromJson(Map<String, dynamic> json) {
    return MyGym(
      memberGymMapId: json['memberGymMapId'] as int?,
      gymId: json['gymId'] as int,
      gymName: json['gymName'] as String,
      addressRoad: json['addressRoad'] as String?,
      addressDetail: json['addressDetail'] as String?,
      phoneNumber: json['phoneNumber'] as String?,
      memberRole: json['memberRole'] as String,
      memberStatus: json['memberStatus'] as String,
      beltName: json['beltName'] as String? ?? 'white',
      point: json['point'] as int? ?? 0,
      memberAge: json['memberAge'] as int?,
      memberPhoneNumber: json['memberPhoneNumber'] as String?,
      joinedDate: json['joinedDate'] as String?,
    );
  }
}

class RegionOption {
  const RegionOption({
    required this.regionCode,
    required this.regionName,
  });

  final String regionCode;
  final String regionName;

  factory RegionOption.fromJson(Map<String, dynamic> json) {
    return RegionOption(
      regionCode: json['regionCode'] as String,
      regionName: json['regionName'] as String,
    );
  }
}

class GymListItem {
  const GymListItem({
    required this.gymId,
    required this.gymName,
    required this.ownerName,
    required this.regionCode,
    required this.serviceStatus,
    this.phoneNumber,
    this.addressRoad,
    this.addressDetail,
  });

  final int gymId;
  final String gymName;
  final String ownerName;
  final String regionCode;
  final String serviceStatus;
  final String? phoneNumber;
  final String? addressRoad;
  final String? addressDetail;

  factory GymListItem.fromJson(Map<String, dynamic> json) {
    return GymListItem(
      gymId: json['gymId'] as int,
      gymName: json['gymName'] as String,
      ownerName: json['ownerName'] as String,
      phoneNumber: json['phoneNumber'] as String?,
      addressRoad: json['addressRoad'] as String?,
      addressDetail: json['addressDetail'] as String?,
      regionCode: json['regionCode'] as String? ?? '',
      serviceStatus: json['serviceStatus'] as String,
    );
  }
}

class GoalLevel {
  const GoalLevel({
    required this.category,
    required this.point,
    required this.level,
  });

  final String category;
  final int point;
  final int level;

  factory GoalLevel.fromJson(Map<String, dynamic> json) {
    return GoalLevel(
      category: json['category'] as String,
      point: json['point'] as int? ?? 0,
      level: json['level'] as int? ?? 0,
    );
  }
}

class AttendanceDay {
  const AttendanceDay({
    required this.attendanceId,
    required this.attendanceDate,
  });

  final int attendanceId;
  final DateTime attendanceDate;

  factory AttendanceDay.fromJson(Map<String, dynamic> json) {
    return AttendanceDay(
      attendanceId: json['attendanceId'] as int,
      attendanceDate: DateTime.parse(json['attendanceDate'] as String),
    );
  }
}

class GoalItem {
  const GoalItem({
    required this.goalId,
    required this.name,
    required this.category,
    required this.point,
    required this.goalStatus,
    required this.reapplyAvailable,
    this.memberGoalId,
    this.description,
    this.link,
    this.completedAt,
  });

  final int goalId;
  final int? memberGoalId;
  final String name;
  final String category;
  final int point;
  final String goalStatus;
  final String? description;
  final String? link;
  final DateTime? completedAt;
  final bool reapplyAvailable;

  factory GoalItem.fromJson(Map<String, dynamic> json) {
    return GoalItem(
      goalId: json['goalId'] as int,
      memberGoalId: json['memberGoalId'] as int?,
      name: json['name'] as String,
      description: json['description'] as String?,
      link: json['link'] as String?,
      category: json['category'] as String,
      point: json['point'] as int? ?? 0,
      goalStatus: json['goalStatus'] as String? ?? 'waiting',
      completedAt: _parseDateTime(json['completedAt']),
      reapplyAvailable: json['reapplyAvailable'] as bool? ?? false,
    );
  }

  static DateTime? _parseDateTime(dynamic value) {
    if (value is String && value.isNotEmpty) {
      return DateTime.tryParse(value);
    }

    return null;
  }
}

class MemberApiService {
  MemberApiService();

  static String get _baseUrl {
    if (kIsWeb) {
      return 'http://localhost:8080';
    }

    if (defaultTargetPlatform == TargetPlatform.android) {
      return 'http://10.0.2.2:8080';
    }

    return 'http://localhost:8080';
  }

  Future<void> signup({
    required String loginId,
    required String loginPassword,
    required String memberName,
    String? age,
    String? phoneNumber,
    String? postalCode,
    String? addressRoad,
    String? addressDetail,
  }) async {
    final response = await http.post(
      Uri.parse('$_baseUrl/api/members/signup'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'loginId': loginId,
        'loginPassword': loginPassword,
        'memberName': memberName,
        'age': _parseAge(age),
        'phoneNumber': _emptyToNull(phoneNumber),
        'postalCode': _emptyToNull(postalCode),
        'addressRoad': _emptyToNull(addressRoad),
        'addressDetail': _emptyToNull(addressDetail),
      }),
    );

    if (response.statusCode < 200 || response.statusCode >= 300) {
      throw Exception(_extractMessage(response.body));
    }
  }

  Future<LoginResult> login({
    required String loginId,
    required String loginPassword,
  }) async {
    final response = await http.post(
      Uri.parse('$_baseUrl/api/members/login'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'loginId': loginId,
        'loginPassword': loginPassword,
      }),
    );

    if (response.statusCode < 200 || response.statusCode >= 300) {
      throw Exception(_extractMessage(response.body));
    }

    final json = jsonDecode(response.body) as Map<String, dynamic>;
    return LoginResult.fromJson(json['data'] as Map<String, dynamic>);
  }

  Future<List<MyGym>> fetchMyGyms(int memberId) async {
    final response = await http.get(
      Uri.parse('$_baseUrl/api/members/$memberId/gyms'),
    );

    if (response.statusCode < 200 || response.statusCode >= 300) {
      throw Exception(_extractMessage(response.body));
    }

    final json = jsonDecode(response.body) as Map<String, dynamic>;
    final data = json['data'] as List<dynamic>;
    return data
        .map((item) => MyGym.fromJson(item as Map<String, dynamic>))
        .toList();
  }

  Future<List<RegionOption>> fetchRegions() async {
    final response = await http.get(Uri.parse('$_baseUrl/api/gyms/regions'));

    if (response.statusCode < 200 || response.statusCode >= 300) {
      throw Exception(_extractMessage(response.body));
    }

    final json = jsonDecode(response.body) as Map<String, dynamic>;
    final data = json['data'] as List<dynamic>;
    return data
        .map((item) => RegionOption.fromJson(item as Map<String, dynamic>))
        .toList();
  }

  Future<List<GymListItem>> fetchGyms({String? regionCode}) async {
    final uri = Uri.parse('$_baseUrl/api/gyms').replace(
      queryParameters: regionCode == null || regionCode.isEmpty
          ? null
          : {'regionCode': regionCode},
    );
    final response = await http.get(uri);

    if (response.statusCode < 200 || response.statusCode >= 300) {
      throw Exception(_extractMessage(response.body));
    }

    final json = jsonDecode(response.body) as Map<String, dynamic>;
    final data = json['data'] as List<dynamic>;
    return data
        .map((item) => GymListItem.fromJson(item as Map<String, dynamic>))
        .toList();
  }

  Future<void> joinGym({
    required int memberId,
    required int gymId,
  }) async {
    final response = await http.post(
      Uri.parse('$_baseUrl/api/members/$memberId/gyms'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({'gymId': gymId}),
    );

    if (response.statusCode < 200 || response.statusCode >= 300) {
      throw Exception(_extractMessage(response.body));
    }
  }

  Future<List<GoalLevel>> fetchGoalLevels(int memberGymMapId) async {
    final response = await http.get(
      Uri.parse('$_baseUrl/api/member-gym-maps/$memberGymMapId/goal-levels'),
    );

    if (response.statusCode < 200 || response.statusCode >= 300) {
      throw Exception(_extractMessage(response.body));
    }

    final json = jsonDecode(response.body) as Map<String, dynamic>;
    final data = json['data'] as List<dynamic>;
    return data
        .map((item) => GoalLevel.fromJson(item as Map<String, dynamic>))
        .toList();
  }

  Future<List<AttendanceDay>> fetchAttendances({
    required int memberGymMapId,
    required int year,
    required int month,
  }) async {
    final uri = Uri.parse(
      '$_baseUrl/api/member-gym-maps/$memberGymMapId/attendances',
    ).replace(
      queryParameters: {
        'year': '$year',
        'month': '$month',
      },
    );
    final response = await http.get(uri);

    if (response.statusCode < 200 || response.statusCode >= 300) {
      throw Exception(_extractMessage(response.body));
    }

    final json = jsonDecode(response.body) as Map<String, dynamic>;
    final data = json['data'] as List<dynamic>;
    return data
        .map((item) => AttendanceDay.fromJson(item as Map<String, dynamic>))
        .toList();
  }

  Future<AttendanceDay> attendToday(int memberGymMapId) async {
    final response = await http.post(
      Uri.parse(
        '$_baseUrl/api/member-gym-maps/$memberGymMapId/attendances/today',
      ),
    );

    if (response.statusCode < 200 || response.statusCode >= 300) {
      throw Exception(_extractMessage(response.body));
    }

    final json = jsonDecode(response.body) as Map<String, dynamic>;
    return AttendanceDay.fromJson(json['data'] as Map<String, dynamic>);
  }

  Future<List<GoalItem>> fetchGoals({String? category}) async {
    final uri = Uri.parse('$_baseUrl/api/goals').replace(
      queryParameters: category == null || category.isEmpty
          ? null
          : {'category': category},
    );
    final response = await http.get(uri);

    if (response.statusCode < 200 || response.statusCode >= 300) {
      throw Exception(_extractMessage(response.body));
    }

    final json = jsonDecode(response.body) as Map<String, dynamic>;
    final data = json['data'] as List<dynamic>;
    return data
        .map((item) => GoalItem.fromJson(item as Map<String, dynamic>))
        .toList();
  }

  Future<List<GoalItem>> fetchMemberGoals(int memberGymMapId) async {
    final response = await http.get(
      Uri.parse('$_baseUrl/api/member-gym-maps/$memberGymMapId/goals'),
    );

    if (response.statusCode < 200 || response.statusCode >= 300) {
      throw Exception(_extractMessage(response.body));
    }

    final json = jsonDecode(response.body) as Map<String, dynamic>;
    final data = json['data'] as List<dynamic>;
    return data
        .map((item) => GoalItem.fromJson(item as Map<String, dynamic>))
        .toList();
  }

  Future<GoalItem> applyGoal({
    required int memberGymMapId,
    required int goalId,
  }) async {
    final response = await http.post(
      Uri.parse('$_baseUrl/api/member-gym-maps/$memberGymMapId/goals/$goalId'),
    );

    if (response.statusCode < 200 || response.statusCode >= 300) {
      throw Exception(_extractMessage(response.body));
    }

    final json = jsonDecode(response.body) as Map<String, dynamic>;
    return GoalItem.fromJson(json['data'] as Map<String, dynamic>);
  }

  int? _parseAge(String? value) {
    final text = value?.trim();
    if (text == null || text.isEmpty) {
      return null;
    }

    return int.tryParse(text);
  }

  String? _emptyToNull(String? value) {
    final text = value?.trim();
    if (text == null || text.isEmpty) {
      return null;
    }

    return text;
  }

  String _extractMessage(String body) {
    try {
      final json = jsonDecode(body);
      final message = json['message'];
      if (message is String && message.isNotEmpty) {
        return message;
      }
    } catch (_) {
      return '요청에 실패했습니다.';
    }

    return '요청에 실패했습니다.';
  }
}
