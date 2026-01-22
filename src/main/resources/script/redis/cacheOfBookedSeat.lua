-- show:{showId}:seat:booked:{seatId}

-- 예매된 좌석 캐싱
-- ARGV[1]: 사용자 아이디 (userId)
-- ARGV[2]: 사용자가 예매할 좌석 개수 (seatCount)
-- ARGV[3]: 키의 TTL 시간 (ttl, 상수)

-- 입력 매개변수 파싱
local userId = ARGV[1]
local seatCount = tonumber(ARGV[2])
local ttl = tonumber(ARGV[3])

-- 조건 검사 1: 선점된 좌석 제거
for i = seatCount + 1, #KEYS do
    redis.call("DEL", KEYS[i])
end

-- 조건 검사 2: 예매된 좌석 캐싱
for i = 1, #KEYS - seatCount do
    redis.call("SET", KEYS[i], userId, "EX", ttl)
end

return 1  -- 성공