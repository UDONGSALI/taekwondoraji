import 'package:flutter/material.dart';

import '../services/member_api_service.dart';
import '../theme/app_colors.dart';
import '../theme/app_text_styles.dart';
import 'daily_quest_screen.dart';
import 'goal_screen.dart';

enum _StoreItemFilter {
  all('전체'),
  available('구매 가능'),
  requested('요청됨');

  const _StoreItemFilter(this.label);

  final String label;
}

class StoreScreen extends StatefulWidget {
  const StoreScreen({
    required this.memberGymMapId,
    required this.gymName,
    super.key,
  });

  final int? memberGymMapId;
  final String gymName;

  @override
  State<StoreScreen> createState() => _StoreScreenState();
}

class _StoreScreenState extends State<StoreScreen> {
  Future<PointItemStore>? _storeFuture;
  int? _requestingItemId;
  _StoreItemFilter _filter = _StoreItemFilter.all;

  @override
  void initState() {
    super.initState();
    _storeFuture = _createStoreFuture();
  }

  Future<PointItemStore> _createStoreFuture() {
    final memberGymMapId = widget.memberGymMapId;
    return memberGymMapId == null
        ? Future.value(const PointItemStore(currentPoint: 0, items: []))
        : MemberApiService().fetchPointItemStore(memberGymMapId);
  }

  Future<void> _purchase(PointStoreItem item) async {
    final memberGymMapId = widget.memberGymMapId;
    if (memberGymMapId == null || _requestingItemId != null) {
      return;
    }

    setState(() {
      _requestingItemId = item.gymPointItemId;
    });

    try {
      final store = await MemberApiService().purchasePointItem(
        memberGymMapId: memberGymMapId,
        gymPointItemId: item.gymPointItemId,
      );

      if (!mounted) {
        return;
      }

      setState(() {
        _storeFuture = Future.value(store);
      });
    } catch (error) {
      if (!mounted) {
        return;
      }

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(error.toString().replaceFirst('Exception: ', ''))),
      );
    } finally {
      if (mounted) {
        setState(() {
          _requestingItemId = null;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      body: SafeArea(
        child: Column(
          children: [
            const _StoreHeader(),
            Expanded(
              child: Padding(
                padding: const EdgeInsets.fromLTRB(24, 8, 24, 0),
                child: FutureBuilder<PointItemStore>(
                  future: _storeFuture ??= _createStoreFuture(),
                  builder: (context, snapshot) {
                    if (snapshot.connectionState != ConnectionState.done) {
                      return Center(child: CircularProgressIndicator());
                    }

                    if (snapshot.hasError) {
                      return Center(child: Text('상점 정보를 불러오지 못했습니다.'));
                    }

                    final store = snapshot.data ??
                        const PointItemStore(currentPoint: 0, items: []);
                    return Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        _PointSummary(currentPoint: store.currentPoint),
                        const SizedBox(height: 18),
                        _StoreFilterChips(
                          selectedFilter: _filter,
                          onChanged: (filter) {
                            setState(() {
                              _filter = filter;
                            });
                          },
                        ),
                        const SizedBox(height: 18),
                        Expanded(
                          child: _StoreItemList(
                            store: store,
                            filter: _filter,
                            requestingItemId: _requestingItemId,
                            onPurchase: _purchase,
                          ),
                        ),
                      ],
                    );
                  },
                ),
              ),
            ),
            _StoreBottomNav(
              memberGymMapId: widget.memberGymMapId,
              gymName: widget.gymName,
            ),
          ],
        ),
      ),
    );
  }
}

class _StoreHeader extends StatelessWidget {
  const _StoreHeader();

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.fromLTRB(24, 22, 24, 10),
      color: AppColors.background,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            '상점',
            style: AppTextStyles.screenTitle.copyWith(fontSize: 42),
          ),
        ],
      ),
    );
  }
}

class _PointSummary extends StatelessWidget {
  const _PointSummary({required this.currentPoint});

  final int currentPoint;

  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Text(
          '보유 포인트',
          style: TextStyle(
            color: AppColors.muted,
            fontSize: 15,
            fontWeight: FontWeight.w800,
          ),
        ),
        Row(
          children: [
            Text(
              'P',
              style: TextStyle(
                color: AppColors.primary,
                fontSize: 20,
                fontWeight: FontWeight.w900,
              ),
            ),
            const SizedBox(width: 6),
            Text(
              '$currentPoint',
              style: TextStyle(
                color: AppColors.primary,
                fontSize: 24,
                fontWeight: FontWeight.w900,
              ),
            ),
          ],
        ),
      ],
    );
  }
}

