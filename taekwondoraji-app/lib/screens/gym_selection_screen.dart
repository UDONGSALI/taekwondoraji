import 'package:flutter/material.dart';

import '../services/member_api_service.dart';
import '../theme/app_colors.dart';
import '../theme/app_text_styles.dart';

const _allRegionCode = '';

class GymSelectionScreen extends StatelessWidget {
  const GymSelectionScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return const Scaffold(body: SafeArea(child: GymSelectionView()));
  }
}

class GymSelectionView extends StatefulWidget {
  const GymSelectionView({
    this.onSelectGym,
    super.key,
  });

  final ValueChanged<GymListItem>? onSelectGym;

  @override
  State<GymSelectionView> createState() => _GymSelectionViewState();
}

class _GymSelectionViewState extends State<GymSelectionView> {
  final _apiService = MemberApiService();
  String _selectedRegionCode = _allRegionCode;
  late Future<List<RegionOption>> _regionsFuture;
  late Future<List<GymListItem>> _gymsFuture;

  @override
  void initState() {
    super.initState();
    _regionsFuture = _apiService.fetchRegions();
    _gymsFuture = _apiService.fetchGyms();
  }

  void _changeRegion(String value) {
    setState(() {
      _selectedRegionCode = value;
      _gymsFuture = _apiService.fetchGyms(
        regionCode: value == _allRegionCode ? null : value,
      );
    });
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(18, 16, 18, 16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          FutureBuilder<List<RegionOption>>(
            future: _regionsFuture,
            builder: (context, snapshot) {
              final regions = snapshot.data ?? [];

              return DropdownButtonFormField<String>(
                value: _selectedRegionCode,
                decoration: InputDecoration(
                  labelText: '지역',
                  filled: true,
                  fillColor: AppColors.surface,
                  contentPadding: const EdgeInsets.symmetric(
                    horizontal: 16,
                    vertical: 14,
                  ),
                  border: _inputBorder(AppColors.border),
                  enabledBorder: _inputBorder(AppColors.border),
                  focusedBorder: _inputBorder(AppColors.primary),
                ),
                dropdownColor: AppColors.surface,
                icon: Icon(Icons.keyboard_arrow_down_rounded),
                items: [
                  const DropdownMenuItem<String>(
                    value: _allRegionCode,
                    child: Text('전체'),
                  ),
                  ...regions.map(
                    (region) => DropdownMenuItem<String>(
                      value: region.regionCode,
                      child: Text(region.regionName),
                    ),
                  ),
                ],
                onChanged: (value) {
                  if (value != null) {
                    _changeRegion(value);
                  }
                },
              );
            },
          ),
          const SizedBox(height: 14),
          Expanded(
            child: FutureBuilder<List<GymListItem>>(
              future: _gymsFuture,
              builder: (context, snapshot) {
                if (snapshot.connectionState != ConnectionState.done) {
                  return Center(child: CircularProgressIndicator());
                }

                if (snapshot.hasError) {
                  return Center(child: Text('도장 목록을 불러오지 못했습니다.'));
                }

                final gyms = snapshot.data ?? [];
                if (gyms.isEmpty) {
                  return Center(child: Text('선택한 지역에 도장이 없어요'));
                }

                return ListView.separated(
                  itemCount: gyms.length,
                  separatorBuilder: (context, index) =>
                      const SizedBox(height: 10),
                  itemBuilder: (context, index) {
                    return _GymListTile(
                      gym: gyms[index],
                      onSelect: widget.onSelectGym,
                    );
                  },
                );
              },
            ),
          ),
        ],
      ),
    );
  }

  OutlineInputBorder _inputBorder(Color color) {
    return OutlineInputBorder(
      borderRadius: BorderRadius.circular(10),
      borderSide: BorderSide(color: color),
    );
  }
}

class _GymListTile extends StatelessWidget {
  const _GymListTile({
    required this.gym,
    required this.onSelect,
  });

  final GymListItem gym;
  final ValueChanged<GymListItem>? onSelect;

  @override
  Widget build(BuildContext context) {
    final address = [
      gym.addressRoad,
      gym.addressDetail,
    ].whereType<String>().where((text) => text.isNotEmpty).join(' ');

    return Container(
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        color: AppColors.white,
        border: Border.all(color: AppColors.border),
        borderRadius: BorderRadius.circular(12),
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
                  style: TextStyle(
                    color: AppColors.text,
                    fontSize: 17,
                    fontWeight: FontWeight.w800,
                  ),
                ),
              ),
              _StatusBadge(status: gym.serviceStatus),
            ],
          ),
          if (address.isNotEmpty) ...[
            const SizedBox(height: 8),
            Text(address, style: AppTextStyles.body),
          ],
          const SizedBox(height: 10),
          Align(
            alignment: Alignment.centerRight,
            child: TextButton(
              onPressed: onSelect == null ? null : () => onSelect!(gym),
              child: const Text('선택'),
            ),
          ),
        ],
      ),
    );
  }
}

class _StatusBadge extends StatelessWidget {
  const _StatusBadge({required this.status});

  final String status;

  @override
  Widget build(BuildContext context) {
    final normalizedStatus = status.toLowerCase();
    final label = switch (normalizedStatus) {
      'active' => '운영중',
      'stop' => '중지',
      _ => '준비중',
    };

    final isActive = normalizedStatus == 'active';

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 9, vertical: 5),
      decoration: BoxDecoration(
        color: isActive ? AppColors.primary : AppColors.softBorder,
        borderRadius: BorderRadius.circular(999),
      ),
      child: Text(
        label,
        style: TextStyle(
          color: isActive ? AppColors.white : AppColors.muted,
          fontSize: 12,
          fontWeight: FontWeight.w800,
        ),
      ),
    );
  }
}
