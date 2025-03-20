//package com.exam.cartAnalysis;
//
//
//import com.exam.cartAnalysis.entity.Orders;
//import com.exam.cartAnalysis.entity.SaleData;
//import com.exam.cartAnalysis.repository.OrdersRepository;
//import com.exam.cartAnalysis.repository.SaleDataRepository;
//import com.exam.goods.Goods;
//import com.exam.goods.GoodsRepository;
//import com.exam.member.MemberEntity;
//import com.exam.member.MemberRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.*;
//
//@Component
//public class AssociationDummyDataInserter implements CommandLineRunner {
//
//    @Autowired
//    private OrdersRepository ordersRepo;
//
//    @Autowired
//    private SaleDataRepository saleDataRepo;
//
//    @Autowired
//    private GoodsRepository goodsRepo;
//
//    @Autowired
//    private MemberRepository memberRepo;
//
//    private static final int NUM_ORDERS = 100000; // 주문 데이터 개수 (랜덤+연관 데이터 포함)
//    private static final int NUM_SALE_DATA = 500000; // 판매 데이터 개수
//
//    @Override
//    @Transactional
//    public void run(String... args) throws Exception {
//
//        Random random = new Random();
//        LocalDateTime currentDateTime = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
//        int saleDataCnt = 0;
//
//        // ✅ 상품 카테고리별 목록 가져오기
//        Map<String, List<Goods>> categoryMap = new HashMap<>();
//        categoryMap.put("삼각김밥", goodsRepo.findBySubCategoryId(1L));
//        categoryMap.put("주먹밥", goodsRepo.findBySubCategoryId(2L));
//        categoryMap.put("컵밥", goodsRepo.findBySubCategoryId(3L));
//        categoryMap.put("도시락", goodsRepo.findBySubCategoryId(4L)); //
//        categoryMap.put("핫도그", goodsRepo.findBySubCategoryId(5L));
//        categoryMap.put("햄버거", goodsRepo.findBySubCategoryId(6L));
//        categoryMap.put("컵라면", goodsRepo.findBySubCategoryId(7L));
//        categoryMap.put("봉지라면", goodsRepo.findBySubCategoryId(8L));
//        categoryMap.put("우동", goodsRepo.findBySubCategoryId(9L));
//        categoryMap.put("쌀국수", goodsRepo.findBySubCategoryId(10L));
//        categoryMap.put("샌드위치", goodsRepo.findBySubCategoryId(11L));
//        categoryMap.put("크로와상", goodsRepo.findBySubCategoryId(12L));
//        categoryMap.put("도넛", goodsRepo.findBySubCategoryId(13L));
//        categoryMap.put("베이글", goodsRepo.findBySubCategoryId(14L));
//        categoryMap.put("냉동피자", goodsRepo.findBySubCategoryId(15L));
//        categoryMap.put("닭강정", goodsRepo.findBySubCategoryId(16L));
//        categoryMap.put("만두", goodsRepo.findBySubCategoryId(17L));
//        categoryMap.put("핫바", goodsRepo.findBySubCategoryId(18L));
//        categoryMap.put("튀김류", goodsRepo.findBySubCategoryId(19L));
//        categoryMap.put("감자칩", goodsRepo.findBySubCategoryId(20L));
//
//        categoryMap.put("팝콘", goodsRepo.findBySubCategoryId(21L));
//        categoryMap.put("비스킷", goodsRepo.findBySubCategoryId(22L));
//        categoryMap.put("견과류", goodsRepo.findBySubCategoryId(23L));
//        categoryMap.put("젤리", goodsRepo.findBySubCategoryId(24L));
//        categoryMap.put("초콜릿", goodsRepo.findBySubCategoryId(25L));
//        categoryMap.put("아이스크림", goodsRepo.findBySubCategoryId(26L));
//        categoryMap.put("젤라또", goodsRepo.findBySubCategoryId(27L));
//        categoryMap.put("요거트", goodsRepo.findBySubCategoryId(28L));
//        categoryMap.put("캔커피", goodsRepo.findBySubCategoryId(29L));
//
//        categoryMap.put("병커피", goodsRepo.findBySubCategoryId(30L));
//        categoryMap.put("티백", goodsRepo.findBySubCategoryId(31L));
//        categoryMap.put("스틱커피", goodsRepo.findBySubCategoryId(32L));
//
//        categoryMap.put("콜라", goodsRepo.findBySubCategoryId(33L));
//        categoryMap.put("사이다", goodsRepo.findBySubCategoryId(34L));
//        categoryMap.put("에너지드링크", goodsRepo.findBySubCategoryId(35L));
//        categoryMap.put("오렌지주스", goodsRepo.findBySubCategoryId(36L));
//        categoryMap.put("비타민음료", goodsRepo.findBySubCategoryId(37L));
//        categoryMap.put("우유", goodsRepo.findBySubCategoryId(38L));
//        categoryMap.put("두유", goodsRepo.findBySubCategoryId(39L));
//        categoryMap.put("요거트", goodsRepo.findBySubCategoryId(40L));
//
//        categoryMap.put("맥주", goodsRepo.findBySubCategoryId(41L));
//        categoryMap.put("소주", goodsRepo.findBySubCategoryId(42L));
//        categoryMap.put("와인", goodsRepo.findBySubCategoryId(43L));
//        categoryMap.put("칵테일음료", goodsRepo.findBySubCategoryId(44L));
//        categoryMap.put("휴지", goodsRepo.findBySubCategoryId(45L));
//        categoryMap.put("물티슈", goodsRepo.findBySubCategoryId(46L));
//        categoryMap.put("손소독제", goodsRepo.findBySubCategoryId(47L));
//        categoryMap.put("샴푸", goodsRepo.findBySubCategoryId(48L));
//        categoryMap.put("바디워시", goodsRepo.findBySubCategoryId(49L));
//        categoryMap.put("칫솔", goodsRepo.findBySubCategoryId(50L));
//
//        categoryMap.put("치약", goodsRepo.findBySubCategoryId(50L));
//        categoryMap.put("면도기", goodsRepo.findBySubCategoryId(52L));
//        categoryMap.put("립밤", goodsRepo.findBySubCategoryId(53L));
//        categoryMap.put("핸드크림", goodsRepo.findBySubCategoryId(54L));
//        categoryMap.put("스킨케어", goodsRepo.findBySubCategoryId(55L));
//        categoryMap.put("헤어왁스", goodsRepo.findBySubCategoryId(56L));
//        categoryMap.put("밴드", goodsRepo.findBySubCategoryId(57L));
//        categoryMap.put("진통제", goodsRepo.findBySubCategoryId(58L));
//        categoryMap.put("소독약", goodsRepo.findBySubCategoryId(59L));
//        categoryMap.put("영양제", goodsRepo.findBySubCategoryId(60L));
//
//        categoryMap.put("충전기", goodsRepo.findBySubCategoryId(61L));
//        categoryMap.put("보조배터리", goodsRepo.findBySubCategoryId(62L));
//        categoryMap.put("이어폰", goodsRepo.findBySubCategoryId(63L));
//        categoryMap.put("볼펜", goodsRepo.findBySubCategoryId(64L));
//        categoryMap.put("노트", goodsRepo.findBySubCategoryId(65L));
//        categoryMap.put("포스트잇", goodsRepo.findBySubCategoryId(66L));
//        categoryMap.put("테이프", goodsRepo.findBySubCategoryId(67L));
//        categoryMap.put("스티커", goodsRepo.findBySubCategoryId(68L));
//
//
//        List<Goods> allGoods = goodsRepo.findAll();
//
//        // ✅ 시간대별 연관 상품 조합 (확률적 조정 가능)
//        Map<String, List<String[]>> timePairs = new HashMap<>();
//        timePairs.put("아침", Arrays.asList(
//                new String[]{"삼각김밥", "커피"},
//                new String[]{"샌드위치", "콜라"},
//                new String[]{"베이글", "커피"},
//                new String[]{"샌드위치", "우유"},
//                new String[]{"요거트", "베이글"}
//        ));
//
//        timePairs.put("점심", Arrays.asList(
//                new String[]{"도시락", "에너지드링크"},
//                new String[]{"삼각김밥", "컵라면"},
//                new String[]{"핫바", "콜라"},
//                new String[]{"견과류", "두유"},
//                new String[]{"컵라면", "콜라"},
//                new String[]{"요거트", "젤리"}
//        ));
//
//        timePairs.put("한산한 오후", Arrays.asList(
//                new String[]{"초콜릿", "커피"},        // 달달한 간식 + 커피
//                new String[]{"감자칩", "젤리"},        // 짭짤한 과자 + 달달한 젤리
//                new String[]{"아이스크림", "칫솔"},    // 아이스크림 먹고 칫솔 구매
//                new String[]{"팝콘", "콜라"}, // 영화 감상용 조합
//                new String[]{"볼펜", "노트"},// 공부 또는 업무용 문구 조합
//                new String[]{"핸드크림", "립밤"} // 건조한 오후 피부 관리 조합
//        ));
//
//        timePairs.put("저녁", Arrays.asList(
//                new String[]{"맥주", "소주"},// 맥주 + 간식
//                new String[]{"햄버거", "콜라"},  // 패스트푸드 조합
//                new String[]{"칫솔", "면도기"}, // 위생용품 조합
//                new String[]{"닭강정", "맥주"},  // 술안주 조합
//                new String[]{"컵라면", "소주"}, // 간편식 조합
//                new String[]{"샴푸", "칫솔"}// 샤워용품 조합
//        ));
//
//        timePairs.put("심야", Arrays.asList(
//                new String[]{"소주", "비타민음료"},// 술 + 숙취해소
//                new String[]{"닭강정", "아이스크림"}, // // 단짠 조합
//                new String[]{"도넛", "아이스크림"}, // 간식 + 건강 조합
//                new String[]{"컵라면", "햄버거"},      // 늦은 밤 간편식
//                new String[]{"초콜릿", "에너지드링크"},// 당 충전 + 카페인
//                new String[]{"맥주", "견과류"}         // 맥주 + 건강한 안주
//        ));
//
//
//
//        // 🔥 주문 데이터 생성 (랜덤 + 연관 데이터)
//        for (int i = 0; i < NUM_ORDERS; i++) {
//            if (saleDataCnt >= NUM_SALE_DATA) break;
//
//            // 🔹 랜덤 시간 간격 적용
//            List<Integer> timeIntervals = getTimeIntervalForTime(currentDateTime.toLocalTime());
//            int randomNum = timeIntervals.get(random.nextInt(timeIntervals.size()));
//            currentDateTime = currentDateTime.plusMinutes(randomNum);
//
//            // 🔹 주문 생성
//            Orders order = new Orders();
//            MemberEntity member = memberRepo.findById(1L + random.nextInt(100))
//                    .orElseThrow(() -> new RuntimeException("멤버 데이터를 먼저 삽입하세요"));
//            order.setMember(member);
//            order.setOrdersDate(currentDateTime);
//
//            Orders savedOrder = ordersRepo.save(order);
//
//            // 🔥 60% 확률로 시간대별 연관 데이터 추가
//            if (random.nextDouble() < 0.5) {
//                String timeText = getTimeSlot(currentDateTime.toLocalTime());
//                if(timeText.equals("기본")){ // 기본이 나오면 다른 랜덤 시간대로 보내기
//                    List<String> keys = new ArrayList<>(timePairs.keySet());
//                    timeText = keys.get(random.nextInt(keys.size()));
//                }
//                List<String[]> pairs = timePairs.get(timeText);
//
//                if (pairs != null) {
//                    for (int k = 0; k < 2; k++) { // 같은 시간대에서 2개 조합 추가
//                        String[] selectedPair = pairs.get(random.nextInt(pairs.size()));
//
//                        List<Goods> itemA = categoryMap.get(selectedPair[0]);
//                        List<Goods> itemB = categoryMap.get(selectedPair[1]);
//
//                        if (itemA != null && !itemA.isEmpty() && itemB != null && !itemB.isEmpty()) {
//                            createSaleData(savedOrder, itemA.get(random.nextInt(itemA.size())), currentDateTime, random);
//                            createSaleData(savedOrder, itemB.get(random.nextInt(itemB.size())), currentDateTime, random);
//                        }
//                    }
//                }
//            }
//
//            // 🔥 40% 확률로 랜덤 상품 1~3개 추가
//            if (random.nextDouble() < 0.5) {
//                int numRandomGoods = 1 + random.nextInt(4);
//                for (int j = 0; j < numRandomGoods; j++) {
//                    Goods randomGoods = allGoods.get(random.nextInt(allGoods.size()));
//                    createSaleData(savedOrder, randomGoods, currentDateTime, random);
//                }
//            }
//
//        }
//
//        System.out.println("🔥 랜덤 + 연관 데이터 삽입 성공!!");
//    }
//
//    private List<Integer> getTimeIntervalForTime(LocalTime time) {
//        if (time.isAfter(LocalTime.of(5, 0)) && time.isBefore(LocalTime.of(10, 59))) {
//            return Arrays.asList(8, 10, 12, 15); // 출근 시간
//        } else if (time.isAfter(LocalTime.of(11, 0)) && time.isBefore(LocalTime.of(14, 59))) {
//            return Arrays.asList(8, 10, 12, 15); // 점심 피크
//        } else if (time.isAfter(LocalTime.of(15, 0)) && time.isBefore(LocalTime.of(17, 59))) {
//            return Arrays.asList(12, 15, 18); // 오후 한산한 시간
//        } else if (time.isAfter(LocalTime.of(18, 0)) && time.isBefore(LocalTime.of(22, 59))) {
//            return Arrays.asList(8, 10, 12, 15); // 퇴근+저녁 시간
//        } else if (time.isAfter(LocalTime.of(23, 0))|| time.isBefore(LocalTime.of(5, 59))) {
//            return Arrays.asList(15, 20, 25, 30); // 심야 시간
//        } else {
//            return Arrays.asList(10, 15, 20); // 기본 간격
//        }
//    }
//
//    private String getTimeSlot(LocalTime time) {
//        if (time.isAfter(LocalTime.of(5, 0)) && time.isBefore(LocalTime.of(10, 59))) return "아침";
//        if (time.isAfter(LocalTime.of(11, 0)) && time.isBefore(LocalTime.of(14, 59))) return "점심";
//        if (time.isAfter(LocalTime.of(15, 0)) && time.isBefore(LocalTime.of(17, 59))) return "한산한 오후";
//        if (time.isAfter(LocalTime.of(18, 0)) && time.isBefore(LocalTime.of(22, 59))) return "저녁";
//        if (time.isAfter(LocalTime.of(23, 0)) || time.isBefore(LocalTime.of(5, 59))) return "심야";
//        return "기본";
//    }
//
//    private SaleData createSaleData(Orders order, Goods goods, LocalDateTime dateTime, Random random) {
//        SaleData saleData = new SaleData();
//        saleData.setOrders(order);
//        saleData.setGoods(goods);
//        saleData.setSaleAmount(1+ random.nextLong(3)); // 1개에서 3개 랜덤으로
//        saleData.setSalePrice(goods.getGoods_price());
//        saleData.setSaleDate(dateTime);
//
//        saleDataRepo.save(saleData);
//        return saleData;
//    }
//}
