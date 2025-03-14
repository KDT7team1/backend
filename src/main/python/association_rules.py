import pymysql # MySQL 랑 연결하고, 쿼리 실행하는 라이브러리
import pandas as pd
from apyori import apriori # Apriori 알고리즘을 제공하는 라이브러리

# MySQL 연결하기
conn = pymysql.connect(
    host='localhost',
    user='root',
    password='1234',
    db='final',
    charset='utf8'
)
cursor = conn.cursor() # 쿼리를 실행하기 위한 cursor 객체 생성

# 상품 ID를 상품명으로 매핑
cursor.execute("""
    SELECT g.goods_id, s.sub_name
    FROM goods g
    JOIN sub_category s ON g.sub_category_id = s.sub_category_id
""")

category_map = {}
for g_id, sub_name in cursor.fetchall():
    category_map[str(g_id)] = sub_name # id : 상품 카테고리명 == "101": "음료"


##################################################################
# orders_id로 묶어서 같은 주문끼리 묶이도록
cursor.execute("SELECT orders_id, goods_id FROM sale_data ORDER BY orders_id")
sales = cursor.fetchall()

transactions = []
current_order = None
current_items = set()  # sub_name 저장용 set

for orders_id, goods_id in sales:
    # 맨 처음일때 current_order를 첫주문id로 초기화
    if current_order is None:
        current_order = orders_id

    if orders_id != current_order:
        # 지금까지 모은 sub_name들을 하나의 거래로 추가
        transactions.append(list(current_items))
        current_items = set() # 초기화
        current_order = orders_id

    # goods_id에 해당하는 sub_name을 set에 저장
    sub_name = category_map[str(goods_id)] # goods_id → sub_name 변환
    current_items.add(sub_name) # set에 추기 (중복되면 추가안되겠지)

# 마지막 주문 처리
if current_items:
    transactions.append(list(current_items))

print(f"📌 총 거래 개수: {len(transactions)}")
print(f"📌 예시 거래 내역 5개:")
for idx in range(min(5, len(transactions))):
    print(f" - {transactions[idx]}")

# 단일 품목 주문 제외 : 연관관계 분석에 의미가 없으니까
filtered_transactions = [t for t in transactions if len(t) > 1]


# 3) Apriori 실행
##################################################################
results = apriori(
    filtered_transactions,
    min_support=0.02,      # 예: 0.02
    min_confidence=0.3,    # 예: 0.3
    min_lift=1.0           # 예: 1.0
)

results_list = list(results)
print(f"✅ Apriori 결과 개수: {len(results_list)}")

##################################################################
# 4) 결과 저장 (자기 자신 예측 등 불필요 규칙 필터링)
##################################################################
unique_rules = {} #(A,B) : (support,confidence, lift)  형태의 딕셔너리

# 연관관계 분석한 거 하나씩 돌려서 계산
for rule in results_list:
    for item in rule.ordered_statistics:
        base_items = item.items_base # 조건부 A
        add_items  = item.items_add  # 결과 B

        # Base와 Add가 겹치면 건너뛴다 (맥주 => 맥주)
        if not base_items or not add_items:
            continue # 조건 또는 결과 비어있으면 건너뛰기


        for base in base_items:
            for add in add_items:
                if base == add:
                    continue

                itemset_a = base
                itemset_b =  add
                support = round(rule.support,4)
                confidence = round(item.confidence,4)
                lift = round(item.lift,4)

                # (A → B)와 (B → A) 중 더 신뢰도(confidence)가 높은 것을 저장
                selected_rule = tuple(sorted([itemset_a, itemset_b]))

                if selected_rule in unique_rules:
                    prev_confidence = unique_rules[selected_rule][0] # 기존 저장된 confidence 값
                    if confidence > prev_confidence:
                        unique_rules[selected_rule] = (support,confidence,lift,itemset_a,itemset_b)
                else:
                    unique_rules[selected_rule] = (support,confidence,lift,itemset_a,itemset_b)

# (key, value) => (selected_rule, (s,c,...)) =>  ('감자칩', '콜라'): (0.126, 0.437, 1.509, '감자칩', '콜라'),
for selected_rule, (support, confidence, lift,itemset_a, itemset_b) in unique_rules.items():
    sql = """
                    INSERT INTO association_rules 
                        (itemset_a, itemset_b, support, confidence, lift)
                    VALUES 
                        (%s, %s, %s, %s, %s)
            """


    try:
           print(f"🚀 INSERT: {itemset_a} -> {itemset_b}, support={support}, confidence={confidence}, lift={lift}")
           cursor.execute(sql, (itemset_a, itemset_b, support, confidence, lift))
    except Exception as e:
            print(f"❌ SQL 실행 오류: {e}")


# 5) 커밋 & DB 연결 종료
conn.commit()
cursor.close()
conn.close()

print("✅ 연관 규칙 저장 완료! 테이블 확인해 보세요. 🚀")
