import pymysql # MySQL 랑 연결하고, 쿼리 실행하는 라이브러리
import pandas as pd
from apyori import apriori



# MySQL 연결하기
conn = pymysql.connect(
    host='localhost',
    user='root',
    password='1234',
    db='final',
    charset='utf8'
)
cursor = conn.cursor()

cursor.execute("""
    select g.goods_id, s.sub_name
    from goods g
    join sub_category s on g.sub_category_id  = s.sub_category_id 

""")

category_map = {str(g_id) : sub_name for g_id, sub_name in cursor.fetchall()}

# 시간대 정의 함수
def get_time_period(time):
    if 5 <= time < 11:
        return "아침"
    elif 11 <= time < 15:
        return "점심"
    elif 15 <= time < 18:
        return "한가한 오후"
    elif 18 <= time < 23:
        return "저녁"
    elif time >= 23 or time < 5:
        return "심야"
    else:
        return "기본"


    # 시간대별 판매 데이터 가져오기
cursor.execute("""
        select orders_id, goods_id, sale_date from sale_data
order by orders_id
""")
sales = cursor.fetchall()

# 시간대별 거래 데이터 저장
transactions_byTime = {
    "아침" : [],
    "점심" : [],
    "한가한 오후" : [],
    "저녁" : [],
    "심야" : [],
    "기본" : [],
}

current_order = None
current_items = set()  # 상품명을 저장할 set (중복 방지)
current_time_period = None

for orders_id, goods_id, sale_date in sales:
    sale_time = sale_date.hour
    print("sale_time", sale_time)
    time_period = get_time_period(sale_time) # 주문시간에 맞는 시간대별 확인

    if current_order is None:
        current_order = orders_id
        current_time_period = time_period

    if orders_id != current_order:
        # 주문이 바뀌면 기존 아이템들을 저장
        if current_items:
            transactions_byTime[current_time_period].append(list(current_items))

        current_items = set()
        current_order = orders_id
        current_time_period = time_period

    sub_name = category_map.get(str(goods_id))
    if sub_name:
        current_items.add(sub_name)

# 마지막 주문 추가
if current_items:
    transactions_byTime[current_time_period].append(list(current_items))

# 4) 각 시간대별로 Apriori 실행
for time_period, transactions in transactions_byTime.items():
    print(f"📌 {time_period} 시간대 거래 개수: {len(transactions)}")

    # 단일 품목 주문 제거
    filtered_transactions = [t for t in transactions if len(t) > 1]

    #Apriori 실행
    results = apriori(
        filtered_transactions,
        min_support=0.02,
        min_confidence=0.3,
        min_lift=1.0
    )
    results_list = list(results)
    print(f"✅ {time_period} 시간대 Apriori 결과 개수: {len(results_list)}")

    # 결과 저장 (양방향 중 더 신뢰도 높은 것만 저장)
    unique_rules = {}

    for rule in results_list:
        for item in rule.ordered_statistics:
            base_items = item.items_base  # 조건
            add_items = item.items_add    # 결과

            if not base_items or not add_items:
                continue  # 조건 또는 결과가 비어있으면 건너뛰기

            for base in base_items:
                for add in add_items:
                    if base == add:
                        continue  # 같은 상품이 조건과 결과로 등장하는 경우 제외

                    itemset_a = base
                    itemset_b = add
                    support = round(rule.support, 4)
                    confidence = round(item.confidence,4)
                    lift = round(item.lift,4)

                    # (A → B)와 (B → A) 중 더 신뢰도 높은 것만 저장
                    rule_key = tuple(sorted([itemset_a, itemset_b]))

                    if rule_key in unique_rules:
                        existing_confidence = unique_rules[rule_key][0]
                        if confidence > existing_confidence:
                            unique_rules[rule_key] = (confidence, support, lift, itemset_a, itemset_b)
                    else:
                        unique_rules[rule_key] = (confidence, support, lift, itemset_a, itemset_b)

    # MySQL 저장
    for confidence, support, lift, itemset_a, itemset_b in unique_rules.values():
        sql = """
            INSERT INTO association_TimeRules 
                (time_period, itemset_a, itemset_b, support, confidence, lift, created_at)
            VALUES 
                (%s, %s, %s, %s, %s, %s, NOW())
        """
        try:
            print(f"🚀 INSERT ({time_period}): {itemset_a} -> {itemset_b}, support={support}, confidence={confidence}, lift={lift}")
            cursor.execute(sql, (time_period, itemset_a, itemset_b, support, confidence, lift))
        except Exception as e:
            print(f"❌ SQL 실행 오류: {e}")

# 5) 커밋 & DB 연결 종료
conn.commit()
cursor.close()
conn.close()
print("✅ 시간대별 연관 규칙 저장 완료! 🚀")