class _StoreFilterChips extends StatelessWidget {
  const _StoreFilterChips({
    required this.selectedFilter,
    required this.onChanged,
  });

  final _StoreItemFilter selectedFilter;
  final ValueChanged<_StoreItemFilter> onChanged;

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      scrollDirection: Axis.horizontal,
      child: Row(
        children: _StoreItemFilter.values.map((filter) {
          final selected = selectedFilter == filter;

          return Padding(
            padding: const EdgeInsets.only(right: 9),
            child: InkWell(
              onTap: () => onChanged(filter),
              borderRadius: BorderRadius.circular(999),
              child: Container(
                height: 44,
                padding: const EdgeInsets.symmetric(horizontal: 18),
                decoration: BoxDecoration(
                  color: selected ? AppColors.primary : AppColors.white,
                  border: Border.all(
                    color: selected ? AppColors.primary : AppColors.primary,
                    width: 1.4,
                  ),
                  borderRadius: BorderRadius.circular(999),
                ),
                alignment: Alignment.center,
                child: Text(
                  filter.label,
                  style: TextStyle(
                    color: selected ? AppColors.white : AppColors.primary,
                    fontSize: 16,
                    fontWeight: FontWeight.w800,
                  ),
                ),
              ),
            ),
          );
        }).toList(),
      ),
    );
  }
}

class _StoreItemList extends StatelessWidget {
  const _StoreItemList({
    required this.store,
    required this.filter,
    required this.requestingItemId,
    required this.onPurchase,
  });

  final PointItemStore store;
  final _StoreItemFilter filter;
  final int? requestingItemId;
  final ValueChanged<PointStoreItem> onPurchase;

  @override
  Widget build(BuildContext context) {
    final items = store.items.where((item) {
      return switch (filter) {
        _StoreItemFilter.all => true,
        _StoreItemFilter.available =>
          !item.requested && store.currentPoint >= item.point,
        _StoreItemFilter.requested => item.requested,
      };
    }).toList();

    if (items.isEmpty) {
      return Container(
        width: double.infinity,
        height: double.infinity,
        decoration: BoxDecoration(
          color: AppColors.softAccent,
          borderRadius: BorderRadius.circular(16),
        ),
        child: Center(
          child: Text(
            filter == _StoreItemFilter.all
                ? '판매 중인 아이템이 없습니다.'
                : '조건에 맞는 아이템이 없습니다.',
            style: AppTextStyles.body,
          ),
        ),
      );
    }

    return GridView.builder(
      padding: const EdgeInsets.only(bottom: 18),
      itemCount: items.length,
      gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
        crossAxisCount: 2,
        crossAxisSpacing: 14,
        mainAxisSpacing: 14,
        childAspectRatio: 0.78,
      ),
      itemBuilder: (context, index) {
        final item = items[index];
        return _StoreItemTile(
          item: item,
          currentPoint: store.currentPoint,
          requesting: requestingItemId == item.gymPointItemId,
          onPurchase: () => onPurchase(item),
        );
      },
    );
  }
}

class _StoreItemTile extends StatelessWidget {
  const _StoreItemTile({
    required this.item,
    required this.currentPoint,
    required this.requesting,
    required this.onPurchase,
  });

  final PointStoreItem item;
  final int currentPoint;
  final bool requesting;
  final VoidCallback onPurchase;

