import 'package:flutter/material.dart';

const _allRegionCode = 'all';
const _screenTitle = '도장 선택';
const _screenSubtitle = '소속 도장을 선택해 주세요';
const _regionLabel = '지역';
const _allRegionName = '전체';
const _emptyMessage = '선택한 지역에 도장이 없어요';
const _activeStatus = '운영중';
const _waitingStatus = '준비중';
const _stoppedStatus = '중지';
const _selectButton = '선택';

class RegionCodeInfo {
  const RegionCodeInfo({required this.regionCode, required this.regionName});

  final String regionCode;
  final String regionName;
}

class GymInfo {
  const GymInfo({
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
}

class GymSelectionScreen extends StatefulWidget {
  const GymSelectionScreen({super.key});

  @override
  State<GymSelectionScreen> createState() => _GymSelectionScreenState();
}

class _GymSelectionScreenState extends State<GymSelectionScreen> {
  String _selectedRegionCode = _allRegionCode;

  List<GymInfo> get _filteredGyms {
    if (_selectedRegionCode == _allRegionCode) {
      return _sampleGyms;
    }

    return _sampleGyms
        .where((gym) => gym.regionCode == _selectedRegionCode)
        .toList();
  }

  @override
  Widget build(BuildContext context) {
    final filteredGyms = _filteredGyms;

    return Scaffold(
      backgroundColor: const Color(0xFFF8F8F8),
      appBar: AppBar(
        backgroundColor: const Color(0xFFF8F8F8),
        foregroundColor: Colors.black,
        elevation: 0,
        scrolledUnderElevation: 0,
      ),
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.fromLTRB(22, 8, 22, 20),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const Text(
                _screenTitle,
                style: TextStyle(
                  color: Colors.black,
                  fontSize: 30,
                  fontWeight: FontWeight.w800,
                  letterSpacing: 0,
                ),
              ),
              const SizedBox(height: 8),
              const Text(
                _screenSubtitle,
                style: TextStyle(
                  color: Color(0xFF666666),
                  fontSize: 15,
                  fontWeight: FontWeight.w500,
                  letterSpacing: 0,
                ),
              ),
              const SizedBox(height: 24),
              DropdownButtonFormField<String>(
                value: _selectedRegionCode,
                decoration: InputDecoration(
                  labelText: _regionLabel,
                  filled: true,
                  fillColor: Colors.white,
                  contentPadding: const EdgeInsets.symmetric(
                    horizontal: 16,
                    vertical: 14,
                  ),
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(10),
                    borderSide: const BorderSide(color: Colors.black12),
                  ),
                  enabledBorder: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(10),
                    borderSide: const BorderSide(color: Colors.black12),
                  ),
                  focusedBorder: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(10),
                    borderSide: const BorderSide(color: Colors.black),
                  ),
                ),
                dropdownColor: Colors.white,
                icon: const Icon(Icons.keyboard_arrow_down_rounded),
                items: [
                  const DropdownMenuItem<String>(
                    value: _allRegionCode,
                    child: Text(_allRegionName),
                  ),
                  ..._sampleRegions.map(
                    (region) => DropdownMenuItem<String>(
                      value: region.regionCode,
                      child: Text(region.regionName),
                    ),
                  ),
                ],
                onChanged: (value) {
                  if (value == null) {
                    return;
                  }

                  setState(() {
                    _selectedRegionCode = value;
                  });
                },
              ),
              const SizedBox(height: 18),
              Expanded(
                child: filteredGyms.isEmpty
                    ? const Center(
                        child: Text(
                          _emptyMessage,
                          style: TextStyle(
                            color: Color(0xFF777777),
                            fontSize: 15,
                            fontWeight: FontWeight.w600,
                          ),
                        ),
                      )
                    : ListView.separated(
                        itemCount: filteredGyms.length,
                        separatorBuilder: (context, index) =>
                            const SizedBox(height: 10),
                        itemBuilder: (context, index) {
                          return _GymListTile(gym: filteredGyms[index]);
                        },
                      ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _GymListTile extends StatelessWidget {
  const _GymListTile({required this.gym});

  final GymInfo gym;

  @override
  Widget build(BuildContext context) {
    final address = [
      gym.addressRoad,
      gym.addressDetail,
    ].whereType<String>().where((text) => text.isNotEmpty).join(' ');

    return Material(
      color: Colors.white,
      borderRadius: BorderRadius.circular(8),
      child: InkWell(
        borderRadius: BorderRadius.circular(8),
        onTap: () {},
        child: Container(
          padding: const EdgeInsets.all(16),
          decoration: BoxDecoration(
            border: Border.all(color: const Color(0xFFE6E6E6)),
            borderRadius: BorderRadius.circular(8),
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  Expanded(
                    child: Text(
                      gym.gymName,
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                      style: const TextStyle(
                        color: Colors.black,
                        fontSize: 18,
                        fontWeight: FontWeight.w800,
                        letterSpacing: 0,
                      ),
                    ),
                  ),
                  _StatusBadge(status: gym.serviceStatus),
                ],
              ),
              if (address.isNotEmpty) ...[
                const SizedBox(height: 8),
                Text(
                  address,
                  maxLines: 2,
                  overflow: TextOverflow.ellipsis,
                  style: const TextStyle(
                    color: Color(0xFF555555),
                    fontSize: 14,
                    height: 1.35,
                  ),
                ),
              ],
              const SizedBox(height: 14),
              Row(
                children: [
                  Expanded(
                    child: Text(
                      gym.phoneNumber ?? '',
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                      style: const TextStyle(
                        color: Color(0xFF777777),
                        fontSize: 13,
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                  ),
                  TextButton(
                    onPressed: () {},
                    style: TextButton.styleFrom(
                      foregroundColor: Colors.black,
                      padding: const EdgeInsets.symmetric(horizontal: 10),
                    ),
                    child: const Text(
                      _selectButton,
                      style: TextStyle(fontWeight: FontWeight.w800),
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _StatusBadge extends StatelessWidget {
  const _StatusBadge({required this.status});

  final String status;

  @override
  Widget build(BuildContext context) {
    final label = switch (status) {
      'active' => _activeStatus,
      'stop' => _stoppedStatus,
      _ => _waitingStatus,
    };

    final isActive = status == 'active';

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 9, vertical: 5),
      decoration: BoxDecoration(
        color: isActive ? Colors.black : const Color(0xFFF1F1F1),
        borderRadius: BorderRadius.circular(999),
      ),
      child: Text(
        label,
        style: TextStyle(
          color: isActive ? Colors.white : const Color(0xFF555555),
          fontSize: 12,
          fontWeight: FontWeight.w800,
          letterSpacing: 0,
        ),
      ),
    );
  }
}

const _sampleRegions = [
  RegionCodeInfo(regionCode: '1100', regionName: '서울'),
  RegionCodeInfo(regionCode: '2600', regionName: '부산'),
  RegionCodeInfo(regionCode: '4100', regionName: '경기'),
];

const _sampleGyms = [
  GymInfo(
    gymId: 1,
    gymName: '태권도라지 강남도장',
    ownerName: '홍길동',
    regionCode: '1100',
    serviceStatus: 'active',
    phoneNumber: '02-1234-5678',
    addressRoad: '서울시 강남구 테헤란로 12',
    addressDetail: '3F',
  ),
  GymInfo(
    gymId: 2,
    gymName: '검은띠 분당 도장',
    ownerName: '김수련',
    regionCode: '4100',
    serviceStatus: 'wait',
    phoneNumber: '031-222-3344',
    addressRoad: '경기도 성남시 분당구 야탑로 24',
  ),
  GymInfo(
    gymId: 3,
    gymName: '해운대 태권도장',
    ownerName: '이사범',
    regionCode: '2600',
    serviceStatus: 'active',
    phoneNumber: '051-987-6543',
    addressRoad: '부산시 해운대구 달맞이길 9',
  ),
];
