-- show:{showId}:seat:booked:{seatId}
-- show:{showId}:seat:preempt:{seatId}

-- 좌석 선점
-- ARGV[1]: 사용자 아이디 (userId)
-- ARGV[2]: 사용자가 예매할 좌석 개수 (seatCount)
-- ARGV[3]: 키의 TTL 시간 (ttl, 상수)

-- 입력 매개변수 파싱
local userId = ARGV[1]
local seatCount = tonumber(ARGV[2])
local ttl = tonumber(ARGV[3])

-- 조건 검사 1: 예매할 좌석이 예매되었는지 검사 (좌석 캐시 조회)
for i = 1, #KEYS - seatCount do
    if redis.call("EXISTS", KEYS[i]) == 1 then
        return -1  -- 좌석이 이미 예매됨
    end
end

-- 조건 검사 2: 예매할 좌석이 선점되었는지 검사 (좌석 선점 조회: 동시성 제어)
for i = seatCount + 1, #KEYS do
    if redis.call("EXISTS", KEYS[i]) == 1 then
        return 0  -- 좌석이 이미 선점됨
    end
end

-- 조건 검사 3: 선점 되어있지 않다면 좌석 선점(lock) 및 TTL 설정
for i = seatCount + 1, #KEYS do
    redis.call("SET", KEYS[i], userId, "EX", ttl)
end

-- 사용자 예약 수 증가 + TTL
--redis.call("INCRBY", KEYS[#KEYS], seatCount)
--redis.call("EXPIRE", KEYS[#KEYS], ttl)

return 1  -- 성공