  @override
  Widget build(BuildContext context) {
    final affordable = currentPoint >= item.point;
    final buttonLabel = item.requested
        ? (item.memberItemStatusLabel ?? '요청됨')
        : affordable
            ? '구매 요청'
            : '포인트 부족';

    return Material(
      color: AppColors.softAccent,
      borderRadius: BorderRadius.circular(14),
      child: InkWell(
        onTap: item.requested || !affordable || requesting ? null : onPurchase,
        borderRadius: BorderRadius.circular(14),
        child: Padding(
          padding: const EdgeInsets.fromLTRB(14, 16, 14, 14),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Expanded(
                child: Center(
                  child: _StoreItemImage(imageUrl: item.imageUrl),
                ),
              ),
              const SizedBox(height: 10),
              Text(
                item.name,
                maxLines: 1,
                overflow: TextOverflow.ellipsis,
                style: TextStyle(
                  color: AppColors.muted,
                  fontSize: 15,
                  fontWeight: FontWeight.w800,
                ),
              ),
              const SizedBox(height: 5),
              Row(
                children: [
                  Text(
                    'P',
                    style: TextStyle(
                      color: AppColors.primary,
                      fontSize: 14,
                      fontWeight: FontWeight.w900,
                    ),
                  ),
                  const SizedBox(width: 5),
                  Expanded(
                    child: Text(
                      '${item.point}',
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                      style: TextStyle(
                        color: AppColors.primary,
                        fontSize: 18,
                        fontWeight: FontWeight.w900,
                      ),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 10),
              SizedBox(
                width: double.infinity,
                height: 36,
                child: ElevatedButton(
                  onPressed: item.requested || !affordable || requesting
                      ? null
                      : onPurchase,
                  style: ElevatedButton.styleFrom(
                    padding: EdgeInsets.zero,
                    textStyle: TextStyle(
                      fontSize: 13,
                      fontWeight: FontWeight.w900,
                    ),
                  ),
                  child: Text(requesting ? '요청 중' : buttonLabel),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
class _StoreItemImage extends StatelessWidget {
  const _StoreItemImage({required this.imageUrl});

  final String? imageUrl;

  @override
  Widget build(BuildContext context) {
    return ClipRRect(
      borderRadius: BorderRadius.circular(12),
      child: Container(
        width: 96,
        height: 96,
        color: Colors.transparent,
        child: imageUrl == null
            ? Icon(
                Icons.card_giftcard_rounded,
                color: AppColors.primary,
                size: 54,
              )
            : Image.network(
                imageUrl!,
                fit: BoxFit.cover,
                errorBuilder: (context, error, stackTrace) {
                  return Icon(
                    Icons.card_giftcard_rounded,
                    color: AppColors.primary,
                    size: 54,
                  );
                },
              ),
      ),
    );
  }
}

class _StoreBottomNav extends StatelessWidget {
  const _StoreBottomNav({
    required this.memberGymMapId,
    required this.gymName,
  });

  final int? memberGymMapId;
  final String gymName;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.fromLTRB(18, 8, 18, 22),
      decoration: BoxDecoration(
        color: AppColors.white,
        border: Border(top: BorderSide(color: AppColors.softBorder)),
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceAround,
        children: [
          _NavItem(
            icon: Icons.home_rounded,
            label: '홈',
            onTap: () => Navigator.of(context).pop(),
          ),
          _NavItem(
            icon: Icons.track_changes_rounded,
            label: '목표',
            onTap: () {
              Navigator.of(context).pushReplacement(
                MaterialPageRoute(
                  builder: (context) => GoalScreen(
                    memberGymMapId: memberGymMapId,
                    gymName: gymName,
                  ),
                ),
              );
            },
          ),
          _NavItem(
            icon: Icons.list_alt_rounded,
            label: '일퀘',
            onTap: () {
              Navigator.of(context).pushReplacement(
                MaterialPageRoute(
                  builder: (context) => DailyQuestScreen(
                    memberGymMapId: memberGymMapId,
                    gymName: gymName,
                  ),
                ),
              );
            },
          ),
          const _NavItem(
            icon: Icons.shopping_bag_outlined,
            label: '상점',
            active: true,
          ),
          const _NavItem(icon: Icons.emoji_events_outlined, label: '랭킹'),
        ],
      ),
    );
  }
}

class _NavItem extends StatelessWidget {
  const _NavItem({
    required this.icon,
    required this.label,
    this.active = false,
    this.onTap,
  });

  final IconData icon;
  final String label;
  final bool active;
  final VoidCallback? onTap;

  @override
  Widget build(BuildContext context) {
    return Semantics(
      label: label,
      selected: active,
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(12),
        child: SizedBox(
          width: 54,
          height: 48,
          child: Center(
            child: AnimatedContainer(
              duration: const Duration(milliseconds: 160),
              width: 42,
              height: 36,
              decoration: BoxDecoration(
                color: active ? AppColors.primary : Colors.transparent,
                borderRadius: BorderRadius.circular(12),
              ),
              child: Icon(
                icon,
                color: active ? AppColors.white : AppColors.primary,
                size: active ? 25 : 28,
              ),
            ),
          ),
        ),
      ),
    );
  }
}
